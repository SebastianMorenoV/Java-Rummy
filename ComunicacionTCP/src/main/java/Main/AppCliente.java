package main;

import sockets.ClienteTCP;
import pruebas.Producto;
import pruebas.SerializadorProducto;
import java.util.Date;
import java.nio.charset.StandardCharsets;

public class AppCliente {
    public static void main(String[] args) {
        ClienteTCP cliente = new ClienteTCP();
        String host = "127.0.0.1"; 
        int puerto = 5000;

        try {
            // --- PRUEBA 1: MODO TEXTO (Original) ---
            System.out.println("\n--- ENVIANDO EN MODO TEXTO ---");
            Producto p1 = new Producto("TXT001", "Coca Cola Texto", 25.50, 10, "Femsa", new Date());
            
            String msgTxt = SerializadorProducto.serializar(p1);
            byte[] bytesTxt = msgTxt.getBytes(StandardCharsets.UTF_8);
            
            // Enviamos con tipo 0
            cliente.enviar(host, puerto, bytesTxt, 0); 
            Thread.sleep(500);

            // --- PRUEBA 2: MODO BINARIO (Nuevo) ---
            System.out.println("\n--- ENVIANDO EN MODO BINARIO ---");
            Producto p2 = new Producto("BIN002", "Pepsi Binaria", 22.00, 50, "Pepsico", new Date());
            
            byte[] bytesBin = SerializadorProducto.serializarBinario(p2);
            
            // Enviamos con tipo 1
            cliente.enviar(host, puerto, bytesBin, 1);
            
            Thread.sleep(1000); // Esperar a que se envíen

        } catch (Exception e) {
            e.printStackTrace();
        }
        
        cliente.detener();
    }
}