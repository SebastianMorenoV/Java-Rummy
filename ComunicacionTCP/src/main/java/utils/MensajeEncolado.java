package utils;

public class MensajeEncolado {
    public final String host;
    public final int puerto;
    public final byte[] datos; // Cambiado de String a byte[]
    public final int tipo;     // 0 = Texto, 1 = Binario

    public MensajeEncolado(String host, int puerto, byte[] datos, int tipo) {
        this.host = host;
        this.puerto = puerto;
        this.datos = datos;
        this.tipo = tipo;
    }
}