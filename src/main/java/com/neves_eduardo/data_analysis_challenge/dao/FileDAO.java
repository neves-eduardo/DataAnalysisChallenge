package com.neves_eduardo.data_analysis_challenge.dao;

import java.nio.file.Path;
import java.util.List;

public interface FileDAO {
    List<Path> readFiles();
}
