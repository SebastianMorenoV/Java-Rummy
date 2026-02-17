package procesadores;

import contratos.iProcesador;
import pruebas.Producto;
import pruebas.SerializadorProducto;

public class ProcesadorProductos implements iProcesador {

    @Override
    public void procesar(String ipRemitente, String mensaje) {
        try {
            // Deserializamos el mensaje recibido
            Producto producto = SerializadorProducto.deserializar(mensaje);
            
            System.out.println("------------------------------------------------");
            System.out.println("[Servidor] Producto recibido de: " + ipRemitente);
            System.out.println("Detalles: " + producto.toString());
            System.out.println("------------------------------------------------");
            
        } catch (Exception e) {
            System.err.println("Error al procesar el producto: " + e.getMessage());
        }
    }
}