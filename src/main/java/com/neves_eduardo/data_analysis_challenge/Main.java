package com.neves_eduardo.data_analysis_challenge;

import com.neves_eduardo.data_analysis_challenge.dao.BatFileDAO;
import com.neves_eduardo.data_analysis_challenge.dao.FileDAO;
import com.neves_eduardo.data_analysis_challenge.decoder.FileDecoder;
import com.neves_eduardo.data_analysis_challenge.decoder.SalesReportFileDecoder;
import com.neves_eduardo.data_analysis_challenge.service.DataInterpreter;
import com.neves_eduardo.data_analysis_challenge.service.SalesReportDataInterpreter;

import java.io.IOException;
import java.nio.file.*;
import java.util.stream.Stream;

public class Main {
    public static void main(String[] args) {
        DirectoryObserver directoryObserver = new DirectoryObserver();
        directoryObserver.appBoot();
        while(true){directoryObserver.appCheckDirectory();}

    }

}



