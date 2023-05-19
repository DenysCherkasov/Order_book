package com.cherkasov.fileWorkers;

import com.cherkasov.service.Service;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class InputFileParser {
    private static final String FILE_NAME = "input.txt";
    private static final Path FILE_PATH = Paths.get(FILE_NAME);
    private static final Charset CHARSET = StandardCharsets.UTF_8;
    private final Service service;

    public InputFileParser(Service service) {
        this.service = service;
    }

    public void parseFile() {
        try {
            List<String> lines = Files.readAllLines(FILE_PATH, CHARSET);
            for (String line : lines) {
                char firstChar = line.charAt(0);
                switch (firstChar) {
                    case 'u' -> service.saveOrder(line);
                    case 'o' -> service.updateOrderSize(line);
                    case 'q' -> service.writeInfoInFile(line);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
