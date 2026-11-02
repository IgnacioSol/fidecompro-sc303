# Fidecompro - Sistema de facturación

Proyecto final de SC-303 Programación Cliente/Servidor Concurrente (Universidad Fidélitas).
Autor: Ignacio Solano.

Aplicación de escritorio en Java (Swing) para la cadena de venta al por mayor Fidecompro:
inicio de sesión, mantenimiento de clientes y productos, y creación de facturas con su
archivo de desglose de pago.

## Estado: Avance 2

- Hecho: login, menú, CRUD de clientes, CRUD de productos (con filtro por tipo),
  facturación con cálculo de IVA, descuento de inventario y archivo `.txt` de la factura.
- Los datos se guardan con serialización en archivos `.dat` (en la carpeta desde donde corre el programa).
- Pendiente para la entrega final: servidor con sockets e hilos para varios cajeros a la vez
  y cambiar los archivos `.dat` por la base de datos.

## Cómo abrirlo en NetBeans

1. *File > New Project > Java with Ant > Java Project with Existing Sources*.
2. Nombre: `Fidecompro`. Como carpeta del proyecto se puede usar esta misma carpeta.
3. En *Source Package Folders* agregar la carpeta `src`.
4. Clic derecho al proyecto > *Properties > Run > Main Class*: `fidecompro.Fidecompro`.
5. *Run Project* (F6).

Usuarios de prueba (se crean solos la primera vez): `admin` / `admin123` y `cajero1` / `cajero123`.

## Paquetes

| Paquete | Contenido |
|---|---|
| `fidecompro` | `Fidecompro` (clase principal) |
| `fidecompro.modelo` | `Persona` (abstracta), `Usuario`, `Cliente`, `Producto`, `Factura`, `DetalleFactura` |
| `fidecompro.excepciones` | Excepciones propias del sistema |
| `fidecompro.datos` | `Archivo` y los DAO que guardan y leen los `.dat` |
| `fidecompro.vista` | Ventanas (`JFrame`) |
| `fidecompro.hilos` | `HiloFactura`, que escribe el archivo de la factura en otro hilo |

Requiere JDK 17 o superior. No usa librerías externas.
