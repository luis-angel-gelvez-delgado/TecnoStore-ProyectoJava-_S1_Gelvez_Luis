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

public class VentaControlador {

    private VentaDB ventaDB;
    private CelularDB celularDB;
    private ClienteDB clienteDB;

    public VentaControlador() {
        ventaDB = new VentaDB();
        celularDB = new CelularDB();
        clienteDB = new ClienteDB();
    }

    // Registrar venta completa
    public void registrarVenta(int idCliente, List<Integer> idsCelulares, List<Integer> cantidades) {

        // Validar que cliente existe
        Cliente cliente = clienteDB.buscarPorId(idCliente);
        if (cliente == null) {
            System.out.println("Error: Cliente no encontrado");
            return;
        }

        // Crear la venta
        Venta venta = new Venta();
        venta.setCliente(cliente);
        venta.setFecha(LocalDate.now());

        // Procesar cada celular
        for (int i = 0; i < idsCelulares.size(); i++) {
            int idCelular = idsCelulares.get(i);
            int cantidad = cantidades.get(i);

            // Buscar celular
            Celular celular = celularDB.buscarPorId(idCelular);

            if (celular == null) {
                System.out.println("Error: Celular con ID " + idCelular + " no encontrado");
                continue;
            }

            // Validar stock disponible
            if (celular.getStock() < cantidad) {
                System.out.println("Error: Stock insuficiente para " + celular.getMarca().getNombre()
                        + " " + celular.getModelo() + ". Disponible: " + celular.getStock());
                continue;
            }

            // Crear item de venta
            double subtotal = celular.getPrecio() * cantidad;
            ItemVenta item = new ItemVenta(celular, cantidad, subtotal);

            // Agregar item a la venta (esto calcula el total con IVA)
            venta.agregarItem(item);

            // Actualizar stock del celular
            int nuevoStock = celular.getStock() - cantidad;
            celularDB.actualizarStock(idCelular, nuevoStock);
        }

        // Validar que la venta tenga items
        if (venta.getItems().isEmpty()) {
            System.out.println("Error: No se pudo procesar ningún item de la venta");
            return;
        }

        // Guardar venta en BD
        ventaDB.guardarVenta(venta);

        System.out.println("\n=== VENTA REGISTRADA ===");
        System.out.println("Cliente: " + cliente.getNombre());
        System.out.println("Fecha: " + venta.getFecha());
        System.out.println("Total (con IVA 19%): $" + String.format("%.2f", venta.getTotal()));
        System.out.println("========================\n");
    }

    // Top 3 celulares más vendidos (Stream API)
    public void mostrarTop3MasVendidos() {
        List<Venta> ventas = ventaDB.obtenerTodasLasVentas();

        if (ventas.isEmpty()) {
            System.out.println("No hay ventas registradas");
            return;
        }

        // Crear un mapa para contar cantidad vendida por celular
        Map<String, Integer> ventasPorCelular = new HashMap<>();

        // Recorrer todas las ventas y sus items
        ventas.stream()
                .flatMap(v -> v.getItems().stream())
                .forEach(item -> {
                    String nombreCelular = item.getCelular().getMarca().getNombre()
                            + " " + item.getCelular().getModelo();
                    ventasPorCelular.merge(nombreCelular, item.getCantidad(), Integer::sum);
                });

        // Obtener top 3 usando Stream API
        System.out.println("\n=== TOP 3 CELULARES MAS VENDIDOS ===");

        ventasPorCelular.entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .limit(3)
                .forEach(entry
                        -> System.out.println(entry.getKey() + " - Vendidos: " + entry.getValue() + " unidades")
                );

        System.out.println("=====================================\n");
    }

    // Ventas totales por mes (Stream API)
    public void mostrarVentasPorMes() {
        List<Venta> ventas = ventaDB.obtenerTodasLasVentas();

        if (ventas.isEmpty()) {
            System.out.println("No hay ventas registradas");
            return;
        }

        System.out.println("\n=== VENTAS TOTALES POR MES ===");

        // Agrupar ventas por mes usando stream api
        Map<String, Double> ventasPorMes = ventas.stream()
                .collect(Collectors.groupingBy(
                        v -> v.getFecha().format(DateTimeFormatter.ofPattern("yyyy-MM")),
                        Collectors.summingDouble(Venta::getTotal)
                ));

        // Mostrar resultados ordenados por mes
        ventasPorMes.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .forEach(entry
                        -> System.out.println("Mes: " + entry.getKey()
                        + " - Total: $" + String.format("%.2f", entry.getValue()))
                );

        System.out.println("===============================\n");
    }

    // Generar archivo reporte_ventas.txt
    public void generarReporteVentasTxt() {
        List<Venta> ventas = ventaDB.obtenerTodasLasVentas();

        if (ventas.isEmpty()) {
            System.out.println("No hay ventas para generar reporte");
            return;
        }

        String nombreArchivo = "reporte_ventas.txt";

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(nombreArchivo))) {

            writer.write("========================================\n");
            writer.write("       REPORTE DE VENTAS - TECNOSTORE\n");
            writer.write("========================================\n");
            writer.write("Fecha de generacion: " + LocalDate.now() + "\n");
            writer.write("Total de ventas: " + ventas.size() + "\n");
            writer.write("========================================\n\n");

            double totalGeneral = 0;

            for (Venta venta : ventas) {
                writer.write("----------------------------------------\n");
                writer.write("Venta ID: " + venta.getId() + "\n");
                writer.write("Cliente: " + venta.getCliente().getNombre() + "\n");
                writer.write("Fecha: " + venta.getFecha() + "\n");
                writer.write("Items vendidos:\n");

                for (ItemVenta item : venta.getItems()) {
                    writer.write("  - " + item.getCelular().getMarca().getNombre()
                            + " " + item.getCelular().getModelo()
                            + " | Cantidad: " + item.getCantidad()
                            + " | Subtotal: $" + String.format("%.2f", item.getSubtotal()) + "\n");
                }

                writer.write("TOTAL (con iva): $" + String.format("%.2f", venta.getTotal()) + "\n");
                writer.write("----------------------------------------\n\n");

                totalGeneral += venta.getTotal();
            }

            writer.write("========================================\n");
            writer.write("TOTAL GENERAL: $" + String.format("%.2f", totalGeneral) + "\n");
            writer.write("========================================\n");

            System.out.println("Reporte generado exitosamente: " + nombreArchivo);

        } catch (IOException e) {
            System.out.println("Error al generar el archivo de reporte: " + e.getMessage());
        }
    }

    // Listar todas las ventas
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

    public List<Venta> obtenerListaVentas() {
        return ventaDB.obtenerTodasLasVentas();
    }

}
