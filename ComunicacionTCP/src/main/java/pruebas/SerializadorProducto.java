package pruebas;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import pruebas.Producto;

public class SerializadorProducto {
    
    private static final String SEPARADOR = "/";
    private static final SimpleDateFormat FORMATO_FECHA = new SimpleDateFormat("dd/MM/yyyy");

    // Convierte un Producto a String
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

    // Convierte un String a Producto
    public static Producto deserializar(String mensaje) throws ParseException, NumberFormatException {
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
}