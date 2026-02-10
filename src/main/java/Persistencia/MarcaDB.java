package Persistencia;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import Modelo.Marca;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

// clase DAO para marcas de celulares
// maneja las operaciones de base de datos de la tabla marcas
public class MarcaDB {

    // metodo para guardar una nueva marca en la base de datos
    public void guardarMarca(String nombreMarca) {

        String sql = "INSERT INTO marcas (nombre) VALUES (?)";

        try (Connection con = ConexionDB.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, nombreMarca); 
            ps.executeUpdate(); // ejecutar el insert

            System.out.println("Marca registrada correctamente");

        } catch (SQLException e) {
            System.out.println("Error al guardar la marca");
            e.printStackTrace();
        }
    }
    
    // metodo para traer todas las marcas de la base de datos
    public List<Marca> obtenerMarcas() {
        List<Marca> marcas = new ArrayList<>();
        String sql = "SELECT * FROM marcas";
        
        try (Connection conn = ConexionDB.obtenerConexion();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            // recorrer cada marca
            while (rs.next()) {
                Marca m = new Marca();
                m.setId(rs.getInt("id"));
                m.setNombre(rs.getString("nombre"));
                marcas.add(m);
            }
        } catch (SQLException e) {
            System.out.println("error al obtener marcas: " + e.getMessage());
        }
        
        return marcas;
    }

    // metodo para buscar una marca especifica por su id
    public Marca buscarPorId(int id) {
        String sql = "SELECT * FROM marcas WHERE id = ?";
        
        try (Connection conn = ConexionDB.obtenerConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            
            // si encuentra la marca, devolverla
            if (rs.next()) {
                Marca m = new Marca();
                m.setId(rs.getInt("id"));
                m.setNombre(rs.getString("nombre"));
                return m;
            }
        } catch (SQLException e) {
            System.out.println("Error al buscar marca: " + e.getMessage());
        }
        
        return null; // no encontro nada
    }
    
    
}