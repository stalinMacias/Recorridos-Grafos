
import java.util.ArrayList;
import java.util.Stack;

import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;

/**
 *
 * @author laptop-stalin
 */
public class AlgoritmoDijkstra {

    int pesoAcumulado = 0;

    public ArrayList<Integer> algoritmoDijkstra(int nodoInicio, int nodoFinal, Grafo g) {

        ArrayList<Integer> visitados = new ArrayList<Integer>();

        Stack<HijoFase3> estadoFinal = new Stack<HijoFase3>();
        ArrayList<HijoFase3> estadoTemporal = new ArrayList<HijoFase3>();

        HijoFase3 auxNodoInicio = new HijoFase3(nodoInicio, pesoAcumulado, -1);  //El pesoAcumulado del nodo inicio es 0 y El nodoInicio no tiene padre, y será identificado con un -1,
        estadoTemporal.add(auxNodoInicio);

        HijoFase3 nodoActual = auxNodoInicio;    //Hace nodoActual igual al nodoInicio que esta en la pila como tipo HijoFase3

        while (!estadoTemporal.isEmpty()) {

            visitados.add(nodoActual.numVerticeHijoFase3);
            estadoTemporal.remove(nodoActual);
            estadoFinal.push(nodoActual);

            ArrayList<HijoFase3> hijos = g.getEnlaces(nodoActual.numVerticeHijoFase3, nodoActual.pesoAcumulado); //El equivalente a generarSucesores();

            hijos = tratarRepetidos(hijos, estadoTemporal, visitados); //Eliminar los hijos que se encuentren en estadosCerrados o estadosAbiertos
            for (int i = 0; i < hijos.size(); i++) {
                estadoTemporal.add(hijos.get(i));                           //Almacenar en estadosAbiertos los hijos que no han sido tratados en el algoritmo! [Nodos pendientes por visitar!]
            }

            Collections.sort(estadoTemporal, new Comparator<HijoFase3>() {
                @Override
                public int compare(HijoFase3 nodo1, HijoFase3 nodo2) {
                    return new Integer(nodo1.getPesoAcumulado()).compareTo(new Integer(nodo2.getPesoAcumulado()));
                }
            });

            if (estadoTemporal.isEmpty()) {
                break;       //Cuando en estadoTemporal queda un solo elemento, aun entra al ciclo, pero antes de llegar a esta linea se extrae y queda vacía...
            } //Se agrega esta validación precisamente para ese punto y evitar un desbordamiento!
            else {
                nodoActual = estadoTemporal.get(0); //El nodo actual será el que se encuentre en la posición 0, ya que se ordenaron de menor a mayor
            }
            //System.out.println();
        }//Cierre while

        //Mostrar el camino...
        ArrayList<Integer> camino = recorrerEstadoFinal(estadoFinal, nodoFinal);
        return camino;

    }//Cierre algoritmoDijsktra

    public Stack<HijoFase3> ordenar(Stack<HijoFase3> estadosAbiertos) {
        Stack<HijoFase3> tempStack = new Stack<HijoFase3>();

        while (!estadosAbiertos.isEmpty()) {
            HijoFase3 tempObj = estadosAbiertos.pop();
            while (!tempStack.isEmpty() && tempStack.peek().pesoAcumulado < tempObj.pesoAcumulado) {
                estadosAbiertos.push(tempStack.pop());
            }
            tempStack.push(tempObj);
        }
        return tempStack;
    }//Cierre metodo ordenar

    /**
     * @param hijos -> Es la lista de los hijos del nodo actual
     * @param estadoTemporal -> Es la lista de los nodos que se encuentran
     * pendientes por visitar y que pueden ser mejorados dependiendo su peso
     * @param visitados -> Es la lista de los nodos que han sido visitados
     *
     * @return -> El metodo se encarga de verificar cuales de los hijos ya han
     * sido visitados para que los elimine, y tambien busca si alguno de los
     * hijos no visitados mejora el costo que tiene el nodo en estadoTemporal,
     * si si lo mejora, elimina el nodo de estado temporal, sino, lo elimina de
     * hijos. Al final retorna los hijos que podrán ser agregados a
     * estadoTemporal
     *
     */
    public ArrayList<HijoFase3> tratarRepetidos(ArrayList<HijoFase3> hijos, ArrayList<HijoFase3> estadoTemporal, ArrayList<Integer> visitados) {
        ArrayList<HijoFase3> auxHijos = hijos;
        ArrayList<HijoFase3> auxToDeleteHijos = new ArrayList<HijoFase3>();

        ArrayList<HijoFase3> auxToDeleteTemporales = new ArrayList<HijoFase3>();

        if (!hijos.isEmpty()) {
            //Recorrer cada hijo y verificar que no se encuentre ni en estadosCerrados ni en estadosAbiertos
            for (int i = 0; i < auxHijos.size(); i++) {
                HijoFase3 hijo = auxHijos.get(i);
                //System.out.println("HijoFase3 analizando: "+hijo.numVerticeHijoFase3);

                //Verificar si algun hijo se encuentra en visitados
                for (int x = 0; x < visitados.size(); x++) {
                    int auxvisitados = visitados.get(x);
                    //Si si, el nodo se debe de eliminar de hijos
                    if (hijo.numVerticeHijoFase3 == auxvisitados) {
                        auxToDeleteHijos.add(hijo); //Guardar el objeto a eliminar una vez que se termine de verificar cada hijo
                    }
                }
                //Verificar que se encuentre algun hijo en estados abiertos, y si sí verificar cual tiene mayor pesoAcumulado
                for (int x = 0; x < estadoTemporal.size(); x++) {
                    HijoFase3 auxEstadoTemporal = estadoTemporal.get(x);
                    if (hijo.numVerticeHijoFase3 == auxEstadoTemporal.numVerticeHijoFase3) { //Verificar si el hijo actual analizando esta en estado temporal
                        if (hijo.pesoAcumulado >= auxEstadoTemporal.pesoAcumulado) {
                            auxToDeleteHijos.add(hijo); //Si el pesoAcumulado del hijo es mayor, se elimina
                        } else {
                            auxToDeleteTemporales.add(estadoTemporal.get(x));  //Si el pesoAcumulado del hijo es menor que el de estados abiertos, se conserva y se elimina de la pila de estados abiertos
                        }
                    }
                }
            }//Cierre del recorrido de cada hijo

            //Eliminar de su lugar correspondiente
            //Recorrer la lista de hijos a eliminar y eliminarlos de la lista de hijos
            for (int i = 0; i < auxToDeleteHijos.size(); i++) {
                hijos.remove(auxToDeleteHijos.get(i));
                //System.out.println("Se elimino el hijo: Hijos: "+auxToDeleteHijos.get(i).numVerticeHijoFase3+","+auxToDeleteHijos.get(i).pesoAcumulado+","+auxToDeleteHijos.get(i).padre);
            }
            //Recorrer la lista de auxToDeleteAbiertos a eliminar y eliminarlos de la pila de estadosAbiertos
            for (int i = 0; i < auxToDeleteTemporales.size(); i++) {
                estadoTemporal.remove(auxToDeleteTemporales.get(i));
                //System.out.println("Se elimino el hijo: EstadosTemporal: "+auxToDeleteTemporales.get(i).numVerticeHijoFase3+","+auxToDeleteTemporales.get(i).pesoAcumulado+","+auxToDeleteTemporales.get(i).padre);
            }
        }
        //Copiar a la variable auxiliar el estado final de hijos
        auxHijos = hijos;
        return auxHijos;
    }//Cierre tratarRepetidos

    /**
     * Para obtener el camino se debe de recorrer de manera inversa el ArrayList
     * estadosCerrados, la ultima posicion contienen el nodo final y el padre.
     * El nodo padre del ultimo hijo será usado para encontrar al predecesor, el
     * siguiente hijo que sea igual al nodo padre será el siguiente y así
     * sucesivamente... De esta manera se encuentra el camino, pero queda de
     * manera inversa, del fin al inicio, para mostrar el resultado, se debe de
     * invertir dicho camino, para que se presente de inicio a fin
     *
     * @param estadoFinal -> Es el ArrayList de todos los estados finales que se
     * fueron agregando en el algoritmo
     * @param fin -> Es el numero del nodo final, se utilizará para encontrar el
     * nodo final y su padre, para despues proceder con la encadenación y
     * creción del camino
     */
    public ArrayList<Integer> recorrerEstadoFinal(Stack<HijoFase3> estadoFinal, int fin) {
        ArrayList<Integer> auxCamino = new ArrayList<Integer>();
        int auxNodoPadre = 0;
        int pesoFinalCamino = 0;
        //Recorrer estadoFinal para encontrar el nodo que sea igual al fin, y apartir de ahí comenzar a crear el camino...
        for (int i = 0; i < estadoFinal.size(); i++) {
            if (estadoFinal.get(i).numVerticeHijoFase3 == fin) {
                //System.out.print("Se encontro el nodo final: "+estadoFinal.get(i).numVerticeHijoFase3+" con un peso de: "+estadoFinal.get(i).pesoAcumulado);
                auxCamino.add(estadoFinal.get(i).numVerticeHijoFase3);  //Guardar el nodo que es igual al fin
                auxNodoPadre = estadoFinal.get(i).padre;       //Guardar el padre del nodo que se acaba de guardar en auxCamino.. dicho padre será usado para crear la encadenacion y encontrar el camino
                pesoFinalCamino = estadoFinal.get(i).pesoAcumulado;                       //Guardar el pesoAcumulado que tenga este nodo, dado que es el nodo final del camino, significa que su peso acumulado es el peso total del camino!
                //System.out.print(" y su padre es: "+auxNodoPadre+"\n");
                break;  //Si ya encontro el nodo padre, proceder con la encadenación para encontrar el camino
            }
        }//Fin de la busqueda del nodo final

        System.out.println("--------------------------\nCamino creado con el Algoritmo de Dijkstra:");
        //Encadenación y creación del camino!
        while (auxNodoPadre != -1) {
            for (int i = 0; i < estadoFinal.size(); i++) {
                if (estadoFinal.get(i).numVerticeHijoFase3 == auxNodoPadre) {
                    auxNodoPadre = estadoFinal.get(i).padre;
                    auxCamino.add(estadoFinal.get(i).numVerticeHijoFase3);  //Si el siguiente hijo es igual al nodoPadre, agregarlo a la lista de caminos
                }
            }
        }
        //Presentar el camino de inicio a fin
        for (int i = auxCamino.size() - 1; i > 0; i--) {
            System.out.println("Del nodo: " + auxCamino.get(i) + " al nodo: " + auxCamino.get(i - 1));
        }
        System.out.println("El peso total del camino es: " + pesoFinalCamino);

        this.pesoAcumulado = pesoFinalCamino;
        return auxCamino;
    }//Cierre recorrerEstadosCerrados

    public int getCostoTotal() {
        return pesoAcumulado;
    }
}
