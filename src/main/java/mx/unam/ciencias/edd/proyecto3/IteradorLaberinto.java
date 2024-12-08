package mx.unam.ciencias.edd.proyecto3;

import mx.unam.ciencias.edd.Lista;
import mx.unam.ciencias.edd.VerticeGrafica;
import mx.unam.ciencias.edd.Grafica;
import mx.unam.ciencias.edd.IteradorLista;
import mx.unam.ciencias.edd.proyecto3.GeneradorLaberinto;

import java.util.Iterator;


public class IteradorLaberinto implements Iterator<Byte> {

    private int i = 0;
    private int j = 0;
    private byte width;
    private byte height;

    
    public IteradorLaberinto(GeneradorLaberinto generadorLaberinto) {
        this.generadorLaberinto = generadorLaberinto;
    }
    
    private GeneradorLaberinto generadorLaberinto= new GeneradorLaberinto(semilla, w, h);
    IteradorLaberinto iterador = new IteradorLaberinto(generadorLaberinto);

    /**
     * Crea un iterador para recorrer el laberinto.
     * @param width el ancho del laberinto.
     * @param height el alto del laberinto.
     */
    
    // ...
    @Override
    public boolean hasNext() {
        return i < generadorLaberinto.convertToUnsignedByte(height) && j < generadorLaberinto.convertToUnsignedByte(width);
    }

    /**
     * Regresa el siguiente byte en el laberinto. Si el final de una fila se ha
     * alcanzado, se pasa a la siguiente fila y se reinicia el conteo de
     * columnas.
     * @return el siguiente byte en el laberinto.
     */
    @Override
    public Byte next() {
        byte e = generadorLaberinto.obtenerCasilla((byte) i, (byte) j++); // Usa el método público
        if (j >= generadorLaberinto.convertToUnsignedByte(width)) {
            j = 0;
            i++;
        }
        return Byte.valueOf(e); // Devuelve el valor como un objeto Byte
    }

}

