package logica;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 * Cliente que se conecta a servidor para solicitar un fichero, que este le
 * enviara si existe.
 *
 * @author alberto
 */
public class cliente {

    /**
     * @param args agumentos ignorados.
     */
    public static void main(String[] args) {
        final String HOST = "localhost";
        final int PUERTO = 1500;
        DataInputStream in;
        DataOutputStream out;
        String nombreFichero, usuario, pass, seleccion;
        int i, limite;
        boolean control = true;
        String msj;

        try {

            /*Creación del socket cliente con los datos del servidor*/
            Socket sc = new Socket(HOST, PUERTO);

            /*Flujos de datos*/
            in = new DataInputStream(sc.getInputStream());
            out = new DataOutputStream(sc.getOutputStream());

            /*Bucle para validar la contraseña*/
            while (control) {

                /*Solicitud del nombre de usuario, en este caso cualquiera vale*/
                usuario = JOptionPane.showInputDialog(null, "Introduzca nombre de usuario:");
                out.writeUTF(usuario);

                /*Solicitud de contraseña, contraseña fija. Es procesos*/
                pass = JOptionPane.showInputDialog(null, "Introduzca contraseña (procesos):");
                out.writeUTF(pass);

                /*STOP esperamos respuesta del servidor*/
                msj = in.readUTF();
                System.out.println(msj);

                /*Tenemos acceso*/
                if (msj.contains("¡Acceso concedido!")) {

                    control = false;
                } else {
                }//Si no se cumple la condición se reitera en el bucle.
            }

            control = true;

            while (control) {
                
                /*STOP esperamos las opciones de selección del servidor*/
                msj = in.readUTF();
                seleccion = JOptionPane.showInputDialog(null, msj);
                out.writeUTF(seleccion);
                
                /*Caminos según seleccion*/
                if ("1".equals(seleccion)) {

                    /*Limite que nos marca cuantos mensajes debemos escuchar*/
                    limite = in.readInt();

                    /*Listamos directorio almacén, que se encuentra en el
                    directorio de trabajo del proyecto, por consola*/
                    for (i = 0; i < limite; i++) {

                        msj = in.readUTF();
                        System.out.println(msj);

                    }

                }

                if ("2".equals(seleccion)) {

                    /*Recibimos petición del nombre de archivo y contestamos*/
                    msj = in.readUTF();
                    nombreFichero = JOptionPane.showInputDialog(null, msj);
                    out.writeUTF(nombreFichero);
                    
                    
                    //*Límite que nos marca cuantos mensajes debemos escuchar*/
                    limite = in.readInt();
                    
                    /*Impresión de los datos del archivo solicitado*/
                    for(i=0; i<limite; i++){
                    
                        msj = in.readUTF();
                        System.out.println(msj);
                    }
                    

                }
                /*Salir del programa*/
                if ("3".equals(seleccion)) {

                    control = false;
                }

            }

        } catch (IOException ex) {
            Logger.getLogger(cliente.class.getName()).log(Level.SEVERE, null, ex);
        }catch(NullPointerException e){
            System.exit(0);
        }
          

    }

}
