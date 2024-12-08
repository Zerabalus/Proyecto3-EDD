package mx.unam.ciencias.edd;

/**
 * Clase para árboles rojinegros. Un árbol rojinegro cumple las siguientes
 * propiedades:
 *
 * <ol>
 *  <li>Todos los vértices son NEGROS o ROJOS.</li>
 *  <li>La raíz es NEGRA.</li>
 *  <li>Todas las hojas (<code>null</code>) son NEGRAS (al igual que la raíz).</li>
 *  <li>Un vértice ROJO siempre tiene dos hijos NEGROS.</li>
 *  <li>Todo camino de un vértice a alguna de sus hojas descendientes tiene el
 *      mismo número de vértices NEGROS.</li>
 * </ol>
 *
 * Los árboles rojinegros se autobalancean.
 */
public class ArbolRojinegro<T extends Comparable<T>>
    extends ArbolBinarioOrdenado<T> {

    /**
     * Clase interna protegida para vértices.
     */
    protected class VerticeRojinegro extends Vertice {

        /** El color del vértice. */
        public Color color;

        /**
         * Constructor único que recibe un elemento.
         * @param elemento el elemento del vértice.
         */
        public VerticeRojinegro(T elemento) {
            // Aquí va su código.
            super(elemento);
            color = Color.NINGUNO;
        }

        /**
         * Regresa una representación en cadena del vértice rojinegro.
         * @return una representación en cadena del vértice rojinegro.
         */
        @Override public String toString() {
            // Aquí va su código.
            if(this.color == Color.NEGRO)
                return "N{" + elemento + "}";
            else
                return "R{" + elemento + "}";
        }

        /**
         * Compara el vértice con otro objeto. La comparación es
         * <em>recursiva</em>.
         * @param objeto el objeto con el cual se comparará el vértice.
         * @return <code>true</code> si el objeto es instancia de la clase
         *         {@link VerticeRojinegro}, su elemento es igual al elemento de
         *         éste vértice, los descendientes de ambos son recursivamente
         *         iguales, y los colores son iguales; <code>false</code> en
         *         otro caso.
         */
        @Override public boolean equals(Object objeto) {
            if (objeto == null || getClass() != objeto.getClass())
                return false;
            @SuppressWarnings("unchecked")
                VerticeRojinegro vertice = (VerticeRojinegro)objeto;
            // Aquí va su código.
            return (color == vertice.color && super.equals(objeto));
        }
    }

    /**
     * Constructor sin parámetros. Para no perder el constructor sin parámetros
     * de {@link ArbolBinarioOrdenado}.
     */
    public ArbolRojinegro() { 
        super(); 
    }

    /**
     * Construye un árbol rojinegro a partir de una colección. El árbol
     * rojinegro tiene los mismos elementos que la colección recibida.
     * @param coleccion la colección a partir de la cual creamos el árbol
     *        rojinegro.
     */
    public ArbolRojinegro(Coleccion<T> coleccion) {
        // Aquí va su código.
        super(coleccion);
    }

    /**
     * Construye un nuevo vértice, usando una instancia de {@link
     * VerticeRojinegro}.
     * @param elemento el elemento dentro del vértice.
     * @return un nuevo vértice rojinegro con el elemento recibido dentro del mismo.
     */
    @Override protected Vertice nuevoVertice(T elemento) {
        // Aquí va su código.
        return new VerticeRojinegro(elemento);
    }

    /**
     * Regresa el color del vértice rojinegro.
     * @param vertice el vértice del que queremos el color.
     * @return el color del vértice rojinegro.
     * @throws ClassCastException si el vértice no es instancia de {@link
     *         VerticeRojinegro}.
     */
    public Color getColor(VerticeArbolBinario<T> vertice) {
        // Aquí va su código.
        VerticeRojinegro colorRojinegro=(VerticeRojinegro) vertice;
        return colorRojinegro.color;
    }

    /**
     * Agrega un nuevo elemento al árbol. El método invoca al método {@link
     * ArbolBinarioOrdenado#agrega}, y después balancea el árbol recoloreando
     * vértices y girando el árbol como sea necesario.
     * @param elemento el elemento a agregar.
     */
    @Override public void agrega(T elemento) {
        // Aquí va su código.
        super.agrega(elemento);
        VerticeRojinegro nuevo=(VerticeRojinegro) ultimoAgregado;
        nuevo.color=Color.ROJO;
        rebalanceoAgrega(nuevo);
    }
    //15.2.1 del libro del profe canek
    private void rebalanceoAgrega(VerticeRojinegro vertice){

        if(vertice==null || !esRojo(vertice))
            return;
        if(vertice.padre==null) {
            vertice.color = Color.NEGRO;
            return;
        }
        VerticeRojinegro padre=(VerticeRojinegro) vertice(vertice.padre);
        if(esNegro(padre))
            return;
        VerticeRojinegro abuelo=(VerticeRojinegro) vertice(padre.padre);
        VerticeRojinegro tio=hermanoPadre(padre);

        if(tio!=null && tio.color==Color.ROJO  ) {
            tio.color = Color.NEGRO;
            padre.color = Color.NEGRO;
            abuelo.color = Color.ROJO;
            rebalanceoAgrega(abuelo);
            return;
        }

        if(esIzquierdo(padre) && esDerecho(vertice)) {
            super.giraIzquierda(padre);
            VerticeRojinegro temp=vertice;
            vertice=padre;
            padre=temp;
        }
        //si el padre es derecho y el hijo izquierdo
        else if(esDerecho(padre) && esIzquierdo(vertice)) {
            super.giraDerecha(padre);
            VerticeRojinegro temp=vertice;
            vertice=padre;
            padre=temp;
        }
        padre.color=Color.NEGRO;
        abuelo.color=Color.ROJO;

        if(esIzquierdo(vertice))
            super.giraDerecha(abuelo);
        else
            super.giraIzquierda(abuelo);
    }

    /**
     * Elimina un elemento del árbol. El método elimina el vértice que contiene
     * el elemento, y recolorea y gira el árbol como sea necesario para
     * rebalancearlo.
     * @param elemento el elemento a eliminar del árbol.
     */
    @Override public void elimina(T elemento) {
        // Aquí va su código.
        if(elemento==null)
            return;

        VerticeRojinegro vertice=(VerticeRojinegro) super.busca(elemento);
        if(vertice==null)
            return;

        if(vertice.hayDerecho() && vertice.hayIzquierdo())
            vertice=(VerticeRojinegro) super.intercambiaEliminable(vertice);
        
            /* Libr 15.2.2 basado en algoritmo para eliminar:

            "si ambos hijos del vértice son ⌀, vamos a
            crearle un vértice que llamaremos fantasma. El punto del vértice fantasma es
            que podamos girar nuestros vértices sin tener que preocuparnos de un hoyo
            en el árbol. Al final del algoritmo eliminaremos el vértice fantasma (si es
            que lo creamos). En nuestra implementación con Java un vértice fantasma
            tendrá como elemento null."" */

        VerticeRojinegro fantasma=(VerticeRojinegro) nuevoVertice(null);
        fantasma.color=Color.ROJO;

        if(!vertice.hayIzquierdo() && !vertice.hayDerecho()) {
            fantasma.color=Color.NEGRO;
            vertice.izquierdo=fantasma;
            fantasma.padre=vertice;
        }

        VerticeRojinegro hijo;
        if(vertice.hayIzquierdo())
            hijo=(VerticeRojinegro) vertice.izquierdo;
        else
            hijo=(VerticeRojinegro)vertice.derecho;

        super.eliminaVertice(vertice);
        elementos--;

        if(esRojo(hijo) || esRojo(vertice) ) {
            hijo.color=Color.NEGRO;
        }
        //caso hijo y vertice son negros
        else
            rebalanceoElimina(hijo);

        if(fantasma==hijo){
            if(raiz==fantasma) {
                super.eliminaVertice(fantasma);
                super.limpia();
            }else if(esIzquierdo(fantasma))
            fantasma.padre.izquierdo=null;
        else
            fantasma.padre.derecho=null;
        }

    }

    private void rebalanceoElimina(VerticeRojinegro vertice){
        if(vertice==null)
            return;
        //caso 1: padre null
        if(vertice.padre==null)
            return;
        VerticeRojinegro padre=(VerticeRojinegro) vertice.padre;
        VerticeRojinegro hermano=hermanoPadre(vertice);

        //caso 2: el hermano es rojo
        if(esRojo(hermano)) {
            padre.color = Color.ROJO;
            hermano.color=Color.NEGRO;
            if(esDerecho(vertice))
                super.giraDerecha(padre);
            else
                super.giraIzquierda(padre);
            padre=(VerticeRojinegro) vertice.padre;
            if(esIzquierdo(vertice))
                hermano=(VerticeRojinegro) padre.derecho;
            else
                hermano=(VerticeRojinegro) padre.izquierdo;
        }

        VerticeRojinegro hojaIzq=(VerticeRojinegro) hermano.izquierdo;
        VerticeRojinegro hojaDer=(VerticeRojinegro) hermano.derecho;
        //caso 3: padre,hermano, iquierda y derecha son negros
        if(esNegro(hermano) && esNegro(hojaIzq) && esNegro(hojaDer)) {
            if(esNegro(padre)) {
                hermano.color = Color.ROJO;
                rebalanceoElimina(padre);
                return;
            }
            //caso 4. el padre es rojo
            else{
                hermano.color=Color.ROJO;
                padre.color=Color.NEGRO;
                return;
            }
        }

        if((esIzquierdo(vertice) && esRojo(hojaIzq) && esNegro(hojaDer)) || (esDerecho(vertice) && esNegro(hojaIzq) && esRojo(hojaDer))) {
            hermano.color = Color.ROJO;
            
            //caso 5 v es izquierdo
            if(esIzquierdo(vertice)){
                hojaIzq.color=Color.NEGRO;
                super.giraDerecha(hermano);
            }else{//caso 5.1 v es derecho
                hojaDer.color=Color.NEGRO;
                super.giraIzquierda(hermano);
            }
            //reasigna hermano
            hermano=hermanoPadre(vertice);
            //reasigna izquierdo y derecho
            hojaIzq=(VerticeRojinegro) hermano.izquierdo;
            hojaDer=(VerticeRojinegro) hermano.derecho;
        }

        hermano.color=padre.color;
        padre.color=Color.NEGRO;
        if(esDerecho(vertice)) {
            hojaIzq.color = Color.NEGRO;
            super.giraDerecha(padre);
        }
        else {
            hojaDer.color = Color.NEGRO;
            super.giraIzquierda(padre);
        }

    }
    //casos
    private boolean esRojo(VerticeRojinegro vertice){
        if(vertice==null)
            return true;
        return vertice.color==Color.ROJO;
    }
    private boolean esNegro(VerticeRojinegro vertice){
        if(vertice==null)
            return true;
        return vertice.color==Color.NEGRO;
    }
    private boolean esIzquierdo(VerticeRojinegro vertice){
        return vertice.padre.izquierdo==vertice;
    }
    private boolean esDerecho(VerticeRojinegro vertice){
        return vertice.padre.derecho==vertice;
    }
    private VerticeRojinegro hermanoPadre(VerticeRojinegro vertice){
        VerticeRojinegro hermano;
        if(esIzquierdo(vertice))
            hermano=(VerticeRojinegro) vertice.padre.derecho;
        else
            hermano=(VerticeRojinegro) vertice.padre.izquierdo;
       return hermano;
    }

    /**
     * Lanza la excepción {@link UnsupportedOperationException}: los árboles
     * rojinegros no pueden ser girados a la izquierda por los usuarios de la
     * clase, porque se desbalancean.
     * @param vertice el vértice sobre el que se quiere girar.
     * @throws UnsupportedOperationException siempre.
     */
    @Override public void giraIzquierda(VerticeArbolBinario<T> vertice) {
        throw new UnsupportedOperationException("Los árboles rojinegros no " +
                                                "pueden girar a la izquierda " +
                                                "por el usuario.");
    }

    /**
     * Lanza la excepción {@link UnsupportedOperationException}: los árboles
     * rojinegros no pueden ser girados a la derecha por los usuarios de la
     * clase, porque se desbalancean.
     * @param vertice el vértice sobre el que se quiere girar.
     * @throws UnsupportedOperationException siempre.
     */
    @Override public void giraDerecha(VerticeArbolBinario<T> vertice) {
        throw new UnsupportedOperationException("Los árboles rojinegros no " +
                                                "pueden girar a la derecha " +
                                                "por el usuario.");
    }
}
