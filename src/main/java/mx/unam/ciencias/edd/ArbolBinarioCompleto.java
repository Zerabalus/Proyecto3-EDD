package mx.unam.ciencias.edd;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * <p>Clase para árboles binarios completos.</p>
 *
 * <p>Un árbol binario completo agrega y elimina elementos de tal forma que el
 * árbol siempre es lo más cercano posible a estar lleno.</p>
 */
public class ArbolBinarioCompleto<T> extends ArbolBinario<T> {

    /* Clase interna privada para iteradores. */
    private class Iterador implements Iterator<T> {

        /* Cola para recorrer los vértices en BFS. */
        private Cola<Vertice> cola;

        /* Inicializa al iterador. */
        private Iterador() {
            // Aquí va su código.
            this.cola = new Cola<Vertice>();
            if(!esVacia())
            this.cola.mete(raiz);
        }

        /* Nos dice si hay un elemento siguiente. */
        @Override public boolean hasNext() {
            // Aquí va su código.
            return (!cola.esVacia());
        }

        /* Regresa el siguiente elemento en orden BFS. */
        @Override public T next() {
            // Aquí va su código.
            if (!hasNext()) throw new NoSuchElementException();
                Vertice v = cola.saca();
            if (v.hayIzquierdo())
                cola.mete(v.izquierdo);
            if (v.hayDerecho())
                cola.mete(v.derecho);
            return v.elemento;
            
        }
    }

    /**
     * Constructor sin parámetros. Para no perder el constructor sin parámetros
     * de {@link ArbolBinario}.
     */
    public ArbolBinarioCompleto() { super(); }

    /**
     * Construye un árbol binario completo a partir de una colección. El árbol
     * binario completo tiene los mismos elementos que la colección recibida.
     * @param coleccion la colección a partir de la cual creamos el árbol
     *        binario completo.
     */
    public ArbolBinarioCompleto(Coleccion<T> coleccion) {
        super(coleccion);
    }

    /**
     * Agrega un elemento al árbol binario completo. El nuevo elemento se coloca
     * a la derecha del último nivel, o a la izquierda de un nuevo nivel.
     * @param elemento el elemento a agregar al árbol.
     * @throws IllegalArgumentException si <code>elemento</code> es
     *         <code>null</code>.
     */
    @Override public void agrega(T elemento) {
        // Aquí va su código.
        if (elemento == null) {
            throw new IllegalArgumentException();
        }
        Vertice v = nuevoVertice(elemento);
        elementos++;
        if (raiz == null) {
            raiz = v;
        } else {
            Cola<Vertice> cola = new Cola<Vertice>();
            cola.mete(raiz);
            while(!(cola.esVacia())) {
                Vertice i = cola.saca();
                if (i.hayIzquierdo()) {
                    cola.mete(i.izquierdo);
                } else if (!(i.hayIzquierdo())) {
                    i.izquierdo = v;
                    v.padre = i;
                    return;
                }
                if (i.hayDerecho()) {
                    cola.mete(i.derecho);
                } else if (!(i.hayDerecho())) {
                    i.derecho = v;
                    v.padre = i;
                    return;
                }
            }
        }
    }

    /**
     * Elimina un elemento del árbol. El elemento a eliminar cambia lugares con
     * el último elemento del árbol al recorrerlo por BFS, y entonces es
     * eliminado.
     * @param elemento el elemento a eliminar.
     */
    @Override public void elimina(T elemento) {
        // Aquí va su código.
        //ayudantía liz
        Vertice eliminar = (ArbolBinario<T>.Vertice) busca(elemento);
        if (eliminar == null) {
            return;
        }
        elementos = elementos - 1;
        if (elementos == 0) {
            raiz = null;
            return;
        }
        Cola<Vertice> cola = new Cola<Vertice>();
        Vertice v = raiz;
        cola.mete(v);
        while (!(cola.esVacia())) {
            Vertice e = cola.saca();
            if (!(e.hayDerecho()) && !(e.hayIzquierdo()) && cola.esVacia()) {
                v = e;
            }
            if (e.hayIzquierdo()) {
                cola.mete(e.izquierdo);
            }
            if (e.hayDerecho()) {
                cola.mete(e.derecho);
            }
        }
        T elimina = v.elemento;
        v.elemento = eliminar.elemento;
        eliminar.elemento = elimina;
        if (v.padre.izquierdo.elemento.equals(elemento)) {
            v.padre.izquierdo = null;
        } else {
            v.padre.derecho = null;
        }
        v.padre = null;
    }

    /**
     * Regresa la altura del árbol. La altura de un árbol binario completo
     * siempre es ⌊log<sub>2</sub><em>n</em>⌋.
     * @return la altura del árbol.
     */
    @Override public int altura() {
        // Aquí va su código.
        if (elementos == 0) return -1;
        return raiz.altura();
    }

    /**
     * Realiza un recorrido BFS en el árbol, ejecutando la acción recibida en
     * cada elemento del árbol.
     * @param accion la acción a realizar en cada elemento del árbol.
     */
    public void bfs(AccionVerticeArbolBinario<T> accion) {
        // Aquí va su código.
        if (raiz == null) {
            return;
        }
        Cola<Vertice> cola = new Cola<>();
        cola.mete(raiz);
        while(!cola.esVacia()){
            Vertice v = cola.saca();
            accion.actua(v);
            if(v.hayIzquierdo())
                cola.mete(vertice(v.izquierdo()));
            if(v.hayDerecho())
                cola.mete(vertice(v.derecho()));
        }
    }

    /**
     * Regresa un iterador para iterar el árbol. El árbol se itera en orden BFS.
     * @return un iterador para iterar el árbol.
     */
    @Override public Iterator<T> iterator() {
        return new Iterador();
    }
}
