package main;

import sockets.ClienteTCP;
import pruebas.Producto;
import pruebas.SerializadorProducto;
import java.util.Date;
import java.io.IOException;

public class AppCliente {
    public static void main(String[] args) {
        // Instanciamos el cliente TCP
        ClienteTCP cliente = new ClienteTCP();
        
        // Configuración
        String host = "localhost"; // O la IP del servidor
        int puerto = 5000;
        int N = 5; // Número de productos a enviar
        
        System.out.println("Iniciando envío de " + N + " productos...");

        for (int i = 1; i <= N; i++) {
            // Crear una instancia de prueba de Producto
            Producto p = new Producto(
                "CLAVE-" + i,
                "Producto " + i,
                100.0 * i,      // Precio
                10 + i,         // Cantidad
                "MarcaGenerica",
                new Date()      // Fecha actual
            );

            // Serializar a String: clave/nombre/precio/cantidad/marca/DD/MM/AAAA
            String mensaje = SerializadorProducto.serializar(p);

            try {
                // Enviar usando el método enviar del ClienteTCP
                cliente.enviar(host, puerto, mensaje);
                
                // Pequeña pausa para no saturar la consola de visualización (opcional)
                Thread.sleep(500); 
            } catch (IOException | InterruptedException e) {
                System.err.println("Error al enviar producto " + i);
            }
        }
        
        // Detener el hilo del cliente al finalizar
        cliente.detener();
    }
}