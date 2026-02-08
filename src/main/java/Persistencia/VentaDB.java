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

public class VentaDB {

    public void guardarVenta(Venta venta) {

        String sqlVenta = "INSERT INTO ventas (id_cliente, fecha, total) VALUES (?, ?, ?)";
        String sqlDetalle = "INSERT INTO detalle_ventas (id_venta, id_celular, cantidad, subtotal) VALUES (?, ?, ?, ?)";

        try (
                Connection conn = ConexionDB.obtenerConexion(); PreparedStatement psVenta = conn.prepareStatement(sqlVenta, Statement.RETURN_GENERATED_KEYS); PreparedStatement psDetalle = conn.prepareStatement(sqlDetalle)) {

            psVenta.setInt(1, venta.getCliente().getId());
            psVenta.setDate(2, Date.valueOf(venta.getFecha()));
            psVenta.setDouble(3, venta.getTotal());

            psVenta.executeUpdate();

            //obtener id generado de la venta
            ResultSet rs = psVenta.getGeneratedKeys();
            int idVentaGenerado = 0;

            if (rs.next()) {
                idVentaGenerado = rs.getInt(1);
            }

            // guardar venta
            List<ItemVenta> items = venta.getItems();

            for (ItemVenta item : items) {
                psDetalle.setInt(1, idVentaGenerado);
                psDetalle.setInt(2, item.getCelular().getId());
                psDetalle.setInt(3, item.getCantidad());
                psDetalle.setDouble(4, item.getSubtotal());

                psDetalle.executeUpdate();
            }

            System.out.println("venta guardada correctamente");

        } catch (SQLException e) {
            System.out.println("Error al guardar la venta");
            e.printStackTrace();
        }

    }

    // Método para obtener las ventas
    public List<Venta> obtenerVentas() {
        List<Venta> ventas = new ArrayList<>();
        String sql = "SELECT * FROM ventas";

        try (Connection conn = ConexionDB.obtenerConexion(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Venta v = new Venta();
                v.setId(rs.getInt("id"));
                v.setFecha(rs.getDate("fecha").toLocalDate());
                v.setTotal(rs.getDouble("total"));

                // Obtener cliente
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

    
    
    
    
    
    
    
    
    
    
    // Método para obtener todas las ventas con sus detalles
    public List<Venta> obtenerTodasLasVentas() {
        List<Venta> ventas = new ArrayList<>();
        String sqlVentas = "SELECT * FROM ventas";
        String sqlDetalles = "SELECT * FROM detalle_ventas WHERE id_venta = ?";

        try (Connection conn = ConexionDB.obtenerConexion(); PreparedStatement psVentas = conn.prepareStatement(sqlVentas); ResultSet rsVentas = psVentas.executeQuery()) {

            ClienteDB clienteDB = new ClienteDB();
            CelularDB celularDB = new CelularDB();

            while (rsVentas.next()) {
                Venta venta = new Venta();
                venta.setId(rsVentas.getInt("id"));
                venta.setFecha(rsVentas.getDate("fecha").toLocalDate());
                venta.setTotal(rsVentas.getDouble("total"));

                // Obtener cliente
                Cliente cliente = clienteDB.buscarPorId(rsVentas.getInt("id_cliente"));
                venta.setCliente(cliente);

                // Obtener items de la venta
                try (PreparedStatement psDetalles = conn.prepareStatement(sqlDetalles)) {
                    psDetalles.setInt(1, venta.getId());
                    ResultSet rsDetalles = psDetalles.executeQuery();

                    List<ItemVenta> items = new ArrayList<>();

                    while (rsDetalles.next()) {
                        Celular celular = celularDB.buscarPorId(rsDetalles.getInt("id_celular"));
                        int cantidad = rsDetalles.getInt("cantidad");
                        double subtotal = rsDetalles.getDouble("subtotal");

                        ItemVenta item = new ItemVenta(celular, cantidad, subtotal);
                        items.add(item);
                    }

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
