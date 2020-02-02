package com.neves_eduardo.data_analysis_challenge.service;

import com.neves_eduardo.data_analysis_challenge.decoder.FileDecoder;
import com.neves_eduardo.data_analysis_challenge.dto.Sale;
import com.neves_eduardo.data_analysis_challenge.dto.SalesReport;
import com.neves_eduardo.data_analysis_challenge.dto.Salesman;

import java.nio.file.Path;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class SalesReportDataInterpreter implements DataInterpreter {
    private FileDecoder salesReportFileDecoder;
    private static final String OUTPUT_TEMPLATE
            = "Customer amount: %s \n" +
            "Salesman amount: %s \n" +
            "ID of the most expensive sale: %s \n" +
            "Worst salesman ever: %s \n";

    public SalesReportDataInterpreter(FileDecoder fileDecoder) {
        this.salesReportFileDecoder = fileDecoder;
    }


    @Override
    public String analyzeData(Path path) {
        SalesReport salesReport = salesReportFileDecoder.decodeFile(path);
        int clients = salesReport.getCustomers().size();
        int salesmen = salesReport.getSalesmen().size();
        String worstSalesmanEver;
        Map<String,Double> salesmenRank = new HashMap<>();
        for (Salesman salesman : salesReport.getSalesmen()) {
            Double salesAmount= salesReport
                    .getSales()
                    .stream()
                    .filter(s ->s.getSalesmanName().equals(salesman.getName()))
                    .map(s -> s.getItems().stream().mapToDouble(i -> i.getPrice() * i.getQuantity()).sum())
                    .findFirst()
                    .orElse(0.0);
            salesmenRank.put(salesman.getName(),salesAmount);
        }

        worstSalesmanEver = salesmenRank.entrySet().stream().sorted(Map.Entry.comparingByValue(Comparator.naturalOrder())).map(Map.Entry::getKey).collect(Collectors.toList()).get(0);

        Comparator<Sale> getMostExpensiveSale = Comparator.comparingDouble((s -> s.getItems().stream().mapToDouble(i -> i.getPrice() * i.getQuantity()).sum()));
        int mostExpensiveSaleID = salesReport.getSales().stream().max(getMostExpensiveSale).get().getSaleId();

        return String.format(OUTPUT_TEMPLATE,clients,salesmen,mostExpensiveSaleID,worstSalesmanEver);
    }
}
