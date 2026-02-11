package Controlador;

import Modelo.Cliente;
import Persistencia.ClienteDB;
import java.util.List;
import java.util.regex.Pattern;

// esta clase maneja toda la logica de negocio de clientes
// valida correos, verifica documentos duplicados, etc
public class ClienteControlador {

    // objeto que se comunica con la base de datos
    private ClienteDB clienteDB;

    // cuando se crea el controlador, se crea la conexion a la bd
    public ClienteControlador() {
        clienteDB = new ClienteDB();
    }

// funcion privada para validar que el correo tenga formato correcto
    // usa expresiones regulares (regex) para verificar el patron
    private boolean validarCorreo(String correo) {
        // patron que define como debe verse un correo valido
        // ejemplo valido: usuario@dominio.com
        String regex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
        return Pattern.matches(regex, correo);
    }

    // funcion privada para verificar si un documento ya esta registrado
    // evita que se registren clientes duplicados
    private boolean documentoExiste(String documento) {

        // trae todos los clientes de la base de datos
        List<Cliente> clientes = clienteDB.obtenerClientes();

        // usa stream api para buscar si alguno tiene el mismo documento
        // anyMatch devuelve true si encuentra al menos uno que coincida
        return clientes.stream().anyMatch(c -> c.getDocumento().equals(documento));
    }

    // metodo para registrar un nuevo cliente (con todas las validaciones)
    public void registarCliente(Cliente cliente) {

        // validacion 1: verifica que el correo tenga formato valido
        if (!validarCorreo(cliente.getCorreo())) {
            System.out.println("error: el formato del correo es invalido");
            return;
        }

        // validacion 2: verifica que el documento no este duplicado
        if (documentoExiste(cliente.getDocumento())) {
            System.out.println("ya existe un cliente con ese numero de documento");
            return;

        }

        // si pasa todas las validaciones, intenta guardar en la bd
        boolean guardado = clienteDB.insertarCliente(cliente);

        if (guardado) {
            System.out.println("cliente registrado exitosamente");
        } else {
            System.out.println("error al registrar al cliente, intentelo de nuevo");
        }
    }

    // metodo para mostrar todos los clientes registrados
    public void listarClientes() {
        List<Cliente> lista = clienteDB.obtenerClientes();

        if (lista.isEmpty()) {
            System.out.println("no hay clientes registrados");
            return;
        }
        // recorre y muestra cada cliente
        for (Cliente c : lista) {
            System.out.println(c);
        }

    }

    // metodo para buscar un cliente especifico por su id
    public Cliente buscarClientePorId(int id) {
        return clienteDB.buscarPorId(id);
    }

    // metodo para eliminar un cliente de la base de datos
    public boolean eliminarCliente(int id) {
        boolean eliminado = clienteDB.eliminarCliente(id);

        if (eliminado) {
            System.out.println("cliente eliminado con exito");
        } else {
            System.out.println("");
        }
        return clienteDB.eliminarCliente(id);
    }

    // metodo para actualizar los datos de un cliente
    public void actualizarCliente(Cliente cliente) {

        // valida que el nuevo correo tenga formato correcto
        if (!validarCorreo(cliente.getCorreo())) {
            System.out.println("error: el formato del correo es invalido");
            return;
        }

        // intenta actualizar en la base de datos
        boolean actualizado = clienteDB.actualizarCliente(cliente);

        if (actualizado) {
            System.out.println("cliente actualizado con exito");
        } else {
            System.out.println("no se pudo actualizar el cliente");
        }
    }

    // metodo auxiliar que devuelve la lista de clientes
    // lo usa la vista para mostrar los clientes con formato
    public List<Cliente> obtenerListaClientes() {
        return clienteDB.obtenerClientes();
    }

}

// ya con esto confirmo finalizado el proyecto y doy paso a implementar correctamente el readme
