package com.cherkasov;

import com.cherkasov.fileWorkers.OutputFileWriter;
import com.cherkasov.fileWorkers.InputFileParser;
import com.cherkasov.repository.Repository;
import com.cherkasov.service.Service;

public class Main {
    public static void main(String[] args) {
        InputFileParser inputFileParser = new InputFileParser(new Service(new Repository(), new OutputFileWriter()));
        inputFileParser.parseFile();
    }
}