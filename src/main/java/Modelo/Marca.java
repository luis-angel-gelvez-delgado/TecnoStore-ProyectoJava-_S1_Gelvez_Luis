package Modelo;

public class Marca {

    private int id;
    private String nombre;

    //constructor vacio
    public Marca() {
    }

    // constructor con parametros
    public Marca(int id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

    // geter y setter ID
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    // GEtter y setter nombre
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    // metodo toString(para que sea facil mostrar la info)
    @Override
    public String toString() {
        return "Marca{" + "id=" + id + ", nombre=" + nombre + '}';
    }

}
