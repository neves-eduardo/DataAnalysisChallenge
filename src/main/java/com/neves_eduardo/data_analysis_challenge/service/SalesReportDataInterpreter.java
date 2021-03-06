package com.neves_eduardo.data_analysis_challenge.service;

import com.neves_eduardo.data_analysis_challenge.dao.FileDAO;
import com.neves_eduardo.data_analysis_challenge.decoder.FileDecoder;
import com.neves_eduardo.data_analysis_challenge.dto.Sale;
import com.neves_eduardo.data_analysis_challenge.dto.SalesReport;
import com.neves_eduardo.data_analysis_challenge.dto.Salesman;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.file.Path;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class SalesReportDataInterpreter implements DataInterpreter {
    private FileDecoder salesReportFileDecoder;
    private FileDAO fileDAO;
    private Logger logger = Logger.getLogger(SalesReport.class);
    private static final String OUTPUT_TEMPLATE
            = "Customer amount: %s \n" +
            "Salesman amount: %s \n" +
            "ID of the most expensive sale: %s \n" +
            "Worst salesman ever: %s \n";
    @Autowired
    public SalesReportDataInterpreter(FileDecoder fileDecoder, FileDAO fileDAO) {
        this.salesReportFileDecoder = fileDecoder;
        this.fileDAO = fileDAO;
    }



    @Override
    public String analyzeData(Path path) {
        logger.info("analyzing data from file: " + path);
        SalesReport salesReport = salesReportFileDecoder.decodeFile(path);
        int clients = salesReport.getCustomers().size();
        int salesmen = salesReport.getSalesmen().size();

        String outputString = String.format(OUTPUT_TEMPLATE,
                clients,
                salesmen,
                getMostExpensiveSale(salesReport.getSales()).getSaleId(),
                getWorstSalesmanEver(salesReport.getSales(),salesReport.getSalesmen()));

        fileDAO.writeFile(path.getFileName().toString().replace(".bat",""), outputString);
        return outputString;
    }

    private String getWorstSalesmanEver(List<Sale>sales,List<Salesman> salesmen){
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

    private Sale getMostExpensiveSale(List<Sale> sales){
        Comparator<Sale> mostExpensiveSale = Comparator
                .comparingDouble((s -> s.getItems().stream().mapToDouble(i -> i.getPrice() * i.getQuantity()).sum()));
        return sales
                .stream()
                .max(mostExpensiveSale)
                .get();
    }

}
