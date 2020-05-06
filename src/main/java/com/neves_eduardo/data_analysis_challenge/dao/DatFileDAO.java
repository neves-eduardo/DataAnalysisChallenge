package com.neves_eduardo.data_analysis_challenge.dao;

import com.neves_eduardo.data_analysis_challenge.Main;
import org.apache.log4j.Logger;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;


public class DatFileDAO implements FileDAO {
    private final Path inputDirectory;
    private final Path outputDirectory;
    private static final String INPUT_FILE_FORMAT = ".dat";
    private static final String OUTPUT_FILE_FORMAT = ".done.dat";
    private Logger logger = Logger.getLogger(DatFileDAO.class);
    public DatFileDAO(Path inputDirectory, Path outputDirectory) {
        this.inputDirectory = inputDirectory;
        this.outputDirectory = outputDirectory;
    }


    @Override
    public List<String> readFile(Path file){
        logger.info("Reading File: " + file.toString());
        if(!Files.exists(inputDirectory)) {throw new IllegalArgumentException("Input Directory does not exist");}
        if(!file.getFileName().toString().endsWith(INPUT_FILE_FORMAT)){throw new IllegalArgumentException("Error: " + file.getFileName() + " is not a .dat file");}

        try {
            return Files.readAllLines(file);
        } catch (IOException e) {
            throw new RuntimeException("ERROR: Error accessing input directory");
        }

    }

    @Override
    public Path writeFile(String fileName, String text) {
        logger.info("Writing File: " + outputDirectory.resolve(Paths.get(fileName.replace(INPUT_FILE_FORMAT,"").concat(OUTPUT_FILE_FORMAT))));
        if(!Files.exists(outputDirectory)) {throw new IllegalArgumentException("Output Directory does not exist");}
        try {
            return Files.write(outputDirectory.resolve(Paths.get(fileName.replace(INPUT_FILE_FORMAT,"").concat(OUTPUT_FILE_FORMAT))), text.getBytes());
        } catch (IOException e) {
            throw new RuntimeException("ERROR: Error accessing input directory");
        }
    }


}
