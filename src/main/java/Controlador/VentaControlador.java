package Controlador;

import Modelo.Celular;
import Modelo.Cliente;
import Modelo.ItemVenta;
import Modelo.Venta;
import Persistencia.CelularDB;
import Persistencia.ClienteDB;
import Persistencia.VentaDB;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;




// esta clase es la mas compleja, maneja ventas, reportes y archivos
// coordina las acciones entre celulares, clientes y ventas
public class VentaControlador {

        // objetos para comunicarse con las diferentes tablas de la bd
    private VentaDB ventaDB;
    private CelularDB celularDB;
    private ClienteDB clienteDB;

        // cuando se crea, inicializa las conexiones a las tres tablas
    public VentaControlador() {
        ventaDB = new VentaDB();
        celularDB = new CelularDB();
        clienteDB = new ClienteDB();
    }



// metodo principal para registrar una venta
    // puede vender varios celulares a la vez
    public void registrarVenta(int idCliente, List<Integer> idsCelulares, List<Integer> cantidades) {

        // paso 1: buscar al cliente en la base de datos
        Cliente cliente = clienteDB.buscarPorId(idCliente);
        if (cliente == null) {
            System.out.println("Error: Cliente no encontrado");
            return;// si no existe el cliente, no puede continuar
        }

        // paso 2: crear la venta nueva con la fecha actual
        Venta venta = new Venta();
        venta.setCliente(cliente);
        venta.setFecha(LocalDate.now());

        // paso 3: procesar cada celular que se esta vendiendo
        for (int i = 0; i < idsCelulares.size(); i++) {
            int idCelular = idsCelulares.get(i);
            int cantidad = cantidades.get(i);

            // buscar el celular en la base de datos
            Celular celular = celularDB.buscarPorId(idCelular);

                        // validacion: verificar que el celular exista
            if (celular == null) {
                System.out.println("Error: Celular con ID " + idCelular + " no encontrado");
                continue;// si no existe, pasa al siguiente
            }

            // validacion: verificar que haya suficiente stock
            if (celular.getStock() < cantidad) {
                System.out.println("Error: Stock insuficiente para " + celular.getMarca().getNombre()
                        + " " + celular.getModelo() + ". Disponible: " + celular.getStock());
                continue;// si no hay stock, pasa al siguiente
            }

            // calcular el subtotal de este item (precio x cantidad)
            double subtotal = celular.getPrecio() * cantidad;
            ItemVenta item = new ItemVenta(celular, cantidad, subtotal);



            // agregar el item a la venta
            // esto automaticamente calcula el total con iva del 19%
            venta.agregarItem(item);

            // actualizar el stock del celular (restar lo vendido)
            int nuevoStock = celular.getStock() - cantidad;
            celularDB.actualizarStock(idCelular, nuevoStock);
        }

        // validacion final: verificar que la venta tenga al menos un item
        if (venta.getItems().isEmpty()) {
            System.out.println("error: No se pudo procesar ningun item de la venta");
            return;
        }

        // guardar la venta completa en la base de datos
        ventaDB.guardarVenta(venta);

        
                // mostrar resumen de la venta en consola
        System.out.println("\n=== VENTA REGISTRADA ===");
        System.out.println("Cliente: " + cliente.getNombre());
        System.out.println("Fecha: " + venta.getFecha());
        System.out.println("Total (con IVA 19%): $" + String.format("%.2f", venta.getTotal()));
        System.out.println("========================\n");
    }


// metodo para mostrar los 3 celulares mas vendidos (reporte)
    // usa stream api para hacer calculos complejos de forma eficiente
    public void mostrarTop3MasVendidos() {
               // traer todas las ventas de la base de datos
        List<Venta> ventas = ventaDB.obtenerTodasLasVentas();

        if (ventas.isEmpty()) {
            System.out.println("No hay ventas registradas");
            return;
        }


// crear un mapa para contar cuantos de cada celular se vendieron
        // clave: nombre del celular, valor: cantidad total vendida
        Map<String, Integer> ventasPorCelular = new HashMap<>();


// recorrer todas las ventas y contar los celulares vendidos
        // flatMap "aplana" todas las listas de items en una sola
        ventas.stream()
                .flatMap(v -> v.getItems().stream())// convierte lista de ventas en lista de items
                .forEach(item -> {
                                        // crear el nombre completo del celular
                    String nombreCelular = item.getCelular().getMarca().getNombre()
                            + " " + item.getCelular().getModelo();
                   
                    
                                        // merge suma las cantidades si el celular ya esta en el mapa
                    ventasPorCelular.merge(nombreCelular, item.getCantidad(), Integer::sum);
                });

        // mostrar los 3 mas vendidos usando stream api
        System.out.println("\n=== TOP 3 CELULARES MAS VENDIDOS ===");

        ventasPorCelular.entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .limit(3)// tomar solo los primeros 3
                .forEach(entry
                        -> System.out.println(entry.getKey() + " - Vendidos: " + entry.getValue() + " unidades")
                );

        System.out.println("=====================================\n");
    }


// metodo para mostrar las ventas totales agrupadas por mes (reporte)
    // tambien usa stream api para agrupar y sumar
    public void mostrarVentasPorMes() {
        List<Venta> ventas = ventaDB.obtenerTodasLasVentas();

        if (ventas.isEmpty()) {
            System.out.println("No hay ventas registradas");
            return;
        }

        System.out.println("\n=== VENTAS TOTALES POR MES ===");

        
        
        // agrupar ventas por mes y sumar los totales
        // collectors.groupingby agrupa segun un criterio (el mes)
        // collectors.summingdouble suma los valores de cada grupo
        Map<String, Double> ventasPorMes = ventas.stream()
                .collect(Collectors.groupingBy(
                        v -> v.getFecha().format(DateTimeFormatter.ofPattern("yyyy-MM")),
                        Collectors.summingDouble(Venta::getTotal)
                ));

        // mostrar los resultados ordenados por mes
        ventasPorMes.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())// ordenar por mes
                .forEach(entry
                        -> System.out.println("Mes: " + entry.getKey()
                        + " - Total: $" + String.format("%.2f", entry.getValue()))
                );

        System.out.println("===============================\n");
    }

 // metodo para generar un archivo de texto con el reporte de ventas
    // usa bufferedwriter para escribir de forma eficiente
    public void generarReporteVentasTxt() {
        List<Venta> ventas = ventaDB.obtenerTodasLasVentas();

        if (ventas.isEmpty()) {
            System.out.println("No hay ventas para generar reporte");
            return;
        }

        String nombreArchivo = "reporte_ventas.txt";

        
        // try-with-resources: cierra automaticamente el archivo al terminar
        // esto evita errores de archivos abiertos
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(nombreArchivo))) {

                        // escribir encabezado del reporte
            writer.write("========================================\n");
            writer.write("       REPORTE DE VENTAS - TECNOSTORE\n");
            writer.write("========================================\n");
            writer.write("Fecha de generacion: " + LocalDate.now() + "\n");
            writer.write("Total de ventas: " + ventas.size() + "\n");
            writer.write("========================================\n\n");

            double totalGeneral = 0;

            
                        // escribir cada venta con sus detalles
            for (Venta venta : ventas) {
                writer.write("----------------------------------------\n");
                writer.write("Venta ID: " + venta.getId() + "\n");
                writer.write("Cliente: " + venta.getCliente().getNombre() + "\n");
                writer.write("Fecha: " + venta.getFecha() + "\n");
                writer.write("Items vendidos:\n");

                
                                // escribir cada item de la venta
                for (ItemVenta item : venta.getItems()) {
                    writer.write("  - " + item.getCelular().getMarca().getNombre()
                            + " " + item.getCelular().getModelo()
                            + " | Cantidad: " + item.getCantidad()
                            + " | Subtotal: $" + String.format("%.2f", item.getSubtotal()) + "\n");
                }

                writer.write("TOTAL (con iva): $" + String.format("%.2f", venta.getTotal()) + "\n");
                writer.write("----------------------------------------\n\n");

                
                                // ir sumando el total general de todas las ventas
                totalGeneral += venta.getTotal();
            }

                        // escribir el total general al final
            writer.write("========================================\n");
            writer.write("TOTAL GENERAL: $" + String.format("%.2f", totalGeneral) + "\n");
            writer.write("========================================\n");

            System.out.println("Reporte generado exitosamente: " + nombreArchivo);

        } catch (IOException e) {
            
                        // si hay error al escribir el archivo, mostrar mensaje
            System.out.println("Error al generar el archivo de reporte: " + e.getMessage());
        }
    }

    // metodo para mostrar todas las ventas en consola
    public void listarVentas() {
        List<Venta> ventas = ventaDB.obtenerTodasLasVentas();

        if (ventas.isEmpty()) {
            System.out.println("No hay ventas registradas");
            return;
        }

        System.out.println("\n=== LISTADO DE VENTAS ===");
        for (Venta v : ventas) {
            System.out.println(v);
        }
        System.out.println("=========================\n");

    }

    
    // metodo auxiliar para obtener la lista de ventas
    // lo usa la vista para mostrar con formato

    public List<Venta> obtenerListaVentas() {
        return ventaDB.obtenerTodasLasVentas();
    }

}
