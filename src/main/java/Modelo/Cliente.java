package Modelo;

public class Cliente extends Persona {

private String correo;

public Cliente(){
}

    public Cliente(String correo) {
        this.correo = correo;
    }

    public Cliente(String correo, int id, String nombre, String documento, String telefono) {
        super(id, nombre, documento, telefono);
        this.correo = correo;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    @Override// este toString sirve para ver toda la info del cliente, no solo el correo(por que lo hice asi? la plena se me hizo menos codigo <*>_<*>)
    public String toString() {
    return super.toString() + ", correo='" + correo + "'";
}


    

 

}
