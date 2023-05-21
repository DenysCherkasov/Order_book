package com.cherkasov.fileWorkers;

import com.cherkasov.service.Service;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class InputFileParser {
    private static final String FILE_NAME = "input.txt";
    private static final Path FILE_PATH = getInputFilePath();
    private static final Charset CHARSET = StandardCharsets.UTF_8;
    private final Service service;

    public InputFileParser(Service service) {
        this.service = service;
    }

    private static Path getInputFilePath() {
        try {
            URL url = InputFileParser.class.getProtectionDomain().getCodeSource().getLocation();
            Path jarPath = Paths.get(url.toURI());
            Path folderPath = jarPath.getParent();
            return folderPath.resolve(FILE_NAME);
        } catch (URISyntaxException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void parseFile() {
        if (FILE_PATH == null) {
            System.out.println("Error: Unable to determine file path.");
            return;
        }

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
