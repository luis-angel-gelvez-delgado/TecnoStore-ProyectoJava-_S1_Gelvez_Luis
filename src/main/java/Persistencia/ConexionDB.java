package Persistencia;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

// clase que maneja la conexion a la base de datos mysql
// cada vez que alguien necesita hablar con la bd, usa esta clase
public class ConexionDB {
    
    // datos de conexion a mysql (configuracion)
    // jdbc:mysql://localhost:3306/TecnoStore significa:
    // - jdbc:mysql: tipo de base de datos
    // - localhost: el servidor esta en esta misma computadora
    // - 3306: puerto por defecto de mysql
    // - TecnoStore: nombre de la base de datos
    private static final String URL = "jdbc:mysql://localhost:3306/TecnoStore";
    private static final String USUARIO = "root"; // usuario de mysql
    private static final String PASSWORD = "kiane1750"; // contraseña de mysql
    
    // metodo estatico que crea y devuelve una conexion a la base de datos
    // estatico significa que no necesitas crear un objeto ConexionDB para usarlo
    // cada vez que se llama, crea una conexion nueva
    public static Connection obtenerConexion() {
        Connection conexion = null;
        
        try {
            // intenta conectarse a mysql con los datos de arriba
            conexion = DriverManager.getConnection(URL, USUARIO, PASSWORD);
            System.out.println("conexion exitosa a la base de datos");
        } catch (SQLException e) {
            // si algo sale mal (usuario incorrecto, bd no existe, etc), muestra error
            System.out.println("error al conectar a la base de datos");
            e.printStackTrace(); // muestra detalles del error
        }
        
        return conexion; // devuelve la conexion (o null si fallo)
    }
}