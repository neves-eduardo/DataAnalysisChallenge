package com.neves_eduardo.data_analysis_challenge.decoder;

import com.neves_eduardo.data_analysis_challenge.dto.SalesReport;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

public interface FileDecoder {
    SalesReport decodeFile(Path file);
}
