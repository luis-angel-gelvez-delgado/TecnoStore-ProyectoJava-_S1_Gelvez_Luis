
package Modelo;

public class FactoryCelular {
    
    // patron Factory para crear celulares segun la gama
    public static Celular crearCelular(String gama, Marca marca, String modelo, String so, double precio, int stock) {
        
        CategoriaGama categoriaGama;
        
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
                System.out.println("gama no valida, asignando MEDIA por defecto");
                categoriaGama = CategoriaGama.MEDIA;
        }
        
        return new Celular(0, marca, modelo, so, categoriaGama, precio, stock);
    }
}