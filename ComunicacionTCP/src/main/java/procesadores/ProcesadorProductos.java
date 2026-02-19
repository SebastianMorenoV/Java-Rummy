package procesadores;

import pruebas.Producto;
import pruebas.SerializadorProducto;
import java.nio.charset.StandardCharsets;

public class ProcesadorProductos   {

    
    public void procesar(String ipRemitente, byte[] datos, int tipo, long tiempoInicio) {
        long tiempoFin = System.nanoTime();
        double tiempoProcesamientoMs = (tiempoFin - tiempoInicio) / 1_000_000.0;
        
        try {
            Producto producto = null;
            String modo = "";

            if (tipo == 0) {
                modo = "TEXTO";
                String mensajeStr = new String(datos, StandardCharsets.UTF_8);
                producto = SerializadorProducto.deserializar(mensajeStr);
            } else if (tipo == 1) {
                modo = "BINARIO";
                producto = SerializadorProducto.deserializarBinario(datos);
            }

            System.out.println("------------------------------------------------");
            System.out.println("[Servidor] Recibido de: " + ipRemitente);
            System.out.println(" MODO: " + modo);
            System.out.println(" TAMAÑO PAQUETE: " + datos.length + " bytes");
            System.out.println(" TIEMPO PROCESAMIENTO: " + String.format("%.4f", tiempoProcesamientoMs) + " ms");
            System.out.println(" DETALLES: " + producto.toString());
            System.out.println("------------------------------------------------");

        } catch (Exception e) {
            System.err.println("Error al procesar (" + (tipo==0?"Txt":"Bin") + "): " + e.getMessage());
            e.printStackTrace();
        }
    }
}