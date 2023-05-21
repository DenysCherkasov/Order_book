package com.cherkasov.fileWorkers;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class OutputFileWriter {
    private static final String FILE_NAME = "output.txt";
    private static final Path FILE_PATH = getOutputFilePath();

    private static Path getOutputFilePath() {
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


    public void writeDataToFile(String data) {
        try {
            if (data != null) {
                if (!Files.exists(FILE_PATH)) {
                    Files.createFile(FILE_PATH);
                }
                Files.writeString(FILE_PATH, data, StandardOpenOption.APPEND);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
