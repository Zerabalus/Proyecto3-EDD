package mx.unam.ciencias.edd.proyecto3;

import java.util.Random;

import mx.unam.ciencias.edd.Grafica;
import mx.unam.ciencias.edd.IteradorLista;
import mx.unam.ciencias.edd.Lista;
import mx.unam.ciencias.edd.VerticeGrafica;
import mx.unam.ciencias.edd.proyecto3.IteradorLaberinto;
import mx.unam.ciencias.edd.proyecto3.GeneradorLaberinto;

public class LaberintoSVG {

    private Random r;
    private byte width;
    private byte height;
    private byte[][] casillas;
    private Lista<Integer> visitadas = new Lista<Integer>();
    private int[] visitadasRandom;
    private int visitadasRandomElementos = 0;
    private int entrada = -1;
    private int salida = -1;
    

    // Constructor
    
    private GeneradorLaberinto generadorLaberinto;


    private Grafica<Integer> grafica() {
        Grafica<Integer> g = new Grafica<Integer>();
        for (int i = 0; i < generadorLaberinto.convertToUnsignedByte(height); i++) {
            for (int j = 0; j < generadorLaberinto.convertToUnsignedByte(width); j++) {
                g.agrega(generadorLaberinto.getPar((byte) j, (byte) i));
            }
        }
        for (int i = 0; i < generadorLaberinto.convertToUnsignedByte(height); i++) {
            for (int j = 0; j < generadorLaberinto.convertToUnsignedByte(width); j++) {
                byte c1 = generadorLaberinto.obtenerCasilla((byte) j, (byte) i);
                if ((c1 & 0b0001) == 0b0000) {
                    if (j < width - 1) {
                        byte c2 = generadorLaberinto.obtenerCasilla((byte) (j + 1), (byte) i);
                        try { //pendiente la linea 45
                            g.conecta(generadorLaberinto.getPar((byte) j, (byte) i), generadorLaberinto.getPar((byte) (j + 1), (byte) i),
                            generadorLaberinto.obtenValor((byte) j, (byte) i) + generadorLaberinto.obtenValor((byte) (j + 1), (byte) i) + 1);
                        } catch (Exception e) {

                        }
                    } else {
                        salida = generadorLaberinto.getPar((byte) j, (byte) i);
                    }
                }
                if ((c1 & 0b0010) == 0b0000 && i > 0) {
                    byte c2 = generadorLaberinto.obtenerCasilla((byte) j, (byte) (i - 1));
                    try {
                        g.conecta(generadorLaberinto.getPar((byte) j, (byte) i),generadorLaberinto.getPar((byte) j, (byte) (i - 1)), generadorLaberinto.obtenValor((byte) j, (byte) i) + generadorLaberinto.obtenValor((byte) j, (byte) (i - 1)) + 1);
                    } catch (Exception e) {

                    }
                }
                if ((c1 & 0b0100) == 0b0000) {
                    if (j > 0) {
                        byte c2 = generadorLaberinto.obtenerCasilla((byte) (j - 1), (byte) i);
                        try {
                            g.conecta(generadorLaberinto.getPar((byte) j, (byte) i), generadorLaberinto.getPar((byte) (j - 1), (byte) i),
                            generadorLaberinto.obtenValor((byte) j, (byte) i) + generadorLaberinto.obtenValor((byte) (j - 1), (byte) i) + 1);
                        } catch (Exception e) {

                        }
                    } else {
                        entrada = generadorLaberinto.getPar((byte) j, (byte) i);
                    }
                }
                if ((c1 & 0b1000) == 0b0000 && i < height - 1) {
                    byte c2 = generadorLaberinto.obtenerCasilla((byte) j, (byte) (i + 1));
                    try {
                        g.conecta(generadorLaberinto.getPar((byte) j, (byte) i), generadorLaberinto.getPar((byte) j, (byte) (i + 1)), generadorLaberinto.obtenValor((byte) j, (byte) i) + generadorLaberinto.obtenValor((byte) j, (byte) (i + 1)) + 1);
                    } catch (Exception e) {

                    }
                }
            }
        }
        return g;
    }

    public Lista<VerticeGrafica<Integer>> resuelve() {
        Grafica<Integer> g = grafica();
        return g.dijkstra(entrada, salida);
    }

    public String toSVG() {
        StringBuilder s = new StringBuilder();
        s.append("<svg version='1.1' width='" + (generadorLaberinto.convertToUnsignedByte(width) * 10 + 4) + "' height='"
                + (generadorLaberinto.convertToUnsignedByte(height) * 10 + 4) + "'>\n");
        s.append("\t<rect x='2' y='2' width='" + (generadorLaberinto.convertToUnsignedByte(width) * 10) + "' height='"
                + (generadorLaberinto.convertToUnsignedByte(height) * 10) + "' fill='white' />\n");
        s.append("\t<!-- Lineas horizontales -->\n");
        for (int i = 0; i < generadorLaberinto.convertToUnsignedByte(height); i++) {
            int ini = 0;
            int fin;
            for (int j = 0; j <= generadorLaberinto.convertToUnsignedByte(width); j++) {
                if (j == generadorLaberinto.convertToUnsignedByte(width) || (casillas[i][j] & 0b0010) == 0b0000) {
                    fin = j;
                    if (ini != j)
                        s.append("\t<line x1='" + (2 + ini * 10) + "' y1='" + (2 + i * 10) + "' x2='" + (2 + fin * 10)
                                + "' y2='" + (2 + i * 10) + "' stroke='black' />\n");
                    ini = j + 1;
                }
            }
        }
        int i = generadorLaberinto.convertToUnsignedByte(height) - 1;
        int ini = 0;
        int fin;
        for (int j = 0; j <= generadorLaberinto.convertToUnsignedByte(width); j++) {
            if (j == generadorLaberinto.convertToUnsignedByte(width) || (casillas[i][j] & 0b1000) == 0b0000) {
                fin = j;
                if (ini != j)
                    s.append("\t<line x1='" + (2 + ini * 10) + "' y1='" + (2 + (i + 1) * 10) + "' x2='" + (2 + fin * 10)
                            + "' y2='" + (2 + (i + 1) * 10) + "' stroke='black' />\n");
                ini = j + 1;
            }
        }
        s.append("\t<!-- Lineas verticales -->\n");
        for (int j = 0; j < generadorLaberinto.convertToUnsignedByte(width); j++) {
            ini = 0;
            fin = -1;
            for (i = 0; i <= generadorLaberinto.convertToUnsignedByte(height); i++) {
                if (i == generadorLaberinto.convertToUnsignedByte(height) || (casillas[i][j] & 0b0100) == 0b0000) {
                    fin = i;
                    if (ini != i)
                        s.append("\t<line x1='" + (2 + j * 10) + "' y1='" + (2 + ini * 10) + "' x2='" + (2 + j * 10)
                                + "' y2='" + (2 + fin * 10) + "' stroke='black' />\n");
                    ini = i + 1;
                }
            }
        }
        int j = generadorLaberinto.convertToUnsignedByte(width) - 1;
        ini = 0;
        fin = -1;
        for (i = 0; i <= generadorLaberinto.convertToUnsignedByte(height); i++) {
            if (i == generadorLaberinto.convertToUnsignedByte(height) || (casillas[i][j] & 0b0001) == 0b0000) {
                fin = i;
                if (ini != i)
                    s.append("\t<line x1='" + (2 + (j + 1) * 10) + "' y1='" + (2 + ini * 10) + "' x2='"
                            + (2 + (j + 1) * 10) + "' y2='" + (2 + fin * 10) + "' stroke='black' />\n");
                ini = i + 1;
            }
        }
        s.append("\t<!-- Solución -->\n");
        Lista<VerticeGrafica<Integer>> p = resuelve();
        IteradorLista<VerticeGrafica<Integer>> it = p.iteradorLista();
        i = 0;
        int[] sol = new int[p.getElementos()];
        while (it.hasNext())
            sol[i++] = it.next().get();
        if (sol.length > 0)
            s.append("\t<line x1='" + (2) + "' y1='" + (generadorLaberinto.convertToUnsignedByte(generadorLaberinto.getAlto(sol[0])) * 10 + 5 + 2) + "' x2='"
                    + (generadorLaberinto.convertToUnsignedByte(generadorLaberinto.getAncho(sol[0])) * 10 + 5 + 2) + "' y2='"
                    + (generadorLaberinto.convertToUnsignedByte(generadorLaberinto.getAlto(sol[0])) * 10 + 5 + 2) + "'  stroke='green' />\n");
        for (i = 0; i < sol.length; i++) {
            int n = sol[i];
            if (i < sol.length - 1) {
                int n2 = sol[i + 1];
                s.append("\t<line x1='" + (generadorLaberinto.convertToUnsignedByte(generadorLaberinto.getAncho(n)) * 10 + 5 + 2) + "' y1='"
                        + (generadorLaberinto.convertToUnsignedByte(generadorLaberinto.getAlto(n)) * 10 + 5 + 2) + "' x2='" + (generadorLaberinto.convertToUnsignedByte(generadorLaberinto.getAncho(n2)) * 10 + 5 + 2)
                        + "' y2='" + (generadorLaberinto.convertToUnsignedByte(generadorLaberinto.getAlto(n2)) * 10 + 5 + 2) + "'  stroke='green' />\n");
            }
        }
        if (sol.length > 0)
            s.append("\t<line x1='" + (generadorLaberinto.convertToUnsignedByte(generadorLaberinto.getAncho(sol[sol.length - 1])) * 10 + 5 + 2) + "' y1='"
                    + (generadorLaberinto.convertToUnsignedByte(generadorLaberinto.getAlto(sol[sol.length - 1])) * 10 + 5 + 2) + "' x2='"
                    + (generadorLaberinto.convertToUnsignedByte(generadorLaberinto.getAncho(sol[sol.length - 1])) * 10 + 10 + 2) + "' y2='"
                    + (generadorLaberinto.convertToUnsignedByte(generadorLaberinto.getAlto(sol[sol.length - 1])) * 10 + 5 + 2) + "'  stroke='green' />\n");
        s.append("</svg>");
        return s.toString();
    }
}
