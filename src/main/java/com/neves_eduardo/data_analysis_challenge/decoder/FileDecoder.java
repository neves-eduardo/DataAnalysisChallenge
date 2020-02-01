package com.neves_eduardo.data_analysis_challenge.decoder;

import java.io.IOException;
import java.nio.file.Path;

public interface FileDecoder {
    void decode(Path file) throws IOException;
}
