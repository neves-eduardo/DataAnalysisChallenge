package com.neves_eduardo.data_analysis_challenge.decoder;

import com.neves_eduardo.data_analysis_challenge.dao.FileDAO;
import com.neves_eduardo.data_analysis_challenge.dto.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SalesReportFileDecoder implements FileDecoder {
    private FileDAO fileDAO;

    private static final char SEPARATOR = 'ç';
    private static final String ITEM_LIST_SEPARATOR = ",";
    private static final int MIN_NUMBER_OF_CEDILLAS= 3;
    private static final int MAXIMUM_SPLIT_SIZE = 4;
    private static final int SPLIT_ATTRIBUTE_ZERO = 0;
    private static final int SPLIT_FIRST_ATTRIBUTE = 1;
    private static final int SPLIT_SECOND_ATTRIBUTE = 2;
    private static final int SPLIT_THIRD_ATTRIBUTE = 3;

    @Autowired
    public SalesReportFileDecoder(FileDAO fileDAO) {
        this.fileDAO = fileDAO;
    }


    public void validateLine(String line) {

        List<DataTypes> validTypes = Arrays.asList(DataTypes.values());
        if (line.split(String.valueOf(SEPARATOR)).length < MIN_NUMBER_OF_CEDILLAS) {
            throw new IllegalArgumentException("ERROR: File contains lines that cannot be interpreted");
        }
        if (validTypes.stream().noneMatch(s -> s.getCode().equals(line.substring(0, 3)))) {
            throw new IllegalArgumentException("ERROR: File contains invalid code");
        }
    }

    private Salesman decodeSalesman(String line) {
        if (line.chars().filter(ch -> ch == SEPARATOR).count() >= MAXIMUM_SPLIT_SIZE) {
            String[] attributes = StringUtils.split(line, String.valueOf(SEPARATOR), MAXIMUM_SPLIT_SIZE-1);
            String last = StringUtils.substringAfterLast(attributes[SPLIT_SECOND_ATTRIBUTE], String.valueOf(SEPARATOR));
            String antepenultimate = StringUtils.substringBeforeLast(attributes[SPLIT_SECOND_ATTRIBUTE], "ç");
            return new Salesman(attributes[SPLIT_FIRST_ATTRIBUTE], antepenultimate, Double.valueOf(last));
        } else {
            String[] attributes = line.split("ç");
            return new Salesman(attributes[SPLIT_FIRST_ATTRIBUTE], attributes[SPLIT_SECOND_ATTRIBUTE], Double.parseDouble(attributes[SPLIT_THIRD_ATTRIBUTE]));
        }

    }

    private Customer decodeCustomer(String line) {
        if (line.chars().filter(ch -> ch == 'ç').count() >= MAXIMUM_SPLIT_SIZE) {
            String[] attributes = StringUtils.split(line, String.valueOf(SEPARATOR), MAXIMUM_SPLIT_SIZE-1);
            String last = StringUtils.substringAfterLast(attributes[SPLIT_SECOND_ATTRIBUTE], String.valueOf(SEPARATOR));
            String antepenultimate = StringUtils.substringBeforeLast(attributes[SPLIT_SECOND_ATTRIBUTE], "ç");
            return new Customer(attributes[SPLIT_FIRST_ATTRIBUTE], antepenultimate, last);
        } else {
            String[] attributes = line.split(String.valueOf(SEPARATOR));
            return new Customer(attributes[SPLIT_FIRST_ATTRIBUTE], attributes[SPLIT_SECOND_ATTRIBUTE], attributes[SPLIT_THIRD_ATTRIBUTE]);
        }

    }

    private Sale decodeSale(String line) {
        String[] attributes = StringUtils.split(line, String.valueOf(SEPARATOR), MAXIMUM_SPLIT_SIZE);
        List<String> itemsText = Arrays.asList(
                attributes[SPLIT_SECOND_ATTRIBUTE]
                        .substring(attributes[SPLIT_SECOND_ATTRIBUTE]
                                .indexOf("[") + 1, attributes[SPLIT_SECOND_ATTRIBUTE].indexOf("]") - 1)
                        .split(ITEM_LIST_SEPARATOR));
        List<Item> items = itemsText.stream().map(this::decodeItem).collect(Collectors.toList());

        return new Sale(Integer.parseInt(attributes[SPLIT_FIRST_ATTRIBUTE]), items, attributes[SPLIT_THIRD_ATTRIBUTE]);
    }

    private Item decodeItem(String itemLine) {
        String[] attributes = itemLine.split("-");
        return new Item(Integer.parseInt(attributes[SPLIT_ATTRIBUTE_ZERO]), Integer.parseInt(attributes[SPLIT_FIRST_ATTRIBUTE]), Double.parseDouble(attributes[SPLIT_SECOND_ATTRIBUTE]));

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
