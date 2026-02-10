package Modelo;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

// clase que representa una venta completa
// contiene: quien compro (cliente), que compro (items), cuando compro (fecha), y cuanto pago (total con iva)
public class Venta {

    private int id;
    private Cliente cliente; // quien compro
    private LocalDate fecha; // cuando compro
    private List<ItemVenta> items; // lista de todos los celulares que compro
    private double total; // total a pagar con iva incluido

    // constructor vacio que inicializa la lista y la fecha actual
    public Venta() {
        items = new ArrayList<>(); // crea una lista vacia para ir agregando items
        fecha = LocalDate.now(); // pone la fecha de hoy
    }

    // constructor completo que se usa al traer ventas de la base de datos
    public Venta(int id, Cliente cliente, LocalDate fecha, List<ItemVenta> items) {
        this.id = id;
        this.cliente = cliente;
        this.fecha = fecha;
        // si items es null, crea una lista vacia
        this.items = items != null? items : new ArrayList<>();
        calcularTotal(); // calcula el total con iva
    }

    // metodo para agregar un item a la venta
    // ejemplo: agregar 2 Samsung Galaxy
    public void agregarItem(ItemVenta item){
        items.add(item); // agrega el item a la lista
        calcularTotal(); // recalcula el total porque se agrego algo nuevo
    }

    // metodo privado que calcula el total de la venta con iva del 19%
    // suma todos los subtotales de los items y le aplica el iva
    private void calcularTotal() {
        double suma = 0;
        
        // recorre todos los items y suma sus subtotales
        for (ItemVenta item : items){
            suma += item.getSubtotal();
        }
        
        // multiplica por 1.19 para agregar el 19% de iva
        // ejemplo: si suma=100000, total=119000
        total = suma * 1.19;
    }

    // getters y setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public List<ItemVenta> getItems() {
        return items;
    }

    public void setItems(List<ItemVenta> items) {
        this.items = items;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    // metodo para mostrar la venta como texto
    @Override
    public String toString() {
        return "Venta{" + "id=" + id + ", cliente=" + cliente + ", fecha=" + fecha + ", total=" + total + '}';
    }
    
}