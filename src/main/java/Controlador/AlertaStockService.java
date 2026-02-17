//Agrega una nueva funcionalidad denominada “Alerta de Stock Bajo”, que permita al sistema:
//
//Detectar automáticamente qué modelos de celulares tienen una cantidad disponible menor o igual a 5 unidades.
//Mostrar en consola un listado con:
//ID y nombre del celular
//Marca
//Cantidad disponible
//Precio de venta
//Permitir al usuario generar un archivo de texto (stock_bajo.txt) con este reporte, el cual contendrá la fecha y hora de generación.
//Ejemplo de salida esperada en consola:
//
//--- ALERTA DE STOCK BAJO ---
//ID: 14 | Modelo: Redmi Note 12 | Marca: Xiaomi | Stock: 5 | Precio: 700000
//Archivo generado: stock_bajo.txt
//Requisitos técnicos
//POO y SRP:
// Crear una nueva clase AlertaStockService o similar, con responsabilidad única de manejar esta lógica.
//Conexión JDBC:
// Ejecutar una consulta a la base de datos (SELECT * FROM celular WHERE  stock <=5).
//Colecciones:
// Guardar los resultados en una List<Celular> y recorrerlos para mostrar la alerta.
//Manejo de archivos:
// Generar el archivo stock_bajo.txt con los datos listados.
//Excepciones:
// Manejar posibles errores de conexión, lectura/escritura o ausencia de resultados.
//Uso de Streams (opcional):
// Se valorará el uso de Stream API o lambdas para filtrar y formatear la salida.
package Controlador;

import Modelo.Celular;
import Persistencia.CelularDB;
import java.util.List;
import Controlador.CelularControlador;
import java.util.Scanner;

public class AlertaStockService {

// objeto que se comunica con la base de datos
    private CelularDB celularDB;

// cuando se crea el controlador, tambien se crea la conexion a la base de datos
    public AlertaStockService() {
        celularDB = new CelularDB();
    }

    public static void mostrarCelularesStockBajo() {

        // trae todos los celulares
        List<Celular> todos = CelularDB.obtenerCelulares();
        System.out.println("\n=== CELULARES CON STOCK BAJO (menos de 5 unidades) ===");

        List<Celular> stockBajo = todos.stream()
                .filter(c -> c.getStock() < 5)// filtra: solo los que cumplen la condicion

                .toList();// convierte el resultado en una lista

        // si no hay celulares con stock bajo, avisa
        if (stockBajo.isEmpty()) {
            System.out.println("no hay celulares con stock bajo");
        } else {

            
            //ID: 8 | Modelo: Galaxy A15 | Marca: Samsung | Stock: 3 | Precio: 850000

            stockBajo.forEach(c
                    -> System.out.println("Id: " + c.getId() + "/" + "Marca: " + c.getMarca().getNombre() + "/" + "Modelo: " +c.getModelo() + "/" 
                            + " - Stock: " + c.getStock() + "/" +"Precio: " + c.getPrecio())
            );
        }
    }

    public List<Celular> obtenerListaCelulares() {
        return celularDB.obtenerCelulares();
    }
}
