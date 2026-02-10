package Modelo;

// clase que representa una marca de celulares
// ejemplo: Samsung, Apple, Xiaomi, etc
public class Marca {

    private int id;
    private String nombre;

    // constructor vacio
    public Marca() {
    }

    // constructor con parametros
    public Marca(int id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

    // getters y setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    // metodo para mostrar la marca como texto
    @Override
    public String toString() {
        return "Marca{" + "id=" + id + ", nombre=" + nombre + '}';
    }

}