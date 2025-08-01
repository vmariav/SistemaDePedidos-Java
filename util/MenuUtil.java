package util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class MenuUtil {

    public static void mostrarMenu(String nomeArquivo) {
        try {
            Files.lines(Paths.get(nomeArquivo)).forEach(System.out::println);
        } catch (IOException e) {
            System.out.println("Erro ao ler " + nomeArquivo + ": " + e.getMessage());
        }
    }
}
