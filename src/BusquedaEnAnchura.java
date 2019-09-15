
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author laptop-stalin
 */
public class BusquedaEnAnchura {
    /**
     * 
     * @param nodoInicio Nodo Inicial del Camino
     * @param nodoFinal  Nodo Final del Camino
     * @param g          Objeto de la Clase Grafo para acceder a los métodos y clases internas de dicha clase
     * @return este metodo devuleve el camino creado con el algoritmo busqueda en anchura
     */
    public ArrayList<Integer> algoritmoBusquedaEnAnchura(int nodoInicio, int nodoFinal, Grafo g) {
        Queue<Hijo> estadosAbiertos = new LinkedList<>();
        ArrayList<Hijo> estadosCerrados = new ArrayList<Hijo>();

        Hijo auxNodoInicio = new Hijo(nodoInicio, -1);  //El nodoInicio no tiene padre, y será identificado con un -1
        estadosAbiertos.add(auxNodoInicio);
        Hijo nodoActual = estadosAbiertos.peek();     //Hace nodoActual igual al nodoInicio que esta en la pila como tipo Hijo

        while (!esFinal(nodoActual.numVerticeHijo, nodoFinal)) {
            estadosAbiertos.remove();         //Quitar la head de la cola!
            estadosCerrados.add(nodoActual);
            ArrayList<Hijo> hijos = g.getEnlaces(nodoActual.numVerticeHijo, false); //El equivalente a generarSucesores();
            hijos = tratarRepetidos(hijos, estadosAbiertos, estadosCerrados); //Eliminar los hijos que se encuentren en estadosCerrados o estadosAbiertos

            for (int i = 0; i < hijos.size(); i++) {
                estadosAbiertos.add(hijos.get(i));                           //Almacenar en estadosAbiertos los hijos que no han sido tratados en el algoritmo! [Nodos pendientes por visitar!]
            }
            nodoActual = estadosAbiertos.peek();                            //Hacer el nodoActual el primer nodo de estadosAbiertos
        }
        ArrayList<Integer> camino = new ArrayList<Integer>();
        
        if (esFinal(nodoActual.numVerticeHijo, nodoFinal)) {
            estadosCerrados.add(nodoActual);          //Guardar el nodo actual en estadosCerrados para completar el camino!
            //textAreaResultados.append("\tEl camino encontrado usando el algoritmo de búsqueda en anchura es: \n\n");
            System.out.println("El camino encontrado usando el algoritmo búsqueda en anchura es: ");
            camino = recorrerEstadosCerrados(estadosCerrados); //Metodo para recorrer e imprimir el camino creado!
        } else {
            System.out.println("No hay camino");
        }
        
        return camino;
    }//Cierre metodo algoritmoBusquedaEnAnchura

    
    /**
     ***************************************************  Este metodo es utilizado para calcular la heuristia ***************************************************
     * 
     *  @varDummy Se usa esta variable para poder diferenciar entre este metodo y el anterior!
        @return este metodo se encarga de calcular la heuristia del nodo que recibe como nodo inicio hasta el nodo final,
              y devuelve el numero de nodos que tiene que recorrer hasta alcanzar dicho final (Sin contar el nodo que en cuestion es el inicio)
    */
    public int algoritmoBusquedaEnAnchura(int nodoInicio, int nodoFinal, Grafo g, int varDummy) {
        Queue<Hijo> estadosAbiertos = new LinkedList<>();
        ArrayList<Hijo> estadosCerrados = new ArrayList<Hijo>();

        Hijo auxNodoInicio = new Hijo(nodoInicio, -1);  //El nodoInicio no tiene padre, y será identificado con un -1
        estadosAbiertos.add(auxNodoInicio);
        Hijo nodoActual = estadosAbiertos.peek();     //Hace nodoActual igual al nodoInicio que esta en la pila como tipo Hijo

        while (!esFinal(nodoActual.numVerticeHijo, nodoFinal)) {
            estadosAbiertos.remove();         //Quitar la head de la cola!
            estadosCerrados.add(nodoActual);
            ArrayList<Hijo> hijos = g.getEnlaces(nodoActual.numVerticeHijo, false); //El equivalente a generarSucesores();
            hijos = tratarRepetidos(hijos, estadosAbiertos, estadosCerrados); //Eliminar los hijos que se encuentren en estadosCerrados o estadosAbiertos

            for (int i = 0; i < hijos.size(); i++) {
                estadosAbiertos.add(hijos.get(i));                           //Almacenar en estadosAbiertos los hijos que no han sido tratados en el algoritmo! [Nodos pendientes por visitar!]
            }
            nodoActual = estadosAbiertos.peek();                            //Hacer el nodoActual el primer nodo de estadosAbiertos
        }
        ArrayList<Integer> camino = new ArrayList<Integer>();

        if (esFinal(nodoActual.numVerticeHijo, nodoFinal)) {
            estadosCerrados.add(nodoActual);          //Guardar el nodo actual en estadosCerrados para completar el camino!
            //textAreaResultados.append("\tEl camino encontrado usando el algoritmo de búsqueda en anchura es: \n\n");
            //System.out.println("El camino encontrado usando el algoritmo búsqueda en anchura es: ");
            camino = recorrerEstadosCerrados(estadosCerrados); //Metodo para recorrer e imprimir el camino creado!
        } else {
            System.out.println("No hay camino");
        }

        return camino.size() -1; //El tamaño del ArrayList camino representa el numero de nodos que hacen falta para llegar al nodo final considerando el nodo actual
                                //Se le resta -1 por que solamente nos interesa saber el numero de nodos que hacen falta por recorrer a partir del nodo hijo!

    }//Cierre metodo algoritmoBusquedaEnAnchura
    
    
    
    
    
    public static ArrayList<Hijo> tratarRepetidos(ArrayList<Hijo> hijos, Queue<Hijo> estadosAbiertos, ArrayList<Hijo> estadosCerrados) {
        ArrayList<Hijo> auxHijos = hijos;
        ArrayList<Hijo> auxToDeleteHijos = new ArrayList<Hijo>();

        if (!hijos.isEmpty()) {
            //Recorrer cada hijo y verificar que no se encuentre ni en estadosCerrados ni en estadosAbiertos
            for (int i = 0; i < auxHijos.size(); i++) {
                Hijo hijo = auxHijos.get(i);
                //System.out.println("Hijo analizando: "+hijo.numVerticeHijo);

                //Verificar que no se encuentre algun hijo en estados abiertos
                /*  Para recorrer los valores de una cola se hacen del primero al final, para esto, se saca de la cola, se analiza y se vuelve a insertar,
            dicho valor se va almacenando al final de la cola!, pero cuando termina de recorrerse completamente, vuelve a su posición original!*/
                int auxSizeQueue = estadosAbiertos.size();
                for (int x = 0; x < auxSizeQueue; x++) {
                    Hijo auxNodoEstadoAbierto = estadosAbiertos.poll();
                    if (hijo.numVerticeHijo == auxNodoEstadoAbierto.numVerticeHijo) {
                        auxToDeleteHijos.add(hijo); //Guardar el objeto a eliminar una vez que se termine de verificar cada hijo
                    }
                    estadosAbiertos.add(auxNodoEstadoAbierto);
                }

                //Verificar que no se encuentre algun hijo en estados cerrados
                for (int x = 0; x < estadosCerrados.size(); x++) {
                    Hijo auxEstadoCerrado = estadosCerrados.get(x);
                    if (hijo.numVerticeHijo == auxEstadoCerrado.numVerticeHijo) {
                        auxToDeleteHijos.add(hijo); //Guardar el objeto a eliminar una vez que se termine de verificar cada hijo
                    }
                }

            }//Cierre del recorrido de cada hijo

            //Recorrer la lista de hijos a eliminar y eliminarlos de la lista de hijos
            for (int i = 0; i < auxToDeleteHijos.size(); i++) {
                hijos.remove(auxToDeleteHijos.get(i));
                //System.out.println("Se elimino el hijo: "+auxToDeleteHijos.get(i).numVerticeHijo+" y su padre era: "+auxToDeleteHijos.get(i).padre);
            }

        }
        //Copiar a la variable auxiliar el estado final de hijos
        auxHijos = hijos;
        return auxHijos;
    }//Cierre tratarRepetidos

    
    public static boolean esFinal(int nodoActual, int nodoFinal) {
        if (nodoActual == nodoFinal) {
            return true;
        } else {
            return false;
        }
    }//Cierre esFinal

    /**
     * Para obtener el camino se debe de recorrer de manera inversa el ArrayList
     * estadosCerrados, la ultima posicion contienen el nodo final y el padre.
     * El nodo padre del ultimo hijo será usado para encontrar al predecesor, el
     * siguiente hijo que sea igual al nodo padre será el siguiente y así
     * sucesivamente... De esta manera se encuentra el camino, pero queda de
     * manera inversa, del fin al inicio, para mostrar el resultado, se debe de
     * invertir dicho camino, para que se presente de inicio a fin
     *
     * @param estadosCerrados -> Es el ArrayList de todos los estadosCerrados
     * que se fueron agregando en el algoritmo
     */
    public ArrayList<Integer> recorrerEstadosCerrados(ArrayList<Hijo> estadosCerrados) {
        ArrayList<Integer> auxCamino = new ArrayList<Integer>();
        auxCamino.add(estadosCerrados.get(estadosCerrados.size() - 1).numVerticeHijo);  //Guardar el ultimo nodo hijo que se encuentra en estados cerrados... dicho nodo es el final!
        int auxNodoPadre = estadosCerrados.get(estadosCerrados.size() - 1).padre;       //Guardar el padre del ultmo nodo que se encuentra en estados cerrados... dicho nodo valor servira para encontrar el camino!

        //System.out.println("--------------------------\nCamino creado:");
        for (int i = estadosCerrados.size() - 1; i > 0; i--) {
            if (estadosCerrados.get(i - 1).numVerticeHijo == auxNodoPadre) {
                auxNodoPadre = estadosCerrados.get(i - 1).padre;
                auxCamino.add(estadosCerrados.get(i - 1).numVerticeHijo);  //Si el siguiente hijo es igual al nodoPadre, agregarlo a la lista de caminos
                //[Se agrega la posición i-1 por que se esta recorriendo de manera inversa el ArrayList estadosCerrados]
            }
        }
        System.out.println("--------------------------\nCamino creado con el Algoritmo Busqueda en Anchura:");
        //Presentar el camino de inicio a fin
        for (int i = auxCamino.size() - 1; i > 0; i--) {
            System.out.println("Del nodo: " + auxCamino.get(i) + " al nodo: " + auxCamino.get(i - 1));
            //textAreaResultados.append("\tDel nodo: " + nombreNodos.get(auxCamino.get(i)) + " al nodo: " + nombreNodos.get(auxCamino.get(i - 1)) + "\n");
        }
        
        return auxCamino;
    }//Cierre recorrerEstadosCerrados
    
}
