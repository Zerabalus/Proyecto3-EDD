package mx.unam.ciencias.edd;

/**
 * Clase para métodos estáticos con dispersores de bytes.
 */
public class Dispersores {

    /* Constructor privado para evitar instanciación. */
    private Dispersores() {}

    /**
     * Función de dispersión XOR.
     * @param llave la llave a dispersar.
     * @return la dispersión de XOR de la llave.
     */
    public static int dispersaXOR(byte[] llave) {
        // Aquí va su código.
        int r=0;
        int i=0;
        int l=llave.length;

        while (l>=4){
            r^= bytesSumados(llave[i], llave[i+1], llave[i+2], llave[i+3]);
            i+=4;
            l-=4;
        }

        int t=0;

        switch (l){
            case 3:
                t |= desplaza(llave[i+2],8);

            case 2:
                t |= desplaza(llave[i+1], 16);

            case 1:
                t |= desplaza(llave[i], 24);
        }

        return r^t;
    }

    /**
     * Función de dispersión de Bob Jenkins.
     * @param llave la llave a dispersar.
     * @return la dispersión de Bob Jenkins de la llave.
     */
    public static int dispersaBJ(byte[] llave) {
        // Aquí va su código.}
        
        /* Libro: Dispersión de Bob Jenkins. 20.5
         * se inicializan:
         * variables a, b y c a los valores 9E3779B9, 9E3779B9 y FFFFFFFF
         * respectivamente. El valor 9E3779B9 es la proporción áurea, pero realmente
         * es un valor arbitrario, lo mismo que FFFFFFFF.
         */

        int a = 0x9E3779B9;
        int b = 0x9E3779B9;
        int c = 0xFFFFFFFF;

        int i = 0;
        int l;
        int n = llave.length;
        l = n;

        int[] arr;

        while (l >= 12) {
            a += bytesSumados(llave[i + 3], llave[i + 2], llave[i + 1], llave[i]);
            b += bytesSumados(llave[i + 7], llave[i + 6], llave[i + 5], llave[i + 4]);
            c += bytesSumados(llave[i + 11], llave[i + 10], llave[i + 9], llave[i + 8]);

            arr = mezcla(a, b, c);
            a = arr[0];
            b = arr[1];
            c = arr[2];

            i += 12;
            l -= 12;
        }

        c += n;

        switch (l) {
            case 11:
                c += desplaza(llave[i + 10], 24);
            case 10:
                c += desplaza(llave[i + 9], 16);
            case 9:
                c += desplaza(llave[i + 8], 8);

            case 8:
                b += desplaza(llave[i + 7], 24);
            case 7:
                b += desplaza(llave[i + 6], 16);
            case 6:
                b += desplaza(llave[i + 5], 8);
            case 5:
                b += desplaza(llave[i + 4], 0);

            case 4:
                a += desplaza(llave[i + 3], 24);
            case 3:
                a += desplaza(llave[i + 2], 16);
            case 2:
                a += desplaza(llave[i + 1], 8);
            case 1:
                a += desplaza(llave[i], 0);
        }

        return mezcla(a, b, c)[2];
    }
    

    private static int bytesSumados(byte a, byte b, byte c, byte d){
        return ((a & 0xFF) << 24) | ((b & 0xFF) << 16) |
                ((c & 0xFF) << 8) | ((d & 0xFF));
    }

    /**
     * Función de dispersión Daniel J. Bernstein.
     * @param llave la llave a dispersar.
     * @return la dispersión de Daniel Bernstein de la llave.
     */
    public static int dispersaDJB(byte[] llave) {
        // Aquí va su código.
        /* Basado en el listado 20.6 del libro */
        int h= 5381;
        for (byte b : llave)
            h = (h*33) + desplaza(b, 0); //33 numero mágico

        return h;
    }


    /**
     * Algoritmo 20.5 mezcla
     * el algoritmo modifica su entrada; 
     * @return arreglo de bytes a, b y c actualizados (mezclados) entre ellos.
     */
    private static int[] mezcla(int a, int b, int c){

        int[] valores=new int[3];

        a -= b; a -= c; a ^= (c >>> 13);
        b -= c; b -= a; b ^= (a <<  8);
        c -= a; c -= b; c ^= (b >>> 13);
        a -= b; a -= c; a ^= (c >>> 12);
        b -= c; b -= a; b ^= (a <<  16);
        c -= a; c -= b; c ^= (b >>> 5);
        a -= b; a -= c; a ^= (c >>> 3);
        b -= c; b -= a; b ^= (a <<  10);
        c -= a; c -= b; c ^= (b >>> 15);

        valores[0]=a;
        valores[1]=b;
        valores[2]=c;

        return valores;
    }
    /**
     * 20.4 del libro, el valor de a se desplaza por n bits a la izquierda
     * Si n=0, se regresa el entero sin signo
     * 
     * @param a el byte a desplazar
     * @param n cantidad de bits para desplazar al byte
     * @return el entero resultante sin signo
     */
    private static int desplaza(byte a, int n){
        return (a & 0xFF) << n;
    }
    
}
