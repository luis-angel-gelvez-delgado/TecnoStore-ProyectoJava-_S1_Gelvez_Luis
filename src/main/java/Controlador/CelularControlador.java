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

    public void actualizarCelular(Celular celular) {
        
        if (celular.getPrecio() <= 0) {
            System.out.println("El precio debe ser mayor de 0, o lo va a regalar?");
            return;
        }
        
        if (celular.getStock() < 0) {
            System.out.println("El stock no puede ser negativo");
            return;
        }
        
        boolean actualizado = celularDB.actualizarCelular(celular);
        
        if (actualizado) {
            System.out.println("Celular actualizado con éxito");
        } else {
            System.out.println("No se pudo actualizar el celular");
        }
    }

    public void mostrarCelularesStockBajo() {
        List<Celular> todos = celularDB.obtenerCelulares();
        
        System.out.println("\n=== CELULARES CON STOCK BAJO (menos de 5 unidades) ===");
        
        // Usar Stream API (requisito del proyecto)
        List<Celular> stockBajo = todos.stream()
            .filter(c -> c.getStock() < 5)
            .toList();
        
        if (stockBajo.isEmpty()) {
            System.out.println("No hay celulares con stock bajo");
        } else {
            stockBajo.forEach(c -> 
                System.out.println(c.getMarca().getNombre() + " " + c.getModelo() + 
                                 " - Stock: " + c.getStock())
            );
        }
    }
    
    public List<Celular> obtenerListaCelulares() {
    return celularDB.obtenerCelulares();
}
}



