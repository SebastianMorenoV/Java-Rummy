package sockets;

import utils.PeticionCliente;
import contratos.iListener;
import contratos.iProcesador;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import procesadores.ProcesadorProductos;

public class ServerTCP implements iListener {
    private final ProcesadorProductos procesador;
    private final BlockingQueue<PeticionCliente> colaDeEntrada;
    private volatile boolean ejecutando = true;
    private ServerSocket serverSocket;

    public ServerTCP(ProcesadorProductos procesador) {
        this.procesador = procesador;
        this.colaDeEntrada = new LinkedBlockingQueue<>();
        new Thread(this::procesarCola).start();
    }

    @Override
    public void iniciar(int puerto) throws IOException {
        serverSocket = new ServerSocket(puerto);
        System.out.println("[Listener " + puerto + "] Esperando conexiones...");

        while (ejecutando) {
            try {
                Socket socketCliente = serverSocket.accept();
                DataInputStream in = new DataInputStream(socketCliente.getInputStream());

                // PROTOCOLO DE LECTURA:
                // 1. Leer Tipo
                byte tipo = in.readByte();
                // 2. Leer Longitud
                int longitud = in.readInt();
                // 3. Leer Datos
                byte[] buffer = new byte[longitud];
                in.readFully(buffer);

                // Encolamos (el constructor de PeticionCliente captura el System.nanoTime())
                colaDeEntrada.put(new PeticionCliente(socketCliente, buffer, tipo));

            } catch (IOException e) {
                if (ejecutando) System.err.println("Error conexión: " + e.getMessage());
            } catch (InterruptedException e) {
                ejecutando = false;
                Thread.currentThread().interrupt();
            }
        }
    }

    private void procesarCola() {
        while (ejecutando) {
            PeticionCliente peticion = null;
            try {
                peticion = colaDeEntrada.take();
                // Delegar al procesador pasando datos y métricas
                this.procesador.procesar(peticion.ipCliente, peticion.datos, peticion.tipo, peticion.tiempoInicio);

            } catch (InterruptedException e) {
                ejecutando = false;
            } catch (Exception e) {
                System.err.println("Error procesando: " + e.getMessage());
            } finally {
                if (peticion != null && peticion.socketCliente != null) {
                    try { peticion.socketCliente.close(); } catch (IOException ex) {}
                }
            }
        }
    }

    @Override
    public void detener() throws IOException {
        ejecutando = false;
        if (serverSocket != null) serverSocket.close();
    }
}