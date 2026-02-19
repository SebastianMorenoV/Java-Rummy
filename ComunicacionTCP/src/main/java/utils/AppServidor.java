package utils;


import sockets.ServerTCP;
import procesadores.ProcesadorProductos;
import java.io.IOException;

public class AppServidor {
    public static void main(String[] args) {
        ProcesadorProductos procesador = new ProcesadorProductos();

        ServerTCP servidor = new ServerTCP(procesador);
        
        try {
            servidor.iniciar(9000);
        } catch (IOException e) {
            System.err.println("Error en el servidor: " + e.getMessage());
        }
    }
}