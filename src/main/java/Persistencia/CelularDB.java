package Persistencia;

import Modelo.CategoriaGama;
import Modelo.Celular;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CelularDB {

    // metodo para insertar celulares
    public boolean insertarCelular(Celular celular) {
        String sql = "INSERT INTO celulares (marca, modelo, sistema_operativo, gama precio, stock) VALUES (?, ?, ?, ?, ?, ?)";

        try (
                
            Connection conn = ConexionDB.obtenerConexion();
            PreparedStatement ps = conn.prepareStatement(sql)){
            
            ps.setString(1, celular.getMarca().getNombre());
            ps.setString(2, celular.getModelo());
            ps.setString(3, celular.getSistemaOperativo());
            ps.setString(4, celular.getGama().name());
            ps.setDouble(5, celular.getPrecio());
            ps.setInt(6, celular.getStock());

            ps.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.out.println("error al insertar celular: " + e.getMessage());
            return false;
        }
    }

    //metodo para listar los celulares
    public List<Celular> obtenerCelulares() {

        List<Celular> lista = new ArrayList<>();
        String sql = "SELECT * FROM celulares";

        try (
            Connection conn = ConexionDB.obtenerConexion();
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery()){

            while (rs.next()) {
                Celular c = new Celular();

                c.setId(rs.getInt("id"));
                c.setModelo(rs.getString("modelo"));
                c.setSistemaOperativo(rs.getString("sistema_operativo"));
                c.setPrecio(rs.getDouble("precio"));
                c.setStock(rs.getInt("stock"));

                // convertir texto de la bd a enum
                c.setGama(CategoriaGama.valueOf(rs.getString("gama")));

                lista.add(c);
            }
        } catch (SQLException e) {
            System.out.println("error al listar los celulares " + e.getLocalizedMessage());
        }
        return lista;

    }

    // para buscar por id
    public Celular buscarPorId(int id) {

        String sql = "SELECT * FROM celulares WHERE id = ?";

        try(
            Connection conn = ConexionDB.obtenerConexion();
            PreparedStatement ps = conn.prepareStatement(sql)){
            ps.setInt(1, id);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                Celular c = new Celular();

                c.setId(rs.getInt("id"));
                c.setModelo(rs.getString("modelo"));
                c.setSistemaOperativo(rs.getString("sistema_operativo"));
                c.setPrecio(rs.getDouble("precio"));
                c.setStock(rs.getInt("stock"));
                c.setGama(CategoriaGama.valueOf(rs.getString("gama")));

                return c;
            }

        } catch (SQLException e) {
            System.out.println("error  al buscar celular " + e.getLocalizedMessage());
        }

        return null;
    }

    // para actualizar el stock
    public boolean actualizarStock(int id, int nuevoStock) {
        String sql = "UPDATE celulares SET stock = ? WHERE id = ?";

        try (
            Connection conn = ConexionDB.obtenerConexion();
            PreparedStatement ps = conn.prepareStatement(sql)){

            ps.setInt(1, nuevoStock);
            ps.setInt(2, id);

            int filas = ps.executeUpdate();
            return filas > 0;

        } catch (SQLException e) {
            System.out.println("error al actualizar el stock: " + e.getMessage());
            return false;

        }

    }

    
    
    
    
    
    // borrar celular
    
        public boolean eliminarCelular(int id) {

        String sql = "DELETE FROM celulares WHERE id = ?";

        try (
            Connection conn = ConexionDB.obtenerConexion();
            PreparedStatement ps = conn.prepareStatement(sql)){
            ps.setInt(1, id);

            int filas = ps.executeUpdate();
            return filas > 0;

        } catch (SQLException e) {
            System.out.println("Error al eliminar celular: " + e.getMessage());
            return false;
        }
    }
}

