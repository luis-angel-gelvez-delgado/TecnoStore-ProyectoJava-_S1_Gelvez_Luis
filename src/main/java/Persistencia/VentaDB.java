package Persistencia;

import Modelo.ItemVenta;
import Modelo.Venta;
import java.util.ArrayList;
import Modelo.Cliente;
import Modelo.Celular;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Date;
import java.util.List;

// clase DAO para ventas
// esta es la mas compleja porque maneja dos tablas: ventas y detalle_ventas
public class VentaDB {

    // metodo para guardar una venta completa (venta + items)
    public void guardarVenta(Venta venta) {

        // dos queries: una para la venta principal y otra para los detalles
        String sqlVenta = "INSERT INTO ventas (id_cliente, fecha, total) VALUES (?, ?, ?)";
        String sqlDetalle = "INSERT INTO detalle_ventas (id_venta, id_celular, cantidad, subtotal) VALUES (?, ?, ?, ?)";

        try (
                Connection conn = ConexionDB.obtenerConexion(); 
                // RETURN_GENERATED_KEYS: permite obtener el id que mysql genera automaticamente
                PreparedStatement psVenta = conn.prepareStatement(sqlVenta, Statement.RETURN_GENERATED_KEYS); 
                PreparedStatement psDetalle = conn.prepareStatement(sqlDetalle)) {

            // paso 1: guardar la venta principal
            psVenta.setInt(1, venta.getCliente().getId());
            psVenta.setDate(2, Date.valueOf(venta.getFecha())); // convertir LocalDate a Date de sql
            psVenta.setDouble(3, venta.getTotal());

            psVenta.executeUpdate();

            // paso 2: obtener el id que mysql le asigno a la venta
            ResultSet rs = psVenta.getGeneratedKeys();
            int idVentaGenerado = 0;

            if (rs.next()) {
                idVentaGenerado = rs.getInt(1); // el primer valor es el id
            }

            // paso 3: guardar cada item de la venta en detalle_ventas
            List<ItemVenta> items = venta.getItems();

            for (ItemVenta item : items) {
                psDetalle.setInt(1, idVentaGenerado); // relacionar con la venta
                psDetalle.setInt(2, item.getCelular().getId());
                psDetalle.setInt(3, item.getCantidad());
                psDetalle.setDouble(4, item.getSubtotal());

                psDetalle.executeUpdate(); // guardar cada item
            }

            System.out.println("venta guardada correctamente");

        } catch (SQLException e) {
            System.out.println("Error al guardar la venta");
            e.printStackTrace();
        }

    }

    // metodo simple para obtener ventas sin sus items
    // se usa para listados rapidos
    public List<Venta> obtenerVentas() {
        List<Venta> ventas = new ArrayList<>();
        String sql = "SELECT * FROM ventas";

        try (Connection conn = ConexionDB.obtenerConexion(); 
             PreparedStatement ps = conn.prepareStatement(sql); 
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Venta v = new Venta();
                v.setId(rs.getInt("id"));
                v.setFecha(rs.getDate("fecha").toLocalDate()); // convertir Date de sql a LocalDate
                v.setTotal(rs.getDouble("total"));

                // buscar y asignar el cliente
                ClienteDB clienteDB = new ClienteDB();
                Cliente cliente = clienteDB.buscarPorId(rs.getInt("id_cliente"));
                v.setCliente(cliente);

                ventas.add(v);
            }
        } catch (SQLException e) {
            System.out.println("Error al obtener ventas: " + e.getMessage());
        }

        return ventas;
    }

    // metodo completo para obtener ventas con todos sus items
    // se usa para reportes detallados
    public List<Venta> obtenerTodasLasVentas() {
        List<Venta> ventas = new ArrayList<>();
        String sqlVentas = "SELECT * FROM ventas";
        String sqlDetalles = "SELECT * FROM detalle_ventas WHERE id_venta = ?";

        try (Connection conn = ConexionDB.obtenerConexion(); 
             PreparedStatement psVentas = conn.prepareStatement(sqlVentas); 
             ResultSet rsVentas = psVentas.executeQuery()) {

            // crear los DAO que se van a necesitar
            ClienteDB clienteDB = new ClienteDB();
            CelularDB celularDB = new CelularDB();

            // recorrer cada venta
            while (rsVentas.next()) {
                Venta venta = new Venta();
                venta.setId(rsVentas.getInt("id"));
                venta.setFecha(rsVentas.getDate("fecha").toLocalDate());
                venta.setTotal(rsVentas.getDouble("total"));

                // buscar el cliente de esta venta
                Cliente cliente = clienteDB.buscarPorId(rsVentas.getInt("id_cliente"));
                venta.setCliente(cliente);

                // ahora buscar todos los items de esta venta
                try (PreparedStatement psDetalles = conn.prepareStatement(sqlDetalles)) {
                    psDetalles.setInt(1, venta.getId());
                    ResultSet rsDetalles = psDetalles.executeQuery();

                    List<ItemVenta> items = new ArrayList<>();

                    // recorrer cada item de la venta
                    while (rsDetalles.next()) {
                        // buscar el celular de este item
                        Celular celular = celularDB.buscarPorId(rsDetalles.getInt("id_celular"));
                        int cantidad = rsDetalles.getInt("cantidad");
                        double subtotal = rsDetalles.getDouble("subtotal");

                        // crear el item con todos sus datos
                        ItemVenta item = new ItemVenta(celular, cantidad, subtotal);
                        items.add(item);
                    }

                    // asignar todos los items a la venta
                    venta.setItems(items);
                }

                ventas.add(venta);
            }

        } catch (SQLException e) {
            System.out.println("error al obtener ventas: " + e.getMessage());
        }

        return ventas;
    }

}