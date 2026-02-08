package Persistencia;

import Modelo.ItemVenta;
import Modelo.Venta;

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

            // guardar item vendido
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

}
