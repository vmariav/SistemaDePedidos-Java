package service;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LogService {
    public static void registrar(String mensagem) {
        try (FileWriter fw = new FileWriter("log.txt", true)) {
            LocalDateTime agora = LocalDateTime.now();
            String dataHora = agora.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            fw.write("[" + dataHora + "] " + mensagem + "\n");
        } catch (IOException e) {
            System.out.println("Erro ao escrever no log: " + e.getMessage());
        }
    }
}
