package pruebas;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SerializadorProducto {
    
    private static final String SEPARADOR = "/";
    private static final SimpleDateFormat FORMATO_FECHA = new SimpleDateFormat("dd/MM/yyyy");

    // --- MÉTODOS EXISTENTES (TEXTO) ---
    public static String serializar(Producto p) {
        if (p == null) return "";
        return String.join(SEPARADOR,
                p.getClave(),
                p.getNombre(),
                String.valueOf(p.getPrecio()),
                String.valueOf(p.getCantidad()),
                p.getMarca(),
                FORMATO_FECHA.format(p.getFechaRegistro())
        );
    }

    public static Producto deserializar(String mensaje) throws Exception {
        String[] datos = mensaje.split(SEPARADOR);
        if (datos.length < 6) throw new IllegalArgumentException("Formato de mensaje incorrecto");
        Producto p = new Producto();
        p.setClave(datos[0]);
        p.setNombre(datos[1]);
        p.setPrecio(Double.valueOf(datos[2]));
        p.setCantidad(Integer.parseInt(datos[3]));
        p.setMarca(datos[4]);
        p.setFechaRegistro(FORMATO_FECHA.parse(datos[5]));
        return p;
    }

    // --- NUEVOS MÉTODOS (BINARIO) ---
    
    // Serializa a bytes con tamaños fijos
    public static byte[] serializarBinario(Producto p) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);

        // Clave (6 chars)
        writeStringFijo(dos, p.getClave(), 6);
        // Nombre (30 chars)
        writeStringFijo(dos, p.getNombre(), 30);
        // Precio (Double - 8 bytes)
        dos.writeDouble(p.getPrecio());
        // Cantidad (Int - 4 bytes)
        dos.writeInt(p.getCantidad());
        // Marca (30 chars)
        writeStringFijo(dos, p.getMarca(), 30);
        // Fecha (Long/Timestamp - 8 bytes)
        dos.writeLong(p.getFechaRegistro().getTime());

        return baos.toByteArray();
    }

    // Deserializa desde bytes
    public static Producto deserializarBinario(byte[] datos) throws IOException {
        ByteArrayInputStream bais = new ByteArrayInputStream(datos);
        DataInputStream dis = new DataInputStream(bais);

        Producto p = new Producto();
        p.setClave(readStringFijo(dis, 6));
        p.setNombre(readStringFijo(dis, 30));
        p.setPrecio(dis.readDouble());
        p.setCantidad(dis.readInt());
        p.setMarca(readStringFijo(dis, 30));
        p.setFechaRegistro(new Date(dis.readLong()));

        return p;
    }

    // --- UTILIDADES PARA STRINGS FIJOS ---
    
    // Escribe chars rellenando con espacios si es corto, o cortando si es largo
    private static void writeStringFijo(DataOutputStream dos, String str, int length) throws IOException {
        if (str == null) str = "";
        // Ajustar tamaño
        if (str.length() > length) {
            str = str.substring(0, length);
        } else {
            while (str.length() < length) {
                str += " "; // Relleno
            }
        }
        dos.writeChars(str); // Escribe 2 bytes por caracter
    }

    private static String readStringFijo(DataInputStream dis, int length) throws IOException {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append(dis.readChar());
        }
        return sb.toString().trim(); // Elimina espacios de relleno
    }
}