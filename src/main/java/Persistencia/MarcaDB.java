package Persistencia;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import Modelo.Marca;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class MarcaDB {

    public void guardarMarca(String nombreMarca) {

        String sql = "INSERT INTO marcas (nombre) VALUES (?)";

        try (Connection con = ConexionDB.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, nombreMarca); 
            ps.executeUpdate();           

            System.out.println("Marca registrada correctamente");

        } catch (SQLException e) {
            System.out.println("Error al guardar la marca");
            e.printStackTrace();
        }
    }
    
    
    
    // Método para obtener todas las marcas
public List<Marca> obtenerMarcas() {
    List<Marca> marcas = new ArrayList<>();
    String sql = "SELECT * FROM marcas";
    
    try (Connection conn = ConexionDB.obtenerConexion();
         PreparedStatement ps = conn.prepareStatement(sql);
         ResultSet rs = ps.executeQuery()) {
        
        while (rs.next()) {
            Marca m = new Marca();
            m.setId(rs.getInt("id"));
            m.setNombre(rs.getString("nombre"));
            marcas.add(m);
        }
    } catch (SQLException e) {
        System.out.println("Error al obtener marcas: " + e.getMessage());
    }
    
    return marcas;
}

// Método para buscar marca por ID
public Marca buscarPorId(int id) {
    String sql = "SELECT * FROM marcas WHERE id = ?";
    
    try (Connection conn = ConexionDB.obtenerConexion();
         PreparedStatement ps = conn.prepareStatement(sql)) {
        
        ps.setInt(1, id);
        ResultSet rs = ps.executeQuery();
        
        if (rs.next()) {
            Marca m = new Marca();
            m.setId(rs.getInt("id"));
            m.setNombre(rs.getString("nombre"));
            return m;
        }
    } catch (SQLException e) {
        System.out.println("Error al buscar marca: " + e.getMessage());
    }
    
    return null;
}
    
    
    
    
}
