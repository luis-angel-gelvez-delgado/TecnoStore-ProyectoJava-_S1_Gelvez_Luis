package Vista;

// controladores que conectan la vista (lo que se ve) con la logica del sistema
import Controlador.CelularControlador;
import Controlador.ClienteControlador;
import Controlador.VentaControlador;

// clases del modelo que representan los datos del negocio
import Modelo.CategoriaGama;
import Modelo.Celular;
import Modelo.Cliente;
import Modelo.ItemVenta;
import Modelo.Marca;
import Modelo.Venta;
import Modelo.FactoryCelular;

// clases de persistencia para acceso directo a marcas
import Persistencia.MarcaDB;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

// este import que capta un dato que es diferente al esperado, ejemplo: si se pide numero y el usuario pone "gf" se lanza el error
import java.util.InputMismatchException;

// esta clase es la interfaz de usuario del sistema
// muestra todos los menus y captura la informacion que el usuario escribe
// es como el tablero de control del sistema
public class MenuPrincipal {

    // atributos privados: referencias a los controladores
    // estos objetos permiten ejecutar las acciones del sistema
    private CelularControlador celularControlador;
    private ClienteControlador clienteControlador;
    private VentaControlador ventaControlador;

    // scanner para leer lo que el usuario escribe en el teclado
    private Scanner scanner;

    // constructor: se ejecuta cuando se crea un objeto MenuPrincipal
    // recibe los controladores y el scanner desde Main.java
    // esto se llama "inyeccion de dependencias"
    public MenuPrincipal(CelularControlador celularControlador, ClienteControlador clienteControlador,
            VentaControlador ventaControlador, Scanner scanner) {
        this.celularControlador = celularControlador;
        this.clienteControlador = clienteControlador;
        this.ventaControlador = ventaControlador;
        this.scanner = scanner;
    }

    // ========== metodos auxiliares para lectura segura ==========
    
    // metodo que lee un numero entero de forma segura
    // si el usuario escribe letras, no se rompe el programa
    // en su lugar, muestra un error y vuelve a preguntar
    private int leerEntero(String mensaje) {
        while (true) {
            try {
                System.out.print(mensaje);
                int valor = scanner.nextInt();
                scanner.nextLine(); // limpiar buffer del scanner
                return valor;
            } catch (InputMismatchException e) {
                // si escribe algo que no es numero, mostrar error
                System.out.println("error: Debe ingresar un numero entero valido");
                scanner.nextLine(); // limpiar el buffer para no quedar en loop infinito
            }
        }
    }

    // metodo que lee un numero decimal de forma segura
    // funciona igual que leerEntero pero acepta decimales
    private double leerDecimal(String mensaje) {
        while (true) {
            try {
                System.out.print(mensaje);
                double valor = scanner.nextDouble();
                scanner.nextLine(); // limpiar buffer
                return valor;
            } catch (InputMismatchException e) {
                System.out.println("error: debe ingresar un numero decimal valido (ejemplo: 1500000.50)");
                scanner.nextLine(); // limpiar buffer
            }
        }
    }

    // ========== MENU PRINCIPAL ==========
    
    // metodo principal que muestra el menu inicial
    // este menu se repite hasta que el usuario elija salir
    public void mostrarMenu() {
        int opcion;

        // do-while: ejecuta el menu al menos una vez
        // y sigue repitiendose mientras opcion sea diferente de 5
        do {
            // dibuja el menu en pantalla
            System.out.println("\n========================================");
            System.out.println("    bienvenido a TecnoStore");
            System.out.println("========================================");
            System.out.println("1. Gestion de Celulares");
            System.out.println("2. Gestion de Clientes");
            System.out.println("3. Gestion de Ventas");
            System.out.println("4. Reportes");
            System.out.println("5. Salir");
            System.out.println("========================================");

            // usa el metodo seguro para leer la opcion
            opcion = leerEntero("Seleccione una opcion: ");

            // segun la opcion elegida, llama al submenu correspondiente
            switch (opcion) {
                case 1:
                    menuCelulares(); // abre el menu de celulares
                    break;
                case 2:
                    menuClientes(); // abre el menu de clientes
                    break;
                case 3:
                    menuVentas(); // abre el menu de ventas
                    break;
                case 4:
                    menuReportes(); // abre el menu de reportes
                    break;
                case 5:
                    System.out.println("saliendo del sistema...");
                    break;
                default:
                    // si escribe un numero que no existe, muestra error
                    System.out.println("opcion invalida, intente de nuevo.");
            }

        } while (opcion != 5); // repite hasta que elija salir
    }

    // ================= submenu: gestion de celulares =================
    
    // este menu maneja todo lo relacionado con celulares
    private void menuCelulares() {
        int opcion;

        do {
            System.out.println("\n=== GESTION DE CELULARES ===");
            System.out.println("1. Registrar celular");
            System.out.println("2. Listar celulares");
            System.out.println("3. Buscar celular por ID");
            System.out.println("4. Actualizar stock");
            System.out.println("5. Eliminar celular");
            System.out.println("6. Registrar nueva marca");
            System.out.println("7. Menu principal");

            opcion = leerEntero("Opcion: ");

            // ejecuta la accion segun la opcion
            switch (opcion) {
                case 1:
                    registrarCelular(); // pide datos y guarda un celular nuevo
                    break;
                case 2:
                    listarCelulares(); // muestra todos los celulares
                    break;
                case 3:
                    buscarCelular(); // busca un celular especifico
                    break;
                case 4:
                    actualizarStockCelular(); // cambia el stock
                    break;
                case 5:
                    eliminarCelular(); // borra un celular
                    break;
                case 6:
                    registrarNuevaMarca(); // registra una marca nueva
                    break;
                case 7:
                    System.out.println("Volviendo al menu principal...");
                    break;
                default:
                    System.out.println("Opcion invalida");
            }

        } while (opcion != 7); // repite hasta que elija volver
    }

    // metodo que registra una nueva marca en la base de datos
    private void registrarNuevaMarca() {
        try {
            System.out.println("\n--- Registrar Nueva Marca ---");
            System.out.print("Nombre de la marca: ");
            String nombreMarca = scanner.nextLine();

            // validar que no este vacio
            if (nombreMarca.trim().isEmpty()) {
                System.out.println("error: el nombre de la marca no puede estar vacio");
                return;
            }

            // guardar en la base de datos
            MarcaDB marcaDB = new MarcaDB();
            marcaDB.guardarMarca(nombreMarca);

        } catch (Exception e) {
            System.out.println("error al registrar marca: " + e.getMessage());
        }
    }

    // metodo que pide todos los datos para registrar un celular
    // ahora muestra las marcas disponibles primero
    private void registrarCelular() {
        try {
            System.out.println("\n--- Registrar Celular ---");

            // paso 1: mostrar marcas disponibles
            System.out.println("\n=== MARCAS DISPONIBLES ===");
            MarcaDB marcaDB = new MarcaDB();
            List<Marca> marcasDisponibles = marcaDB.obtenerMarcas();

            // validar que haya marcas registradas
            if (marcasDisponibles.isEmpty()) {
                System.out.println("No hay marcas registradas.");
                System.out.println("Por favor, registre una marca primero (opcion 6 del menu).");
                return;
            }

            // mostrar la lista numerada de marcas
            for (Marca m : marcasDisponibles) {
                System.out.println(m.getId() + ". " + m.getNombre());
            }
            System.out.println("==========================\n");

            // paso 2: pedir que seleccione una marca
            int idMarca = leerEntero("Selecciona el ID de la marca: ");

            // buscar la marca seleccionada
            Marca marcaSeleccionada = marcaDB.buscarPorId(idMarca);

            if (marcaSeleccionada == null) {
                System.out.println("Error: Marca no encontrada con ese ID");
                return;
            }

            // paso 3: pedir los demas datos del celular
            System.out.print("Modelo: ");
            String modelo = scanner.nextLine();

            System.out.print("Sistema Operativo: ");
            String so = scanner.nextLine();

            System.out.print("Gama (ALTA/MEDIA/BAJA): ");
            String gamaStr = scanner.nextLine().toUpperCase();

            // validar que la gama existe
            CategoriaGama gama;
            try {
                gama = CategoriaGama.valueOf(gamaStr);
            } catch (IllegalArgumentException e) {
                System.out.println("error: gama invalida, use ALTA, MEDIA o BAJA");
                return;
            }

            // pedir precio y stock usando metodos seguros
            double precio = leerDecimal("Precio: ");
            int stock = leerEntero("Stock: ");

            // crear el celular usando factory
            Celular celular = FactoryCelular.crearCelular(gamaStr, marcaSeleccionada, modelo, so, precio, stock);

            // enviar al controlador para guardar
            celularControlador.registrarCelular(celular);

        } catch (Exception e) {
            System.out.println("error al registrar celular: " + e.getMessage());
            scanner.nextLine(); // limpiar buffer
        }
    }

    // metodo que muestra todos los celulares con formato bonito
    // usa string.format para darle formato al precio con puntos de miles
    private void listarCelulares() {
        // traer todos los celulares desde el controlador
        List<Celular> lista = celularControlador.obtenerListaCelulares();

        // si no hay celulares, avisar y salir
        if (lista.isEmpty()) {
            System.out.println("No hay celulares registrados");
            return;
        }

        // mostrar encabezado
        System.out.println("\n========================================");
        System.out.println("         LISTADO DE CELULARES");
        System.out.println("========================================");

        // recorrer cada celular y mostrarlo con formato
        for (Celular c : lista) {
            System.out.println("\nID: " + c.getId());
            System.out.println("Marca: " + c.getMarca().getNombre());
            System.out.println("Modelo: " + c.getModelo());
            System.out.println("Sistema Operativo: " + c.getSistemaOperativo());
            System.out.println("Gama: " + c.getGama());
            // %,.0f = formato con comas o puntos de miles, sin decimales
            System.out.println("Precio: $" + String.format("%,.0f", c.getPrecio()));
            System.out.println("Stock: " + c.getStock() + " unidades");
            System.out.println("----------------------------------------");
        }
    }

    // busca y muestra un celular especifico por su id
    private void buscarCelular() {
        try {
            int id = leerEntero("Ingrese ID del celular: ");

            // buscar el celular usando el controlador
            Celular c = celularControlador.buscarCelularPorId(id);

            // si lo encuentra, lo muestra sino, avisa
            if (c != null) {
                System.out.println(c);
            } else {
                System.out.println("Celular no encontrado");
            }
        } catch (Exception e) {
            System.out.println("error al buscar celular: " + e.getMessage());
        }
    }

    // cambia el stock de un celular existente
    private void actualizarStockCelular() {
        try {
            int id = leerEntero("ID del celular: ");
            int nuevoStock = leerEntero("Nuevo stock: ");

            // llamar al controlador para actualizar
            celularControlador.actualizarStock(id, nuevoStock);
        } catch (Exception e) {
            System.out.println("Error al actualizar stock: " + e.getMessage());
        }
    }

    // elimina un celular de la base de datos
    private void eliminarCelular() {
        try {
            int id = leerEntero("ID del celular a eliminar: ");
            celularControlador.eliminarCelular(id);
        } catch (Exception e) {
            System.out.println("Error al eliminar celular: " + e.getMessage());
        }
    }

    // ================= submenu: gestion de clientes =================
    
    // este menu maneja todo lo relacionado con clientes
    private void menuClientes() {
        int opcion;

        do {
            System.out.println("\n=== GESTION DE CLIENTES ===");
            System.out.println("1. Registrar cliente");
            System.out.println("2. Listar clientes");
            System.out.println("3. Buscar cliente por ID");
            System.out.println("4. Eliminar cliente");
            System.out.println("5. Volver al menu principal");

            opcion = leerEntero("Opcion: ");

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

    // pide los datos y registra un nuevo cliente
    // el controlador se encarga de validar correo y documento
    private void registrarCliente() {
        try {
            System.out.println("\n--- Registrar Cliente ---");

            System.out.print("Nombre: ");
            String nombre = scanner.nextLine();

            System.out.print("Documento de identidad: ");
            String documento = scanner.nextLine();

            System.out.print("Correo electronico: ");
            String correo = scanner.nextLine();

            System.out.print("Telefono: ");
            String telefono = scanner.nextLine();

            // crear el objeto cliente con todos sus datos
            // el 0 en el id porque la base de datos lo genera automaticamente
            Cliente cliente = new Cliente(correo, 0, nombre, documento, telefono);

            // enviar al controlador para validar y guardar
            clienteControlador.registarCliente(cliente);

        } catch (Exception e) {
            System.out.println("error al registrar cliente: " + e.getMessage());
            scanner.nextLine();
        }
    }

    // muestra todos los clientes con formato
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

    // busca un cliente por su id
    private void buscarCliente() {
        try {
            int id = leerEntero("Ingrese ID del cliente: ");

            Cliente c = clienteControlador.buscarClientePorId(id);
            if (c != null) {
                System.out.println(c);
            } else {
                System.out.println("Cliente no encontrado");
            }
        } catch (Exception e) {
            System.out.println("Error al buscar cliente: " + e.getMessage());
        }
    }

    // elimina un cliente de la base de datos
    private void eliminarCliente() {
        try {
            int id = leerEntero("ID del cliente a eliminar: ");
            clienteControlador.eliminarCliente(id);
        } catch (Exception e) {
            System.out.println("Error al eliminar cliente: " + e.getMessage());
        }
    }

    // ================= submenu: gestion de venas =================
    
    // este menu permite registrar y listar ventas
    private void menuVentas() {
        int opcion;

        do {
            System.out.println("\n=== GESTION DE VENTAS ===");
            System.out.println("1. Registrar venta");
            System.out.println("2. Listar ventas");
            System.out.println("3. Volver al menu principal");

            opcion = leerEntero("Opcion: ");

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

    // registra una venta que puede tener varios celulares
    // permite agregar multiples items en una sola venta
    private void registrarVenta() {
        try {
            System.out.println("\n--- Registrar Venta ---");

            // pedir el id del cliente
            int idCliente = leerEntero("ID del cliente: ");

            // crear listas para guardar los celulares y cantidades
            List<Integer> idsCelulares = new ArrayList<>();
            List<Integer> cantidades = new ArrayList<>();

            String continuar;
            // do-while que permite agregar varios celulares
            do {
                int idCelular = leerEntero("ID del celular: ");
                int cantidad = leerEntero("Cantidad: ");

                // agregar a las listas
                idsCelulares.add(idCelular);
                cantidades.add(cantidad);

                // preguntar si quiere agregar otro celular
                System.out.print("¿Agregar otro celular? (s/n): ");
                continuar = scanner.nextLine();

            } while (continuar.equalsIgnoreCase("s")); // repite si escribe "s" o "S"

            // enviar toda la informacion al controlador
            // el controlador se encarga de validar, calcular iva, actualizar stock, etc
            ventaControlador.registrarVenta(idCliente, idsCelulares, cantidades);

        } catch (Exception e) {
            System.out.println("error al registrar venta: " + e.getMessage());
            scanner.nextLine();
        }
    }

    // muestra todas las ventas realizadas con sus detalles
    private void listarVentas() {
        List<Venta> ventas = ventaControlador.obtenerListaVentas();

        if (ventas.isEmpty()) {
            System.out.println("No hay ventas registradas");
            return;
        }

        System.out.println("\n========================================");
        System.out.println("          LISTA DE VENTAS");
        System.out.println("========================================");

        // recorrer cada venta
        for (Venta v : ventas) {
            System.out.println("\nVenta ID: " + v.getId());
            System.out.println("Cliente: " + v.getCliente().getNombre());
            System.out.println("Fecha: " + v.getFecha());
            System.out.println("Items vendidos:");

            // recorrer cada item de la venta
            for (ItemVenta item : v.getItems()) {
                System.out.println("  - " + item.getCelular().getMarca().getNombre()
                        + " " + item.getCelular().getModelo()
                        + " | Cantidad: " + item.getCantidad()
                        + " | Subtotal: $" + String.format("%,.0f", item.getSubtotal()));
            }

            // mostrar el total con iva y formato de miles
            System.out.println("TOTAL (con IVA): $" + String.format("%,.0f", v.getTotal()));
            System.out.println("----------------------------------------");
        }
    }

    // ================= submenu:reportes =================
    
    // este menu muestra reportes usando stream api
    private void menuReportes() {
        int opcion;

        do {
            System.out.println("\n=== REPORTES ===");
            System.out.println("1. Celulares con stock bajo");
            System.out.println("2. 3 celulares mas vendidos");
            System.out.println("3. Ventas totales por mes");
            System.out.println("4. Generar reporte de ventas (TXT)");
            System.out.println("5. Menu principal");

            opcion = leerEntero("Opcion: ");

            switch (opcion) {
                case 1:
                    // llama al controlador para mostrar stock bajo (usa stream api filter)
                    celularControlador.mostrarCelularesStockBajo();
                    break;
                case 2:
                    // muestra top 3 mas vendidos (usa stream api sorted, limit)
                    ventaControlador.mostrarTop3MasVendidos();
                    break;
                case 3:
                    // agrupa ventas por mes (usa stream api groupingby)
                    ventaControlador.mostrarVentasPorMes();
                    break;
                case 4:
                    // genera archivo txt con bufferedwriter
                    ventaControlador.generarReporteVentasTxt();
                    break;
                case 5:
                    System.out.println("Volviendo al menu principal...");
                    break;
                default:
                    System.out.println("Opcion invalida");
            }

        } while (opcion != 5);
    }

}