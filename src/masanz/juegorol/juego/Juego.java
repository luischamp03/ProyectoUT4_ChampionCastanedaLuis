package masanz.juegorol.juego;

import masanz.juegorol.items.Pocion;
import masanz.juegorol.personajes.Enemigo;
import masanz.juegorol.personajes.Jugador;
import masanz.juegorol.util.Util;

import java.util.Scanner;

public class Juego {
    private Jugador jugador;
    private Enemigo enemigo1, enemigo2, enemigo3;
    private Pocion pocion1, pocion2, pocion3;
    private int altura;
    private int anchura;

    // Constructor de la clase Juego, recibe un jugador y las dimensiones del mapa
    public Juego(Jugador jugador, int altura, int anchura) {
        this.jugador = jugador;
        this.altura = altura;
        this.anchura = anchura;
        this.enemigo1 = new Enemigo(1, 4);
        this.enemigo2 = new Enemigo(3, 7, Util.MAX_VIDA / 2, Util.MAX_ENERGIA / 2);
        this.enemigo3 = new Enemigo(5, 2, Util.MAX_VIDA / 2, Util.MAX_ENERGIA / 2);
        this.pocion1 = new Pocion(4, 5);
        this.pocion2 = new Pocion(1, 9);
        this.pocion3 = new Pocion("vida", 2, 3);
        // Se muestra el mapa inicial
        this.mostrarMapa();
    }

    // Metodo para realizar una acción en el mapa inicial o el de combate
    public void realizarAccion() {
        Scanner sc = new Scanner(System.in);
        System.out.print("¿Que accion quieres realizar? ");
        String accion = sc.nextLine();
        accion = accion.toUpperCase();

        switch (accion) {
            case "W":
            case "A":
            case "S":
            case "D":
                // Si no está en combate, se actualiza la posición del jugador
                if (!this.jugador.isEnCombate()) {
                    this.actualizarPosiciones(accion);
                } else {
                    this.actualizarCombate(0);
                }
                break;
            case "1":
                this.actualizarCombate(1);
                break;
            case "2":
                this.actualizarCombate(2);
                break;
            case "3":
                this.actualizarCombate(3);
                break;
            case "V":
                int valorVida = this.jugador.usarPocion("vida");
                if (this.jugador.isEnCombate()) {
                    this.actualizarCombate(valorVida);
                }
                break;
            case "E":
                int valorEnergia = this.jugador.usarPocion("energia");
                if (this.jugador.isEnCombate()) {
                    this.actualizarCombate(valorEnergia);
                }
                break;
            default:
                if (this.jugador.isEnCombate()) {
                    this.actualizarCombate(0);
                }
                break;
        }
    }

    // Metodo para actualizar las posiciones del jugador, enemigos y se verifica si se recogen pociones o si se entra en combate
    public void actualizarPosiciones(String tecla) {
        if (this.jugador.isEnCombate()) {
            // Si el jugador esta en combate, no se puede mover
            return;
        }
        // Se actualizan las coordenadas del jugador según la tecla ingresada
        int x = this.jugador.getPosX();
        int y = this.jugador.getPosY();

        switch (tecla) {
            case "W":
                x--;
                break;
            case "A":
                y--;
                break;
            case "S":
                x++;
                break;
            case "D":
                y++;
                break;
        }

        // Verificar si el jugador recoge alguna poción al moverse
        this.verificarRecoleccionPocion(x, y, this.pocion1);
        this.verificarRecoleccionPocion(x, y, this.pocion2);
        this.verificarRecoleccionPocion(x, y, this.pocion3);

        // Comprobar si la nueva posición es válida
        if (this.validarPosicion(x, y)) {
            this.jugador.actualizarPosicion(tecla);
        }

        // Mover a los enemigos y comprobar si el jugador entra en combate con ellos
        this.moverEnemigo(this.enemigo1);
        if (this.verificarCombate(this.jugador, this.enemigo1)) {
            this.procesarCombate(this.jugador, this.enemigo1);
        }

        this.moverEnemigo(this.enemigo2);
        if (this.verificarCombate(this.jugador, this.enemigo2)) {
            this.procesarCombate(this.jugador, this.enemigo2);
        }

        this.moverEnemigo(this.enemigo3);
        if (this.verificarCombate(this.jugador, this.enemigo3)) {
            this.procesarCombate(this.jugador, this.enemigo3);
        }
    }

    public void actualizarCombate(int tipoAtaque) {
        Enemigo enemigo = this.getEnemigo();
        // Mientras el jugador y el enemigo tengan vida, continúa el combate
        while (this.jugador.getVida() > 0 && enemigo.getVida() > 0) {
            int danioJugador = 0;
            String mensajeJugador = "", mensajeEnemigo = "", mensajePifiaOCritico = "";
            String ataqueJugador = "ATAQUE BASICO";

            // Se verifican las condiciones en las que no hay pociones de vida o energía
            if (tipoAtaque == -1) {
                mensajeJugador = "¡El jugador no tienes pociones de " + Util.textoColor("VIDA", "ROJO") + "!";
            } else if (tipoAtaque == -2) {
                mensajeJugador = "¡El jugador no tienes pociones de " + Util.textoColor("ENERGIA", "VERDE") + "!";
                // Si el ataque está dentro del rango de vida, recuperamos vida
            } else if (tipoAtaque >= Util.RANGO_BASE_VIDA && tipoAtaque < Util.RANGO_BASE_ENERGIA) {
                mensajeJugador = "El jugador ha recuperado " + "[" + Util.textoColor(Integer.toString(tipoAtaque - Util.RANGO_BASE_VIDA), "ROJO") + "]" + " puntos de vida.";
                mensajeEnemigo = this.realizarAtaqueEnemigo(enemigo);
                // Si el ataque está en el rango de energía, recuperamos energía
            } else if (tipoAtaque >= Util.RANGO_BASE_ENERGIA) {
                mensajeJugador = "El jugador ha recuperado " + "[" + Util.textoColor(Integer.toString(tipoAtaque - Util.RANGO_BASE_ENERGIA), "VERDE") + "]" + " puntos de energia.";
                mensajeEnemigo = this.realizarAtaqueEnemigo(enemigo);
                // Si el ataque es mayor a cero, ejecutamos el tipo de ataque
            } else if (tipoAtaque > 0) {
                switch (tipoAtaque) {
                    case 1:
                        ataqueJugador = "ATAQUE BASICO";
                        danioJugador = this.jugador.realizarAtaque("simple");
                        break;
                    case 2:
                        ataqueJugador = "ATAQUE MEDIO";
                        danioJugador = this.jugador.realizarAtaque("medio");
                        break;
                    case 3:
                        ataqueJugador = "ATAQUE POTENTE";
                        danioJugador = this.jugador.realizarAtaque("potente");
                        break;
                }

                if (danioJugador != -1) {
                    // Se verifica si hay pifia o crítico
                    if (Util.hayPifia()) {
                        danioJugador = 0;
                        mensajePifiaOCritico = " ¡que pifia!";
                    } else if (Util.hayCritico()) {
                        danioJugador *= 2;
                        mensajePifiaOCritico = " ¡menudo critico!";
                    }
                    // Mensaje que indica el daño del jugador
                    mensajeJugador += "El jugador ha realizado un " + Util.textoColor(ataqueJugador, "AZUL") + " y ha hecho [" + Util.textoColor(Integer.toString(danioJugador), "ROJO") + "] de daño" + mensajePifiaOCritico;

                    // Si el daño del jugador es suficiente para vencer al enemigo, se derrota al enemigo
                    if (enemigo.getVida() - danioJugador <= 0) {
                        enemigo.setVida(0);
                        this.jugador.setEnemigosDerrotados(this.jugador.getEnemigosDerrotados() + 1);
                        // Se termina el combate
                        return;
                    } else {
                        enemigo.setVida(enemigo.getVida() - danioJugador);
                    }

                    mensajeEnemigo = this.realizarAtaqueEnemigo(enemigo);
                } else {
                    mensajeJugador = "El jugador no tiene suficiente energia para hacer el " + Util.textoColor(ataqueJugador, "AZUL") + ". Elige otro ataque.";
                }
            }

            if (this.jugador.getVida() <= 0 || enemigo.getVida() <= 0) {
                return;
            }

            // Se muestra el estado del combate después de cada turno
            this.mostrarMapaCombate(enemigo, mensajeJugador, mensajeEnemigo);
            System.out.println();
            // Se permite al jugador realizar otra acción
            this.realizarAccion();
        }
    }

    // Metodo para validar si la posición X e Y es válida en el mapa
    public boolean validarPosicion(int x, int y) {
        for (int i = 1; i <= this.altura; i++) {
            for (int j = 1; j <= this.anchura; j++) {
                // Si la posición coincide con la del jugador, enemigos o pociones no es válida
                if (x == this.jugador.getPosX() && y == this.jugador.getPosY()) {
                    return false;
                } else if (x == this.enemigo1.getPosX() && y == this.enemigo1.getPosY() ||
                        x == this.enemigo2.getPosX() && y == this.enemigo2.getPosY() ||
                        x == this.enemigo3.getPosX() && y == this.enemigo3.getPosY()) {
                    return false;
                } else if (x == this.pocion1.getPosX() && y == this.pocion1.getPosY() ||
                        x == this.pocion2.getPosX() && y == this.pocion2.getPosY() ||
                        x == this.pocion3.getPosX() && y == this.pocion3.getPosY()) {
                    return false;
                    // Si está fuera del mapa, la posición no es válida
                } else if (x < 1 || y < 1 || x > this.altura || y > this.anchura) {
                    return false;
                } else {
                    return true;
                }
            }
        }
        // Si no se encuentra ninguna razón para invalidar la posición, entonces esta es válida
        return true;
    }

    // Metodo para comprobar si la posicion X e Y se encuentra como minimo a 3 cuadriculas de la posicion del jugador
    public boolean estaLejosDelJugador(int x, int y) {
        int distanciaX = Math.abs(x - this.jugador.getPosX());
        int distanciaY = Math.abs(y - this.jugador.getPosY());

        // Si la distancia en cualquier eje es mayor o igual a 4, se considera válida
        if (distanciaX >= 4 || distanciaY >= 4) {
            return true;
        }
        return false;
    }

    public void mostrarMapa() {
        // Bucle donde se muestra el mapa mientras el jugador esté vivo
        while (this.jugador.getVida() > 0) {
            Util.refrescoPantalla();
            this.jugador.pintarInfo();
            // Recorremos el mapa para dibujar cada celda
            for (int i = 1; i <= this.altura; i++) {
                System.out.println("----".repeat(this.anchura) + "-");
                for (int j = 1; j <= this.anchura; j++) {
                    if (i == this.jugador.getPosX() && j == this.jugador.getPosY()) {
                        if (j == this.anchura) {
                            System.out.printf("| %s |", Util.textoColor("J", "AZUL"));
                        } else {
                            System.out.printf("| %s ", Util.textoColor("J", "AZUL"));
                        }
                    } else if (i == this.enemigo1.getPosX() && j == this.enemigo1.getPosY() ||
                            i == this.enemigo2.getPosX() && j == this.enemigo2.getPosY() ||
                            i == this.enemigo3.getPosX() && j == this.enemigo3.getPosY()) {
                        if (j == this.anchura) {
                            System.out.printf("| %s |", Util.textoColor("E", "ROJO"));
                        } else {
                            System.out.printf("| %s ", Util.textoColor("E", "ROJO"));
                        }
                    } else if (i == this.pocion1.getPosX() && j == this.pocion1.getPosY() ||
                            i == this.pocion2.getPosX() && j == this.pocion2.getPosY() ||
                            i == this.pocion3.getPosX() && j == this.pocion3.getPosY()) {
                        if (j == this.anchura) {
                            System.out.printf("| %s |", Util.textoColor("P", "VERDE"));
                        } else {
                            System.out.printf("| %s ", Util.textoColor("P", "VERDE"));
                        }
                    } else {
                        if (j == this.anchura) {
                            System.out.printf("| %s |", " ");
                        } else {
                            System.out.printf("| %s ", " ");
                        }
                    }
                }
                System.out.println();
                if (i == this.altura) {
                    System.out.println("----".repeat(this.anchura) + "-");
                }
            }

            // Se muestran acciones que se pueden realizar
            System.out.println();
            System.out.printf("%-12s%-16s%-11s%-10s", "W=Arriba", "A=Izquierda", "S=Abajo", "D=Derecha");
            System.out.println();
            System.out.printf("%-28s%-30s", "V=Tomar pocion de vida", "E=Tomar pocion de energia");
            System.out.println();
            System.out.println();
            this.realizarAccion();
        }
    }

    private void mostrarMapaCombate(Enemigo enemigo, String mensajeJugador, String mensajeEnemigo) {
        Util.refrescoPantalla();
        System.out.println("**************************************");
        System.out.println("* ESTAS EN COMBATE CONTRA UN ENEMIGO *");
        System.out.println("**************************************");
        System.out.println();

        // Se muestran estadísticas del jugador y del enemigo
        int vidaJugador = this.jugador.getVida(), vidaEnemigo = enemigo.getVida();
        int energiaJugador = this.jugador.getEnergia(), energiaEnemigo = enemigo.getEnergia();
        int pocionesVJugador = this.jugador.getPocionesVida(), pocionesVEnemigo = 0;
        int pocionesEJugador = this.jugador.getPocionesEnergia(), pocionesEEnemigo = 0;

        System.out.printf("%-10s %-10s %2s %-14s %n", " ", Util.textoColor("JUGADOR", "AZUL"), " ", Util.textoColor("ENEMIGO", "ROJO"));

        System.out.printf("%s%-18s %s%-8d %s%8d %n", "", Util.textoColor("VIDA:", "AZUL"), " ", vidaJugador, " ", vidaEnemigo);
        System.out.printf("%s%-18s %s%-8d %s%8d %n", "", Util.textoColor("ENERGIA:", "MAGENTA"), " ", energiaJugador, " ", energiaEnemigo);
        System.out.printf("%s%-18s %s%-8d %s%8d %n", "", Util.textoColor("POC. V.:", "ROJO"), " ", pocionesVJugador, " ", pocionesVEnemigo);
        System.out.printf("%s%-18s %s%-8d %s%8d %n", "", Util.textoColor("POC. E.:", "VERDE"), " ", pocionesEJugador, " ", pocionesEEnemigo);

        System.out.println();

        // Se muestran mensajes de combate
        if (!mensajeJugador.isEmpty()) {
            System.out.println(mensajeJugador);
            if (!mensajeEnemigo.isEmpty()) {
                System.out.println(mensajeEnemigo);
            }
            System.out.println();
        }

        // Se muestran acciones que se pueden realizar
        System.out.printf("%-25s%-25s%-11s", "1=Ataque basico (+3E)", "2=Ataque Medio (-5E)", "3=Ataque Potente (-10E)");
        System.out.println();
        System.out.printf("%-28s%-30s", "V=Tomar pocion de vida", "E=Tomar pocion de energia");
        System.out.println();
    }

    private Enemigo getEnemigo() {
        Enemigo enemigo = this.enemigo1;
        // Se determina qué enemigo está en combate actualmente
        if (this.enemigo1.isEnCombate() && !this.enemigo2.isEnCombate() && !this.enemigo3.isEnCombate()) {
            enemigo = this.enemigo1;
        } else if (this.enemigo2.isEnCombate() && !this.enemigo1.isEnCombate() && !this.enemigo3.isEnCombate()) {
            enemigo = this.enemigo2;
        } else if (this.enemigo3.isEnCombate() && !this.enemigo1.isEnCombate() && !this.enemigo2.isEnCombate()) {
            enemigo = this.enemigo3;
        } else {
            enemigo = this.enemigo1;
        }
        return enemigo;
    }

    private void verificarRecoleccionPocion(int x, int y, Pocion pocion) {
        // Si las coordenadas coinciden con la posición de la poción, entonces el jugador recoge la pocion
        if (x == pocion.getPosX() && y == pocion.getPosY()) {
            this.jugador.recogerPocion(pocion);

            boolean posicionValida = false;
            while (!posicionValida) {
                int nuevaPosX = Util.generarNumeroAleatorio(1, this.altura);
                int nuevaPosY = Util.generarNumeroAleatorio(1, this.anchura);
                // Se verifica que la nueva posición sea válida y este lejos del jugador
                if (this.estaLejosDelJugador(nuevaPosX, nuevaPosY) && this.validarPosicion(nuevaPosX, nuevaPosY)) {
                    pocion.respawn(nuevaPosX, nuevaPosY);
                    posicionValida = true;
                }
            }
        }
    }

    private void moverEnemigo(Enemigo enemigo) {
        int posXEnemigo = enemigo.getPosX();
        int posYEnemigo = enemigo.getPosY();
        String haciaDonde = "arriba";
        // Se generan nuevas coordenadas hasta que sean válidas
        while (!this.validarPosicion(posXEnemigo, posYEnemigo)) {
            posXEnemigo = enemigo.getPosX();
            posYEnemigo = enemigo.getPosY();
            int opcion = Util.generarNumeroAleatorio(1, 4);
            switch (opcion) {
                case 1:
                    posXEnemigo--;
                    haciaDonde = "arriba";
                    break;
                case 2:
                    posXEnemigo++;
                    haciaDonde = "abajo";
                    break;
                case 3:
                    posYEnemigo--;
                    haciaDonde = "izquierda";
                    break;
                case 4:
                    posYEnemigo++;
                    haciaDonde = "derecha";
                    break;
                default:
                    posXEnemigo--;
                    haciaDonde = "arriba";
                    break;
            }
        }
        enemigo.actualizarPosicion(haciaDonde);
    }

    private boolean verificarCombate(Jugador jugador, Enemigo enemigo) {
        // Comprueba si el enemigo está en cualquiera de las posiciones adyacentes al jugador
        return (jugador.getPosX() + 1 == enemigo.getPosX() && jugador.getPosY() == enemigo.getPosY()) ||
                (jugador.getPosX() - 1 == enemigo.getPosX() && jugador.getPosY() == enemigo.getPosY()) ||
                (jugador.getPosY() + 1 == enemigo.getPosY() && jugador.getPosX() == enemigo.getPosX()) ||
                (jugador.getPosY() - 1 == enemigo.getPosY() && jugador.getPosX() == enemigo.getPosX());
    }

    private String realizarAtaqueEnemigo(Enemigo enemigo) {
        // El enemigo realiza su ataque
        int energiaAntesDeAtaque = enemigo.getEnergia();
        int danioEnemigo = enemigo.realizarAtaque();
        int energiaDespuesDeAtaque = enemigo.getEnergia();
        String ataqueEnemigo = "ATAQUE BASICO", mensajePifiaOCritico = "";

        // Se determina el tipo de ataque del enemigo en función de su energía
        if (energiaDespuesDeAtaque - energiaAntesDeAtaque == -10) {
            ataqueEnemigo = "ATAQUE POTENTE";
        } else if (energiaDespuesDeAtaque - energiaAntesDeAtaque == -5) {
            ataqueEnemigo = "ATAQUE MEDIO";
        } else if (energiaDespuesDeAtaque - energiaAntesDeAtaque >= 0) {
            ataqueEnemigo = "ATAQUE BASICO";
        }

        if (Util.hayPifia()) {
            danioEnemigo = 0;
            mensajePifiaOCritico = " ¡que pifia!";
        } else if (Util.hayCritico()) {
            danioEnemigo *= 2;
            mensajePifiaOCritico = " ¡menudo critico!";
        }

        if (this.jugador.getVida() - danioEnemigo <= 0) {
            this.jugador.setVida(0);
        } else {
            this.jugador.setVida(this.jugador.getVida() - danioEnemigo);
        }

        return "El enemigo ha realizado un " + Util.textoColor(ataqueEnemigo, "AZUL") + " y ha hecho [" + Util.textoColor(Integer.toString(danioEnemigo), "ROJO") + "] de daño" + mensajePifiaOCritico;
    }

    private void procesarCombate(Jugador jugador, Enemigo enemigo) {
        jugador.setEnCombate(true);
        enemigo.setEnCombate(true);
        this.actualizarCombate(0);
        jugador.setEnCombate(false);
        enemigo.setEnCombate(false);
        // Si el jugador pierde el combate
        if (jugador.getVida() <= 0) {
            Util.refrescoPantalla();
            System.out.println(Util.textoColor("██████  ███████ ██████  ██████   ██████  ████████  █████ ", "ROJO"));
            System.out.println(Util.textoColor("██   ██ ██      ██   ██ ██   ██ ██    ██    ██    ██   ██", "ROJO"));
            System.out.println(Util.textoColor("██   ██ █████   ██████  ██████  ██    ██    ██    ███████", "ROJO"));
            System.out.println(Util.textoColor("██   ██ ██      ██   ██ ██   ██ ██    ██    ██    ██   ██", "ROJO"));
            System.out.println(Util.textoColor("██████  ███████ ██   ██ ██   ██  ██████     ██    ██   ██", "ROJO"));
            System.out.println();
            System.out.println("El jugador [" + Util.textoColor(jugador.getNombre(), "ROJO") + "] ha perdido el combate");
            System.out.println("Numero total de enemigos derrotados: [" + Util.textoColor(Integer.toString(jugador.getEnemigosDerrotados()), "ROJO") + "]");
            return;
            // Si el jugador gana el combate
        } else {
            Util.refrescoPantalla();
            System.out.println(Util.textoColor("██    ██ ██  ██████ ████████  ██████  ██████  ██  █████", "AZUL"));
            System.out.println(Util.textoColor("██    ██ ██ ██         ██    ██    ██ ██   ██ ██ ██   ██", "AZUL"));
            System.out.println(Util.textoColor("██    ██ ██ ██         ██    ██    ██ ██████  ██ ███████", "AZUL"));
            System.out.println(Util.textoColor(" ██  ██  ██ ██         ██    ██    ██ ██   ██ ██ ██   ██", "AZUL"));
            System.out.println(Util.textoColor("  ████   ██  ██████    ██     ██████  ██   ██ ██ ██   ██ ", "AZUL"));
            System.out.println();
            System.out.println("El jugador [" + Util.textoColor(jugador.getNombre(), "ROJO") + "] ha ganado el combate");
            System.out.print("Ingresa cualquier tecla para continuar jugando... ");
            Scanner sc = new Scanner(System.in);
            sc.nextLine();
        }

        // Se reposiciona al enemigo tras el combate
        int nuevaPosXEnemigo, nuevaPosYEnemigo;
        boolean posicionValida = false;
        while (!posicionValida) {
            nuevaPosXEnemigo = Util.generarNumeroAleatorio(1, this.altura);
            nuevaPosYEnemigo = Util.generarNumeroAleatorio(1, this.anchura);
            if (this.estaLejosDelJugador(nuevaPosXEnemigo, nuevaPosYEnemigo) && this.validarPosicion(nuevaPosXEnemigo, nuevaPosYEnemigo)) {
                enemigo.respawn(nuevaPosXEnemigo, nuevaPosYEnemigo);
                posicionValida = true;
            }
        }
    }
}
