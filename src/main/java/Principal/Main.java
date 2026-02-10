package Principal;

// importamos los controladores que manejan la logica del programa
import Controlador.CelularControlador;
import Controlador.ClienteControlador;
import Controlador.VentaControlador;

// importamos la clase que muestra el menu en pantalla
import Vista.MenuPrincipal;

// importamos scanner para poder leer datos desde el teclado
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        // creamos las instancias de los controladores
        // un controlador se encarga de manejar la logica entre la vista y los datos

        // controlador que maneja todo lo relacionado a celulares
        CelularControlador celularControlador = new CelularControlador();

        // controlador que maneja todo lo relacionado a clientes
        ClienteControlador clienteControlador = new ClienteControlador();

        // controlador que maneja todo lo relacionado a ventas
        VentaControlador ventaControlador = new VentaControlador();

        // creamos un objeto scanner para poder leer lo que el usuario escribe
        Scanner scanner = new Scanner(System.in);

        // creamos el menu principal y le enviamos los controladores y el scanner
        // esto permite que el menu pueda usar la logica del sistema y leer opciones del usuario
        MenuPrincipal menu = new MenuPrincipal(
            celularControlador,
            clienteControlador,
            ventaControlador,
            scanner
        );

        // llamamos al metodo que muestra el menu en pantalla
        // desde aqui el usuario podra elegir opciones
        menu.mostrarMenu();

        // cerramos el scanner cuando el programa termina
        // buena practica para liberar recursos
        scanner.close();

        // mensaje final que indica que el sistema se ha cerrado
        System.out.println("sistema finalizado, vuelve pronto!!!!");
    }

}
