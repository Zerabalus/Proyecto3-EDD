package mx.unam.ciencias.edd;

import java.util.Iterator;
import java.util.NoSuchElementException;

import mx.unam.ciencias.edd.ArbolBinario.Vertice;

/**
 * Clase para gráficas. Una gráfica es un conjunto de vértices y aristas, tales
 * que las aristas son un subconjunto del producto cruz de los vértices.
 */
public class Grafica<T> implements Coleccion<T> {

    /* Clase interna privada para iteradores. */
    private class Iterador implements Iterator<T> {

        /* Iterador auxiliar. */
        private Iterator<Vertice> iterador;

        /* Construye un nuevo iterador, auxiliándose de la lista de vértices. */
        public Iterador() {
            // Aquí va su código.
            iterador = vertices.iterator(); // itera los vertices de la gráfica
        }

        /* Nos dice si hay un siguiente elemento. */
        @Override public boolean hasNext() {
            // Aquí va su código.
            return iterador.hasNext(); //se llama al método hasnext del iterador
        }

        /* Regresa el siguiente elemento. */
        @Override public T next() {
            // Aquí va su código.
            return iterador.next().elemento; //se regresa el elem de llamar next con el iterador
        }
    }

    /* Clase interna privada para vértices. */
    private class Vertice implements VerticeGrafica<T>, Comparable<Vertice> {

        /* El elemento del vértice. */
        private T elemento;
        /* La distancia del vértice. */
        private double distancia;
        /* El diccionario de vecinos del vértice. */
        private Diccionario<T, Vecino> vecinos;

        /* Crea un nuevo vértice a partir de un elemento. */
        public Vertice(T elemento) {
            // Aquí va su código.

            //19.2 IMPLEMENTACION DE JAVA
            this.elemento = elemento; //constructor define el elemento del vértice como el elemento recibido
            this.vecinos = new Diccionario<>(); //utilizamos diccionarios en lugar de propiedades
        }

        /* Regresa el elemento del vértice. */
        @Override public T get() {
            // Aquí va su código.
            return elemento;
        }

        /* Regresa el grado del vértice. */
        @Override public int getGrado() {
            // Aquí va su código.
            return vecinos.getElementos(); //utilizamos diccionarios en lugar de propiedades
        }

        /* Regresa un iterable para los vecinos. */
        @Override public Iterable<? extends VerticeGrafica<T>> vecinos() {
            // Aquí va su código.
            return vecinos;//regresa vertices vecinos
        }

        /* Compara dos vértices por distancia. */
        @Override public int compareTo(Vertice vertice) {
            // Aquí va su código.
            return Double.compare(distancia, vertice.distancia);
        }

        private boolean esVecinoDe(Vertice esVecino) {
            return vecinos.contiene(esVecino.elemento);
        }

        private Vecino getVecino(Vertice esVecino) {
            if (!vecinos.contiene(esVecino.elemento))
                return null;

            return vecinos.get(esVecino.elemento);
        
        }
    }

    /* Clase interna privada para vértices vecinos. */
    private class Vecino implements VerticeGrafica<T> {

        /* El vértice vecino. */
        public Vertice vecino;
        /* El peso de la arista conectando al vértice con su vértice vecino. */
        public double peso;

        /* Construye un nuevo vecino con el vértice recibido como vecino y el
         * peso especificado. */
        public Vecino(Vertice vecino, double peso) {
            // Aquí va su código.
            this.vecino = vecino;
            this.peso = peso;
        }

        /* Regresa el elemento del vecino. */
        @Override public T get() {
            // Aquí va su código.
            return vecino.elemento;
        }

        /* Regresa el grado del vecino. */
        @Override public int getGrado() {
            // Aquí va su código.
            return vecino.getGrado();
        }

        /* Regresa un iterable para los vecinos del vecino. */
        @Override public Iterable<? extends VerticeGrafica<T>> vecinos() {
            // Aquí va su código.
            return vecino.vecinos();
        }
    }

    /* Interface para poder usar lambdas al buscar el elemento que sigue al
     * reconstruir un camino. */
    @FunctionalInterface
    private interface BuscadorCamino<T> {
        /* Regresa true si el vértice se sigue del vecino. */
        public boolean seSiguen(Grafica<T>.Vertice v, Grafica<T>.Vecino a);
    }

    /* Vértices. */
    private Diccionario<T, Vertice> vertices;
    /* Número de aristas. */
    private int aristas;

    /**
     * Constructor único.
     */
    public Grafica() {
        // Aquí va su código.
        vertices = new Diccionario<>(); //utilizamos diccionarios en listas
    }

    /**
     * Regresa el número de elementos en la gráfica. El número de elementos es
     * igual al número de vértices.
     * @return el número de elementos en la gráfica.
     */
    @Override public int getElementos() {
        // Aquí va su código.
        return vertices.getElementos(); //no Longitud
    }

    /**
     * Regresa el número de aristas.
     * @return el número de aristas.
     */
    public int getAristas() {
        // Aquí va su código.
        return aristas;
    }

    /**
     * Agrega un nuevo elemento a la gráfica.
     * @param elemento el elemento a agregar.
     * @throws IllegalArgumentException si el elemento ya había sido agregado a
     *         la gráfica.
     */
    @Override public void agrega(T elemento) {
        // Aquí va su código.
        if(contiene(elemento) || elemento == null)
            throw new IllegalArgumentException("El elemento ya existe o es nulo");
        vertices.agrega(elemento, new Vertice(elemento));
    }

    /**
     * Conecta dos elementos de la gráfica. Los elementos deben estar en la
     * gráfica. El peso de la arista que conecte a los elementos será 1.
     * @param a el primer elemento a conectar.
     * @param b el segundo elemento a conectar.
     * @throws NoSuchElementException si a o b no son elementos de la gráfica.
     * @throws IllegalArgumentException si a o b ya están conectados, o si a es
     *         igual a b.
     */
    public void conecta(T a, T b) {
        // Aquí va su código.
        
        Vertice verticeA = (Vertice)vertice(a);
        Vertice verticeB = (Vertice)vertice(b);
        Vecino vecinoA = new Vecino(verticeA, 1);
        Vecino vecinoB = new Vecino(verticeB, 1);

        if(!contiene(a) || !contiene(b))
            throw new NoSuchElementException("Faltan elementos en la gráfica");

        if (a == b)
            throw new IllegalArgumentException("Un vertice no puede conectarse a si mismo");

        if (sonVecinos(a, b))
            throw new IllegalArgumentException("Los vertices ya están conectados");

        verticeA.vecinos.agrega(b, vecinoB);
        verticeB.vecinos.agrega(a, vecinoA);
        aristas++;
    }

    /**
     * Conecta dos elementos de la gráfica. Los elementos deben estar en la
     * gráfica.
     * @param a el primer elemento a conectar.
     * @param b el segundo elemento a conectar.
     * @param peso el peso de la nueva vecino.
     * @throws NoSuchElementException si a o b no son elementos de la gráfica.
     * @throws IllegalArgumentException si a o b ya están conectados, si a es
     *         igual a b, o si el peso es no positivo.
     */
    public void conecta(T a, T b, double peso) {
        // Aquí va su código.

        if(!contiene(a) || !contiene(b))
            throw new NoSuchElementException("Faltan elementos en la gráfica");
        
        Vertice verticeA = (Vertice)vertice(a);
        Vertice verticeB = (Vertice)vertice(b);
        Vecino vecinoA = new Vecino(verticeA, peso);
        Vecino vecinoB = new Vecino(verticeB, peso);

        if (a == b)
            throw new IllegalArgumentException("Un vertice no puede conectarse a si mismo");

        if (peso <= 0)
            throw new IllegalArgumentException("El peso no puede ser negativo");

        if (sonVecinos(a, b))
            throw new IllegalArgumentException("Los vertices ya están conectados");
        

        verticeA.vecinos.agrega(b, vecinoB);
        verticeB.vecinos.agrega(a, vecinoA);
        aristas++;
    }

    /**
     * Desconecta dos elementos de la gráfica. Los elementos deben estar en la
     * gráfica y estar conectados entre ellos.
     * @param a el primer elemento a desconectar.
     * @param b el segundo elemento a desconectar.
     * @throws NoSuchElementException si a o b no son elementos de la gráfica.
     * @throws IllegalArgumentException si a o b no están conectados.
     */
    public void desconecta(T a, T b) {
        // Aquí va su código.

        if(!contiene(a) || !contiene(b))
            throw new NoSuchElementException("Faltan elementos en la gráfica");

        if (a == b)
            throw new IllegalArgumentException("No puedes conectar un vertice a si mismo");

        if (!sonVecinos(a, b))
            throw new IllegalArgumentException("Los vertices no están conectados");
        
        Vertice verticeA = (Vertice) vertice(a);
        Vertice verticeB = (Vertice) vertice(b);

        if(!verticeA.esVecinoDe(verticeB)){
            throw new IllegalArgumentException();
        }

        verticeA.vecinos.elimina(b);
        verticeB.vecinos.elimina(a);
        aristas--;
    }

    /**
     * Nos dice si el elemento está contenido en la gráfica.
     * @return <code>true</code> si el elemento está contenido en la gráfica,
     *         <code>false</code> en otro caso.
     */
    @Override public boolean contiene(T elemento) {
        // Aquí va su código.
        try {
            VerticeGrafica<T> v = vertice(elemento);
        } catch (NoSuchElementException e) {
            return false;
        }
        return true;
    }

    /**
     * Elimina un elemento de la gráfica. El elemento tiene que estar contenido
     * en la gráfica.
     * @param elemento el elemento a eliminar.
     * @throws NoSuchElementException si el elemento no está contenido en la
     *         gráfica.
     */
    @Override public void elimina(T elemento) {
        // Aquí va su código.
        if(!contiene(elemento)){
            throw new NoSuchElementException("El elemento no esta contenido en la gráfica");
        }

        Vertice eliminado = (Vertice)vertice(elemento);
        Diccionario<T, Vecino> vecinos = eliminado.vecinos;
        for(Vecino vecinoElim : vecinos){
            vecinoElim.vecino.vecinos.elimina(elemento);
            aristas--;
        }
        vertices.elimina(elemento);
    }

    /**
     * Nos dice si dos elementos de la gráfica están conectados. Los elementos
     * deben estar en la gráfica.
     * @param a el primer elemento.
     * @param b el segundo elemento.
     * @return <code>true</code> si a y b son vecinos, <code>false</code> en otro caso.
     * @throws NoSuchElementException si a o b no son elementos de la gráfica.
     */
    public boolean sonVecinos(T a, T b) {
        // Aquí va su código.
        return (((Vertice)vertice(a)).esVecinoDe(((Vertice) vertice(b))));
    }

    /**
     * Regresa el peso de la arista que comparten los vértices que contienen a
     * los elementos recibidos.
     * @param a el primer elemento.
     * @param b el segundo elemento.
     * @return el peso de la arista que comparten los vértices que contienen a
     *         los elementos recibidos.
     * @throws NoSuchElementException si a o b no son elementos de la gráfica.
     * @throws IllegalArgumentException si a o b no están conectados.
     */
    public double getPeso(T a, T b) {
        // Aquí va su código.

        if(!contiene(a) || !contiene(b))
            throw new NoSuchElementException("Faltan elementos en la gráfica");

        if(!sonVecinos(a,b))
            throw new IllegalArgumentException("Los elementos no son vecinos");

        if(a==null || b==null)
            throw new IllegalArgumentException("NO se permiten elementos nulos");

        Vertice verticeA = (Vertice) vertice(a);
        Vertice verticeB = (Vertice) vertice(b);

        if(!verticeA.esVecinoDe(verticeB))
            throw new IllegalArgumentException();

        return verticeA.vecinos.get(b).peso;
    }

    /**
     * Define el peso de la arista que comparten los vértices que contienen a
     * los elementos recibidos.
     * @param a el primer elemento.
     * @param b el segundo elemento.
     * @param peso el nuevo peso de la arista que comparten los vértices que
     *        contienen a los elementos recibidos.
     * @throws NoSuchElementException si a o b no son elementos de la gráfica.
     * @throws IllegalArgumentException si a o b no están conectados, o si peso
     *         es menor o igual que cero.
     */
    public void setPeso(T a, T b, double peso) {
        // Aquí va su código.

        if(!contiene(a) || !contiene(b))
            throw new NoSuchElementException("No está algun elemento en la grafica");

        if(!sonVecinos(a,b))
            throw new IllegalArgumentException("Los elementos no son vecinos");

        if(peso <=0)
            throw new IllegalArgumentException("EL peso es negativo");

        Vertice verticeA = (Vertice) vertice(a);
        Vertice verticeB = (Vertice) vertice(b);

        if(!verticeA.esVecinoDe(verticeB) || peso <= 0)
            throw new IllegalArgumentException();

        verticeA.vecinos.get(b).peso = peso;
        verticeB.vecinos.get(a).peso = peso;
    }

    /**
     * Regresa el vértice correspondiente el elemento recibido.
     * @param elemento el elemento del que queremos el vértice.
     * @throws NoSuchElementException si elemento no es elemento de la gráfica.
     * @return el vértice correspondiente el elemento recibido.
     */
    public VerticeGrafica<T> vertice(T elemento) {
        // Aquí va su código.
        for(Vertice vertices: vertices)
            if(vertices.elemento.equals(elemento))
                return vertices;

        throw new NoSuchElementException("No está el elemento");
    }

    /**
     * Nos dice si la gráfica es conexa.
     * @return <code>true</code> si la gráfica es conexa, <code>false</code> en
     *         otro caso.
     */
    public boolean esConexa() {
        // Aquí va su código.
        boolean conexa = true;
        Conjunto<Vertice> noRecorridos = new Conjunto<>();

        for(Vertice vertice1 : vertices){
            noRecorridos.agrega(vertice1);
        }
        Pila<Vertice> pila = new Pila<>();
        pila.mete(vertices.iterator().next());
        while(!pila.esVacia()){ 
            Vertice v = pila.saca();
            noRecorridos.elimina(v);
            for(Vecino vecino : v.vecinos)
                if(noRecorridos.contiene(vecino.vecino))
                    pila.mete(vecino.vecino);
        }

        for(Vertice v : vertices)
            if(noRecorridos.contiene(v))
                conexa = false;

        return conexa;
    }

    /**
     * Realiza la acción recibida en cada uno de los vértices de la gráfica, en
     * el orden en que fueron agregados.
     * @param accion la acción a realizar.
     */
    public void paraCadaVertice(AccionVerticeGrafica<T> accion) {
        // Aquí va su código.
        for(Vertice v : vertices){
            accion.actua(v);
        }
    }

    /**
     * Realiza la acción recibida en todos los vértices de la gráfica, en el
     * orden determinado por BFS, comenzando por el vértice correspondiente al
     * elemento recibido.
     * @param elemento el elemento sobre cuyo vértice queremos comenzar el
     *        recorrido.
     * @param accion la acción a realizar.
     * @throws NoSuchElementException si el elemento no está en la gráfica.
     */
    public void bfs(T elemento, AccionVerticeGrafica<T> accion) {
        // Aquí va su código.
        Cola<Vertice> struct = new Cola<>();
        firstSearch(elemento, accion, struct);
    }

    /**
     * Realiza la acción recibida en todos los vértices de la gráfica, en el
     * orden determinado por DFS, comenzando por el vértice correspondiente al
     * elemento recibido.
     * @param elemento el elemento sobre cuyo vértice queremos comenzar el
     *        recorrido.
     * @param accion la acción a realizar.
     * @throws NoSuchElementException si el elemento no está en la gráfica.
     */
    public void dfs(T elemento, AccionVerticeGrafica<T> accion) {
        // Aquí va su código.
        Pila<Vertice> struct = new Pila<>();
        firstSearch(elemento, accion, struct);
    }

    private void firstSearch(T elemento, AccionVerticeGrafica<T> accion, MeteSaca<Vertice> struct){
        Conjunto<Vertice> noRecorridos = new Conjunto<>();
        for(Vertice vertice : vertices){
            noRecorridos.agrega(vertice);
        }
        Vertice vElemento = (Vertice) (vertice(elemento));
        noRecorridos.elimina(vElemento);
        struct.mete(vElemento);
        while(!struct.esVacia()){
            Vertice actual = struct.saca();
            accion.actua(actual);

            for(Vecino vecino : actual.vecinos)
                if(noRecorridos.contiene(vecino.vecino)){
                    noRecorridos.elimina(vecino.vecino);
                    struct.mete(vecino.vecino);
                }

        }
    }

    /**
     * Nos dice si la gráfica es vacía.
     * @return <code>true</code> si la gráfica es vacía, <code>false</code> en
     *         otro caso.
     */
    @Override public boolean esVacia() {
        // Aquí va su código.
        return (vertices.esVacia());
    }

    /**
     * Limpia la gráfica de vértices y aristas, dejándola vacía.
     */
    @Override public void limpia() {
        // Aquí va su código.
        vertices.limpia();
        aristas = 0;
    }

    /**
     * Regresa una representación en cadena de la gráfica.
     * @return una representación en cadena de la gráfica.
     */
    @Override public String toString() {
        // Aquí va su código.
        String toString = "{";

        //Lista de vertices
        for(Vertice v : vertices)
            toString += v.elemento + ", ";

        toString += "}, {";

        //Lista de aristas

        Conjunto<Vertice> verticesPasados = new Conjunto<>();
        for (Vertice vertice : vertices) {
            for (Vecino vecino : vertice.vecinos)
                if (!verticesPasados.contiene(vecino.vecino))
                    toString += "(" + vertice.elemento.toString() + ", " + vecino.vecino.elemento.toString() + "), ";

                    verticesPasados.agrega(vertice);
        }

        toString += "}";
        return toString;
    }

    /**
     * Nos dice si la gráfica es igual al objeto recibido.
     * @param objeto el objeto con el que hay que comparar.
     * @return <code>true</code> si la gráfica es igual al objeto recibido;
     *         <code>false</code> en otro caso.
     */
    @Override public boolean equals(Object objeto) {
        if (objeto == null || getClass() != objeto.getClass())
            return false;
        @SuppressWarnings("unchecked") Grafica<T> grafica = (Grafica<T>)objeto;
        // Aquí va su código.
        //Comparacion de cardenalidades de los conjuntos
        if(aristas != grafica.aristas || vertices.getElementos() != grafica.vertices.getElementos())
            return false;

        //Comenzamos a recorrer la lista de vertices.
        for(Vertice v : vertices){

            Vertice v2;
            //Si no encontramos su equivalente, acabamos
            try{
                v2 = (Vertice)grafica.vertice(v.elemento);
            }catch(NoSuchElementException e){
                return false;
            }

            //Si encontramos su equivalente, verificamos sus vecinos
            for(Vecino vecino : v.vecinos){
                boolean contiene = false;

                //Buscamos cada vecino en su equivalente.
                for(Vecino veci2 : v2.vecinos)
                    if(vecino.vecino.elemento.equals(veci2.vecino.elemento)){
                        contiene = true;
                        break;
                    }

                if(!contiene)
                    return false;
            }
        }
        return true;
    }

    /**
     * Regresa un iterador para iterar la gráfica. La gráfica se itera en el
     * orden en que fueron agregados sus elementos.
     * @return un iterador para iterar la gráfica.
     */
    @Override public Iterator<T> iterator() {
        return new Iterador();
    }

    /**
     * Calcula una trayectoria de distancia mínima entre dos vértices.
     * @param origen el vértice de origen.
     * @param destino el vértice de destino.
     * @return Una lista con vértices de la gráfica, tal que forman una
     *         trayectoria de distancia mínima entre los vértices <code>a</code> y
     *         <code>b</code>. Si los elementos se encuentran en componentes conexos
     *         distintos, el algoritmo regresa una lista vacía.
     * @throws NoSuchElementException si alguno de los dos elementos no está en
     *         la gráfica.
     */
    public Lista<VerticeGrafica<T>> trayectoriaMinima(T origen, T destino) {
        // Aquí va su código.
        Vertice vOrigen = (Vertice)vertice(origen);
        Vertice vDestino = (Vertice)vertice(destino);

        Lista<VerticeGrafica<T>> trayectoria = new Lista<>();
        trayectoria.agregaFinal(vDestino);

        if(vOrigen == vDestino) //Caso en el que origen y destino son lo mismo
            return trayectoria;

        //Seteo de distancias
        paraCadaVertice((v) -> {((Vertice)v).distancia = Double.MAX_VALUE;});
        vDestino.distancia = 0;

        //Recorrido BFS
        Cola<Vertice> queue = new Cola<>();
        queue.mete(vDestino);
        while(!queue.esVacia()){
            Vertice v = queue.saca();
            for(Vecino vecino : v.vecinos){
                if(vecino.vecino.distancia == Double.MAX_VALUE){
                    vecino.vecino.distancia = v.distancia + 1;
                    queue.mete(vecino.vecino);
                }
            }
        }

        //Reconstruccion de la ruta
        if(vOrigen.distancia == Double.MAX_VALUE){
            trayectoria.limpia();
            return trayectoria;
        }

        while(!trayectoria.getPrimero().equals(vOrigen)){
            Vertice v = (Vertice)trayectoria.getPrimero();
            for(Vecino vecino : v.vecinos){
                if(vecino.vecino.distancia == v.distancia + 1)
                    trayectoria.agregaInicio(vecino.vecino);
            }
        }

        return trayectoria;
    }

    /**
     * Calcula la ruta de peso mínimo entre el elemento de origen y el elemento
     * de destino.
     * @param origen el vértice origen.
     * @param destino el vértice destino.
     * @return una trayectoria de peso mínimo entre el vértice <code>origen</code> y
     *         el vértice <code>destino</code>. Si los vértices están en componentes
     *         conexas distintas, regresa una lista vacía.
     * @throws NoSuchElementException si alguno de los dos elementos no está en
     *         la gráfica.
     */
    public Lista<VerticeGrafica<T>> dijkstra(T origen, T destino) {
        // Aquí va su código.
        Vertice vOrigen = (Vertice) vertice(origen);
        Vertice vDestino = (Vertice) vertice(destino);

        Lista<VerticeGrafica<T>> trayectoria = new Lista<>();
        trayectoria.agregaFinal(vDestino);

        if(vOrigen == vDestino) //Caso en el que origen y destino son lo mismo
            return trayectoria;

        //Seteo de distancias
        paraCadaVertice((v) -> {((Vertice)v).distancia = Double.MAX_VALUE;});
        vOrigen.distancia = 0;

        //Creacion del monticulo
        MonticuloDijkstra<Vertice> monticulo;
        int n = vertices.getElementos();
        if(aristas > ((n * (n-1)) /2) - n)
            monticulo = new MonticuloArreglo<Vertice>((Iterable<Vertice>)vertices, vertices.getElementos());
        else
            monticulo = new MonticuloMinimo<Vertice>((Iterable<Vertice>)vertices, vertices.getElementos());

        //Its monticuleo time
        while(!monticulo.esVacia()){
            Vertice v = monticulo.elimina();

            for(Vecino vecino : v.vecinos){
                if(vecino.vecino.distancia > v.distancia + vecino.peso){
                    vecino.vecino.distancia = v.distancia + vecino.peso;
                    monticulo.reordena(vecino.vecino);
                }
            }
        }

        //Caso de componentes conexos distintos
        if(vDestino.distancia == Double.MAX_VALUE){
            trayectoria.limpia();
            return trayectoria;
        }

        //Reconstruccion de trayectoria
        while(((Vertice)trayectoria.getPrimero()).distancia != 0){
            Vertice v = (Vertice) trayectoria.getPrimero();

            for(Vecino vecino : v.vecinos){
                if(v.distancia == vecino.vecino.distancia + vecino.peso){
                    trayectoria.agregaInicio(vecino.vecino);
                    break;
                }
            }
        }

        return trayectoria;

    }
}
