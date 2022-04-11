package logica;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
 * Metodos estaticos para manejo de ficheros.
 * @author alberto No utilizamos la clase en el proyecto.
 */
public class herramientas {

    /**
     *
     * @param nombre
     * @return boolean Método que nos indica la existencia de un fichero.
     */
    public static boolean Existe(String nombre) {

        /*Comprobamos la existencia del archivo con isFile*/
        File archivo = new File(nombre);

        return (archivo.isFile());
    }

    /**
     *
     * @param archivo
     * @throws FileNotFoundException, IOException
     * Visualización de un archivo local.
     */
    public static void verDoc(String archivo) throws FileNotFoundException, IOException {
        String cadena;
        FileReader f = new FileReader(archivo);
        BufferedReader b = new BufferedReader(f);
        while ((cadena = b.readLine()) != null) {
            System.out.println(cadena);
        }
        b.close();
    }

    /**
     *
     * @param archivo
     * @return
     * @throws FileNotFoundException
     * @throws IOException 
     * Metodo que concatatena texto en variable, los
     * archivos estan en la carpeta almacen del proyecto.
     */
    public static String docEntero(String archivo) throws FileNotFoundException, IOException {
        String cadena = "";
        FileReader f = new FileReader("almacen/" + archivo);
        BufferedReader b = new BufferedReader(f);

        cadena = b.readLine();

        return cadena;
    }

}
