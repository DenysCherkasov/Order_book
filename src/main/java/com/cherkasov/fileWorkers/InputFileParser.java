package com.cherkasov.fileWorkers;

import com.cherkasov.service.Service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class InputFileParser {
    private static final String FILE_NAME = "./input.txt";
    private final Service service;

    public InputFileParser(Service service) {
        this.service = service;
    }

    public void parseFile() {
        try {
            BufferedReader bufferedReader
                    = new BufferedReader((new FileReader(FILE_NAME)));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                char firstChar = line.charAt(0);
                switch (firstChar) {
                    case 'u' -> service.saveOrder(line);
                    case 'o' -> service.updateOrderSize(line);
                    case 'q' -> service.writeInfoInFile(line);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
