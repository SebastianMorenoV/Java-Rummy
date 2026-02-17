package sockets;

import utils.MensajeEncolado;
import contratos.iDespachador;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class ClienteTCP implements iDespachador, Runnable {
    private final BlockingQueue<MensajeEncolado> colaDeSalida;
    private volatile boolean ejecutando = true;

    public ClienteTCP() {
        this.colaDeSalida = new LinkedBlockingQueue<>();
        new Thread(this).start();
    }

    // Método actualizado para recibir bytes y tipo
    @Override
    public void enviar(String host, int puerto, byte[] datos, int tipo) throws IOException {
        try {
            colaDeSalida.put(new MensajeEncolado(host, puerto, datos, tipo));
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    @Override
    public void run() {
        System.out.println("[Despachador] Hilo iniciado.");
        while (ejecutando) {
            try {
                MensajeEncolado msg = colaDeSalida.take();
                enviarDestinatario(msg);
            } catch (InterruptedException e) {
                ejecutando = false;
                Thread.currentThread().interrupt();
            } catch (IOException e) {
                System.err.println("Error enviando: " + e.getMessage());
            }
        }
    }

    private void enviarDestinatario(MensajeEncolado msg) throws IOException {
        try (Socket socket = new Socket(msg.host, msg.puerto); 
             DataOutputStream out = new DataOutputStream(socket.getOutputStream())) {
            
            // PROTOCOLO PROPIO:
            // 1. Enviar Tipo (byte)
            out.writeByte(msg.tipo);
            // 2. Enviar Longitud (int)
            out.writeInt(msg.datos.length);
            // 3. Enviar Datos (bytes)
            out.write(msg.datos);
            
            System.out.println("[Cliente] Enviado paquete de " + msg.datos.length + " bytes (Tipo: " + (msg.tipo == 0 ? "TXT" : "BIN") + ")");
        }
    }

    public void detener() { this.ejecutando = false; }
}