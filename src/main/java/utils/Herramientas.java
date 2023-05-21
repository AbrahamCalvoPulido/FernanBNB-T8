package utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class Herramientas {

    public static void delay(String mensaje) {
        System.out.print(mensaje);
        for (int i = 0; i < 4; i++) {
            System.out.print(".");
            try {
                Thread.sleep(800);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

        }


    }
    public static void pulseParacontinuar(){
        Scanner s = new Scanner(System.in);
        System.out.println("Pulse para continuar");
        s.nextLine();
    }
    public static String fechaHoraActualString() {
        String fecha = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
        String hora = LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm"));
        return fecha + " a las " + hora;
    }
    public static String formateaCiudad(String texto) {
        String[] tildes = { "á", "é", "í", "ó", "ú", "Á", "É", "Í", "Ó", "Ú", "Ñ", "ñ" };
        String[] sinTildes = { "a", "e", "i", "o", "u", "A", "E", "I", "O", "U", "N", "n" };

        // Reemplazar tildes
        for (int i = 0; i < tildes.length; i++) {
            texto = texto.replace(tildes[i], sinTildes[i]);
        }

        // Convertir a minúsculas
        texto = texto.toLowerCase();

        return texto;
    }
}
