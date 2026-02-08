package Persistencia;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

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
}
