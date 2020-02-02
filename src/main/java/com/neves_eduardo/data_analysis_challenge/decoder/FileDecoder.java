package com.neves_eduardo.data_analysis_challenge.decoder;

import com.neves_eduardo.data_analysis_challenge.dto.SalesReport;

import java.io.IOException;
import java.nio.file.Path;

public interface FileDecoder {
    SalesReport decode(Path file) throws IOException;
}
