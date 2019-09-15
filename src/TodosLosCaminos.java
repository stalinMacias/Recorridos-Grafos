
import java.util.ArrayList;
import java.util.Stack;


/**
 *
 * @author laptop-stalin
 */
public class TodosLosCaminos {
    /**
     * Este metodo se encarga de obtener todos los caminos posibles dados un
     * nodo inicial y un nodo final La base del metodo es una pila que se
     * encarga de ir almacenando el recorrido que va creando el metodo, una vez
     * que se llega al nodo fin, dicha pila se convierte a un ArrayList de
     * Enteros y se agrega al ArrayList de caminos! Cuando se llega al nodo fin,
     * se devuelve un nodo anterior para continuar con la busqueda
     *
     * @param inicio -> Representa el nodo inicio de donde comenzara la busqueda
     * de los caminos
     * @param fin -> Representa el nodo final. Cuando se encuentre este nodo, el
     * camino creado para llegar a el se agregara a la lista de caminos
     * @param g -> Este es un objeto de la clase Grafo y se requiere para
     * acceder a los metodo que contiene dicha clase
     * @return -> Se devuelve un ArrayList de ArrayList de Enteros que
     * representan los caminos obtenidos!
     */
    public ArrayList<ArrayList<Integer>> getAllPaths(int inicio, int fin, Grafo g) {
        ArrayList<ArrayList<Integer>> caminos = new ArrayList<ArrayList<Integer>>(); //Arraylist de ArrayList de enteros que contiene los caminos!
        Stack<Integer> ruta = new Stack<Integer>();

        int nodoActual = inicio; // Representa el nodo actual. Se inicializa con el nodo de inicio pasado como parámetro
        ruta.push(-99);  // Se mete un -99 a la pila para detener el while cuando se hayan recorrido todas las rutas

        ArrayList<Integer> nodos = g.obtenerTodosLosNodosDelGrafo();  //Obtener todos los nodos del grafo
        int enlaceVisitados[] = new int[nodos.size()];  // Arreglo que guarda el index del siguiente enlace para cada nodo

        while (nodoActual != -99) { //Cuando nodoActual sea igual a -99 significa que ya termino de recorrer los caminos!
            int index = getNodoIndex(nodoActual, nodos);  // Index del nodo actual que se usa para acceder al index de su siguiente enlace en el arreglo enlaceVisitados
            ArrayList<InfoArista> enlaces = g.getEnlaces(nodoActual);  //Obtener los enlaces del nodo actual!

            if (nodoActual == fin) { // Si el nodo actual es el final de la ruta
                ruta.push(nodoActual);
                ArrayList<Integer> aux = new ArrayList(ruta);  //Convertir la pila a un ArrayList auxiliar para poder guardar el camino!

                //En la pila en la posición 0 se encuentra el valor -99 para parar el ciclo, y al convertir la pila a un arraylist,
                //tambien se agregar dicho valor, así que antes de agregar el array list aux a la lista de caminos, se tiene que eliminar
                //la posicion 0, para que de esta manera no se considere el -99 dentro de la lista de caminos!
                aux.remove(0);

                caminos.add(aux); //Agregar el camino a la lista de caminos!
                nodoActual = ruta.pop();  //Volver al nodo anterior!
            }

            if (enlaceVisitados[index] == 0) {  // Si es la primera vez que se visita el nodo
                if (enlaces.isEmpty()) {  // Si no tiene enlaces se regresa al anterior
                    nodoActual = ruta.pop();
                } else {
                    if (nodoActual != enlaces.get(0).numVertice) { // Se verifica que no sea un lazo
                        ruta.push(nodoActual);  // Se mete el nodo a la ruta
                        nodoActual = enlaces.get(0).numVertice;  // Se actualiza el nodo
                    }
                    enlaceVisitados[index] = 1; // Se actualiza el index del siguiente enlace del nodo. Como es la primera vez, el index del siguiente enlace es 1
                }
            } else // Si el nodo ya ha sido visitado con anterioridad
            {
                if (enlaceVisitados[index] >= enlaces.size()) { // Verifica que el nodo todavía tenga enlaces por recorrer
                    nodoActual = ruta.pop();  // Si no quedan más enlaces por recorrer, se regresa al nodo anterior
                } else {  // Si sí tiene enlaces por recorrer
                    if (nodoActual != enlaces.get(enlaceVisitados[index]).numVertice) { // Verifica que no sea un lazo
                        ruta.push(nodoActual);  // Se mete el nodo a la ruta
                        nodoActual = enlaces.get(enlaceVisitados[index]).numVertice; // Se actualiza el nodo actual
                    }
                    enlaceVisitados[index]++; // Se actualiza el index del siguiente enlace por recorrer para este nodo
                }
            }

        }//Cierre while

        return caminos;
    }

    /**
     * Regresa el index al que corresponde un nodo de acuerdo al arreglo de
     * nodos del grafo. Este método es privado y es solo para el funcionamiento
     * interno de la clase, por ejemplo, para ver el index que le corresponde a
     * un nodo y con el cual se va a acceder al al arreglo de enlaces siguientes
     * en el método de las rutas.
     *
     * @param nodo Nodo del cual se quiere saber el index
     * @param nodos ArrayList con los nodos del grafo
     * @return Index del nodo pasado como parámetro. Si no se encuentra el nodo
     * regresa -1
     */
    private static int getNodoIndex(int nodo, ArrayList<Integer> nodos) {
        for (int i = 0; i < nodos.size(); i++) {
            if (nodos.get(i) == nodo) {
                return i;
            }
        }
        return -1;
    }//Cierre metodo getNodoIndex
}
