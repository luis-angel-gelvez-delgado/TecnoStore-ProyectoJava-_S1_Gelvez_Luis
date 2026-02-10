package Controlador;

import Modelo.Celular;
import Persistencia.CelularDB;
import java.util.List;

// esta clase maneja toda la logica de negocio relacionada con celulares
// se encarga de validar datos antes de guardarlos en la base de datos
public class CelularControlador {

    // objeto que se comunica con la base de datos
    private CelularDB celularDB;

// cuando se crea el controlador, tambien se crea la conexion a la base de datos
    public CelularControlador() {
        celularDB = new CelularDB();
    }

    // metodo para registrar un nuevo celular
    public void registrarCelular(Celular celular) {

        // validacion: el precio tiene que ser mayor a 0
        if (celular.getPrecio() <= 0) {
            System.out.println("el precio debe ser mayor de 0");
            return;
        }

        // validacion: el stock no puede ser negativo
        if (celular.getStock() <= 0) {
            System.out.println("el stock no puede ser negativo");
            return;// si no cumple, se sale del metodo
        }

        // si pasa las validaciones, intenta guardar en la base de datos
        boolean guardado = celularDB.insertarCelular(celular);

        // muestra mensaje segun si se guardo o no
        if (guardado) {
            System.out.println("celular registrado correctamente");
        } else {
            System.out.println("error al registrar el celular");
        }

    }

    // metodo para mostrar todos los celulares registrados
    public void listarCelulares() {
        // trae todos los celulares de la base de datos

        List<Celular> lista = celularDB.obtenerCelulares();

        // si no hay celulares, avisa y se sale
        if (lista.isEmpty()) {
            System.out.println("no hay celulares registrados");
            return;
        }

        // recorre la lista y muestra cada celular
        for (Celular c : lista) {
            System.out.println(c);
        }

    }

    // metodo para buscar un celular especifico por su id
    public Celular buscarCelularPorId(int id) {
        return celularDB.buscarPorId(id);
    }

    // metodo para cambiar el stock de un celular
    public void actualizarStock(int id, int nuevoStock) {

        // validacion: el stock no puede ser negativo
        if (nuevoStock < 0) {
            System.out.println("el stok no puede ser negativo");
            return;
        }

        // intenta actualizar en la base de datos
        boolean actualizado = celularDB.actualizarStock(id, nuevoStock);
        // muestra mensaje segun el resultado

        if (actualizado) {
            System.out.println("stock actualizado con exito");
        } else {
            System.out.println("no se pudo actualizar el stock");
        }

    }

    // metodo para borrar un celular de la base de datos
    public void eliminarCelular(int id) {
        boolean eliminado = celularDB.eliminarCelular(id);

        if (eliminado) {
            System.out.println("celular eliminado con exito");
        } else {
            System.out.println("no se pudo eliminar el celular");
        }
    }

    // metodo para actualizar todos los datos de un celular
    public void actualizarCelular(Celular celular) {
        // validacion de precio
        if (celular.getPrecio() <= 0) {
            System.out.println("el precio debe ser mayor de 0, o lo va a regalar?");
            return;
        }

        // validacion de stock
        if (celular.getStock() < 0) {
            System.out.println("el stock no puede ser negativo");
            return;
        }

        // intenta actualizar en la base de datos
        boolean actualizado = celularDB.actualizarCelular(celular);

        if (actualizado) {
            System.out.println("celular actualizado con exito");
        } else {
            System.out.println("no se pudo actualizar el celular");
        }
    }

    // metodo para mostrar celulares con poco stock (reporte)
    public void mostrarCelularesStockBajo() {

        // trae todos los celulares
        List<Celular> todos = celularDB.obtenerCelulares();

        System.out.println("\n=== CELULARES CON STOCK BAJO (menos de 5 unidades) ===");

        // usa stream api para filtrar solo los que tienen menos de 5 en stock
        // esto es programacion funcional, mas eficiente que un for tradicional
        List<Celular> stockBajo = todos.stream()
                .filter(c -> c.getStock() < 5)// filtra: solo los que cumplen la condicion

                .toList();// convierte el resultado en una lista

        // si no hay celulares con stock bajo, avisa
        if (stockBajo.isEmpty()) {
            System.out.println("no hay celulares con stock bajo");
        } else {

            // recorre y muestra cada celular con stock bajo
            stockBajo.forEach(c
                    -> System.out.println(c.getMarca().getNombre() + " " + c.getModelo()
                            + " - Stock: " + c.getStock())
            );
        }
    }

    // metodo auxiliar que devuelve la lista de celulares
    // lo usa la vista para mostrar los celulares con formato
    public List<Celular> obtenerListaCelulares() {
        return celularDB.obtenerCelulares();
    }
}
