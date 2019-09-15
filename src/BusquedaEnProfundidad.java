
import java.util.ArrayList;
import java.util.Stack;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author laptop-stalin
 */
public class BusquedaEnProfundidad {
    
    public ArrayList<Integer> algoritmoBusquedaEnProfundidad(int nodoInicio, int nodoFinal, Grafo g) {

        Stack<Hijo> estadosAbiertos = new Stack<Hijo>();
        ArrayList<Hijo> estadosCerrados = new ArrayList<Hijo>();

        Hijo auxTerminarCiclo = new Hijo(-99, -99);
        estadosAbiertos.push(auxTerminarCiclo);        //La condición de paro del ciclo while del algoritmo será cuando hijo.numVerticeHijo sea igual a -99!

        Hijo auxNodoInicio = new Hijo(nodoInicio, -1);  //El nodoInicio no tiene padre, y será identificado con un -1
        estadosAbiertos.push(auxNodoInicio);
        Hijo nodoActual = estadosAbiertos.peek();     //Hace nodoActual igual al nodoInicio que esta en la pila como tipo Hijo

        while (!esFinal(nodoActual.numVerticeHijo, nodoFinal)) {
            //System.out.println("=================");
            //System.out.println("Nodo Actual: "+nodoActual.numVerticeHijo);

            if (nodoActual.numVerticeHijo == -99) {
                break;
            } else {
                estadosAbiertos.pop();
                estadosCerrados.add(nodoActual);
                ArrayList<Hijo> hijos = g.getEnlaces(nodoActual.numVerticeHijo, false); //El equivalente a generarSucesores();

                //System.out.print("Todos los hijos del actual: "); for(int i = 0; i < hijos.size();i++) System.out.print(","+hijos.get(i).numVerticeHijo); System.out.println();
                //System.out.print("Nodos en estados abiertos: "); for(int i = 0; i < estadosAbiertos.size();i++) System.out.print(","+estadosAbiertos.get(i).numVerticeHijo);  System.out.println();
                //System.out.print("Nodos en estados cerrados: "); for(int i = 0; i < estadosCerrados.size();i++) System.out.print(","+estadosCerrados.get(i).numVerticeHijo);  System.out.println();
                hijos = tratarRepetidos(hijos, estadosAbiertos, estadosCerrados); //Eliminar los hijos que se encuentren en estadosCerrados o estadosAbiertos
                for (int i = 0; i < hijos.size(); i++) {
                    estadosAbiertos.push(hijos.get(i));                           //Almacenar en estadosAbiertos los hijos que no han sido tratados en el algoritmo! [Nodos pendientes por visitar!]
                }
                nodoActual = estadosAbiertos.peek();                            //Hacer el nodoActual el primer nodo de estadosAbiertos
            }
        }
        
        ArrayList<Integer> camino = new ArrayList<Integer>();
        if (esFinal(nodoActual.numVerticeHijo, nodoFinal)) {
            estadosCerrados.add(nodoActual);          //Guardar el nodo actual en estadosCerrados para completar el camino!
            System.out.println("El camino encontrado usando el algoritmo de búsqueda en profundidad es: ");
            //textAreaResultados.append("\tEl camino encontrado usando el algoritmo de búsqueda en profundidad es: \n\n");
            camino = recorrerEstadosCerrados(estadosCerrados); //Metodo para recorrer e imprimir el camino creado!
        } else {
            //Si el camino se devuelve vacio, significa que no existe camino!
            System.out.println("No hay camino");
        }
        
        return camino;  //Si el camino se devuelve vacio, significa que no existe camino!
    }//Cierre método busquedaEnProfundidad

    /**
     * @param hijos -> Es la lista de los hijos del nodo actual
     * @param estadosAbiertos -> Es la lista de los nodos que se encuentran
     * pendientes por visitar
     * @param estadosCerrados -> Es la lista de los nodos que han sido visitados
     *
     * @return -> El metodo se encarga de verificar cuales de los hijos del nodo
     * actual no han sido usados/considerados en el algoritmo, para eso se
     * auxilia de estadosAbiertos y estadosCerrados, si un nodo se encuentra en
     * alguna de las 2, significa que ya ha sido bien sea usado si esta en
     * estadosCerrados o bien sea considerado si se encuentra en
     * estadosAbiertos... Si el hijo se encuentra en alguna de las 2 es
     * eliminado, de lo contrario se conserva y se devuelve en la lista final de
     * hijos [Ya tratada -> (Sin repetidos)!].
     */
    public ArrayList<Hijo> tratarRepetidos(ArrayList<Hijo> hijos, Stack<Hijo> estadosAbiertos, ArrayList<Hijo> estadosCerrados) {
        ArrayList<Hijo> auxHijos = hijos;
        ArrayList<Hijo> auxToDeleteHijos = new ArrayList<Hijo>();

        if (!hijos.isEmpty()) {
            //Recorrer cada hijo y verificar que no se encuentre ni en estadosCerrados ni en estadosAbiertos
            for (int i = 0; i < auxHijos.size(); i++) {
                Hijo hijo = auxHijos.get(i);
                //System.out.println("Hijo analizando: "+hijo.numVerticeHijo);

                //Verificar que no se encuentre algun hijo en estados abiertos
                for (int x = 0; x < estadosAbiertos.size(); x++) {
                    Hijo auxEstadoAbierto = estadosAbiertos.get(x);
                    if (hijo.numVerticeHijo == auxEstadoAbierto.numVerticeHijo) {
                        auxToDeleteHijos.add(hijo); //Guardar el objeto a eliminar una vez que se termine de verificar cada hijo
                    }
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
        System.out.println("--------------------------\nCamino creado con el Algoritmo Busqueda en Profundidad:");
        //Presentar el camino de inicio a fin
        for (int i = auxCamino.size() - 1; i > 0; i--) {
            System.out.println("Del nodo: " + auxCamino.get(i) + " al nodo: " + auxCamino.get(i - 1));
            //textAreaResultados.append("\tDel nodo: " + nombreNodos.get(auxCamino.get(i)) + " al nodo: " + nombreNodos.get(auxCamino.get(i - 1)) + "\n");
        }
        
        return auxCamino;
    }//Cierre recorrerEstadosCerrados
}
