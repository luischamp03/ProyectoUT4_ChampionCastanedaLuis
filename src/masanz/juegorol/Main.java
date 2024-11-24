package masanz.juegorol;

import masanz.juegorol.juego.Juego;
import masanz.juegorol.personajes.Jugador;
import masanz.juegorol.util.Util;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int altura = -1, anchura = -1;
        while (altura < 5 || anchura < 10) {
            Util.refrescoPantalla();
            System.out.println(Util.textoColor("==== BIENVENIDO AL JUEGO ====", "AZUL"));
            System.out.println("Ingrese las dimensiones del mapa (minimo 5x10):");
            System.out.print("ALTURA: ");
            altura = sc.nextInt();
            System.out.print("ANCHURA: ");
            anchura = sc.nextInt();
        }

        Jugador jugador = new Jugador("Luis");
        Juego juego = new Juego(jugador, altura, anchura);
    }
}