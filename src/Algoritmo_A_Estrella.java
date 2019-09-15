
import java.util.ArrayList;
import java.util.Stack;

import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;

/**
 *
 * @author stalin Macias
 */
public class Algoritmo_A_Estrella {

    int pesoAcumulado = 0;

    public ArrayList<Integer> algoritmoEstrella(int nodoInicio, int nodoFinal, Grafo g) {

        ArrayList<Integer> visitados = new ArrayList<Integer>();

        Stack<HijoEstrella> estadoFinal = new Stack<HijoEstrella>();
        ArrayList<HijoEstrella> estadoTemporal = new ArrayList<HijoEstrella>();

        int pesoAcumulado = 0;

        HijoEstrella auxNodoInicio = new HijoEstrella(nodoInicio, pesoAcumulado, 0, -1);  //El pesoAcumulado del nodo inicio al igual que la heurisita es 0, y El nodoInicio no tiene padre, y será identificado con un -1,
        estadoTemporal.add(auxNodoInicio);

        HijoEstrella nodoActual = auxNodoInicio;    //Hace nodoActual igual al nodoInicio que esta en la pila como tipo HijoEstrella

        while (!estadoTemporal.isEmpty()) {
            //System.out.println("Se eliminará: "+nodoActual.numVerticeHijoEstrella);

            visitados.add(nodoActual.numVerticeHijoEstrella);
            estadoTemporal.remove(nodoActual);
            estadoFinal.push(nodoActual);

            //System.out.println("=================");
            //System.out.print("Nodos en visitados:\n"); for(int i = 0; i < visitados.size();i++) System.out.print(","+visitados.get(i)); System.out.println();
            //System.out.println("Nodo Actual: "+nodoActual.numVerticeHijoEstrella+","+nodoActual.pesoAcumulado+","+nodoActual.padre);
            //System.out.println("Peso Acumulado: "+nodoActual.pesoAcumulado);
            ArrayList<HijoEstrella> hijos = g.getEnlaces(nodoActual.numVerticeHijoEstrella, nodoActual.pesoAcumulado, nodoFinal, g); //El equivalente a generarSucesores();

            hijos = tratarRepetidos(hijos, estadoTemporal, visitados); //Eliminar los hijos que se encuentren en estadosCerrados o estadosAbiertos
            for (int i = 0; i < hijos.size(); i++) {
                estadoTemporal.add(hijos.get(i));                           //Almacenar en estadosAbiertos los hijos que no han sido tratados en el algoritmo! [Nodos pendientes por visitar!]
            }

            Collections.sort(estadoTemporal, new Comparator<HijoEstrella>() {
                @Override
                public int compare(HijoEstrella nodo1, HijoEstrella nodo2) {
                    return new Integer(nodo1.getValorParaDecidir()).compareTo(new Integer(nodo2.getValorParaDecidir()));
                }
            });

            //System.out.print("Nodos en estado final:\n"); for(int i = 0; i < estadoFinal.size();i++) System.out.print(","+estadoFinal.get(i).numVerticeHijoEstrella+","+estadoFinal.get(i).pesoAcumulado+","+estadoFinal.get(i).padre+"\n");  System.out.println();
            //System.out.println("!!!!!");
            /*System.out.print("Nodos en estado temporal:\n"); for(int i = 0; i < estadoTemporal.size();i++) System.out.print(","+estadoTemporal.get(i).numVerticeHijoEstrella+","+estadoTemporal.get(i).pesoAcumulado+","+estadoTemporal.get(i).padre+", Heuristia:"+estadoTemporal.get(i).heuristia
                                                          +", Valor para tomar decision:" + estadoTemporal.get(i).valorParaDecidir+"\n");  System.out.println();*/
            //System.out.println("!!!!!");
            if (estadoTemporal.isEmpty()) {
                break;
            } //Cuando en estadoTemporal queda un solo elemento, aun entra al ciclo, pero antes de llegar a esta linea se extrae y queda vacía...
            //Se agrega esta validación precisamente para ese punto y evitar un desbordamiento!
            //Recorrer la lista de estados temporales y verificar si existe algun nodo con heuristia 0, si existe alguno, hacerlo el nodo actual para que termine el algoritmo.
            else if (nodoConHeuristia0(estadoTemporal) == true) {
                //Si se encontro un nodo con heuristia 0, proceder a asignar dicho nodo a nodo actual, agregarlo a estado final y terminar la busqueda (break)
                nodoActual = nodoConHeuristia0(estadoTemporal, 0);
                estadoFinal.push(nodoActual);
                break;
            } else {
                nodoActual = estadoTemporal.get(0);
            } //El nodo actual será el que se encuentre en la posición 0, ya que se ordenaron de menor a mayor
        }//Cierre while

        //Mostrar el camino...
        ArrayList<Integer> camino = recorrerEstadoFinal(estadoFinal, nodoFinal);
        return camino;

    }//Cierre algoritmoEstrella

    /**
     * @param estadoTemporal recibe la lista de estados temporales para hacer la
     * busqueda del nodo con heuristia
     * @param dummyVar no se utiliza, solamente para sobreescribir el metodo
     * @return Devolverá el nodo que tiene heuristia 0
     */
    public static HijoEstrella nodoConHeuristia0(ArrayList<HijoEstrella> estadoTemporal, int dummyVar) {
        HijoEstrella aux = null;
        for (int i = 0; i < estadoTemporal.size(); i++) {
            if (estadoTemporal.get(i).heuristia == 0) {
                aux = estadoTemporal.get(i);
                break;
            }
        }
        return aux;
    }//Cierre nodoConHeuristia0

    /**
     * @param estadoTemporal recibe la lista de estados temporales para ser
     * recorrida y buscar si algun nodo tiene heuristia 0
     * @return Devolverá un true si se encontro un nodo con heuristia 0 y un
     * false si ningun nodo tiene heuristia 0
     */
    public boolean nodoConHeuristia0(ArrayList<HijoEstrella> estadoTemporal) {
        boolean aux = false;
        for (int i = 0; i < estadoTemporal.size(); i++) {
            if (estadoTemporal.get(i).heuristia == 0) {
                aux = true;
                break;
            }
        }
        return aux;
    }//Cierre nodoConHeuristia0

    /**
     * @param hijos -> Es la lista de los hijos del nodo actual
     * @param estadosAbiertos -> Es la lista de los nodos que se encuentran
     * pendientes por visitar y que pueden ser mejorados dependiendo su peso
     * @param visitados -> Es la lista de los nodos que han sido visitados
     *
     * @return -> El metodo se encarga de verificar cuales de los hijos ya han
     * sido visitados para que los elimine, y tambien busca si alguno de los
     * hijos no visitados mejora el costo que tiene el nodo en estadoTemporal,
     * si si lo mejora, elimina el nodo de estado temporal, sino, lo elimina de
     * hijos. Al final retorna los hijos que podrán ser agregados a
     * estadoTemporal
     */
    public ArrayList<HijoEstrella> tratarRepetidos(ArrayList<HijoEstrella> hijos, ArrayList<HijoEstrella> estadoTemporal, ArrayList<Integer> visitados) {
        ArrayList<HijoEstrella> auxHijos = hijos;
        ArrayList<HijoEstrella> auxToDeleteHijos = new ArrayList<HijoEstrella>();

        ArrayList<HijoEstrella> auxToDeleteTemporales = new ArrayList<HijoEstrella>();

        if (!hijos.isEmpty()) {
            //Recorrer cada hijo y verificar que no se encuentre ni en estadosCerrados ni en estadosAbiertos
            for (int i = 0; i < auxHijos.size(); i++) {
                HijoEstrella hijo = auxHijos.get(i);
                //System.out.println("HijoEstrella analizando: "+hijo.numVerticeHijoEstrella);

                //Verificar si algun hijo se encuentra en visitados
                for (int x = 0; x < visitados.size(); x++) {
                    int auxvisitados = visitados.get(x);
                    //Si si, el nodo se debe de eliminar de hijos
                    if (hijo.numVerticeHijoEstrella == auxvisitados) {
                        auxToDeleteHijos.add(hijo); //Guardar el objeto a eliminar una vez que se termine de verificar cada hijo
                    }
                }
                //Verificar que se encuentre algun hijo en estados abiertos, y si sí verificar cual tiene mayor pesoAcumulado
                for (int x = 0; x < estadoTemporal.size(); x++) {
                    HijoEstrella auxEstadoTemporal = estadoTemporal.get(x);
                    if (hijo.numVerticeHijoEstrella == auxEstadoTemporal.numVerticeHijoEstrella) { //Verificar si el hijo actual analizando esta en estado temporal
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
                //System.out.println("Se elimino el hijo: Hijos: "+auxToDeleteHijos.get(i).numVerticeHijoEstrella+","+auxToDeleteHijos.get(i).pesoAcumulado+","+auxToDeleteHijos.get(i).padre);
            }
            //Recorrer la lista de auxToDeleteAbiertos a eliminar y eliminarlos de la pila de estadosAbiertos
            for (int i = 0; i < auxToDeleteTemporales.size(); i++) {
                estadoTemporal.remove(auxToDeleteTemporales.get(i));
                //System.out.println("Se elimino el hijo: EstadosTemporal: "+auxToDeleteTemporales.get(i).numVerticeHijoEstrella+","+auxToDeleteTemporales.get(i).pesoAcumulado+","+auxToDeleteTemporales.get(i).padre);
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
    public ArrayList<Integer> recorrerEstadoFinal(Stack<HijoEstrella> estadoFinal, int fin) {
        ArrayList<Integer> auxCamino = new ArrayList<Integer>();
        int auxNodoPadre = 0;
        int pesoFinalCamino = 0;

        int valorTotal = 0;
        //Recorrer estadoFinal para encontrar el nodo que sea igual al fin, y apartir de ahí comenzar a crear el camino...
        for (int i = 0; i < estadoFinal.size(); i++) {
            if (estadoFinal.get(i).numVerticeHijoEstrella == fin) {
                //System.out.print("Se encontro el nodo final: "+estadoFinal.get(i).numVerticeHijoEstrella+" con un peso de: "+estadoFinal.get(i).pesoAcumulado);
                auxCamino.add(estadoFinal.get(i).numVerticeHijoEstrella);  //Guardar el nodo que es igual al fin
                auxNodoPadre = estadoFinal.get(i).padre;       //Guardar el padre del nodo que se acaba de guardar en auxCamino.. dicho padre será usado para crear la encadenacion y encontrar el camino
                pesoFinalCamino = estadoFinal.get(i).pesoAcumulado;                       //Guardar el pesoAcumulado que tenga este nodo, dado que es el nodo final del camino, significa que su peso acumulado es el peso total del camino!

                valorTotal = estadoFinal.get(i).valorParaDecidir;

                //System.out.print(" y su padre es: "+auxNodoPadre+"\n");
                break;  //Si ya encontro el nodo padre, proceder con la encadenación para encontrar el camino
            }
        }//Fin de la busqueda del nodo final

        System.out.println("--------------------------\nCamino creado con el Algoritmo de A Estrella:");
        //Encadenación y creación del camino!

        while (auxNodoPadre != -1) {
            for (int i = 0; i < estadoFinal.size(); i++) {
                if (estadoFinal.get(i).numVerticeHijoEstrella == auxNodoPadre) {
                    auxNodoPadre = estadoFinal.get(i).padre;
                    auxCamino.add(estadoFinal.get(i).numVerticeHijoEstrella);  //Si el siguiente hijo es igual al nodoPadre, agregarlo a la lista de caminos
                }
            }
        }
        //Presentar el camino de inicio a fin
        for (int i = auxCamino.size() - 1; i > 0; i--) {
            System.out.println("Del nodo: " + auxCamino.get(i) + " al nodo: " + auxCamino.get(i - 1));
        }
        System.out.println("El peso total del camino es: " + pesoFinalCamino);
        System.out.println("El valor total del camino es: " + valorTotal);

        //Devolver el camino creado con el algoritmo a estrella!
        this.pesoAcumulado = pesoFinalCamino;   //Guardar el peso total del camino creado!

        return auxCamino;
    }//Cierre recorrerEstadosCerrados

    public int getCostoTotal() {
        return pesoAcumulado;
    }
}
