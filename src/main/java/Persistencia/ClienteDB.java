package Persistencia;

import Modelo.Cliente;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

// clase DAO para clientes
// maneja todas las operaciones de base de datos de clientes
public class ClienteDB {

    // metodo para guardar un nuevo cliente en la base de datos
    public boolean insertarCliente(Cliente cliente) {
        String sql = "INSERT INTO clientes (nombre, identificacion, correo, telefono) VALUES (?, ?, ?, ?)";

        try (
                Connection conn = ConexionDB.obtenerConexion(); PreparedStatement ps = conn.prepareStatement(sql)) {

            // asignar los valores del cliente a los parametros
            ps.setString(1, cliente.getNombre());
            ps.setString(2, cliente.getDocumento());
            ps.setString(3, cliente.getCorreo());
            ps.setString(4, cliente.getTelefono());

            ps.executeUpdate(); // ejecutar el insert
            return true;

        } catch (SQLException e) {
            System.out.println("error al insertar cliente: " + e.getMessage());
            return false;
        }

    }

    // metodo para traer todos los clientes de la base de datos
    public List<Cliente> obtenerClientes() {

        List<Cliente> lista = new ArrayList<>();
        String sql = "SELECT * from clientes";

        try (
                Connection conn = ConexionDB.obtenerConexion(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            // recorrer cada cliente que trajo la consulta
            while (rs.next()) {
                Cliente c = new Cliente();

                // leer los datos de cada columna
                c.setId(rs.getInt("id"));
                c.setNombre(rs.getString("nombre"));
                c.setDocumento(rs.getString("identificacion")); // columna se llama identificacion
                c.setCorreo(rs.getString("correo"));
                c.setTelefono(rs.getString("telefono"));

                lista.add(c);

            }

        } catch (SQLException e) {
            System.out.println("error  al obtener clientes: " + e.getMessage());

        }

        return lista;

    }

    // metodo para buscar un cliente especifico por su id
    public Cliente buscarPorId(int id) {

        String sql = "SELECT * FROM clientes WHERE id = ?";

        try (
                Connection conn = ConexionDB.obtenerConexion(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);

            ResultSet rs = ps.executeQuery();

            // si encuentra el cliente, crearlo y devolverlo
            if (rs.next()) {
                Cliente c = new Cliente();

                c.setId(rs.getInt("id"));
                c.setNombre(rs.getString("nombre"));
                c.setDocumento(rs.getString("identificacion"));
                c.setCorreo(rs.getString("correo"));
                c.setTelefono(rs.getString("telefono"));

                return c;

            }

        } catch (SQLException e) {
            System.out.println("error al buscar cliente: " + e.getMessage());
        }

        return null; // no encontro nada

    }

    // metodo para eliminar un cliente de la base de datos
    public boolean eliminarCliente(int id) {

        String sql = "DELETE FROM clientes WHERE id = ?";

        try (
                Connection conn = ConexionDB.obtenerConexion(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);

            int filas = ps.executeUpdate();
            return filas > 0; // true si elimino algo

        } catch (SQLException e) {
            // Detecta si es por clave foránea (cliente con ventas)
            if (e.getMessage().contains("foreign key constraint")) {
                // simplemente retornamos false, sin imprimir nada
                return false;
            }

            // cualquier otro error también retorna false
            return false;
        }

    }

    // metodo para actualizar los datos de un cliente
    public boolean actualizarCliente(Cliente cliente) {
        String sql = "UPDATE clientes SET nombre = ?, identificacion = ?, correo = ?, telefono = ? WHERE id = ?";

        try (Connection conn = ConexionDB.obtenerConexion(); PreparedStatement ps = conn.prepareStatement(sql)) {

            // asignar los nuevos valores
            ps.setString(1, cliente.getNombre());
            ps.setString(2, cliente.getDocumento());
            ps.setString(3, cliente.getCorreo());
            ps.setString(4, cliente.getTelefono());
            ps.setInt(5, cliente.getId()); // id del cliente a actualizar

            int filas = ps.executeUpdate();
            return filas > 0;

        } catch (SQLException e) {
            System.out.println("error al actualizar cliente: " + e.getMessage());
            return false;
        }
    }

}
