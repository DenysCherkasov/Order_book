package com.cherkasov;

import com.cherkasov.fileWorkers.OutputFileWriter;
import com.cherkasov.fileWorkers.InputFileParser;
import com.cherkasov.repository.Repository;
import com.cherkasov.service.Service;

public class Main {
    public static void main(String[] args) {
        long startTime = System.currentTimeMillis();

        InputFileParser inputFileParser = new InputFileParser(new Service(new Repository(), new OutputFileWriter()));
        inputFileParser.parseFile();

        long endTime = System.currentTimeMillis();
        long executionTime = endTime - startTime;

        System.out.println("Время выполнения программы: " + executionTime + " мс");

    }
}