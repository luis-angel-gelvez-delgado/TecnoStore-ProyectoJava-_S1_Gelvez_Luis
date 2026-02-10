package Modelo;

// cliente hereda de persona (extends)
// esto significa que cliente tiene todo lo de persona (id, nombre, documento, telefono)
// y ademas agrega el correo
// esto es un ejemplo de herencia en programacion orientada a objetos
public class Cliente extends Persona {

    // atributo adicional que solo tienen los clientes
    private String correo;

    // constructor vacio
    public Cliente(){
    }

    // constructor solo con correo
    public Cliente(String correo) {
        this.correo = correo;
    }

    // constructor completo que usa super para llamar al constructor de persona
    // super(...) llama al constructor de la clase padre (Persona)
    public Cliente(String correo, int id, String nombre, String documento, String telefono) {
        super(id, nombre, documento, telefono); // inicializa los atributos de persona
        this.correo = correo; // inicializa el correo que es especifico de cliente
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    // override: sobreescribe el metodo toString de persona
    // llama primero a super.toString() para obtener los datos de persona
    // y luego le agrega el correo
    // asi muestra: "Persona{...}, correo='ejemplo@mail.com'"
    @Override
    public String toString() {
        return super.toString() + ", correo='" + correo + "'";
    }

}