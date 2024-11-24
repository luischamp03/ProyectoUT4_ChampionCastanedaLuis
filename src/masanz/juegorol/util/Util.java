package masanz.juegorol.util;

public class Util {
    // Constantes de los valores del juego
    public static final int MAX_VIDA = 100;                 // Vida máxima de un jugador o enemigo
    public static final int MAX_ENERGIA = 130;              // Energía máxima de un jugador o enemigo
    public static final int PUNTOS_POCION_VIDA = 50;        // Puntos de vida máximos restaurados por una poción
    public static final int PUNTOS_POCION_ENERGIA = 45;     // Puntos de energía máximos restaurados por una poción
    public static final int RANGO_BASE_VIDA = 100;          // Base para calcular rangos de recuperación de vida
    public static final int RANGO_BASE_ENERGIA = 200;       // Base para calcular rangos de recuperación de energía

    // Constantes para los rangos de daño al realizar un ataque
    public static final int VALOR_POTENTE_MIN = 4;
    public static final int VALOR_POTENTE_MAX = 20;
    public static final int VALOR_MEDIO_MIN = 2;
    public static final int VALOR_MEDIO_MAX = 12;
    public static final int VALOR_SIMPLE_MIN = 1;
    public static final int VALOR_SIMPLE_MAX = 6;

    public static final double CRITICO_PROBABILIDAD = 0.15; // Probabilidad de realizar un golpe crítico
    public static final double PIFIA_PROBABILIDAD = 0.10;   // Probabilidad de fallar un ataque

    // Códigos de colores
    public static final String RESET = "\u001B[0m";
    public static final String AZUL = "\u001B[34m";
    public static final String ROJO = "\u001B[31m";
    public static final String VERDE = "\u001B[32m";
    public static final String MAGENTA = "\u001B[35m";

    // Metodo estatico para determinar si ocurre una pifia
    public static boolean hayPifia() {
        if (Math.random() < PIFIA_PROBABILIDAD) {
            return true;
        } else {
            return false;
        }
    }

    // Metodo estatico para determinar si ocurre un golpe crítico
    public static boolean hayCritico() {
        if (Math.random() < CRITICO_PROBABILIDAD) {
            return true;
        } else {
            return false;
        }
    }

    // Genera un número aleatorio dentro de un rango especificado
    public static int generarNumeroAleatorio(int valorMin, int valorMax) {
        return (int) (Math.random() * (valorMax - valorMin + 1)) + valorMin;
    }

    // Metodo para simular un refresco de pantalla
    public static void refrescoPantalla() {
        for (int i = 0; i < 40; i++) {
            System.out.println();
        }
    }

    // Metodo para colorear texto en la consola
    public static String textoColor(String texto, String color) {
        color = color.toUpperCase();
        String colorElegido = RESET;

        switch (color) {
            case "AZUL":
                colorElegido = AZUL;
                break;
            case "ROJO":
                colorElegido = ROJO;
                break;
            case "VERDE":
                colorElegido = VERDE;
                break;
            case "MAGENTA":
                colorElegido = MAGENTA;
                break;
            default:
                colorElegido = RESET;
        }

        return colorElegido + texto + RESET;
    }

}
