package Modelo;

// clase que representa un item dentro de una venta
// ejemplo: si alguien compra 2 Samsung Galaxy, eso es un ItemVenta
// una venta puede tener varios items (varios celulares diferentes)
public class ItemVenta {
    
    // el celular que se esta vendiendo
    private Celular celular;
    
    // cuantos se estan vendiendo
    private int cantidad;
    
    // cuanto cuesta el total de este item (precio x cantidad)
    private double subtotal;
    
    // constructor vacio
    public ItemVenta(){
    }

    // constructor que calcula automaticamente el subtotal
    // se usa cuando creamos un item nuevo en una venta
    public ItemVenta(Celular celular, int cantidad) {
        this.celular = celular;
        this.cantidad = cantidad;
        // calcula subtotal: precio del celular x cantidad
        this.subtotal = celular.getPrecio() * cantidad;
    }

    // constructor con subtotal ya calculado
    // se usa cuando traemos items de la base de datos
    // porque ahi ya esta guardado el subtotal
    public ItemVenta(Celular celular, int cantidad, double subtotal) {
        this.celular = celular;
        this.cantidad = cantidad;
        this.subtotal = subtotal;
    }

    // getters y setters
    public Celular getCelular() {
        return celular;
    }

    public void setCelular(Celular celular) {
        this.celular = celular;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(double subtotal) {
        this.subtotal = subtotal;
    }

    // metodo para mostrar el item como texto
    // ejemplo: "ItemVenta{celular=Samsung Galaxy S23, cantidad=2, subtotal=7000000}"
    @Override
    public String toString() {
        return "ItemVenta{" +
                "celular=" + celular.getMarca() + " " + celular.getModelo() +
                ", cantidad=" + cantidad +
                ", subtotal=" + subtotal +
                '}';
    }
    
}