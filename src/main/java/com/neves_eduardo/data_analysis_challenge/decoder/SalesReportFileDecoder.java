package com.neves_eduardo.data_analysis_challenge.decoder;

import com.neves_eduardo.data_analysis_challenge.dao.FileDAO;
import com.neves_eduardo.data_analysis_challenge.dto.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import java.util.Arrays;
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

    private Sale decodeSale(String line) {
        String[] attributes = line.split("ç");
        List<String> itemsText = Arrays.asList(attributes[2].substring(attributes[2].indexOf("[") +1 ,attributes[2].indexOf("]") -1).split(","));
        List<Item> items = itemsText.stream().map(this::decodeItem).collect(Collectors.toList());

        return new Sale(Integer.parseInt(attributes[1]),items,attributes[3]);
    }

    private Item decodeItem(String itemLine) {
        String[] attributes = itemLine.split("-");
        return new Item(Integer.parseInt(attributes[0]),Integer.parseInt(attributes[1]),Double.parseDouble(attributes[2]));

    }


    @Override
    public SalesReport decodeFile(List<String> lines){
        SalesReport salesReport = new SalesReport();
        salesReport.setSalesmen(
                lines
                        .stream()
                        .filter(s -> s.startsWith(DataTypes.SALESMAN.getCode()))
                        .map(this::decodeSalesman)
                        .collect(Collectors.toList()));

        salesReport.setCustomers(
                lines
                        .stream()
                        .filter(s -> s.startsWith(DataTypes.CUSTOMER.getCode()))
                        .map(this::decodeCustomer)
                        .collect(Collectors.toList()));

        salesReport.setSales(
                lines
                        .stream()
                        .filter(s -> s.startsWith(DataTypes.SALE.getCode()))
                        .map(this::decodeSale)
                        .collect(Collectors.toList()));


        return salesReport;






    }


}
