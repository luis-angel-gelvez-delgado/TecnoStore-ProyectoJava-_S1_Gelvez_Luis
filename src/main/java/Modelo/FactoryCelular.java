package Modelo;

// patron de diseño Factory
// este patron sirve para crear objetos de forma centralizada
// en lugar de hacer "new Celular(...)" en muchos lugares diferentes,
// usamos esta clase para crear celulares de forma controlada
public class FactoryCelular {
    
    // metodo estatico: se puede llamar sin crear un objeto FactoryCelular
    // ejemplo: FactoryCelular.crearCelular("ALTA", ...)
    // recibe la gama como texto y la convierte en el enum correspondiente
    public static Celular crearCelular(String gama, Marca marca, String modelo, String so, double precio, int stock) {
        
        CategoriaGama categoriaGama;
        
        // switch para convertir el texto en enum
        // si el usuario escribe "alta", "ALTA" o "AlTa", funciona igual
        switch (gama.toUpperCase()) {
            case "ALTA":
                categoriaGama = CategoriaGama.ALTA;
                break;
            case "MEDIA":
                categoriaGama = CategoriaGama.MEDIA;
                break;
            case "BAJA":
                categoriaGama = CategoriaGama.BAJA;
                break;
            default:
                // si escribe algo que no existe, usa MEDIA por defecto
                System.out.println("gama no valida, asignando MEDIA por defecto");
                categoriaGama = CategoriaGama.MEDIA;
        }
        
        // crea y devuelve el celular con la gama correcta
        return new Celular(0, marca, modelo, so, categoriaGama, precio, stock);
    }
}