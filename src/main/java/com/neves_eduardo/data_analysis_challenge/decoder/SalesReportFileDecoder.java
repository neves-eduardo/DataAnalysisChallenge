package com.neves_eduardo.data_analysis_challenge.decoder;

import com.neves_eduardo.data_analysis_challenge.dao.FileDAO;
import com.neves_eduardo.data_analysis_challenge.dto.*;

import javax.xml.crypto.Data;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class SalesReportFileDecoder implements FileDecoder {
    private FileDAO fileDAO;

    public SalesReportFileDecoder(FileDAO fileDAO) {
        this.fileDAO = fileDAO;
    }


    private Salesman decodeSalesman(String line) {
        String[] attributes = line.split("ç");
        return new Salesman(attributes[1],attributes[2],Double.parseDouble(attributes[3]));
    }

    private Customer decodeCustomer(String line) {
        String[] attributes = line.split("ç");
        return new Customer(attributes[1], attributes[2], attributes[3]);
    }


    @Override
    public SalesReport decode(Path file) throws IOException {
        SalesReport salesReport = new SalesReport();
        List<String> content;
        content = Files.readAllLines(file);

        salesReport.setSalesmen(
                content
                        .stream()
                        .filter(s -> s.startsWith(DataTypes.SALESMAN.getCode()))
                        .map(this::decodeSalesman)
                        .collect(Collectors.toList()));

        salesReport.setCustomers(
                content
                        .stream()
                        .filter(s -> s.startsWith(DataTypes.CUSTOMER.getCode()))
                        .map(this::decodeCustomer)
                        .collect(Collectors.toList()));


        content.stream().filter(s -> s.startsWith(DataTypes.SALE.getCode())).forEach(System.out::println);

        return salesReport;






    }


}
