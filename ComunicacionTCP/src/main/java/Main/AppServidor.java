package Main;


import sockets.ServerTCP;
import procesadores.ProcesadorProductos;
import java.io.IOException;

public class AppServidor {
    public static void main(String[] args) {
        // Instanciamos el procesador específico para productos
        ProcesadorProductos procesador = new ProcesadorProductos();
        
        // Creamos el servidor inyectando el procesador
        // El ServerTCP requiere un iProcesador en su constructor
        ServerTCP servidor = new ServerTCP(procesador);
        
        try {
            // Iniciamos el servidor en el puerto 5000
            servidor.iniciar(5000);
        } catch (IOException e) {
            System.err.println("Error en el servidor: " + e.getMessage());
        }
    }
}