package Persistencia;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionDB {
    
    private static final String URL = "jdbc:mysql://localhost:3306/TecnoStore";
    private static final String USUARIO = "root";
    private static final String PASSWORD = "kiane1750";
    
    public static Connection obtenerConexion(){
    Connection conexion = null;
    
    try{
    conexion = DriverManager.getConnection(URL, USUARIO, PASSWORD);
        System.out.println("Conexion exitosa");
    }catch(SQLException e){
        System.out.println("Error al conectar la base de datos");
        e.printStackTrace();
    }
        return conexion;
    
    }
    
    
}
