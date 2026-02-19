package main;

import sockets.ClienteTCP;
import pruebas.Producto;
import pruebas.SerializadorProducto;
import java.util.Date;
import java.nio.charset.StandardCharsets;

public class AppCliente {

    public static void main(String[] args) {
        ClienteTCP cliente = new ClienteTCP();
        String host = "192.168.100.98";
        int puerto = 9000;

        try {
            System.out.println("\n--- ENVIANDO EN MODO TEXTO ---");
            Producto p1 = new Producto("TXT001", "Coca Cola Texto", 25.50, 10, "Femsa", new Date());

            String msgTxt = SerializadorProducto.serializar(p1);
            byte[] bytesTxt = msgTxt.getBytes(StandardCharsets.UTF_8);

            cliente.enviar(host, puerto, bytesTxt, 0);
            Thread.sleep(500);

            System.out.println("\n--- ENVIANDO EN MODO BINARIO ---");
            Producto p2 = new Producto("BIN002", "Pepsi Binaria", 22.00, 50, "Pepsico", new Date());

            byte[] bytesBin = SerializadorProducto.serializarBinario(p2);

            System.out.println("Vista del paquete crudo (Hexadecimal):");
            StringBuilder sb = new StringBuilder();
            int contador = 0;
            for (byte b : bytesBin) {
                sb.append(String.format("%02X ", b));

                contador++;
                if (contador % 16 == 0) {
                    sb.append("\n");
                }
            }
            System.out.println(sb.toString());
            System.out.println("----------------------------------------");

            cliente.enviar(host, puerto, bytesBin, 1);

            Thread.sleep(1000);

        } catch (Exception e) {
            e.printStackTrace();
        }

        cliente.detener();
    }

}
