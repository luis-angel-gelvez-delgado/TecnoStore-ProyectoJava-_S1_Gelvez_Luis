package Persistencia;

import Modelo.CategoriaGama;
import Modelo.Celular;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

// clase DAO (data access object) para celulares
// maneja todas las operaciones de base de datos relacionadas con celulares
// es como el "traductor" entre java y mysql
public class CelularDB {

    // metodo para guardar un nuevo celular en la base de datos
    public boolean insertarCelular(Celular celular) {
        // query sql con signos de interrogacion (parametros)
        // los ? se reemplazan con valores reales mas abajo
        String sql = "INSERT INTO celulares (marca, modelo, sistema_operativo, gama, precio, stock) VALUES (?, ?, ?, ?, ?, ?)";

        // try-with-resources: cierra automaticamente la conexion y el preparedstatement
        // esto evita que se queden conexiones abiertas y se llene la memoria
        try (
                Connection conn = ConexionDB.obtenerConexion(); 
                PreparedStatement ps = conn.prepareStatement(sql)) {

            // reemplazar los ? con los valores reales del celular
            ps.setString(1, celular.getMarca().getNombre()); // primer ?
            ps.setString(2, celular.getModelo()); // segundo ?
            ps.setString(3, celular.getSistemaOperativo()); // tercer ?
            ps.setString(4, celular.getGama().name()); // cuarto ? (convierte enum a texto)
            ps.setDouble(5, celular.getPrecio()); // quinto ?
            ps.setInt(6, celular.getStock()); // sexto ?

            // ejecutar el insert en la base de datos
            ps.executeUpdate();
            return true; // si llego aqui, se guardo correctamente

        } catch (SQLException e) {
            // si algo falla (error de sql, conexion perdida, etc)
            System.out.println("error al insertar celular: " + e.getMessage());
            return false;
        }
    }

    // metodo para traer todos los celulares de la base de datos
    public static List<Celular> obtenerCelulares() {

        // lista vacia donde se guardaran los celulares
        List<Celular> lista = new ArrayList<>();
        String sql = "SELECT * FROM celulares"; // traer todos los registros

        try (
                Connection conn = ConexionDB.obtenerConexion(); 
                PreparedStatement ps = conn.prepareStatement(sql); 
                ResultSet rs = ps.executeQuery()) { // executeQuery se usa para SELECT

            // while recorre cada fila que trajo la consulta
            while (rs.next()) {
                Celular c = new Celular();

                // leer cada columna de la fila y asignarla al objeto celular
                c.setId(rs.getInt("id"));
                c.setModelo(rs.getString("modelo"));
                c.setSistemaOperativo(rs.getString("sistema_operativo"));
                c.setPrecio(rs.getDouble("precio"));
                c.setStock(rs.getInt("stock"));

                // crear y asignar la marca
                Modelo.Marca marca = new Modelo.Marca();
                marca.setNombre(rs.getString("marca"));
                c.setMarca(marca);

                // convertir el texto "ALTA" de la bd al enum CategoriaGama.ALTA
                c.setGama(CategoriaGama.valueOf(rs.getString("gama")));

                // agregar el celular a la lista
                lista.add(c);
            }
        } catch (SQLException e) {
            System.out.println("error al listar los celulares " + e.getLocalizedMessage());
        }
        return lista; // devolver la lista (puede estar vacia si hubo error)

    }

    // metodo para buscar un celular especifico por su id
    public Celular buscarPorId(int id) {

        // WHERE id = ? busca solo el celular con ese id
        String sql = "SELECT * FROM celulares WHERE id = ?";

        try (
                Connection conn = ConexionDB.obtenerConexion(); 
                PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, id); // reemplazar el ? con el id buscado

            ResultSet rs = ps.executeQuery();

            // if (en lugar de while) porque solo esperamos 1 resultado
            if (rs.next()) {
                Celular c = new Celular();

                c.setId(rs.getInt("id"));
                c.setModelo(rs.getString("modelo"));
                c.setSistemaOperativo(rs.getString("sistema_operativo"));
                c.setPrecio(rs.getDouble("precio"));
                c.setStock(rs.getInt("stock"));

                // crear y asignar la marca
                Modelo.Marca marca = new Modelo.Marca();
                marca.setNombre(rs.getString("marca"));
                c.setMarca(marca);

                c.setGama(CategoriaGama.valueOf(rs.getString("gama")));

                return c; // devolver el celular encontrado
            }

        } catch (SQLException e) {
            System.out.println("error  al buscar celular " + e.getLocalizedMessage());
        }

        return null; // si no encontro nada, devuelve null
    }

    // metodo para actualizar solo el stock de un celular
    public boolean actualizarStock(int id, int nuevoStock) {
        // UPDATE modifica registros existentes
        // SET stock = ? cambia el valor del stock
        // WHERE id = ? solo del celular con ese id
        String sql = "UPDATE celulares SET stock = ? WHERE id = ?";

        try (
                Connection conn = ConexionDB.obtenerConexion(); 
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, nuevoStock); // nuevo valor de stock
            ps.setInt(2, id); // id del celular a actualizar

            // executeUpdate devuelve cuantas filas se modificaron
            int filas = ps.executeUpdate();
            return filas > 0; // si modifico al menos 1, retorna true

        } catch (SQLException e) {
            System.out.println("error al actualizar el stock: " + e.getMessage());
            return false;

        }

    }

    // metodo para eliminar un celular de la base de datos
    public boolean eliminarCelular(int id) {

        // DELETE borra registros
        String sql = "DELETE FROM celulares WHERE id = ?";

        try (
                Connection conn = ConexionDB.obtenerConexion(); 
                PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, id);

            int filas = ps.executeUpdate();
            return filas > 0; // true si elimino algo

        } catch (SQLException e) {
            System.out.println("Error al eliminar celular: " + e.getMessage());
            return false;
        }
    }

    // metodo para actualizar todos los datos de un celular
    public boolean actualizarCelular(Celular celular) {
        // actualiza marca, modelo, so, gama, precio y stock
        String sql = "UPDATE celulares SET marca = ?, modelo = ?, sistema_operativo = ?, " +
                     "gama = ?, precio = ?, stock = ? WHERE id = ?";
        
        try (Connection conn = ConexionDB.obtenerConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            // asignar los nuevos valores
            ps.setString(1, celular.getMarca().getNombre());
            ps.setString(2, celular.getModelo());
            ps.setString(3, celular.getSistemaOperativo());
            ps.setString(4, celular.getGama().name());
            ps.setDouble(5, celular.getPrecio());
            ps.setInt(6, celular.getStock());
            ps.setInt(7, celular.getId()); // id del celular a actualizar
            
            int filas = ps.executeUpdate();
            return filas > 0;
            
        } catch (SQLException e) {
            System.out.println("error al actualizar celular: " + e.getMessage());
            return false;
        }
    }

}