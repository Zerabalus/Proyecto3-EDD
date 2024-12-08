package mx.unam.ciencias.edd.proyecto3;

import mx.unam.ciencias.edd.Grafica;
import mx.unam.ciencias.edd.Lista;
import mx.unam.ciencias.edd.proyecto3.*;

import java.util.Random;
import java.util.Iterator;

public class GeneradorLaberinto implements Iterable<Byte> {
    private Random r;
    private byte width;
    private byte height;
    private byte[][] casillas;
    private Lista<Integer> visitadas = new Lista<Integer>();
    private int[] visitadasRandom;
    private int visitadasRandomElementos = 0;
    private int entrada = -1;
    private int salida = -1;

    public GeneradorLaberinto(long semilla, int w, int h) {
        if (w < 2 || w > 255)
          throw new IllegalArgumentException("El ancho debe ser mayor a 2 y menor a 255");
        
        if (h < 2 || h > 255)
          throw new IllegalArgumentException("El alto debe ser mayor a 2 y menor a 255");
    
        r = new Random(semilla);
        width = toByte(w);
        height = toByte(h);
        
        casillas = new byte[toUnsignedByte(height)][toUnsignedByte(width)];
        for(int i = 0; i < toUnsignedByte(height); i++)
        {
          for(int j = 0; j < toUnsignedByte(width); j++)
          {
            casillas[i][j] = (byte)0xFF;
          }
        }
        generar();
      }

    public GeneradorLaberinto(int w, int h, byte[][] c) {
        if (w < 2 || w > 255)
          throw new IllegalArgumentException("El ancho debe ser mayor a 2 y menor a 255");
        
        if (h < 2 || h > 255)
          throw new IllegalArgumentException("El alto debe ser mayor a 2 y menor a 255");
    
        width = toByte(w);
        height = toByte(h);
        casillas = c;
      }


    public void generar() {
        visitadasRandom = new int[toUnsignedByte(width) * toUnsignedByte(height) + 1];
        int inicial = par(randX(), randY());
        
        visitadas.agregaInicio(inicial);
        setValor(getX(inicial), getY(inicial), toByte(0));
        while (!visitadas.esVacia()) {
            int actual = siguiente();
            
            int[] vecinos = new int[4];
            Lista<Integer> direccionesValidas = new Lista<Integer>();
            for (int i = 0; i < 4; i++) {
                int vecino = getVecino(actual, i);
                vecinos[i] = vecino;
                if (vecino != -1 && valor(getCasilla(getX(vecino), getY(vecino))) == 15) // Es una casilla válida y no
                                                                                         // ha sido visitada
                    direccionesValidas.agrega(i);
            }
            

            if (direccionesValidas.esVacia()) {
                visitadas.elimina(actual);
                eliminarVisitadasRandom(actual);
                continue;
            }

            int i = r.nextInt(direccionesValidas.getLongitud());
            int d = direccionesValidas.get(i);
            int vecinorandom = vecinos[d];
            tiraMuro(getX(actual), getY(actual), d);
            tiraMuro(getX(vecinorandom), getY(vecinorandom), direccionOpuesta(d));
            visitadas.agregaInicio(vecinorandom);
            
            setValor(getX(vecinorandom), getY(vecinorandom), toByte(0));
            setValor(getX(actual), getY(actual), toByte(0));
            visitadasRandom[visitadasRandomElementos++] = vecinorandom;
        }

        for (int i = 0; i < toUnsignedByte(height); i++) {
            for (int j = 0; j < toUnsignedByte(width); j++) {
                setValor((byte) j, (byte) i, (byte) r.nextInt(16));
            }
        }

        int entrada = r.nextInt(toUnsignedByte(height));
        int salida = r.nextInt(toUnsignedByte(height));

        tiraMuro((byte) 0, (byte) entrada, 2);
        tiraMuro((byte) (width - 1), (byte) salida, 0);

        entrada = par((byte) 0, (byte) entrada);
        salida = par((byte) (width - 1), (byte) salida);
    }

    //método para obtener el valor de la casilla
    public int obtenValor(byte x, byte y) {
        return valor(getCasilla(x, y));
    }

    private int valor(byte c) {
        
        return (int) ((int) toUnsignedByte(c) >> 4);
    }

    private byte toByte(int n) {
        return (byte) (n & 0xFF);
    }

    //metodo para obtener UnsignedByte 

    public short convertToUnsignedByte(byte b) {
        return toUnsignedByte(b);
    }
    
    private short toUnsignedByte(byte b) {
        return (short) (((short) b) & 0xFF);
    }

    //metodo para obtener el par de la casilla

    public int getPar(byte x, byte y) {
        return par(x, y);
    }

    private int par(byte x, byte y) {
        short ux = toUnsignedByte(x);
        short uy = toUnsignedByte(y);
        
        return (int) ((ux << 8) | uy);
    }

    
    private byte randX() {
        return toByte(r.nextInt(toUnsignedByte(width)));
    }

    private byte randY() {
        return toByte(r.nextInt(toUnsignedByte(height)));
    }

    //metodo para obtener el ancho publicamente
    public byte getAncho(int par) {
        return getX(par);
    }

    private byte getX(int par) {
        return toByte(par >> 8);
    }

    //metodo para obtener el alto publicamente
    public byte getAlto(int par) {
        return getY(par);
    }

    private byte getY(int par) {
        return toByte(par & 0xFF);
    }

    public byte obtenerCasilla(byte x, byte y) {
        return getCasilla(x, y);
    }
    
    private byte getCasilla(byte x, byte y) {
        return casillas[toUnsignedByte(y)][toUnsignedByte(x)];
    }

    private int siguiente() {
        int c = r.nextInt(100);
        if (c < 25) // Random
        {
            
            if (visitadasRandomElementos <= 0)
                return visitadas.getUltimo();

            int i = r.nextInt(visitadasRandomElementos);
            
            return visitadasRandom[i];
        } else { // Más reciente
            
            return visitadas.getPrimero();
        }
    }

    private void eliminarVisitadasRandom(int e) {
        for (int i = 0; i < visitadasRandomElementos; i++) {
            if (visitadasRandom[i] == e) {
                for (int j = i + 1; j < visitadasRandomElementos; j++) {
                    visitadasRandom[j - 1] = visitadasRandom[j];
                }
                visitadasRandomElementos--;
                break;
            }
        }
    }

    private int getVecino(int par, int d) {
        if (d == 0) { // E
            if (getX(par) == width - 1)
                return -1;
            return par(toByte(getX(par) + 1), getY(par));
        } else if (d == 1) { // N
            if (getY(par) == 0)
                return -1;
            return par(getX(par), toByte(getY(par) - 1));
        } else if (d == 2) { // O
            if (getX(par) == 0)
                return -1;
            return par(toByte(getX(par) - 1), getY(par));
        } else if (d == 3) { // S
            if (getY(par) == height - 1)
                return -1;
            return par(getX(par), toByte(getY(par) + 1));
        }
        return -1;
    }

    private void setValor(byte x, byte y, byte v) {
        casillas[toUnsignedByte(y)][toUnsignedByte(x)] = toByte(
                ((v & 0xF) << 4) | (casillas[toUnsignedByte(y)][toUnsignedByte(x)] & 0xF));
    }

    private void tiraMuro(byte x, byte y, int d) {
        if (d == 0) {
            casillas[toUnsignedByte(y)][toUnsignedByte(x)] &= 0xFE; // casilla & 1111 1110
        } else if (d == 1) {
            casillas[toUnsignedByte(y)][toUnsignedByte(x)] &= 0xFD; // casilla & 1111 1101
        } else if (d == 2) {
            casillas[toUnsignedByte(y)][toUnsignedByte(x)] &= 0xFB; // casilla & 1111 1011
        } else if (d == 3) {
            casillas[toUnsignedByte(y)][toUnsignedByte(x)] &= 0xF7; // casilla & 1111 0111
        }
    }

    private int direccionOpuesta(int d) {
        return (d + 2) % 4;
    }
    
    @Override
    public Iterator<Byte> iterator() {
        return new IteradorLaberinto();
    }

}
