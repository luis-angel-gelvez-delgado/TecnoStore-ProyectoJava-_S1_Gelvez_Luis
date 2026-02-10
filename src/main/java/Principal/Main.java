package Principal;

// importamos los controladores que manejan la logica del programa
// los controladores son como el cerebro del sistema
import Controlador.CelularControlador;
import Controlador.ClienteControlador;
import Controlador.VentaControlador;

// importamos la clase que muestra el menu en pantalla
// esta es la parte visual que el usuario ve
import Vista.MenuPrincipal;

// importamos scanner para poder leer datos desde el teclado
import java.util.Scanner;

// clase principal que arranca todo el programa
// cuando ejecutas el programa, empieza aqui
public class Main {

    // metodo main: el punto de partida del programa
    // java busca este metodo para saber por donde empezar
    public static void main(String[] args) {

        // paso 1: crear las instancias de los controladores
        // un controlador es como un intermediario entre lo que el usuario ve
        // y donde se guardan los datos (base de datos)
        
        // este controlador maneja todo lo relacionado a celulares
        // registrar, listar, buscar, eliminar, etc
        CelularControlador celularControlador = new CelularControlador();

        // este controlador maneja todo lo relacionado a clientes
        // validaciones de correo, documento unico, etc
        ClienteControlador clienteControlador = new ClienteControlador();

        // este controlador maneja todo lo relacionado a ventas
        // calcular iva, actualizar stock, generar reportes
        VentaControlador ventaControlador = new VentaControlador();

        // paso 2: crear un objeto scanner para poder leer lo que el usuario escribe
        // esto permite que el programa "escuche" lo que escribes en el teclado
        Scanner scanner = new Scanner(System.in);

        // paso 3: crear el menu principal y pasarle los controladores y el scanner
        // el menu necesita los controladores para ejecutar acciones
        // (ejemplo: cuando eliges "registrar celular", el menu llama al controlador)
        // y necesita el scanner para leer las opciones que eliges
        MenuPrincipal menu = new MenuPrincipal(
            celularControlador,
            clienteControlador,
            ventaControlador,
            scanner
        );

        // paso 4: mostrar el menu en pantalla y empezar a interactuar
        // desde aqui el usuario puede elegir opciones del menu
        // el programa se queda en este metodo hasta que el usuario elija salir
        menu.mostrarMenu();

        // paso 5: cerrar el scanner cuando el programa termina
        // esto libera los recursos del sistema
        // es como cerrar una puerta cuando sales de una habitacion
        scanner.close();

        // mensaje de despedida que se muestra al cerrar el programa
        System.out.println("sistema finalizado, vuelve pronto!!!!");
    }

}