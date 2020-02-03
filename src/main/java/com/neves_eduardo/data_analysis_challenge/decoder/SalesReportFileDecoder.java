package com.neves_eduardo.data_analysis_challenge.decoder;

import com.neves_eduardo.data_analysis_challenge.dao.FileDAO;
import com.neves_eduardo.data_analysis_challenge.dto.*;
import org.apache.commons.lang3.EnumUtils;
import org.apache.commons.lang3.StringUtils;

import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class SalesReportFileDecoder implements FileDecoder {
    private FileDAO fileDAO;

    public SalesReportFileDecoder(FileDAO fileDAO) {
        this.fileDAO = fileDAO;
    }


    public void validateLine(String line) {

        List<DataTypes> validTypes = Arrays.asList(DataTypes.values());
        if (line.split("ç").length < 3) {
            throw new IllegalArgumentException("ERROR: File contains lines that cannot be interpreted");
        }
        if (validTypes.stream().noneMatch(s -> s.getCode().equals(line.substring(0, 3)))) {
            throw new IllegalArgumentException("ERROR: File contains invalid code");
        }
    }

    private Salesman decodeSalesman(String line) {
        if (line.chars().filter(ch -> ch == 'ç').count() >= 4) {
            String[] attributes = StringUtils.split(line, "ç", 3);
            String last = StringUtils.substringAfterLast(attributes[2], "ç");
            String antepenultimate = StringUtils.substringBeforeLast(attributes[2], "ç");
            return new Salesman(attributes[1], antepenultimate, Double.valueOf(last));
        } else {
            String[] attributes = line.split("ç");
            return new Salesman(attributes[1], attributes[2], Double.parseDouble(attributes[3]));
        }

    }

    private Customer decodeCustomer(String line) {
        if (line.chars().filter(ch -> ch == 'ç').count() >= 4) {
            String[] attributes = StringUtils.split(line, "ç", 3);
            String last = StringUtils.substringAfterLast(attributes[2], "ç");
            String antepenultimate = StringUtils.substringBeforeLast(attributes[2], "ç");
            return new Customer(attributes[1], antepenultimate, last);
        } else {
            String[] attributes = line.split("ç");
            return new Customer(attributes[1], attributes[2], attributes[3]);
        }

    }

    private Sale decodeSale(String line) {
        String[] attributes = StringUtils.split(line, "ç", 4);
        List<String> itemsText = Arrays.asList(attributes[2].substring(attributes[2].indexOf("[") + 1, attributes[2].indexOf("]") - 1).split(","));
        List<Item> items = itemsText.stream().map(this::decodeItem).collect(Collectors.toList());

        return new Sale(Integer.parseInt(attributes[1]), items, attributes[3]);
    }

    private Item decodeItem(String itemLine) {
        String[] attributes = itemLine.split("-");
        return new Item(Integer.parseInt(attributes[0]), Integer.parseInt(attributes[1]), Double.parseDouble(attributes[2]));

    }


    @Override
    public SalesReport decodeFile(Path file) {
        List<String> lines = fileDAO.readFile(file);
        SalesReport salesReport = new SalesReport();

        lines.forEach(this::validateLine);


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
