package Modelo;


public class ItemVenta {
    
    
    private Celular celular;
    
    private int cantidad;
    
    private double subtotal;
    
    public ItemVenta(){
    }

    public ItemVenta(Celular celular, int cantidad) {
    this.celular = celular;
    this.cantidad = cantidad;
    this.subtotal = celular.getPrecio() * cantidad;
}

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

    @Override
    public String toString() {
    return "ItemVenta{" +
            "celular=" + celular.getMarca() + " " + celular.getModelo() +
            ", cantidad=" + cantidad +
            ", subtotal=" + subtotal +
            '}';
}

    
    
    
}
