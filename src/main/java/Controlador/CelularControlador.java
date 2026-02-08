package Controlador;

import Modelo.Celular;
import Persistencia.CelularDB;
import java.util.List;

public class CelularControlador {

    private CelularDB celularDB;

    public CelularControlador() {
        celularDB = new CelularDB();
    }

    public void registrarCelular(Celular celular) {

        if (celular.getPrecio() <= 0) {
            System.out.println("El precio debe ser mayor de 0");
            return;
        }

        if (celular.getStock() <= 0) {
            System.out.println("El stock no puede ser negativo");
            return;
        }

        boolean guardado = celularDB.insertarCelular(celular);

        if (guardado) {
            System.out.println("celular registrado correctamente");
        } else {
            System.out.println("Error al registrar el celular");
        }

    }

    public void listarCelulares() {
        List<Celular> lista = celularDB.obtenerCelulares();

        if (lista.isEmpty()) {
            System.out.println("no hay celulares registrados");
            return;
        }

        for (Celular c : lista) {
            System.out.println(c);
        }

    }

    public Celular buscarCelularPorId(int id) {
        return celularDB.buscarPorId(id);
    }

    public void actualizarStock(int id, int nuevoStock) {

        if (nuevoStock < 0) {
            System.out.println("el stok no puede ser negativo");
            return;
        }

        boolean actualizado = celularDB.actualizarStock(id, nuevoStock);

        if (actualizado) {
            System.out.println("Stock actualizado con exito");
        } else {
            System.out.println("no se pudo actualizar el stock");
        }
        
    }

    public void eliminarCelular(int id) {
        boolean eliminado = celularDB.eliminarCelular(id);

        if (eliminado) {
            System.out.println("Celular eliminado con exito");
        } else {
            System.out.println("no se pudo eliminar el celular");
        }
    }

}
