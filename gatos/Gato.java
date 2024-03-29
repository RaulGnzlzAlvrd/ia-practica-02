/*
 * No redistribuir.
 */
package gatos;

import java.util.LinkedList;

/**
 * Clase para representar un estado del juego del gato. Cada estado sabe cómo
 * generar a sus sucesores.
 *
 * @author Vero
 */
public class Gato {

    public static final int MARCA1 = 1;             // Número usado en el tablero del gato para marcar al primer jugador.
    public static final int MARCA2 = 4;             // Se usan int en lugar de short porque coincide con el tamaÃ±o de la palabra, el código se ejecuta ligeramente más rápido.

    int[][] tablero = new int[3][3];     // Tablero del juego
    Gato padre;                          // Quién generó este estado.
    LinkedList<Gato> sucesores;          // Posibles jugadas desde este estado.
    boolean jugador1 = false;            // Jugador que tiró en este tablero.
    boolean hayGanador = false;          // Indica si la última tirada produjo un ganador.
    int tiradas = 0;                     // Número de casillas ocupadas.

    /**
     * Constructor del estado inicial.
     */
    Gato() {
    }

    /**
     * Constructor que copia el tablero de otro gato y el número de tiradas
     */
    Gato(Gato g) {
        for (int y = 0; y < 3; y++) {
            for (int x = 0; x < 3; x++) {
                tablero[y][x] = g.tablero[y][x];
            }
        }
        tiradas = g.tiradas;
    }

    /**
     * Indica si este estado tiene sucesores expandidos.
     */
    int getNumHijos() {
        if (sucesores != null) {
            return sucesores.size();
        } else {
            return 0;
        }
    }

    /* Función auxiliar.
     * Dada la última posición en la que se tiró y la marca del jugador
     * calcula si esta jugada produjo un ganador y actualiza el atributo correspondiente.
     * 
     * Esta función debe ser lo más eficiente posible para que la generación del árbol no sea demasiado lenta.
     */
    void hayGanador(int x, int y, int marca) {
        // Horizontal
        if (tablero[y][(x + 1) % 3] == marca && tablero[y][(x + 2) % 3] == marca) {
            hayGanador = true;
            return;
        }
        // Vertical
        if (tablero[(y + 1) % 3][x] == marca && tablero[(y + 2) % 3][x] == marca) {
            hayGanador = true;
            return;
        }
        // Diagonal
        if ((x == 1 && y != 1) || (y == 1 && x != 1)) {
            return; // No pueden hacer diagonal
        }      
        // Centro y esquinas
        if (x == 1 && y == 1) {
            // Diagonal \
            if (tablero[0][0] == marca && tablero[2][2] == marca) {
                hayGanador = true;
                return;
            }
            if (tablero[2][0] == marca && tablero[0][2] == marca) {
                hayGanador = true;
                return;
            }
        } else if (x == y) {
            // Diagonal \
            if (tablero[(y + 1) % 3][(x + 1) % 3] == marca && tablero[(y + 2) % 3][(x + 2) % 3] == marca) {
                hayGanador = true;
                return;
            }
        } else {
            // Diagonal /
            if (tablero[(y + 2) % 3][(x + 1) % 3] == marca && tablero[(y + 1) % 3][(x + 2) % 3] == marca) {
                hayGanador = true;
                return;
            }
        }
    }

    /* Función auxiliar.
     * Coloca la marca del jugador en turno para este estado en las coordenadas indicadas.
     * Asume que la casilla está libre.
     * Coloca la marca correspondiente, verifica y asigna la variable si hay un ganador.
     */
    void tiraEn(int x, int y) {
        tiradas++;
        int marca = (jugador1) ? MARCA1 : MARCA2;
        tablero[y][x] = marca;
        hayGanador(x, y, marca);
    }

    /**
     * ------- *** ------- *** -------
     * Este es el método que se deja como práctica.
     * ------- *** ------- *** -------
     * Crea la lista sucesores y 
     * agrega a todos los estados que surjen de tiradas válidas. Se consideran
     * tiradas válidas a aquellas en una casilla libre. Además, se optimiza el
     * proceso no agregando estados con jugadas simétricas. Los estados nuevos
     * tendrán una tirada más y el jugador en turno será el jugador
     * contrario.
     */
    LinkedList<Gato> generaSucesores() {
        if (hayGanador || tiradas == 9) {
            return null; // Es un estado meta.
        }
        sucesores = new LinkedList<>();
        
        // TODO: Tu código aquí.
        for (int y = 0; y < 3; y++) {
            for (int x = 0; x < 3; x++) {
                if (this.tablero[y][x] == 0) {
                    Gato sucesor = new Gato(this);
                    sucesor.padre = this;
                    sucesor.jugador1 = !this.jugador1;
                    sucesor.tiraEn(x, y);
                    if (!sucesores.contains(sucesor)) {
	                    sucesores.add(sucesor);
                    }
                }
            }
        }
        
        return sucesores;
    }

    // ------- *** ------- *** -------
    // Serie de funciones que revisan la equivalencia de estados considerando las simetrías de un cuadrado.
    // ------- *** ------- *** -------
    // http://en.wikipedia.org/wiki/Examples_of_groups#The_symmetry_group_of_a_square_-_dihedral_group_of_order_8
    // ba es reflexion sobre / y ba3 reflexion sobre \.
    
    /**
     * Revisa si ambos gatos son exactamente el mismo.
     */
    boolean esIgual(Gato otro) {
        for (int y = 0; y < 3; y++) {
            for (int x = 0; x < 3; x++) {
                if (tablero[y][x] != otro.tablero[y][x]) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Al reflejar el gato sobre la diagonal \ son iguales (ie traspuesta)
     */
    boolean esSimetricoDiagonalInvertida(Gato otro) {
        
        // TODO
        for (int y = 0; y < 3; y++) {
            for (int x = 0; x < 3; x++) {
                if (this.tablero[y][x] != otro.tablero[x][y]) {
                    return false;
                }
            }
        }
        
        return true;
    }

    /**
     * Al reflejar el gato sobre la diagonal / son iguales (ie traspuesta)
     */
    boolean esSimetricoDiagonal(Gato otro) {
        
        // TODO
        int[][] matriz = {
        	{2, 1, 0}, 
        	{1, 0, 2}, 
        	{0, 2, 1}
        };

        for (int y = 0; y < 3; y++) {
            for (int x = 0; x < 3; x++) {
            	int coordX = (x + matriz[x][y]) % 3;
            	int coordY = (y + matriz[x][y]) % 3;

                if (this.tablero[y][x] != otro.tablero[coordY][coordX]) {
                    return false;
                }
            }
        }

        return true;
    }

    /**
     * Al reflejar el otro gato sobre la vertical son iguales
     */
    boolean esSimetricoVerticalmente(Gato otro) {
        
        // TODO
        int[] matriz = {2, 0, 1};

        for (int y = 0; y < 3; y++) {
            for (int x = 0; x < 3; x++) {
            	int coordX = (x + matriz[x]) % 3;

                if (this.tablero[y][x] != otro.tablero[y][coordX]) {
                    return false;
                }
            }
        }
        
        return true;
    }

    /**
     * Al reflejar el otro gato sobre la horizontal son iguales
     */
    boolean esSimetricoHorizontalmente(Gato otro) {
        
        // TODO
        int[] matriz = {2, 0, 1};

        for (int y = 0; y < 3; y++) {
            for (int x = 0; x < 3; x++) {
            	int coordY = (y + matriz[y]) % 3;

                if (this.tablero[y][x] != otro.tablero[coordY][x]) {
                    return false;
                }
            }
        }
        
        return true;
    }

    /** Regresa un tablero girado 90 grados en dirección de las manecillas del reloj.
     * @return Gato girado 90 grados con respecto al actual (this).
     */
    Gato gira90() {
        Gato girado = new Gato(this);
        int[][][] matriz = {
            {{2,0}, {1,0}, {0,0}},
            {{2,1}, {1,1}, {0,1}},
            {{2,2}, {1,2}, {0,2}}
        };
        for (int x = 0; x < 3; x++) {
            for (int y = 0; y < 3; y++) {
                int[] coordenadas = matriz[x][y];
                girado.tablero[coordenadas[1]][coordenadas[0]] = this.tablero[y][x];
            }
        }

        return girado;
    }

    /** Regresa un tablero girado 180 grados en dirección de las manecillas del reloj.
     * @return Gato girado 180 grados con respecto al actual (this).
     */
    Gato gira180() {
        return this.gira90().gira90();
    }

    /** Regresa un tablero girado 270 grados en dirección de las manecillas del reloj.
     * @return Gato girado 270 grados con respecto al actual (this).
     */
    Gato gira270() {
        return this.gira180().gira90();
    }

    /**
     * Al rotar el otro tablero 90Â° en la dirección de las manecillas del reloj son iguales.
     */
    boolean esSimetrico90(Gato otro) {
        // TODO
        return this.esIgual(otro.gira90());
    }

    /**
     * Al rotar el otro tablero 180Â° en la dirección de las manecillas del reloj son iguales.
     */
    boolean esSimetrico180(Gato otro) {
        // TODO
        return this.esIgual(otro.gira180());
    }

    /**
     * Al rotar el otro tablero 270Â° en la dirección de las manecillas del reloj son iguales.
     */
    boolean esSimetrico270(Gato otro) {
        // TODO
        return this.esIgual(otro.gira270());
    }

    /**
     * Indica si dos estados del juego del gato son iguales, considerando
     * simetrías, de este modo el problema se vuelve manejable.
     */
    @Override
    public boolean equals(Object o) {
        Gato otro = (Gato) o;
        if (esIgual(otro)) {
            return true;
        }
        if (esSimetricoDiagonalInvertida(otro)) {
            return true;
        }
        if (esSimetricoDiagonal(otro)) {
            return true;
        }
        if (esSimetricoVerticalmente(otro)) {
            return true;
        }
        if (esSimetricoHorizontalmente(otro)) {
            return true;
        }
        if (esSimetrico90(otro)) {
            return true;
        }
        if (esSimetrico180(otro)) {
            return true;
        }
        if (esSimetrico270(otro)) {
            return true; // No redujo el diámetro máximo al agregarlo
        }
        return false;
    }

    /**
     * Devuelve una representación con caracteres de este estado. Se puede usar
     * como auxiliar al probar segmentos del código.
     */
    @Override
    public String toString() {
        char simbolo = jugador1 ? 'o' : 'x';
        String gs = "";
        for (int y = 0; y < 3; y++) {
            for (int x = 0; x < 3; x++) {
                gs += tablero[y][x] + " ";
            }
            gs += '\n';
        }
        return gs;
    }
}
