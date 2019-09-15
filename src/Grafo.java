
import java.util.ArrayList;
import javax.swing.JOptionPane;

/**
 *
 * @author Macias
 */
class InfoArista {

    /*nodo fuente es el que se encuentra en la posicion[0] del arreglo de nodos....este indica de donde proviene la arista....*/
    //numVertice hace referencia al nodo de destino de la arista
    //pesoArista se usará para determinar el costo que toma llegar al nodo actual
    //siguiente apuntara a la direccion de memoria del siguiente Nodo que pertenece a la lista del Nodo  fuente
    int numVertice;
    int pesoArista;
    int valHeuristico;
    InfoArista siguiente; //Se usa para crear la relacion entre las aristas que salen del mismo nodo...

    public InfoArista(int numVertice, int pesoArista, int valHeuristico) {
        this.numVertice = numVertice;
        this.pesoArista = pesoArista;
        this.valHeuristico = valHeuristico;
        this.siguiente = null;
    }//Cierre constructor

} //Cierre clase para crear cada objeto de la lista de adyacencia


/*      *** Clase Hijo para trabajar con los adyacentes y guardar cada adyacente con su padre correspondiente!  *** */
class Hijo{
  int numVerticeHijo;
  int padre;

  public Hijo(int numVerticeHijo, int padre){
    this.numVerticeHijo = numVerticeHijo;
    this.padre = padre;
  }
}
/*      *** Clase Hijo para trabajar con los adyacentes y guardar cada adyacente con su padre correspondiente!  *** */


/*      *** Clase HijoFase3 para trabajar con los adyacentes y guardar cada adyacente con su padre correspondiente y el peso!  *** */
//  Cada objeto HijoFase3 quedará -> numVertice, peso, padre
class HijoFase3{
  int numVerticeHijoFase3;
  int pesoAcumulado;
  int padre;

  public HijoFase3(int numVerticeHijoFase3, int pesoAcumulado, int padre){
    this.numVerticeHijoFase3 = numVerticeHijoFase3;
    this.pesoAcumulado = pesoAcumulado;
    this.padre = padre;
  }
  
  public int getPesoAcumulado(){
    return pesoAcumulado;
  }
}
/*      *** Clase HijoFase3 para trabajar con los adyacentes y guardar cada adyacente con su padre correspondiente y el peso!  *** */


/*      *** Clase HijoEstrella para trabajar con los adyacentes y guardar cada adyacente con su padre correspondiente, el peso y la herustia, y claro, la suma de ambos!  *** */
//  Cada objeto HijoEstrella quedará -> numVertice, peso, heuristia, valorParaDecidir, padre
class HijoEstrella{
  int numVerticeHijoEstrella;
  int pesoAcumulado;
  int heuristia;
  int valorParaDecidir; //Esta variable se calculará sumando el pesoAcumulado mas la heuristia que se obtenga del nodo hijo al nodo final
  int padre;

  public HijoEstrella(int numVerticeHijoEstrella, int pesoAcumulado, int heuristia, int padre){
    this.numVerticeHijoEstrella = numVerticeHijoEstrella;
    this.pesoAcumulado = pesoAcumulado;
    this.heuristia = heuristia;
    this.valorParaDecidir = pesoAcumulado + heuristia;
    this.padre = padre;
  }

  public int getPesoAcumulado(){
    return pesoAcumulado;
  }

  public int getValorParaDecidir(){
    return valorParaDecidir;
  }
}
/*      *** Clase HijoEstrella para trabajar con los adyacentes y guardar cada adyacente con su padre correspondiente!  *** */


public class Grafo {

    private int numVertices;
    ArrayList<InfoArista> grafo = new ArrayList<InfoArista>();

    public Grafo(int numVertices) {
        this.numVertices = numVertices;

        //Inicializar el arreglo de nodos en null
        for (int i = 0; i < (numVertices - 1); i++) {
            grafo.add(null);
        }
    }//Cierre constructor

    /*
    @param1 -> Recibe el nodoFuente que indica de donde sale la Arista
    @param2 -> Recibe el nodoDestino que indica a donde se dirige la Arista

    @return -> retorna true si la Arsita existe, y false en caso de que no exista...
     */
    public boolean existeArista(int nodoFuente, int nodoDestino) {
        if (grafo.get(nodoFuente) == null) {
            return false; //Significa que no existe ninguna arista que salga del nodo analizado
        }    //Signfica que ya existe al menos una arista que sale del nodo analizado
        InfoArista actual = grafo.get(nodoFuente);
        while (actual != null) {
            if (actual.numVertice == nodoDestino) {
                //Significa que ya existe una arista entre el nodoFuente y el nodoDestino analizados...
                return true;
            }
            actual = actual.siguiente;
        }
        return false;
    }//Cierre existeArista

    /*
    @param1 -> Recibe el nodoFuente que indica de donde sale la Arista
    @param2 -> Recibe el nodoDestino que indica a donde se dirige la Arista
    @param3 -> Recibe el peso de la arista
     */
    public boolean insertaArista(int nodoFuente, int nodoDestino, int peso, int valHeuristico) {
        /*Si ya existe una arista entre ambos nodos, mostrar mensaje de error al usuario....*/
        if (existeArista(nodoFuente, nodoDestino) == false) {
            //No existe ninguna arista entre ambos nodos.... proceder a insertar la que se esta solicitando

            //Crear un objeto de la clase InfoArista para insertarlo en el arreglo del Grafo
            InfoArista newArista = new InfoArista(nodoDestino, peso, valHeuristico);
            if (grafo.get(nodoFuente) == null) {
                //Significa que aun no existe ninguna arista que sale del nodo fuente..
                grafo.add(nodoFuente, newArista);
            } else {
                //Guardar la informacion de la arista actual que se encuentra en el arreglo de grafo en la posicion del nodo fuente ---> nodoFuente= @param1
                //Se deben de recorrer todas las aristas que se encuentren guardadas para poder insertar la nueva
                InfoArista actual = grafo.get(nodoFuente);
                while (actual.siguiente != null) {
                    actual = actual.siguiente;
                }
                //Cuando el atributo siguiente apunte a null significa que ya se encuentra en la ultima referencia...
                //para insertar la nueva informacion de la arista se debe de actualizar el valor del atributo siguiente con el objeto newArista recien creado
                actual.siguiente = newArista;
            }
            //El nodo se agrego correctamente!
            return true;
        } else {
            //Ya existe una arista entre ambos nodos!
            return false;
        }
    }//Cierre insertaArista

    /*
    @param1 -> Recibe el nodoFuente que indica de donde sale la Arista
    @param2 -> Recibe el nodoDestino que indica a donde se dirige la Arista

     */
    public boolean eliminarArista(int nodoFuente, int nodoDestino) {
        /*Si no existe una arista entre ambos nodos, mostrar mensaje de error al usuario....*/
        if (existeArista(nodoFuente, nodoDestino) == true) {
            //Significa que si existe la arista que se desea borrar...
            InfoArista actual = grafo.get(nodoFuente); //Guardar la informacion que se encuentra en la posicion del  arreglo grafo[nodoFuente]

            if (actual.numVertice == nodoDestino) {
                //Significa que el nodo actual a borrar es el que se encuentra en la posicion del arreglo grafo[nodoFuente]
                grafo.set(nodoFuente, actual.siguiente); //Brincar la referencia que se tenía de la info anterior!
            } else {
                //Se tendran que recorrer todas las referencias hasta encontrar el nodoDestino y saltar su referencia....
                InfoArista anterior = actual;
                actual = actual.siguiente;
                //La variable actual va analizando una referencia adelante de la variable anterior
                while (actual != null) {
                    if (actual.numVertice == nodoFuente) {
                        //Significa que ya encontro la informacion de la arista a eliminar
                        anterior.siguiente = actual.siguiente;  //Guardar el valor del atributo siguiente de la arista a eliminar, en el atributo siguiente de la variable auxliar anterior...
                        break; //Salir del while
                    }
                    //Actualizar ambas variables para continuar con la busqueda
                    anterior = actual;
                    actual = actual.siguiente;
                }
            }
            return true;
        } else {
            return false;
        }
    }//Cierre eliminarArista

    public void liberaGrafo() {
        grafo.clear();
    }//Cierre liberaGrafo

    public String mostrarGrafo(String[] nombreNodos) {
        //System.out.println("numero de nombre de nodos" +nombreNodos.length);

        String cadToShowGraph = "";
        //Recorrer el arreglo grafo[] y por cada posicion ir imprimiendo todas las referencias de objeto de InfoArista que tenga....
        for (int i = 0; i < grafo.size(); i++) {
            InfoArista actual = grafo.get(i);

            while (actual != null) {
                cadToShowGraph += "Del nodo: " + nombreNodos[i] + " al nodo: " + nombreNodos[actual.numVertice] + ", con un peso de: " + actual.pesoArista + " y un valor heuristico de: " + actual.valHeuristico + "-";
                //actual.siguiente para acceder a los datos del siguiente objeto InfoNodo
                actual = actual.siguiente;
            }
            cadToShowGraph += "-";
        }
        return cadToShowGraph;
    }//Cierre mostrarGrafo

    /* Metodos para modificar el peso y valor heuristico    */
    public boolean cambiarPesoArista(int nodoFuente, int nodoDestino, int newPeso) {
        if (existeArista(nodoFuente, nodoDestino) == true) {
            InfoArista auxObj = grafo.get(nodoFuente);
            auxObj.pesoArista = newPeso;
            return true;
        } else {
            return false;
        }
    }//Cierre cambiarPesoArista

    public boolean cambiarValHeuristico(int nodoFuente, int nodoDestino, int newValHeuristico) {
        if (existeArista(nodoFuente, nodoDestino) == true) {
            InfoArista auxObj = grafo.get(nodoFuente);
            auxObj.pesoArista = newValHeuristico;
            return true;
        } else {
            return false;
        }
    }//Cierre cambiarPesoArista

    /*    Metodos para añadir y eliminar Nodos  */
    public void agregarNodo() {
        grafo.add(null);
        numVertices++;
    }

    public void eliminarNodo(int nodeToDelete) {
        grafo.remove(nodeToDelete);
        numVertices--;
    }

    //  ---- Metodos para trabajar con el algoritmo de obtener todos los caminos posibles  --- //
    /**
     * Este metodo se encarga de obtener todos los nodos del grafo,
     * independientemente de que se le hayan creado enlaces o no!
     *
     * @return -> El metodo devuelve un ArrayList de Enteros, cada posicion
     * representa un nodo que contiene el grafo!
     */
    public ArrayList<Integer> obtenerTodosLosNodosDelGrafo() {
        //Devolverá la lista de nodos!
        ArrayList<Integer> nodos = new ArrayList<Integer>();
        //Agregar al ArrayList nodos todos los nodos que tiene el grafo!
        for (int i = 0; i < grafo.size(); i++) {
            nodos.add(i);
        }  //Agregar los nodos del grafo al ArrayList
        return nodos;
    }//Cierre metodo obtenerTodosLosNodosDelGrafo

    /**
     * Este metodo recibe un entero que representa el nodo del cual se quieren
     * obtener sus enlaces
     *
     * @param nodos -> Entero que representa el nodo del cual se quieren obtener
     * sus enlaces
     * @return -> El metodo regresa un ArrayList de tipo InfoArista con todos
     * los enlaces que tiene el nodo recibido
     */
    public ArrayList<InfoArista> getEnlaces(int nodo) {
        ArrayList<InfoArista> enlaces = new ArrayList<InfoArista>();

        InfoArista actual = grafo.get(nodo);
        while (actual != null) {
            enlaces.add(actual); //Si el nodo actual del grafo no es null, significa que tiene una arista!. Agregarlo al array de nodo
            actual = actual.siguiente;
        }
        return enlaces;
    }
    //  ---- Fin Metodos para trabajar con el algoritmo de obtener todos los caminos posibles  --- //
    
    
    //  ---- Metodos para trabajar con los algoritmos de la fase 3  --- //
    /**
      * Este metodo recibe un entero que representa el nodo del cual se quieren obtener sus enlaces
      * @param nodos  -> Entero que representa el nodo del cual se quieren obtener sus enlaces
      * @param ignorarVariable -> Tal cual su nombre lo indica, solamente se uso para poder crear el overridign
      * @return       -> El metodo regresa un ArrayList de tipo Hijo con todos los enlaces que tiene el nodo recibido y el padre correspondiente!
    */
    public ArrayList<Hijo> getEnlaces(int nodo, boolean ignorarVariable){
      ArrayList<Hijo> enlaces = new ArrayList<Hijo>();

      InfoArista actual = grafo.get(nodo);  //Variable para obtener los adyacentes del nodo padre!
      while(actual != null){
        //nodo representa al padre!
        Hijo aux = new Hijo(actual.numVertice,nodo);
        enlaces.add(aux); //Si el nodo actual del grafo no es null, significa que tiene una arista!. Agregarlo al array de nodo
        actual = actual.siguiente;        //Pasar al siguiente adyacente del nodo padre
      }
      return enlaces;
    }
    //  Fin ---- Metodos para trabajar con los algoritmos de la fase 3  --- //
    
    
    //  ---- Metodos para trabajar con los algoritmos de la fase 4  --- //
    /**
      * Este metodo recibe un entero que representa el nodo del cual se quieren obtener sus enlaces
      * @param nodo           -> Entero que representa el nodo del cual se quieren obtener sus enlaces
      * @param pesoAcumulado  -> Es el pesoAcumulado del camino que se ha creado para llegar hasta el nodo  g(n)
      * @return               -> El metodo regresa un ArrayList de tipo HijoFase3 con todos los enlaces que tiene el nodo recibido y el padre correspondiente!
    */
    public ArrayList<HijoFase3> getEnlaces(int nodo,int pesoAcumulado){
      ArrayList<HijoFase3> enlaces = new ArrayList<HijoFase3>();

      InfoArista actual = grafo.get(nodo);  //Variable para obtener los adyacentes del nodo padre!
      while(actual != null){
        //nodo representa al padre! y peso es el peso de la arista para ir a dicho nodo!
        int auxPeso = pesoAcumulado + actual.pesoArista;
        //El peso que tendrá cada nodo será el peso total, es decir, el peso que lleva el recorrido hasta este punto mas el peso de recorrer la arista para llegar al nodo hijo!
        HijoFase3 aux = new HijoFase3(actual.numVertice,auxPeso,nodo);
        enlaces.add(aux); //Si el nodo actual del grafo no es null, significa que tiene una arista!. Agregarlo al array de nodo
        actual = actual.siguiente;        //Pasar al siguiente adyacente del nodo padre
      }
      return enlaces;
    }
    //  ---- Fin Metodos para trabajar con los algoritmos de la fase 4  --- //
    
    
    /**
      * Este metodo recibe un entero que representa el nodo del cual se quieren obtener sus enlaces
      * @param nodo           -> Entero que representa el nodo del cual se quieren obtener sus enlaces
      * @param pesoAcumulado  -> Es el pesoAcumulado del camino que se ha creado para llegar hasta el nodo  g(n)
      * @return               -> El metodo regresa un ArrayList de tipo HijoEstrella con todos los enlaces que tiene el nodo recibido y el padre correspondiente!
    */
    public ArrayList<HijoEstrella> getEnlaces(int nodo,int pesoAcumulado, int nodoFinal, Grafo g){
      ArrayList<HijoEstrella> enlaces = new ArrayList<HijoEstrella>();

      InfoArista actual = grafo.get(nodo);  //Variable para obtener los adyacentes del nodo padre!
      while(actual != null){
        //nodo representa al padre! y peso es el peso de la arista para ir a dicho nodo!
        int auxPeso = pesoAcumulado + actual.pesoArista;
        //El peso que tendrá cada nodo será el peso total, es decir, el peso que lleva el recorrido hasta este punto mas el peso de recorrer la arista para llegar al nodo hijo!

        //Para calcular la heuristia se utilizará el metodo de Busqueda en Anchura, de esta manera se podra calcular cuantos nodos hacen falta por recorrer desde el hijo actual hasta el nodo finales
        BusquedaEnAnchura auxParaCalcularHeuristia = new BusquedaEnAnchura();
        //Se envia el numero del nodo hijo, el numero del nodo final y un obj GrafoAlgoritmoEstrella para que desde aquel metodo pueda acceder a getEnlaces definido en esta clase
        int heuristia = auxParaCalcularHeuristia.algoritmoBusquedaEnAnchura(actual.numVertice,nodoFinal,g,0);   //El parametro 0 no afecta!, es la variable dummy!

        HijoEstrella aux = new HijoEstrella(actual.numVertice,auxPeso,heuristia,nodo);
        enlaces.add(aux); //Si el nodo actual del grafo no es null, significa que tiene una arista!. Agregarlo al array de nodo
        actual = actual.siguiente;        //Pasar al siguiente adyacente del nodo padre
      }
      return enlaces;
    }

    
    
    
    // ----- Metodos para obtener lista de adyacentes -----//
    public boolean listaAdyVacia(int nodo) {
        if (grafo.get(nodo) == null) {
            return true;
        }
        return false;
    }

    public InfoArista getPrimerAd(int nodo) {
        return grafo.get(nodo);
    }

    public InfoArista nextAdy(InfoArista anterior) {
        if (anterior.siguiente == null) {
            return null;
        } else {
            return anterior.siguiente;
        }
    }
    // ----- Fin Metodos para obtener lista de adyacentes -----//

}//Cierre clase Grafo
