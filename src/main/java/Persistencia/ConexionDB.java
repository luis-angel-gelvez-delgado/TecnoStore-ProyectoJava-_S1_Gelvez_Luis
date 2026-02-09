package Persistencia;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionDB {
    
    // url de conexión
    private static final String URL = "jdbc:mysql://localhost:3306/TecnoStore";
    private static final String USUARIO = "root";
    private static final String PASSWORD = "kiane1750";
    
     //  crea una nueva conexión cada vez
    public static Connection obtenerConexion() {
        Connection conexion = null;
        
        try {
            conexion = DriverManager.getConnection(URL, USUARIO, PASSWORD);
            System.out.println("conexion exitosa a la base de datos");
        } catch (SQLException e) {
            System.out.println("error al conectar a la base de datos");
            e.printStackTrace();
        }
        
        return conexion;
    }
}
