package Modelo;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Venta {

    private int id;
    private Cliente cliente;
    private LocalDate fecha;
    private List<ItemVenta> items;
    private double total;

    public Venta() {
        items = new ArrayList<>();
        fecha = LocalDate.now();
    }

    public Venta(int id, Cliente cliente, LocalDate fecha, List<ItemVenta> items) {
        this.id = id;
        this.cliente = cliente;
        this.fecha = LocalDate.now();
        this.items = new ArrayList<>();
    }

    
    public void agregarItem(ItemVenta item){
    items.add(item);
    calcularTotal();
    }

    private void calcularTotal() {
    double suma = 0;
    
    
    for (ItemVenta item : items){
    suma += item.getSubtotal();
    }
    
    total = suma *1.19;
    
}

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

    @Override
    public String toString() {
        return "Venta{" + "id=" + id + ", cliente=" + cliente + ", fecha=" + fecha + ", total=" + total + '}';
    }


    
    
    
}
