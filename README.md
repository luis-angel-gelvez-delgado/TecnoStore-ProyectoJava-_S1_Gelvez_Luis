# TecnoStore - Sistema de Gestión de Ventas

Sistema de consola en Java para la gestión automatizada de ventas, inventario y clientes de una tienda de celulares.

![Java](https://img.shields.io/badge/Java-21-orange)
![MySQL](https://img.shields.io/badge/MySQL-8.0-blue)
![JDBC](https://img.shields.io/badge/JDBC-Connectivity-green)

---

##  Descripción del Proyecto

**TecnoStore** es una tienda minorista dedicada a la venta de telefonos celulares de diferentes marcas y gamas. Este sistema permite:

-  Gestionar catalogo de celulares (CRUD completo)
-  Administrar clientes con validaciones
-  Registrar ventas con calculo automatico de IVA (19%)
-  Generar reportes de negocio con Stream API
-  Persistencia de datos en MySQL con JDBC
-  Exportar reportes a archivos TXT

---

##  Objetivos Cumplidos

### 1. Gestion de Celulares
- Registrar, actualizar, eliminar y listar celulares
- Validacion de precio y stock positivos
- Campos: ID, marca, modelo, sistema operativo, gama, precio, stock

### 2. Gestion de Clientes  
- CRUD completo de clientes
- Validacion de formato de correo electronico
- Validacion de documento de identidad unico
- Campos: ID, nombre, identificacion, correo, telefono

### 3. Gestion de Ventas
- Registro de ventas con multiples celulares
- Calculo automatico de total con IVA del 19%
- Actualizacion automatica de stock
- Persistencia en base de datos MySQL

### 4. Reportes y Analisis
- Celulares con stock bajo (< 5 unidades)
- Top 3 celulares mas vendidos
- Ventas totales agrupadas por mes
- **Uso intensivo de Stream API** (filter, flatMap, sorted, groupingBy, etc.)

### 5. Persistencia
- Conexion a MySQL con JDBC
- Try-with-resources para manejo seguro de conexiones
- Generacion de archivo `reporte_ventas.txt`

### 6. Patrones de Diseño
- **Factory Pattern**: `FactoryCelular` para creacion de celulares por gama
- **DAO Pattern**: Separacion de logica de acceso a datos
- **MVC Pattern**: Separacion Modelo-Vista-Controlador

### 7. Principios POO
-  **Encapsulamiento**: Atributos privados/protected con getters/setters
-  **Herencia**: `Cliente extends Persona`
-  **Composicion**: `Venta` contiene `List<ItemVenta>`, `ItemVenta` contiene `Celular`
-  **Polimorfismo**: Sobrescritura de `toString()` en clases

---

##  Estructura del Proyecto
```
TecnoStore/
├── src/
│   ├── Controlador/
│   │   ├── CelularControlador.java    # Logica de negocio - Celulares
│   │   ├── ClienteControlador.java    # Logica de negocio - Clientes
│   │   └── VentaControlador.java      # Logica de negocio - Ventas
│   │
│   ├── Modelo/
│   │   ├── Celular.java               # Entidad Celular
│   │   ├── Cliente.java               # Entidad Cliente (hereda de Persona)
│   │   ├── Persona.java               # Clase base para Cliente
│   │   ├── Venta.java                 # Entidad Venta
│   │   ├── ItemVenta.java             # Detalle de venta
│   │   ├── Marca.java                 # Entidad Marca
│   │   ├── CategoriaGama.java         # Enum: ALTA, MEDIA, BAJA
│   │   └── FactoryCelular.java        # Patron Factory
│   │
│   ├── Persistencia/
│   │   ├── ConexionDB.java            # Gestion de conexion MySQL
│   │   ├── CelularDB.java             # DAO Celulares
│   │   ├── ClienteDB.java             # DAO Clientes
│   │   ├── VentaDB.java               # DAO Ventas
│   │   └── MarcaDB.java               # DAO Marcas
│   │
│   ├── Vista/
│   │   └── MenuPrincipal.java         # Interfaz de consola
│   │
│   └── Principal/
│       └── Main.java                  # Punto de entrada
│
├── tecnostore_db.sql                  # Script de base de datos
├── reporte_ventas.txt                 # Reporte generado (auto)
└── README.md                          # Este archivo
```

---

##  Diagrama de Clases

### Modelo (Entidades)
```
Persona (abstract)
├── id: int
├── nombre: String
├── documento: String
└── telefono: String
    │
    └── Cliente
        └── correo: String

Celular
├── id: int
├── marca: Marca
├── modelo: String
├── sistemaOperativo: String
├── gama: CategoriaGama
├── precio: double
└── stock: int

Venta
├── id: int
├── cliente: Cliente
├── fecha: LocalDate
├── items: List<ItemVenta>
└── total: double

ItemVenta
├── celular: Celular
├── cantidad: int
└── subtotal: double

<<enum>> CategoriaGama
├── ALTA
├── MEDIA
└── BAJA
```

### Relaciones
- `Cliente` **hereda de** `Persona`
- `Venta` **tiene muchos** `ItemVenta` (composicion)
- `ItemVenta` **tiene un** `Celular` (composicion)
- `Celular` **tiene una** `Marca` (composicion)

---

##  Base de Datos

### Modelo Entidad-Relacion
```
clientes (id, nombre, identificacion, correo, telefono)
    │
    │ 1:N
    ▼
ventas (id, id_cliente, fecha, total)
    │
    │ 1:N
    ▼
detalle_ventas (id, id_venta, id_celular, cantidad, subtotal)
    │
    │ N:1
    ▼
celulares (id, marca, modelo, sistema_operativo, gama, precio, stock)
```

### Script SQL
```sql
CREATE DATABASE TecnoStore;
USE TecnoStore;

-- Tabla de marcas
CREATE TABLE marcas (
    id INT PRIMARY KEY AUTO_INCREMENT,
    nombre VARCHAR(50) UNIQUE NOT NULL
);

-- Tabla de celulares
CREATE TABLE celulares (
    id INT PRIMARY KEY AUTO_INCREMENT,
    marca VARCHAR(50) NOT NULL,
    modelo VARCHAR(50) NOT NULL,
    sistema_operativo VARCHAR(50) NOT NULL,
    gama VARCHAR(20) NOT NULL,
    precio DOUBLE NOT NULL,
    stock INT NOT NULL
);

-- Tabla de clientes
CREATE TABLE clientes (
    id INT PRIMARY KEY AUTO_INCREMENT,
    nombre VARCHAR(100) NOT NULL,
    identificacion VARCHAR(50) UNIQUE NOT NULL,
    correo VARCHAR(100) NOT NULL,
    telefono VARCHAR(20) NOT NULL
);

-- Tabla de ventas
CREATE TABLE ventas (
    id INT PRIMARY KEY AUTO_INCREMENT,
    id_cliente INT NOT NULL,
    fecha DATE NOT NULL,
    total DOUBLE NOT NULL,
    FOREIGN KEY (id_cliente) REFERENCES clientes(id)
);

-- Tabla de detalle de ventas
CREATE TABLE detalle_ventas (
    id INT PRIMARY KEY AUTO_INCREMENT,
    id_venta INT NOT NULL,
    id_celular INT NOT NULL,
    cantidad INT NOT NULL,
    subtotal DOUBLE NOT NULL,
    FOREIGN KEY (id_venta) REFERENCES ventas(id),
    FOREIGN KEY (id_celular) REFERENCES celulares(id)
);
```

---

##  Configuracion e Instalacion

### Requisitos Previos

-  **Java JDK 17 o superior**
-  **MySQL 8.0 o superior**
-  **MySQL Connector/J** (JDBC Driver)
-  **IDE** (NetBeans, IntelliJ IDEA o Eclipse)

### Pasos de Instalacion

#### 1. Clonar el Repositorio
```bash
git clone https://github.com/tu-usuario/tecnostore.git
cd tecnostore
```

#### 2. Configurar Base de Datos
```bash
# Iniciar MySQL
mysql -u root -p

# Ejecutar script de base de datos
mysql -u root -p < tecnostore_db.sql
```

O copiar y pegar el contenido de `tecnostore_db.sql` en MySQL Workbench.

#### 3. Configurar Conexion en el Codigo

Editar `ConexionDB.java` (lineas 8-10):
```java
private static final String URL = "jdbc:mysql://localhost:3306/TecnoStore";
private static final String USUARIO = "root";
private static final String PASSWORD = "tu_contraseña";
```

#### 4. Agregar MySQL Connector al Proyecto

**En NetBeans:**
1. Click derecho en `Libraries`
2. `Add JAR/Folder...`
3. Seleccionar `mysql-connector-j-X.X.X.jar`

**En IntelliJ IDEA:**
1. `File > Project Structure > Libraries`
2. `+ > Java`
3. Seleccionar el JAR del MySQL Connector

#### 5. Compilar y Ejecutar
```bash
# Compilar
javac -cp .:mysql-connector-j-9.6.0.jar Principal/Main.java

# Ejecutar
java -cp .:mysql-connector-j-9.6.0.jar Principal.Main
```

O ejecutar directamente desde el IDE: `Run > Run File` (Shift+F6)

---

## 🎮 Ejemplo de Ejecucion

### Menu Principal
```
========================================
    bienvenido a TecnoStore
========================================
1. Gestion de Celulares
2. Gestion de Clientes
3. Gestion de Ventas
4. Reportes
5. Salir
========================================
Seleccione una opcion (debe ser numerica, ejemplo: 1):
```

### 1. Registrar Celular
```
--- Registrar Celular ---
Marca: Samsung
Modelo: Galaxy S24 Ultra
Sistema Operativo: Android
Gama (ALTA/MEDIA/BAJA): ALTA
Precio: 5200000
Stock: 8

conexion exitosa a la base de datos
celular registrado correctamente
```

### 2. Listar Celulares (Formateado)
```
========================================
         LISTADO DE CELULARES
========================================

ID: 1
Marca: Samsung
Modelo: Galaxy S24 Ultra
Sistema Operativo: Android
Gama: ALTA
Precio: $5,200,000
Stock: 8 unidades
----------------------------------------
```

### 3. Registrar Cliente
```
--- Registrar Cliente ---
Nombre: Carlos Rodriguez
Documento de identidad: 1010234567
Correo electronico: carlos.rodriguez@gmail.com
Telefono: 3001234567

conexion exitosa a la base de datos
cliente registrado exitosamente
```

### 4. Registrar Venta
```
--- Registrar Venta ---
ID del cliente: 1
ID del celular: 1
Cantidad: 2
¿Agregar otro celular? (s/n): n

=== VENTA REGISTRADA ===
Cliente: Carlos Rodriguez
Fecha: 2026-02-08
Total (con IVA 19%): $12,376,000.00
========================
```

### 5. Top 3 Celulares Mas Vendidos
```
=== TOP 3 CELULARES MAS VENDIDOS ===
Samsung Galaxy S24 Ultra - Vendidos: 5 unidades
Xiaomi Redmi Note 13 Pro - Vendidos: 4 unidades
Motorola Moto G54 - Vendidos: 3 unidades
=====================================
```

### 6. Ventas por Mes
```
=== VENTAS TOTALES POR MES ===
Mes: 2026-01 - Total: $15,234,500.00
Mes: 2026-02 - Total: $24,567,800.00
===============================
```

### 7. Generar Reporte TXT
```
Reporte generado exitosamente: reporte_ventas.txt
```

**Contenido de `reporte_ventas.txt`:**
```
========================================
       REPORTE DE VENTAS - TECNOSTORE
========================================
Fecha de generacion: 2026-02-08
Total de ventas: 8
========================================

----------------------------------------
Venta ID: 1
Cliente: Carlos Rodriguez
Fecha: 2026-02-01
Items vendidos:
  - Samsung Galaxy S24 Ultra | Cantidad: 1 | Subtotal: $5,200,000.00
TOTAL (con IVA): $6,188,000.00
----------------------------------------

========================================
TOTAL GENERAL: $39,802,300.00
========================================
```

---

##  Patrones de Diseño Implementados

### 1. Factory Pattern (FactoryCelular)

**Proposito:** Encapsular la creacion de objetos `Celular` segun la gama.

**Implementacion:**
```java
public class FactoryCelular {
    public static Celular crearCelular(String gama, Marca marca, String modelo, 
                                       String so, double precio, int stock) {
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
                categoriaGama = CategoriaGama.MEDIA;
        }
        
        return new Celular(0, marca, modelo, so, categoriaGama, precio, stock);
    }
}
```

**Uso:**
```java
Celular celular = FactoryCelular.crearCelular("ALTA", marca, modelo, so, precio, stock);
```

### 2. DAO Pattern (Data Access Object)

**Proposito:** Separar la logica de acceso a datos de la logica de negocio.

**Implementacion:**
- `CelularDB.java` - CRUD de celulares
- `ClienteDB.java` - CRUD de clientes
- `VentaDB.java` - CRUD de ventas
- `MarcaDB.java` - CRUD de marcas

### 3. MVC Pattern (Modelo-Vista-Controlador)

**Modelo:** Entidades de negocio (`Celular`, `Cliente`, `Venta`, etc.)  
**Vista:** Interfaz de consola (`MenuPrincipal.java`)  
**Controlador:** Logica de negocio (`CelularControlador`, `ClienteControlador`, `VentaControlador`)

---

## 🔬 Tecnologias Utilizadas

| Tecnologia | Uso |
|------------|-----|
| **Java 21** | Lenguaje principal |
| **MySQL 8.0** | Base de datos relacional |
| **JDBC** | Conexion a base de datos |
| **Stream API** | Procesamiento funcional de colecciones |
| **Try-with-Resources** | Gestion automatica de recursos |
| **Enums** | Representacion de gamas de celulares |
| **LocalDate** | Manejo de fechas |
| **BufferedWriter** | Escritura eficiente de archivos |

---

##  Capturas de Pantalla

### Menu Principal
![Menu Principal](docs/screenshots/menu_principal.png)

### Listado de Celulares
![Listado Celulares](docs/screenshots/listado_celulares.png)

### Registro de Venta
![Registro Venta](docs/screenshots/registro_venta.png)

### Reportes
![Reportes](docs/screenshots/reportes.png)

*(Agregar carpeta `docs/screenshots/` con las capturas)*

---

##  Ejemplos de Uso de Stream API

### Stock Bajo
```java
List<Celular> stockBajo = todos.stream()
    .filter(c -> c.getStock() < 5)
    .toList();
```

### Top 3 Mas Vendidos
```java
ventasPorCelular.entrySet().stream()
    .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
    .limit(3)
    .forEach(entry -> System.out.println(entry.getKey() + " - Vendidos: " + entry.getValue()));
```

### Ventas por Mes
```java
Map<String, Double> ventasPorMes = ventas.stream()
    .collect(Collectors.groupingBy(
        v -> v.getFecha().format(DateTimeFormatter.ofPattern("yyyy-MM")),
        Collectors.summingDouble(Venta::getTotal)
    ));
```

### Validacion de Documento Unico
```java
boolean existe = clientes.stream()
    .anyMatch(c -> c.getDocumento().equals(documento));
```

---

##  Manejo de Excepciones

### Try-with-Resources en JDBC
```java
try (Connection conn = ConexionDB.obtenerConexion();
     PreparedStatement ps = conn.prepareStatement(sql)) {
    
    ps.executeUpdate();
    
} catch (SQLException e) {
    System.out.println("Error: " + e.getMessage());
}
```

### Try-with-Resources en Archivos
```java
try (BufferedWriter writer = new BufferedWriter(new FileWriter("reporte_ventas.txt"))) {
    
    writer.write("Contenido del reporte\n");
    
} catch (IOException e) {
    System.out.println("Error al generar archivo: " + e.getMessage());
}
```

---

##  Validaciones Implementadas

### Celulares
-  Precio debe ser mayor a 0
-  Stock no puede ser negativo

### Clientes
-  Formato de correo electronico valido (regex)
-  Numero de identificacion unico en la base de datos

### Ventas
-  Cliente debe existir en la base de datos
-  Celular debe existir en la base de datos
-  Stock suficiente para la venta

---

##  Funcionalidades Destacadas

1. **Calculo Automatico de IVA:** Toda venta aplica el 19% automaticamente
2. **Actualizacion de Stock en Tiempo Real:** Al registrar una venta, el stock se actualiza inmediatamente
3. **Reportes Dinamicos:** Uso intensivo de Stream API para calculos complejos
4. **Formato Mejorado:** Listados con formato profesional y legible
5. **Persistencia Completa:** Todas las operaciones se guardan en MySQL
6. **Exportacion a TXT:** Generacion automatica de reportes en archivo de texto

---

##  Autor

**Luis Angel Gelvez Delgado**  
📧 Email: luisangelgelvezdelgado1750@gmail.com 
🔗 GitHub: [@luis-angel-gelvez-delgado](https://github.com/luis-angel-gelvez-delgado)  
🎓 Proyecto Academico - Programacion de Java

---

##  Licencia

Este proyecto es de uso academico y educativo.

---

## 🙏 Agradecimientos

- Al profesor por la guia brindada a lo largo del camino
- A la comunidad de Stack Overflow por resolver dudas
- A MySQL y Oracle por las herramientas de desarrollo
- Y efectivamente, a ClaudeIA por la ayuda brindada con la busqueda y parche de una gran cantidad de errores

---

**⭐ Si te gusto este proyecto, dale una estrella en GitHub!**
