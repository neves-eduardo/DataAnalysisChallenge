package com.neves_eduardo.data_analysis_challenge.service;

import com.neves_eduardo.data_analysis_challenge.dto.SalesReport;

import java.nio.file.Path;

public interface DataInterpreter {
    String analyzeData(Path path);
}
