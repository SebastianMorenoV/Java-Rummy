package utils;
import java.net.Socket;

public class PeticionCliente {
    public final Socket socketCliente;
    public final byte[] datos; 
    public final int tipo;     
    public final String ipCliente;
    public final long tiempoInicio;

    public PeticionCliente(Socket socketCliente, byte[] datos, int tipo) {
        this.socketCliente = socketCliente;
        this.datos = datos;
        this.tipo = tipo;
        this.ipCliente = socketCliente.getInetAddress().getHostAddress();
        this.tiempoInicio = System.nanoTime(); 
    }
}