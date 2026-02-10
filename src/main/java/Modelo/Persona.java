package Modelo;

// clase base (padre) que contiene datos comunes de una persona
// se usa para aplicar herencia: cliente hereda de persona
// los atributos son protected para que las clases hijas puedan usarlos
public class Persona {

    // protected: las clases hijas (como Cliente) pueden acceder directamente
    protected int id;
    protected String nombre;
    protected String documento;
    protected String telefono;
    
    // constructor vacio
    public Persona(){
    }

    // constructor con parametros
    public Persona(int id, String nombre, String documento, String telefono) {
        this.id = id;
        this.nombre = nombre;
        this.documento = documento;
        this.telefono = telefono;
    }

    // getters y setters para todos los atributos
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

    public String getDocumento() {
        return documento;
    }

    public void setDocumento(String documento) {
        this.documento = documento;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    // metodo para convertir la persona en texto
    @Override
    public String toString() {
        return "Persona{" + "id=" + id + ", nombre=" + nombre + ", documento=" + documento + ", telefono=" + telefono + '}';
    }
    
}