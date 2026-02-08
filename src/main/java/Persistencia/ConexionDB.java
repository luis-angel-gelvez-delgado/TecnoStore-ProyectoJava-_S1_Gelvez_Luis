package Persistencia;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionDB {
    
    // url de conexión
    private static final String URL = "jdbc:mysql://localhost:3306/TecnoStore";
    private static final String USUARIO = "root";
    private static final String PASSWORD = "kiane1750";
    
    // instancia unica (singelton)
    private static ConexionDB instancia;
    private Connection conexion;
    
    // constructor privado (para que no se pueda instanciar desde fuera)
    private ConexionDB() {
        try {
            conexion = DriverManager.getConnection(URL, USUARIO, PASSWORD);
            System.out.println("conexion exitosa a la base de datos");
        } catch (SQLException e) {
            System.out.println("error al conectar a la base de datos");
            e.printStackTrace();
        }
    }
    
    // metodo para obtener la única instancia 
    public static ConexionDB getInstancia() {
        if (instancia == null) {
            instancia = new ConexionDB();
        }
        return instancia;
    }
    
    // metodo para obtener la conexión 
    public static Connection obtenerConexion() {
        return getInstancia().conexion;
    }
    
    // metodo para cerrar la conexión
    public void cerrarConexion() {
        if (conexion != null) {
            try {
                conexion.close();
                System.out.println("conexión cerrada");
            } catch (SQLException e) {
                System.out.println("error al cerrar la conexión");
                e.printStackTrace();
            }
        }
    }
}
