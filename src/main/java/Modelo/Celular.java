package Modelo;

// esta clase representa un celular en la tienda
// es como la ficha tecnica de cada telefono
public class Celular {

    // atributos privados: nadie de afuera puede cambiarlos directamente
    // solo se modifican usando los metodos set
    private int id;
    private Marca marca;
    private String modelo;
    private String sistemaOperativo;
    private CategoriaGama gama;
    private double precio;
    private int stock;

    // constructor vacio: crea un celular sin datos
    // lo usa la base de datos cuando trae informacion
    public Celular() {
    }

    // constructor completo: crea un celular con todos sus datos
    public Celular(int id, Marca marca, String modelo, String sistemaOperativo, CategoriaGama gama, double precio, int stock) {
        this.id = id;
        this.marca = marca;
        this.modelo = modelo;
        this.sistemaOperativo = sistemaOperativo;
        this.gama = gama;
        // usa los setters para precio y stock porque ahi estan las validaciones
        setPrecio(precio);
        setStock(stock);
    }

    // getter: devuelve el id
    public int getId() {
        return id;
    }

    // setter: cambia el id
    public void setId(int id) {
        this.id = id;
    }

    public Marca getMarca() {
        return marca;
    }

    public void setMarca(Marca marca) {
        this.marca = marca;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public String getSistemaOperativo() {
        return sistemaOperativo;
    }

    public void setSistemaOperativo(String sistemaOperativo) {
        this.sistemaOperativo = sistemaOperativo;
    }

    public CategoriaGama getGama() {
        return gama;
    }

    public void setGama(CategoriaGama gama) {
        this.gama = gama;
    }

    public double getPrecio() {
        return precio;
    }

    // setter especial: valida que el precio no sea negativo
    // esto evita errores como poner precio -5000
    public void setPrecio(double precio) {
        if (precio < 0) {
            System.out.println("el precio no puede ser negativo");
            return; // si es negativo, no lo cambia
        }
        this.precio = precio;
    }

    public int getStock() {
        return stock;
    }

    // setter especial: valida que el stock no sea negativo
    // no se pueden tener -10 celulares en inventario
    public void setStock(int stock) {
        if (stock < 0) {
            System.out.println("el stock no puede ser negativo");
            return;
        }
        this.stock = stock;
    }

    // metodo toString: convierte el celular en texto para mostrarlo
    // ejemplo: "Celular{id=1, marca=Samsung, modelo=S23, ...}"
    @Override
    public String toString() {
        return "Celular{" + "id=" + id + ", marca=" + marca + ", modelo=" + modelo + ", sistemaOperativo=" + sistemaOperativo + ", gama=" + gama + ", precio=" + precio + ", stock=" + stock + '}';
    }

}