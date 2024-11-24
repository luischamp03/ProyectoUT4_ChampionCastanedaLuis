package masanz.juegorol.personajes;

import masanz.juegorol.items.Pocion;
import masanz.juegorol.util.Util;

public class Jugador {
    private String nombre;
    private int posX, posY;
    private int vida;
    private int energia;
    private boolean enCombate;
    private int enemigosDerrotados;
    private int pocionesVida, pocionesEnergia;

    // Constructor para inicializar al jugador con valores predeterminados
    public Jugador(String nombre) {
        this.nombre = nombre;
        this.posX = 1;
        this.posY = 1;
        this.vida = Util.MAX_VIDA;
        this.energia = Util.MAX_ENERGIA;
        this.enCombate = false;
        this.enemigosDerrotados = 0;
        this.pocionesVida = 0;
        this.pocionesEnergia = 0;
    }

    // Metodo para actualizar la posición del jugador según la tecla presionada
    public void actualizarPosicion(String tecla) {
        tecla = tecla.toUpperCase();
        switch (tecla) {
            case "W":
                this.posX--;
                break;
            case "A":
                this.posY--;
                break;
            case "S":
                this.posX++;
                break;
            case "D":
                this.posY++;
                break;
            default:
                break;
        }
    }

    // Metodo para realizar un ataque, devolviendo el daño causado
    public int realizarAtaque(String tipoAtaque) {
        tipoAtaque = tipoAtaque.toLowerCase();
        int valorDanio = 0, energiaImpacto = 0;

        switch (tipoAtaque) {
            case "potente":
                if (this.energia >= 10) {
                    valorDanio = Util.generarNumeroAleatorio(Util.VALOR_POTENTE_MIN, Util.VALOR_POTENTE_MAX);
                    energiaImpacto = 10;
                } else {
                    return -1;
                }
                break;
            case "medio":
                if (this.energia >= 5) {
                    valorDanio = Util.generarNumeroAleatorio(Util.VALOR_MEDIO_MIN, Util.VALOR_MEDIO_MAX);
                    energiaImpacto = 5;
                } else {
                    return -1;
                }
                break;
            case "simple":
                valorDanio = Util.generarNumeroAleatorio(Util.VALOR_SIMPLE_MIN, Util.VALOR_SIMPLE_MAX);
                energiaImpacto = -3;
                break;
            default:
                return -1;
        }

        this.energia -= energiaImpacto;

        // Ajusta la energía para que este dentro del rango permitido
        if (this.energia < 0) {
            this.energia = 0;
        } else if (this.energia > Util.MAX_ENERGIA) {
            this.energia = Util.MAX_ENERGIA;
        }

        return valorDanio;
    }

    // Metodo para usar una poción
    public int usarPocion(String tipo) {
        tipo = tipo.toLowerCase();
        int cantidadRecuperada = 0;

        switch (tipo) {
            case "vida":
                if (this.pocionesVida > 0) {
                    this.pocionesVida--;
                    cantidadRecuperada = Util.generarNumeroAleatorio(1, Util.PUNTOS_POCION_VIDA);
                    // Se calcula la cantidad de vida recuperada sin exceder el máximo
                    if (this.vida + cantidadRecuperada >= Util.MAX_VIDA) {
                        cantidadRecuperada = Util.MAX_VIDA - this.vida;
                        this.vida = Util.MAX_VIDA;
                    } else {
                        this.vida += cantidadRecuperada;
                    }
                    // Valor único para el efecto de vida
                    return Util.RANGO_BASE_VIDA + cantidadRecuperada;
                } else {
                    return -1;
                }
            case "energia":
                if (this.pocionesEnergia > 0) {
                    this.pocionesEnergia--;
                    cantidadRecuperada = Util.generarNumeroAleatorio(1, Util.PUNTOS_POCION_ENERGIA);
                    // Se calcula la cantidad de energia recuperada sin exceder el máximo
                    if (this.energia + cantidadRecuperada >= Util.MAX_ENERGIA) {
                        cantidadRecuperada = Util.MAX_ENERGIA - this.energia;
                        this.energia = Util.MAX_ENERGIA;
                    } else {
                        this.energia += cantidadRecuperada;
                    }
                    // Valor único para el efecto de energia
                    return Util.RANGO_BASE_ENERGIA + cantidadRecuperada;
                } else {
                    return -2;
                }
            default:
                return 0;
        }
    }

    // Metodo para recoger una poción
    public void recogerPocion(Pocion pocion) {
        if (pocion.getTipo().equals("vida")) {
            this.pocionesVida++;
        } else {
            this.pocionesEnergia++;
        }
    }

    // Metodo para mostrar informacion del jugador
    public void pintarInfo() {
        int maxDigitos = 3;
        int maxColVida = "VIDA: ".length();
        int maxColEnergia = "ENERGIA: ".length();
        int maxColPocVida = "POC. VIDA: ".length();
        int maxColPocEnergia = "POC. ENERGIA: ".length();
        String infoTop = "-".repeat(maxColVida + maxColEnergia + maxColPocVida + maxColPocEnergia + maxDigitos * 4 + 6) + "\n";

        System.out.printf(infoTop);
        System.out.printf("|  %s %-3d %s %-3d %s %-2d %s %-2d |\n", Util.textoColor("VIDA:", "AZUL"),
                this.vida, Util.textoColor("ENERGIA:", "MAGENTA"), this.energia, Util.textoColor("POC. VIDA:", "ROJO"), this.pocionesVida, Util.textoColor("POC. ENERGIA:", "VERDE"), this.pocionesEnergia);
        System.out.printf(infoTop);

    }

    // Getters y setters
    public String getNombre() {
        return this.nombre;
    }

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

    public int getEnemigosDerrotados() {
        return this.enemigosDerrotados;
    }

    public void setEnemigosDerrotados(int enemigosDerrotados) {
        this.enemigosDerrotados = enemigosDerrotados;
    }

    public int getPocionesVida() {
        return this.pocionesVida;
    }

    public int getPocionesEnergia() {
        return this.pocionesEnergia;
    }
}
