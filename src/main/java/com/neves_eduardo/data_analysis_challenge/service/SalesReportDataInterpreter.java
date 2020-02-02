package com.neves_eduardo.data_analysis_challenge.service;

import com.neves_eduardo.data_analysis_challenge.dao.FileDAO;
import com.neves_eduardo.data_analysis_challenge.decoder.FileDecoder;
import com.neves_eduardo.data_analysis_challenge.decoder.SalesReportFileDecoder;
import com.neves_eduardo.data_analysis_challenge.dto.Item;
import com.neves_eduardo.data_analysis_challenge.dto.Sale;
import com.neves_eduardo.data_analysis_challenge.dto.SalesReport;
import com.neves_eduardo.data_analysis_challenge.dto.Salesman;

import java.nio.file.Path;
import java.util.Comparator;

public class SalesReportDataInterpreter implements DataInterpreter {
    private FileDAO fileDAO;
    private FileDecoder salesReportFileDecoder;
    private static final String OUTPUT_TEMPLATE
            = "Customer amount: %s \n" +
            "Salesman amount: %s \n" +
            "ID of the most expensive sale: %s \n" +
            "Worst salesman ever: %s \n";

    public SalesReportDataInterpreter(FileDAO fileDAO,FileDecoder fileDecoder) {
        this.salesReportFileDecoder = fileDecoder;
        this.fileDAO = fileDAO;
    }


    @Override
    public String analyseData(SalesReport salesReport) {
        int clients = salesReport.getCustomers().size();
        int salesman = salesReport.getSalesmen().size();
        String worstSalesmanEver;
        Comparator<Sale> getMostExpensiveSale = Comparator.comparingDouble((s -> s.getItems().stream().mapToDouble(i -> i.getPrice() * i.getQuantity()).sum()));
        int mostExpensiveSaleID = salesReport.getSales().stream().max(getMostExpensiveSale).get().getSaleId();

        return String.format(OUTPUT_TEMPLATE,clients,salesman,mostExpensiveSaleID,"a");
    }
}
