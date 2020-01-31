package com.neves_eduardo.data_analysis_challenge.dao;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class BatFileDAO implements FileDAO {
    private final Path inputDirectory;
    private final Path outputDirectory;
    private static final String FILE_FORMAT = ".bat";

    public BatFileDAO(Path inputDirectory, Path outputDirectory) {
        this.inputDirectory = inputDirectory;
        this.outputDirectory = outputDirectory;
    }



    public List<Path> readFiles(){
        if(!Files.exists(inputDirectory)) {throw new IllegalArgumentException("Input Directory does not exist");}
        if(!Files.exists(outputDirectory)) {throw new IllegalArgumentException("Output Directory does not exist");}
        try (Stream<Path> paths = Files.walk(inputDirectory)) {
            return paths
                    .filter(Files::isRegularFile)
                    .filter(s -> s.getFileName().toString().endsWith(FILE_FORMAT))
                    .collect(Collectors.toList());
        } catch (IOException e) {
            throw new RuntimeException("ERROR: Error accessing input directory");
        }

    }


}
