
import java.util.ArrayList;
import java.util.Stack;

/**
 *
 * @author laptop-stalin
 */

public class PrimeroElMejor {
    int pesoAcumulado = 0;
    
    public ArrayList<Integer> busquedaPrimeroElmejor(int nodoInicio, int nodoFinal, Grafo g) {
        Stack<HijoFase3> estadosAbiertos = new Stack<HijoFase3>();
        ArrayList<HijoFase3> estadosCerrados = new ArrayList<HijoFase3>();

        HijoFase3 auxNodoInicio = new HijoFase3(nodoInicio, pesoAcumulado, -1);  //El pesoAcumulado del nodo inicio es 0 y El nodoInicio no tiene padre, y será identificado con un -1,
        estadosAbiertos.push(auxNodoInicio);
        HijoFase3 nodoActual = estadosAbiertos.peek();     //Hace nodoActual igual al nodoInicio que esta en la pila como tipo HijoFase3

        while ((!esFinal(nodoActual.numVerticeHijoFase3, nodoFinal)) && !estadosAbiertos.empty()) {
            /*
      System.out.println("=================");
      System.out.println("Nodo Actual: "+nodoActual.numVerticeHijoFase3+","+nodoActual.pesoAcumulado+","+nodoActual.padre);
      System.out.println("Peso Acumulado: "+nodoActual.pesoAcumulado);
             */
            //if(nodoActual.numVerticeHijoFase3 == -99) break;
            //else{
            estadosAbiertos.pop();
            estadosCerrados.add(nodoActual);
            ArrayList<HijoFase3> hijos = g.getEnlaces(nodoActual.numVerticeHijoFase3, nodoActual.pesoAcumulado); //El equivalente a generarSucesores();

            hijos = tratarRepetidos(hijos, estadosAbiertos, estadosCerrados); //Eliminar los hijos que se encuentren en estadosCerrados o estadosAbiertos
            for (int i = 0; i < hijos.size(); i++) {
                estadosAbiertos.push(hijos.get(i));                           //Almacenar en estadosAbiertos los hijos que no han sido tratados en el algoritmo! [Nodos pendientes por visitar!]
            }
            /*
        System.out.print("Nodos en estados abiertos:\n"); for(int i = 0; i < estadosAbiertos.size();i++) System.out.print(","+estadosAbiertos.get(i).numVerticeHijoGuiado+","+estadosAbiertos.get(i).pesoAcumulado+","+estadosAbiertos.get(i).padre+"\n");  System.out.println();
        System.out.println("!!!!!");
        System.out.print("Nodos en estados cerrados:\n"); for(int i = 0; i < estadosCerrados.size();i++) System.out.print(","+estadosCerrados.get(i).numVerticeHijoGuiado+","+estadosCerrados.get(i).pesoAcumulado+","+estadosCerrados.get(i).padre+"\n");  System.out.println();
        System.out.println("!!!!!");
             */
            Stack<HijoFase3> tempStackEstadosAbiertos = new Stack<HijoFase3>();
            tempStackEstadosAbiertos = ordenar(estadosAbiertos);  //Se ordenaran de mayor a menor los estadosAbiertos
            estadosAbiertos = tempStackEstadosAbiertos; //Actualizar la pila de estadosAbiertos
            nodoActual = estadosAbiertos.peek();                            //Hacer el nodoActual el primer nodo de estadosAbiertos
            //}
        }//Cierre while
        ArrayList<Integer> camino = new ArrayList<Integer>();
                
        if (esFinal(nodoActual.numVerticeHijoFase3, nodoFinal)) {
            estadosCerrados.add(nodoActual);          //Guardar el nodo actual en estadosCerrados para completar el camino!
            camino = recorrerEstadosCerrados(estadosCerrados, nodoActual.pesoAcumulado); //Metodo para recorrer e imprimir el camino creado!
        } else {
            System.out.println("No hay camino");
        }

        return camino;

    }//Cierre busquedaPrimeroElmejor

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
    }

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
     *
     */
    public ArrayList<HijoFase3> tratarRepetidos(ArrayList<HijoFase3> hijos, Stack<HijoFase3> estadosAbiertos, ArrayList<HijoFase3> estadosCerrados) {
        ArrayList<HijoFase3> auxHijos = hijos;
        ArrayList<HijoFase3> auxToDeleteHijos = new ArrayList<HijoFase3>();

        ArrayList<HijoFase3> auxToDeleteAbiertos = new ArrayList<HijoFase3>();
        ArrayList<HijoFase3> auxToDeleteCerrados = new ArrayList<HijoFase3>();

        if (!hijos.isEmpty()) {
            //Recorrer cada hijo y verificar que no se encuentre ni en estadosCerrados ni en estadosAbiertos
            for (int i = 0; i < auxHijos.size(); i++) {
                HijoFase3 hijo = auxHijos.get(i);
                //System.out.println("HijoFase3 analizando: "+hijo.numVerticeHijoFase3);

                //Verificar que se encuentre algun hijo en estados abiertos, y si sí verificar cual tiene mayor pesoAcumulado
                for (int x = 0; x < estadosAbiertos.size(); x++) {
                    HijoFase3 auxEstadoAbierto = estadosAbiertos.get(x);
                    if (hijo.numVerticeHijoFase3 == auxEstadoAbierto.numVerticeHijoFase3) { //Verificar si el hijo actual analizando esta en estados abiertos
                        if (hijo.pesoAcumulado >= auxEstadoAbierto.pesoAcumulado) {
                            auxToDeleteHijos.add(hijo); //Si el pesoAcumulado del hijo es mayor, se elimina
                        } else {
                            auxToDeleteAbiertos.add(estadosAbiertos.get(x));  //Si el pesoAcumulado del hijo es menor que el de estados abiertos, se conserva y se elimina de la pila de estados abiertos
                        }
                    }
                }

                //Verificar que  se encuentre algun hijo en estados cerrados, y si sí verificar cual tiene mayor pesoAcumulado
                for (int x = 0; x < estadosCerrados.size(); x++) {
                    HijoFase3 auxEstadoCerrado = estadosCerrados.get(x);
                    if (hijo.numVerticeHijoFase3 == auxEstadoCerrado.numVerticeHijoFase3) {
                        if (hijo.pesoAcumulado >= auxEstadoCerrado.pesoAcumulado) {
                            auxToDeleteHijos.add(hijo); //Guardar el objeto a eliminar una vez que se termine de verificar cada hijo
                        } else {
                            auxToDeleteCerrados.add(estadosCerrados.get(x));
                        }
                    }
                }
            }//Cierre del recorrido de cada hijo

            //Eliminar de su lugar correspondiente1
            //Recorrer la lista de hijos a eliminar y eliminarlos de la lista de hijos
            for (int i = 0; i < auxToDeleteHijos.size(); i++) {
                hijos.remove(auxToDeleteHijos.get(i));
                //System.out.println("Se elimino el hijo: Hijos"+auxToDeleteHijos.get(i).numVerticeHijoFase3+","+auxToDeleteHijos.get(i).pesoAcumulado+","+auxToDeleteHijos.get(i).padre);
            }
            //Recorrer la lista de auxToDeleteAbiertos a eliminar y eliminarlos de la pila de estadosAbiertos
            for (int i = 0; i < auxToDeleteAbiertos.size(); i++) {
                estadosAbiertos.remove(auxToDeleteAbiertos.get(i));
                //System.out.println("Se elimino el hijo: EA: "+auxToDeleteAbiertos.get(i).numVerticeHijoFase3+","+auxToDeleteAbiertos.get(i).pesoAcumulado+","+auxToDeleteAbiertos.get(i).padre);
            }

            //Recorrer la lista de auxToDeleteCerrados a eliminar y eliminarlos de la lista de estadosCerrados
            for (int i = 0; i < auxToDeleteCerrados.size(); i++) {
                estadosCerrados.remove(auxToDeleteCerrados.get(i));
                //System.out.println("Se elimino el hijo: EC: "+auxToDeleteCerrados.get(i).numVerticeHijoFase3+","+auxToDeleteCerrados.get(i).pesoAcumulado+","+auxToDeleteCerrados.get(i).padre);
            }

        }
        //Copiar a la variable auxiliar el estado final de hijos
        auxHijos = hijos;
        return auxHijos;
    }//Cierre tratarRepetidos

    public boolean esFinal(int nodoActual, int nodoFinal) {
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
    public ArrayList<Integer> recorrerEstadosCerrados(ArrayList<HijoFase3> estadosCerrados, int pesoAcumulado) {
        ArrayList<Integer> auxCamino = new ArrayList<Integer>();
        auxCamino.add(estadosCerrados.get(estadosCerrados.size() - 1).numVerticeHijoFase3);  //Guardar el ultimo nodo hijo que se encuentra en estados cerrados... dicho nodo es el final!
        int auxNodoPadre = estadosCerrados.get(estadosCerrados.size() - 1).padre;       //Guardar el padre del ultmo nodo que se encuentra en estados cerrados... dicho nodo valor servira para encontrar el camino!

        System.out.println("--------------------------\nCamino creado con el Algoritmo Primero El Mejor:");
        for (int i = estadosCerrados.size() - 1; i > 0; i--) {

            if (estadosCerrados.get(i - 1).numVerticeHijoFase3 == auxNodoPadre) {
                auxNodoPadre = estadosCerrados.get(i - 1).padre;
                auxCamino.add(estadosCerrados.get(i - 1).numVerticeHijoFase3);  //Si el siguiente hijo es igual al nodoPadre, agregarlo a la lista de caminos
                //[Se agrega la posición i-1 por que se esta recorriendo de manera inversa el ArrayList estadosCerrados]
            }
        }

        //Presentar el camino de inicio a fin
        for (int i = auxCamino.size() - 1; i > 0; i--) {
            System.out.println("Del nodo: " + auxCamino.get(i) + " al nodo: " + auxCamino.get(i - 1));
        }
        System.out.println("El peso total del camino es: " + pesoAcumulado);
        this.pesoAcumulado = pesoAcumulado;         //Gardar el valor del pesoAcumulado para recorrer el camino en la variable global pesoAcumulado
        return auxCamino;
    }//Cierre recorrerEstadosCerrados
    
    
    public int getCostoTotal(){
        return pesoAcumulado;
    }
    
}
