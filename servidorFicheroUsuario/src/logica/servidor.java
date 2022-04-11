package logica;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Servidor que espera clientes para mostrar un directorio y pasar el contenido
 * de los ficheros seleccionados. Gestión multiple mediante hilos.
 *
 * @author Alberto
 */
public class servidor extends Thread {

    static final int PUERTO = 1500;
    Socket sc;

    /*Constructor*/
    public servidor(Socket sCliente) {

        sc = sCliente;

    }

    /**
     * @param args Argumentos ignorados.
     */
    public static void main(String[] args) {
        String num;
        try {
            /*Inicio del servidor*/
            ServerSocket servidor = new ServerSocket(PUERTO);
            System.out.println("Servidor operativo, esperando cliente");

            /*Bucle donde siempre esta el servidor esperando clientes*/
            while (true) {

                /*STOP Iniciamos la escucha del puerto*/
                Socket sc = servidor.accept();

                /*iniciamos el hilo*/
                new servidor(sc).start();
            }

        } catch (IOException ex) {
            Logger.getLogger(servidor.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    /**
     * Acciones que ejecutara el hilo.
     */
    @Override
    public void run() {
        DataInputStream in;
        DataOutputStream out;
        String nombreFichero, usuario, pass, seleccion, doc;
        boolean control, verif;
        int i, limite;
        ArrayList<String> datos = new ArrayList<>();

        verif = true;

        try {
            /*Flujos de entrada y salida*/
            in = new DataInputStream(sc.getInputStream());
            out = new DataOutputStream(sc.getOutputStream());

            while (verif) {
                /*STOP Esperamos el usuario del cliente*/
                usuario = in.readUTF();//No se trabaja la variable usuario.

                /*STOP Esperamos la contraseña del cliente*/
                pass = in.readUTF();

                if (pass.contains("procesos")) {

                    out.writeUTF("¡Acceso concedido!");
                    verif = false;

                } else {
                    out.writeUTF("¡Contraseña incorrecta!");
                }

            }

            verif = true;

        /*Bucle de selección para listar, ver archivo o salir*/
        while (verif) {

            out.writeUTF("Seleccione una opción \n"
                    + "1 Listar directorio\n"
                    + "2 Mostrar contenido del archivo\n"
                    + "3 Salir");

            /*STOP Esperamos la selección del cliente*/
            seleccion = in.readUTF();

            switch (seleccion) {

                case "1":
                    File dir = new File("almacen");
                    File[] ficheros = dir.listFiles();
                    limite = ficheros.length;

                    out.writeInt(limite);

                    for (i = 0; i < limite; i++) {

                        out.writeUTF(ficheros[i].getName());
                    }

                    break;
                case "2":
                    /*IMPORTANTE Es el servidor quien muestra el archivo,
                    el cliente no tiene acceso a el, solo a su contenido*/

                    out.writeUTF("Escriba el nombre del archivo a mostrar:");

                    /*Recibimos nombre del fichero a mostrar*/
                    nombreFichero = in.readUTF();

                    /*Leemos el fichero mediante buffer*/
                    FileReader f = new FileReader("almacen/" + nombreFichero);
                    BufferedReader b = new BufferedReader(f);
                    limite = 0;

                    /*Añadimos las lineas leidas en el array*/
                    while ((doc = b.readLine()) != null) {
                        datos.add(doc);
                    }

                    /*Pasamos limite de lectura*/
                    limite = datos.size();
                    out.writeInt(limite);

                    /*Enviamos los datos*/
                    for (i = 0; i < limite; i++) {

                        out.writeUTF(datos.get(i));
                    }

                    break;
                case "3":
                    verif = false;
                    break;

                }

            }

            /*Cierre de flujos*/
            in.close();
            out.close();

            /*Cierre conexion*/
            sc.close();

        } catch (IOException ex) {
            System.err.println("Se ha perdido la conexión con el cliente.");
        } catch (NullPointerException e) {
            Logger.getLogger(servidor.class.getName()).log(Level.SEVERE, null, e);
        }

    }

}
