package Persistencia;

import Modelo.Cliente;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ClienteDB {

    // insertar cliente en la base de datos
    public boolean insertarCliente(Cliente cliente) {
        String sql = "INSERT INTO clientes (nombre, identificacion, correo, telefono) VALUES (?, ?, ?, ?)";

        try (

            Connection conn = ConexionDB.obtenerConexion();
            PreparedStatement ps = conn.prepareStatement(sql)){

            ps.setString(1, cliente.getNombre());
            ps.setString(2, cliente.getDocumento());
            ps.setString(3, cliente.getCorreo());
            ps.setString(4, cliente.getTelefono());

            ps.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.out.println("error al insertar cliente: " + e.getMessage());
            return false;
        }

    }

    // listar clientes
    public List<Cliente> obtenerClientes() {

        List<Cliente> lista = new ArrayList<>();
        String sql = "SELECT * from clientes";

        try (
            Connection conn = ConexionDB.obtenerConexion();
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery()){

            while (rs.next()) {
                Cliente c = new Cliente();

                c.setId(rs.getInt("id"));
                c.setNombre(rs.getString("nombre"));
                c.setDocumento(rs.getString("identificacion"));
                c.setCorreo(rs.getString("correo"));
                c.setTelefono(rs.getString("telefono"));

                lista.add(c);

            }

        } catch (SQLException e) {
            System.out.println("error  al obtener clientes: " + e.getMessage());

        }

        return lista;

    }

    // buscar cliente por id 
    public Cliente buscarPorId(int id) {

        String sql = "SELECT * FROM clientes WHERE id = ?";

        try (

            Connection conn = ConexionDB.obtenerConexion();
            PreparedStatement ps = conn.prepareStatement(sql)){
            
                ps.setInt(1, id);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                Cliente c = new Cliente();

                c.setId(rs.getInt("id"));
                c.setNombre(rs.getString("nombre"));
                c.setDocumento(rs.getString("identificacion"));
                c.setCorreo(rs.getString("correo"));
                c.setTelefono(rs.getString("telefono"));

                return c;

            }

        } catch (SQLException e) {
            System.out.println("error al buscar cliente: " + e.getMessage());
        }

        return null;

    }

    //eliminar cliente
    public boolean eliminarCliente(int id) {

        String sql = "DELETE FROM clientes WHERE id = ?";

        try (
            Connection conn = ConexionDB.obtenerConexion();
            PreparedStatement ps = conn.prepareStatement(sql)){
                    
            ps.setInt(1, id);

            int filas = ps.executeUpdate();
            return filas > 0;

        } catch (SQLException e) {
            System.out.println("error al eliminar cliente: " + e.getMessage());
            return false;

        }

    }

}
