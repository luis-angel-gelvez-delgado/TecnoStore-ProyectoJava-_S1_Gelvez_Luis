package Controlador;

import Modelo.Cliente;
import Persistencia.ClienteDB;
import java.util.List;
import java.util.regex.Pattern;

public class ClienteControlador {

    private ClienteDB clienteDB;

    public ClienteControlador() {
        clienteDB = new ClienteDB();
    }

// funcion para validar que el correo este bien
    private boolean validarCorreo(String correo) {
        String regex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
        return Pattern.matches(regex, correo);
    }

// para validar que el documento sea unico
    private boolean documentoExiste(String documento) {
        List<Cliente> clientes = clienteDB.obtenerClientes();
        return clientes.stream().anyMatch(c -> c.getDocumento().equals(documento));
    }

// registrar clientes con validaciones
    public void registarCliente(Cliente cliente) {

        // validamos correo
        if (!validarCorreo(cliente.getCorreo())) {
            System.out.println("error: el formato del correo es invalido");
            return;
        }

// validar cliente que exista con el documento
        if (documentoExiste(cliente.getDocumento())) {
            System.out.println("ya existe un cliente con ese numero de documento");
            return;

        }

        boolean guardado = clienteDB.insertarCliente(cliente);

        if (guardado) {
            System.out.println("cliente registrado exitosamente");
        } else {
            System.out.println("error al registrar al cliente, intentelo de nuevo");
        }
    }

//listar clientes
    public void listarClientes() {
        List<Cliente> lista = clienteDB.obtenerClientes();

        if (lista.isEmpty()) {
            System.out.println("no hay clientes registrados");
            return;
        }

        for (Cliente c : lista) {
            System.out.println(c);
        }

    }

// buscar cliente por id
    public Cliente buscarClientePorId(int id) {
        return clienteDB.buscarPorId(id);
    }

//eliminar cliente
    public void eliminarCliente(int id) {
        boolean eliminado = clienteDB.eliminarCliente(id);

        if (eliminado) {
            System.out.println("Cliente eliminado con exito");
        } else {
            System.out.println("no se pudo eliminar, es superior al resto, es una deidad XD");
        }
    }

// actualizar cliente
    public void actualizarCliente(Cliente cliente) {

        // Validar correo
        if (!validarCorreo(cliente.getCorreo())) {
            System.out.println("error: El formato del correo es invalido");
            return;
        }

        boolean actualizado = clienteDB.actualizarCliente(cliente);

        if (actualizado) {
            System.out.println("cliente actualizado con exito");
        } else {
            System.out.println("no se pudo actualizar el cliente");
        }
    }

    
    
    public List<Cliente> obtenerListaClientes() {
    return clienteDB.obtenerClientes();
}
    
}




