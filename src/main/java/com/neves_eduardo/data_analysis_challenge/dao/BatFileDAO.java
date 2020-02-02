package com.neves_eduardo.data_analysis_challenge.dao;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.IllegalFormatException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class BatFileDAO implements FileDAO {
    private final Path inputDirectory;
    private final Path outputDirectory;
    private static final String INPUT_FILE_FORMAT = ".bat";
    private static final String OUTPUT_FILE_FORMAT = ".done.bat";

    public BatFileDAO(Path inputDirectory, Path outputDirectory) {
        this.inputDirectory = inputDirectory;
        this.outputDirectory = outputDirectory;
    }


    @Override
    public List<String> readFile(Path file){
        if(!Files.exists(inputDirectory)) {throw new IllegalArgumentException("Input Directory does not exist");}
        if(!file.getFileName().toString().endsWith(INPUT_FILE_FORMAT)){throw new IllegalArgumentException("Error: " + file.getFileName() + " is not a .bat file");}

        try {
            return Files.readAllLines(file);
        } catch (IOException e) {
            throw new RuntimeException("ERROR: Error accessing input directory");
        }

    }

    @Override
    public Path writeFile(String fileName, String text) {
        if(!Files.exists(outputDirectory)) {throw new IllegalArgumentException("Output Directory does not exist");}
        try {
            return Files.write(outputDirectory.resolve(Paths.get(fileName.concat(OUTPUT_FILE_FORMAT))), text.getBytes());
        } catch (IOException e) {
            throw new RuntimeException("ERROR: Error accessing input directory");
        }
    }


}
