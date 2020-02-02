package com.neves_eduardo.data_analysis_challenge.service;

import com.neves_eduardo.data_analysis_challenge.dao.FileDAO;
import com.neves_eduardo.data_analysis_challenge.decoder.FileDecoder;
import com.neves_eduardo.data_analysis_challenge.dto.Sale;
import com.neves_eduardo.data_analysis_challenge.dto.SalesReport;
import com.neves_eduardo.data_analysis_challenge.dto.Salesman;

import java.nio.file.Path;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class SalesReportDataInterpreter implements DataInterpreter {
    private FileDecoder salesReportFileDecoder;
    private FileDAO fileDAO;
    private static final String OUTPUT_TEMPLATE
            = "Customer amount: %s \n" +
            "Salesman amount: %s \n" +
            "ID of the most expensive sale: %s \n" +
            "Worst salesman ever: %s \n";

    public SalesReportDataInterpreter(FileDecoder fileDecoder, FileDAO fileDAO) {
        this.salesReportFileDecoder = fileDecoder;
        this.fileDAO = fileDAO;
    }



    @Override
    public String analyzeData(Path path) {
        SalesReport salesReport = salesReportFileDecoder.decodeFile(path);
        int clients = salesReport.getCustomers().size();
        int salesmen = salesReport.getSalesmen().size();
        Comparator<Sale> mostExpensiveSale = Comparator.comparingDouble((s -> s.getItems().stream().mapToDouble(i -> i.getPrice() * i.getQuantity()).sum()));
        int mostExpensiveSaleID = salesReport.getSales().stream().max(mostExpensiveSale).get().getSaleId();

        String outputString = String.format(OUTPUT_TEMPLATE,clients,salesmen,mostExpensiveSaleID,getWorstSalesmanEver(salesReport.getSales(),salesReport.getSalesmen()));

        fileDAO.writeFile(path.getFileName().toString(), outputString);
        return outputString;
    }

    public String getWorstSalesmanEver(List<Sale>sales,List<Salesman> salesmen){
        Map<String,Double> salesmenRank = new HashMap<>();
        for (Salesman salesman : salesmen) {
            Double salesAmount= sales
                    .stream()
                    .filter(s ->s.getSalesmanName().equals(salesman.getName()))
                    .map(s -> s.getItems().stream().mapToDouble(i -> i.getPrice() * i.getQuantity()).sum())
                    .findFirst()
                    .orElse(0.0);
            salesmenRank.put(salesman.getName(),salesAmount);
        }
        return salesmenRank
                .entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue(Comparator.naturalOrder()))
                .map(Map.Entry::getKey)
                .collect(Collectors.toList())
                .get(0);
    }

}
