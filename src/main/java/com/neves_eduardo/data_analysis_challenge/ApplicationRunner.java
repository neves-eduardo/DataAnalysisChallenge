package com.neves_eduardo.data_analysis_challenge;

import com.neves_eduardo.data_analysis_challenge.dao.BatFileDAO;
import com.neves_eduardo.data_analysis_challenge.dao.FileDAO;
import com.neves_eduardo.data_analysis_challenge.decoder.FileDecoder;
import com.neves_eduardo.data_analysis_challenge.decoder.SalesReportFileDecoder;
import com.neves_eduardo.data_analysis_challenge.service.DataInterpreter;
import com.neves_eduardo.data_analysis_challenge.service.SalesReportDataInterpreter;

import java.io.IOException;
import java.nio.file.*;
import java.util.stream.Stream;

public class ApplicationRunner {
    private static final Path INPUT_PATH =  Paths.get(System.getenv("HOME").concat("/data/in/"));
    private static final Path OUTPUT_PATH = Paths.get(System.getenv("HOME").concat("/data/out/"));

    public static void main(String[] args) {
        Stream<Path> files;
        FileDAO fileDAO = new BatFileDAO(INPUT_PATH, OUTPUT_PATH);
        FileDecoder fileDecoder = new SalesReportFileDecoder(fileDAO);
        DataInterpreter dataInterpreter = new SalesReportDataInterpreter(fileDecoder);

//        files = Files.walk(INPUT_PATH);
//        files.
//                filter(Files::isRegularFile)
//                .map(dataInterpreter::analyzeData)
//                .forEach(x ->fileDAO.writeFile("output",x));
        try {
            WatchService watchService = FileSystems.getDefault().newWatchService();
            WatchKey watchKey = INPUT_PATH.register(watchService, StandardWatchEventKinds.ENTRY_CREATE);
            while((watchKey = watchService.take()) != null){
                for (WatchEvent<?> event : watchKey.pollEvents()) {
                    Path newFilePath = Paths.get(INPUT_PATH.toString().concat("/"+event.context().toString()));
                    System.out.println("New File Detected: "+ newFilePath);
                    fileDAO.writeFile(event.context().toString(),dataInterpreter.analyzeData(newFilePath));

                }
                watchKey.reset();
            }

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

    }


}
