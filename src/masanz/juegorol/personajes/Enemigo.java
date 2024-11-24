package masanz.juegorol.personajes;

import masanz.juegorol.util.Util;

public class Enemigo {
    private int posX, posY;
    private int vida;
    private int energia;
    private boolean enCombate;

    // Constructor que inicializa la posición y asigna valores aleatorios a vida y energía
    public Enemigo(int posX, int posY) {
        this.posX = posX;
        this.posY = posY;
        this.enCombate = false;
        this.vida = Util.generarNumeroAleatorio(Util.MAX_VIDA / 2, Util.MAX_VIDA);
        this.energia = Util.generarNumeroAleatorio(Util.MAX_ENERGIA / 2, Util.MAX_ENERGIA);
    }

    // Constructor que permite especificar posición, vida y energía
    public Enemigo(int posX, int posY, int vida, int energia) {
        this.posX = posX;
        this.posY = posY;
        this.enCombate = false;

        // Valida y asigna la vida dentro del límite establecido
        if (vida > 0) {
            if (vida >= Util.MAX_VIDA) {
                this.vida = Util.MAX_VIDA;
            } else {
                this.vida = vida;
            }
        } else {
            this.vida = 0;
        }

        // Valida y asigna la energía dentro del límite establecido
        if (energia > 0) {
            if (energia >= Util.MAX_ENERGIA) {
                this.energia = Util.MAX_ENERGIA;
            } else {
                this.energia = energia;
            }
        } else {
            this.energia = 0;
        }
    }

    // Metodo para actualizar la posición del enemigo según una dirección
    public void actualizarPosicion(String haciaDonde) {
        switch (haciaDonde) {
            case "arriba":
                this.posX--;
                break;
            case "abajo":
                this.posX++;
                break;
            case "izquierda":
                this.posY--;
                break;
            case "derecha":
                this.posY++;
                break;
        }
    }

    // Metodo para realizar un ataque y calcular el daño infligido
    public int realizarAtaque() {
        int valorDanio = 0;
        int energiaImpacto = 0;

        boolean ataqueRealizado = false;

        // Se repite hasta que se realice un ataque valido
        while (!ataqueRealizado) {
            //El tipo de ataque se elige aleatoriamente
            int tipoAtaque = Util.generarNumeroAleatorio(1, 3);

            switch (tipoAtaque) {
                case 1:
                    if (this.energia >= 10) {
                        valorDanio = Util.generarNumeroAleatorio(Util.VALOR_POTENTE_MIN, Util.VALOR_POTENTE_MAX);
                        energiaImpacto = 10;
                        ataqueRealizado = true;
                    }
                    break;
                case 2:
                    if (this.energia >= 5) {
                        valorDanio = Util.generarNumeroAleatorio(Util.VALOR_MEDIO_MIN, Util.VALOR_MEDIO_MAX);
                        energiaImpacto = 5;
                        ataqueRealizado = true;

                    }
                    break;
                case 3:
                    valorDanio = Util.generarNumeroAleatorio(Util.VALOR_SIMPLE_MIN, Util.VALOR_SIMPLE_MAX);
                    energiaImpacto = -3;
                    ataqueRealizado = true;
                    break;
            }
        }

        // Actualiza la energía del enemigo tras el ataque
        this.energia -= energiaImpacto;

        // Ajusta la energía para que este dentro del rango permitido
        if (this.energia < 0) {
            this.energia = 0;
        } else if (this.energia > Util.MAX_ENERGIA) {
            this.energia = Util.MAX_ENERGIA;
        }

        return valorDanio;
    }

    // Metodo para reiniciar al enemigo en una nueva posición con vida y energía aleatorias
    public void respawn(int x, int y) {
        this.posX = x;
        this.posY = y;
        this.vida = Util.generarNumeroAleatorio(Util.MAX_VIDA / 2, Util.MAX_VIDA);
        this.energia = Util.generarNumeroAleatorio(Util.MAX_ENERGIA / 2, Util.MAX_ENERGIA);
    }

    // Getters y setters
    public int getPosX() {
        return this.posX;
    }

    public int getPosY() {
        return this.posY;
    }

    public int getVida() {
        return this.vida;
    }

    public void setVida(int vida) {
        this.vida = vida;
    }

    public int getEnergia() {
        return this.energia;
    }

    public boolean isEnCombate() {
        return this.enCombate;
    }

    public void setEnCombate(boolean enCombate) {
        this.enCombate = enCombate;
    }
}
