package com.neves_eduardo.data_analysis_challenge;

import com.neves_eduardo.data_analysis_challenge.dao.DatFileDAO;
import com.neves_eduardo.data_analysis_challenge.dao.FileDAO;
import com.neves_eduardo.data_analysis_challenge.decoder.FileDecoder;
import com.neves_eduardo.data_analysis_challenge.decoder.SalesReportFileDecoder;
import com.neves_eduardo.data_analysis_challenge.service.DataInterpreter;
import com.neves_eduardo.data_analysis_challenge.service.SalesReportDataInterpreter;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.*;
import java.util.List;
import java.util.stream.Collectors;
@Component
public class DirectoryObserver {
    private static final Path INPUT_PATH = Paths.get(System.getenv("HOME").concat("/data/in/"));
    private static final Path OUTPUT_PATH = Paths.get(System.getenv("HOME").concat("/data/out/"));
    private List<Path> files;
    private FileDAO fileDAO;
    private FileDecoder fileDecoder;
    private DataInterpreter dataInterpreter;
    private Logger logger = Logger.getLogger(DirectoryObserver.class);

    @Autowired
    public DirectoryObserver(FileDAO fileDAO, FileDecoder fileDecoder, DataInterpreter dataInterpreter) {
        this.fileDAO = fileDAO;
        this.fileDecoder = fileDecoder;
        this.dataInterpreter = dataInterpreter;
    }

    public void appBoot() {
        logger.info("booting directory observer");
        try {
            files = Files.walk(INPUT_PATH).filter(Files::isRegularFile).collect(Collectors.toList());
            for (Path file : files) {
                if (Files.isRegularFile(file)) {
                    dataInterpreter.analyzeData(file);
                }
            }
        } catch (IOException | IllegalArgumentException e) {
            logger.error(e.getMessage());
        }

    }


    public void appCheckDirectory() {
        try {
            WatchService watchService = FileSystems.getDefault().newWatchService();
            WatchKey watchKey = INPUT_PATH.register(watchService, StandardWatchEventKinds.ENTRY_CREATE);
            while ((watchKey = watchService.take()) != null) {
                for (WatchEvent<?> event : watchKey.pollEvents()) {
                    Path newFilePath = Paths.get(INPUT_PATH.toString().concat("/" + event.context().toString()));
                    logger.info("New File Detected: " + newFilePath);
                     dataInterpreter.analyzeData(newFilePath);

                }
                watchKey.reset();
            }

        } catch (IllegalArgumentException | IOException | InterruptedException exception) {
            logger.error(exception.getMessage());

        }
    }


}
