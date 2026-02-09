package Vista;

import Controlador.CelularControlador;
import Controlador.ClienteControlador;
import Controlador.VentaControlador;
import Modelo.CategoriaGama;
import Modelo.Celular;
import Modelo.Cliente;
import Modelo.ItemVenta;
import Modelo.Marca;
import Modelo.Venta;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class MenuPrincipal {

    private CelularControlador celularControlador;
    private ClienteControlador clienteControlador;
    private VentaControlador ventaControlador;
    private Scanner scanner;

    public MenuPrincipal(CelularControlador celularControlador, ClienteControlador clienteControlador,
            VentaControlador ventaControlador, Scanner scanner) {
        this.celularControlador = celularControlador;
        this.clienteControlador = clienteControlador;
        this.ventaControlador = ventaControlador;
        this.scanner = scanner;
    }

    public void mostrarMenu() {
        int opcion;

        do {
            System.out.println("\n========================================");
            System.out.println("    bienvenido a TecnoStore");
            System.out.println("========================================");
            System.out.println("1. Gestion de Celulares");
            System.out.println("2. Gestion de Clientes");
            System.out.println("3. Gestion de Ventas");
            System.out.println("4. Reportes");
            System.out.println("5. Salir");
            System.out.println("========================================");
            System.out.print("Seleccione una opcion (debe ser numerica, ejemplo: 1): ");

            opcion = scanner.nextInt();
            scanner.nextLine();

            switch (opcion) {
                case 1:
                    menuCelulares();
                    break;
                case 2:
                    menuClientes();
                    break;
                case 3:
                    menuVentas();
                    break;
                case 4:
                    menuReportes();
                    break;
                case 5:
                    System.out.println("saliendo del sistema...");
                    break;
                default:
                    System.out.println("opcion invalida, intente de nuevo.");
            }

        } while (opcion != 5);
    }

    // menus: 
    // menu de celulares
    private void menuCelulares() {
        int opcion;

        do {
            System.out.println("\n=== GESTION DE CELULARES ===");
            System.out.println("1. Registrar celular");
            System.out.println("2. Listar celulares");
            System.out.println("3. Buscar celular por ID");
            System.out.println("4. Actualizar stock");
            System.out.println("5. Eliminar celular");
            System.out.println("6. Menu principal");
            System.out.print("Opcion: ");

            opcion = scanner.nextInt();
            scanner.nextLine();

            switch (opcion) {
                case 1:
                    registrarCelular();
                    break;
                case 2:
                    listarCelulares();
                    break;
                case 3:
                    buscarCelular();
                    break;
                case 4:
                    actualizarStockCelular();
                    break;
                case 5:
                    eliminarCelular();
                    break;
                case 6:
                    System.out.println("Volviendo al menu principal...");
                    break;
                default:
                    System.out.println("Opcion invalida");
            }

        } while (opcion != 6);
    }

    private void registrarCelular() {
        System.out.println("\n--- Registrar Celular ---");

        System.out.print("Marca: ");
        String nombreMarca = scanner.nextLine();
        Marca marca = new Marca();
        marca.setNombre(nombreMarca);

        System.out.print("Modelo: ");
        String modelo = scanner.nextLine();

        System.out.print("Sistema Operativo: ");
        String so = scanner.nextLine();

        System.out.print("Gama (ALTA/MEDIA/BAJA): ");
        String gamaStr = scanner.nextLine().toUpperCase();
        CategoriaGama gama = CategoriaGama.valueOf(gamaStr);

        System.out.print("Precio: ");
        double precio = scanner.nextDouble();

        System.out.print("Stock: ");
        int stock = scanner.nextInt();
        scanner.nextLine();

        Celular celular = new Celular(0, marca, modelo, so, gama, precio, stock);
        celularControlador.registrarCelular(celular);
    }

    private void listarCelulares() {
        List<Celular> lista = celularControlador.obtenerListaCelulares();

        if (lista.isEmpty()) {
            System.out.println("No hay celulares registrados");
            return;
        }

        System.out.println("\n========================================");
        System.out.println("         LISTADO DE CELULARES");
        System.out.println("========================================");

        for (Celular c : lista) {
            System.out.println("\nID: " + c.getId());
            System.out.println("Marca: " + c.getMarca().getNombre());
            System.out.println("Modelo: " + c.getModelo());
            System.out.println("Sistema Operativo: " + c.getSistemaOperativo());
            System.out.println("Gama: " + c.getGama());
            System.out.println("Precio: $" + String.format("%,.0f", c.getPrecio()));
            System.out.println("Stock: " + c.getStock() + " unidades");
            System.out.println("----------------------------------------");
        }
    }

    private void buscarCelular() {
        System.out.print("Ingrese ID del celular: ");
        int id = scanner.nextInt();
        scanner.nextLine();

        Celular c = celularControlador.buscarCelularPorId(id);
        if (c != null) {
            System.out.println(c);
        } else {
            System.out.println("Celular no encontrado");
        }
    }

    private void actualizarStockCelular() {
        System.out.print("ID del celular: ");
        int id = scanner.nextInt();

        System.out.print("Nuevo stock: ");
        int nuevoStock = scanner.nextInt();
        scanner.nextLine();

        celularControlador.actualizarStock(id, nuevoStock);
    }

    private void eliminarCelular() {
        System.out.print("ID del celular a eliminar: ");
        int id = scanner.nextInt();
        scanner.nextLine();

        celularControlador.eliminarCelular(id);
    }

    // menu de clientes
    private void menuClientes() {
        int opcion;

        do {
            System.out.println("\n=== GESTINO DE CLIENTES ===");
            System.out.println("1. Registrar cliente");
            System.out.println("2. Listar clientes");
            System.out.println("3. Buscar cliente por ID");
            System.out.println("4. Eliminar cliente");
            System.out.println("5. Volver al menu principal");
            System.out.print("Opcion: ");

            opcion = scanner.nextInt();
            scanner.nextLine();

            switch (opcion) {
                case 1:
                    registrarCliente();
                    break;
                case 2:
                    listarClientes();
                    break;
                case 3:
                    buscarCliente();
                    break;
                case 4:
                    eliminarCliente();
                    break;
                case 5:
                    System.out.println("Volviendo al menu principal...");
                    break;
                default:
                    System.out.println("Opcion invalida");
            }

        } while (opcion != 5);
    }

    private void registrarCliente() {
        System.out.println("\n--- Registrar Cliente ---");

        System.out.print("Nombre: ");
        String nombre = scanner.nextLine();

        System.out.print("Documento de identidad: ");
        String documento = scanner.nextLine();

        System.out.print("Correo electronico: ");
        String correo = scanner.nextLine();

        System.out.print("Telefono: ");
        String telefono = scanner.nextLine();

        Cliente cliente = new Cliente(correo, 0, nombre, documento, telefono);
        clienteControlador.registarCliente(cliente);
    }

    private void listarClientes() {
        List<Cliente> lista = clienteControlador.obtenerListaClientes();

        if (lista.isEmpty()) {
            System.out.println("no hay clientes registrados");
            return;
        }

        System.out.println("\n========================================");
        System.out.println("          LISTA DE CLIENTES");
        System.out.println("========================================");

        for (Cliente c : lista) {
            System.out.println("\nID: " + c.getId());
            System.out.println("Nombre: " + c.getNombre());
            System.out.println("Documento: " + c.getDocumento());
            System.out.println("Correo: " + c.getCorreo());
            System.out.println("Telefono: " + c.getTelefono());
            System.out.println("----------------------------------------");
        }
    }

    private void buscarCliente() {
        System.out.print("Ingrese ID del cliente: ");
        int id = scanner.nextInt();
        scanner.nextLine();

        Cliente c = clienteControlador.buscarClientePorId(id);
        if (c != null) {
            System.out.println(c);
        } else {
            System.out.println("Cliente no encontrado");
        }
    }

    private void eliminarCliente() {
        System.out.print("ID del cliente a eliminar: ");
        int id = scanner.nextInt();
        scanner.nextLine();

        clienteControlador.eliminarCliente(id);
    }

    // menu de las ventas
    private void menuVentas() {
        int opcion;

        do {
            System.out.println("\n=== GESTION DE VENTAS ===");
            System.out.println("1. Registrar venta");
            System.out.println("2. Listar ventas");
            System.out.println("3. Volver al menu principal");
            System.out.print("Opcion: ");

            opcion = scanner.nextInt();
            scanner.nextLine();

            switch (opcion) {
                case 1:
                    registrarVenta();
                    break;
                case 2:
                    listarVentas();
                    break;
                case 3:
                    System.out.println("Volviendo al menu principal...");
                    break;
                default:
                    System.out.println("Opcion invalida");
            }

        } while (opcion != 3);
    }

    private void registrarVenta() {
        System.out.println("\n--- Registrar Venta ---");

        System.out.print("ID del cliente: ");
        int idCliente = scanner.nextInt();
        scanner.nextLine();

        List<Integer> idsCelulares = new ArrayList<>();
        List<Integer> cantidades = new ArrayList<>();

        String continuar;
        do {
            System.out.print("ID del celular: ");
            int idCelular = scanner.nextInt();

            System.out.print("Cantidad: ");
            int cantidad = scanner.nextInt();
            scanner.nextLine();

            idsCelulares.add(idCelular);
            cantidades.add(cantidad);

            System.out.print("¿Agregar otro celular? (s/n): ");
            continuar = scanner.nextLine();

        } while (continuar.equalsIgnoreCase("s"));

        ventaControlador.registrarVenta(idCliente, idsCelulares, cantidades);
    }

    private void listarVentas() {
        List<Venta> ventas = ventaControlador.obtenerListaVentas();

        if (ventas.isEmpty()) {
            System.out.println("No hay ventas registradas");
            return;
        }

        System.out.println("\n========================================");
        System.out.println("          LISTA DE VENTAS");
        System.out.println("========================================");

        for (Venta v : ventas) {
            System.out.println("\nVenta ID: " + v.getId());
            System.out.println("Cliente: " + v.getCliente().getNombre());
            System.out.println("Fecha: " + v.getFecha());
            System.out.println("Items vendidos:");

            for (ItemVenta item : v.getItems()) {
                System.out.println("  - " + item.getCelular().getMarca().getNombre()
                        + " " + item.getCelular().getModelo()
                        + " | Cantidad: " + item.getCantidad()
                        + " | Subtotal: $" + String.format("%,.0f", item.getSubtotal()));
            }

            System.out.println("TOTAL (con IVA): $" + String.format("%,.0f", v.getTotal()));
            System.out.println("----------------------------------------");
        }
    }

    // menu de reportes
    private void menuReportes() {
        int opcion;

        do {
            System.out.println("\n=== REPORTES ===");
            System.out.println("1. Celulares con stock bajo");
            System.out.println("2. 3 celulares mas vendidos");
            System.out.println("3. Ventas totales por mes");
            System.out.println("4. Generar reporte de ventas (TXT)");
            System.out.println("5. Menu principal");
            System.out.print("Opcion: ");

            opcion = scanner.nextInt();
            scanner.nextLine();

            switch (opcion) {
                case 1:
                    celularControlador.mostrarCelularesStockBajo();
                    break;
                case 2:
                    ventaControlador.mostrarTop3MasVendidos();
                    break;
                case 3:
                    ventaControlador.mostrarVentasPorMes();
                    break;
                case 4:
                    ventaControlador.generarReporteVentasTxt();
                    break;
                case 5:
                    System.out.println("Volviendo al menu principal...");
                    break;
                default:
                    System.out.println("Opción invalida");
            }

        } while (opcion != 5);
    }

}
