package masanz.juegorol.items;

import masanz.juegorol.util.Util;

public class Pocion {
    // Coordenadas de la poción en el mapa
    private int posX, posY;
    private String tipo;

    // Constructor que crea una poción en una posición específica y asigna un tipo aleatorio
    public Pocion(int posX, int posY) {
        int tipoAleatorio = Util.generarNumeroAleatorio(0, 1);

        if (tipoAleatorio == 0) {
            this.tipo = "energia";
        } else {
            this.tipo = "vida";
        }

        this.posX = posX;
        this.posY = posY;
    }

    // Constructor que permite especificar el tipo y la posición de la poción
    public Pocion(String tipo, int posX, int posY) {
        this.tipo = tipo;
        this.posX = posX;
        this.posY = posY;
    }

    // Metodo para hacer que la poción tenga una nueva posición con un nuevo tipo
    public void respawn(int x, int y) {
        int tipoAleatorio = Util.generarNumeroAleatorio(0, 1);
        if (tipoAleatorio == 0) {
            this.tipo = "vida";
        } else {
            this.tipo = "energia";
        }
        this.posX = x;
        this.posY = y;
    }

    // Getters

    public int getPosX() {
        return this.posX;
    }

    public int getPosY() {
        return this.posY;
    }

    public String getTipo() {
        return this.tipo;
    }
}
