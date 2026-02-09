package Principal;

import Controlador.CelularControlador;
import Controlador.ClienteControlador;
import Controlador.VentaControlador;
import Vista.MenuPrincipal;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        // creamos las instancias de los controladores
        CelularControlador celularControlador = new CelularControlador();
        ClienteControlador clienteControlador = new ClienteControlador();
        VentaControlador ventaControlador = new VentaControlador();

        Scanner scanner = new Scanner(System.in);

        // ejeutar menu principal
        MenuPrincipal menu = new MenuPrincipal(celularControlador, clienteControlador, ventaControlador, scanner);
        menu.mostrarMenu();

        scanner.close();
        System.out.println("sistema finalizado, vuelve pronto!!!!");
    }

}
