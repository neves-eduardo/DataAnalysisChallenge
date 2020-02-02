package com.neves_eduardo.data_analysis_challenge;

import com.neves_eduardo.data_analysis_challenge.dao.BatFileDAO;
import com.neves_eduardo.data_analysis_challenge.dao.FileDAO;
import com.neves_eduardo.data_analysis_challenge.decoder.FileDecoder;
import com.neves_eduardo.data_analysis_challenge.decoder.SalesReportFileDecoder;
import com.neves_eduardo.data_analysis_challenge.service.DataInterpreter;
import com.neves_eduardo.data_analysis_challenge.service.SalesReportDataInterpreter;

import java.io.IOException;
import java.nio.file.*;
import java.util.List;
import java.util.stream.Collectors;

public class DirectoryObserver {
    private static final Path INPUT_PATH = Paths.get(System.getenv("HOME").concat("/data/in/"));
    private static final Path OUTPUT_PATH = Paths.get(System.getenv("HOME").concat("/data/out/"));
    private List<Path> files;
    private FileDAO fileDAO = new BatFileDAO(INPUT_PATH, OUTPUT_PATH);
    private FileDecoder fileDecoder = new SalesReportFileDecoder(fileDAO);
    private DataInterpreter dataInterpreter = new SalesReportDataInterpreter(fileDecoder,fileDAO);

    public void appBoot() {
        try {
            files = Files.walk(INPUT_PATH).filter(Files::isRegularFile).collect(Collectors.toList());
            for (Path file : files) {
                if (Files.isRegularFile(file)) {
                    dataInterpreter.analyzeData(file);
                }
            }
        } catch (IOException | IllegalArgumentException e) {
            System.out.println("Error booting application: " + e.getMessage());
        }

    }


    public void appCheckDirectory() {
        try {
            WatchService watchService = FileSystems.getDefault().newWatchService();
            WatchKey watchKey = INPUT_PATH.register(watchService, StandardWatchEventKinds.ENTRY_CREATE);
            while ((watchKey = watchService.take()) != null) {
                for (WatchEvent<?> event : watchKey.pollEvents()) {
                    Path newFilePath = Paths.get(INPUT_PATH.toString().concat("/" + event.context().toString()));
                    System.out.println("New File Detected: " + newFilePath);
                     dataInterpreter.analyzeData(newFilePath);

                }
                watchKey.reset();
            }

        } catch (IllegalArgumentException | IOException | InterruptedException exception) {
            System.out.println("ERROR:" + exception.getMessage());

        }
    }


}
