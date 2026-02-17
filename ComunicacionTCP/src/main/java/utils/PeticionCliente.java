package utils;
import java.net.Socket;

public class PeticionCliente {
    public final Socket socketCliente;
    public final byte[] datos; // Cambiado a byte[]
    public final int tipo;     // 0 = Texto, 1 = Binario
    public final String ipCliente;
    public final long tiempoInicio; // Para medir latencia

    public PeticionCliente(Socket socketCliente, byte[] datos, int tipo) {
        this.socketCliente = socketCliente;
        this.datos = datos;
        this.tipo = tipo;
        this.ipCliente = socketCliente.getInetAddress().getHostAddress();
        this.tiempoInicio = System.nanoTime(); // Marca de tiempo al recibir
    }
}