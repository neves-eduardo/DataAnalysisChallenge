package com.neves_eduardo.data_analysis_challenge.config;

import com.neves_eduardo.data_analysis_challenge.dao.DatFileDAO;
import com.neves_eduardo.data_analysis_challenge.dao.FileDAO;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.nio.file.Paths;

@Configuration
@ComponentScan(basePackages = "com.neves_eduardo.data_analysis_challenge")
public class AppConfig {

    @Bean
    public FileDAO datFileDAO() {
        return new DatFileDAO(Paths.get(System.getenv("HOME").concat("/data/in/")),Paths.get(System.getenv("HOME").concat("/data/out/")));
    }

}
