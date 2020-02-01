package com.neves_eduardo.data_analysis_challenge.decoder;

import com.neves_eduardo.data_analysis_challenge.dao.FileDAO;
import com.neves_eduardo.data_analysis_challenge.dto.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class SalesReportFileDecoder implements FileDecoder {
    private FileDAO fileDAO;

    public SalesReportFileDecoder(FileDAO fileDAO) {
        this.fileDAO = fileDAO;
    }


    private Salesman decodeSalesman(String line) {
        String[] attributes = line.split("ç");
        for (String attribute : attributes) {
            System.out.println(attribute);
        }
        return new Salesman();
    }

    private Sale decodeSale(String line) {
        return new Sale();
    }

    private Customer decodeCustomer(String line) {
        return new Customer();
    }

    @Override
    public void decode(Path file) throws IOException {
        SalesReport salesReport = new SalesReport(new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
        List<String> content;
        content = Files.readAllLines(file);
        content
                .stream()
                .filter(s -> s.startsWith(String.valueOf(DataTypes.SALESMAN)))
                .forEach(this::decodeSalesman);





    }


}
