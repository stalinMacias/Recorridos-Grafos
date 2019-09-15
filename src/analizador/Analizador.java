

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package analizador;

/**
 *
 * @author laptop-stalin
 */
import java.io.*;
import java.util.Scanner;

public class Analizador {
    
    public static void analizadorArchivo(String ruta) throws IOException {
        //System.out.println("Esta seccion esta en construcción");
        BufferedReader bf;
        try {
          bf = new BufferedReader(new FileReader(new File(ruta)));
        } catch(Exception e) {
          System.out.println(e);
          return;
        }

        String st;
        Scanner s;
        while ((st = bf.readLine()) != null) {
            s = new Scanner(st); // Analiza la linea actual del archivo
            
            String aux = "";

            while (((st = bf.readLine()) != null) && (!st.equals("FIN")) ) {
                if(st.trim().length() == 0) continue;
                if(st.contains("#")) continue; // '#'Comentarios en el archivo de texto!
                aux += st+"\n";
            }
            
            System.out.println(aux);

            
        }   
  }//Cierre metodo analizadorArchivo
    /**
     * 
     * to do list:
     *  Crear el metodo para leer el archivo!
     */
}
