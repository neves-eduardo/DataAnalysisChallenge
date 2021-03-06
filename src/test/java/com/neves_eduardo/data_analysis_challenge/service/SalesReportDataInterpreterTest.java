package com.neves_eduardo.data_analysis_challenge.service;

import com.neves_eduardo.data_analysis_challenge.dao.FileDAO;
import com.neves_eduardo.data_analysis_challenge.decoder.FileDecoder;
import com.neves_eduardo.data_analysis_challenge.dto.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.nio.file.Paths;
import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;

@RunWith(MockitoJUnitRunner.class)
public class SalesReportDataInterpreterTest {
    @Mock
    private FileDecoder fileDecoder;
    @Mock
    private FileDAO fileDAO;
    @InjectMocks
    private SalesReportDataInterpreter salesReportDataInterpreter = new SalesReportDataInterpreter(fileDecoder,fileDAO);
    private SalesReport salesReport;
    @Before
    public void init() {
        ArrayList<Item> items1 = new ArrayList<>();
        items1.add(new Item(1,1,100));
        ArrayList<Item> items2 = new ArrayList<>();
        items2.add(new Item(1,1,1000));
        items2.add(new Item(1,10000,1000000000));

        Sale sale1 = new Sale(1,items1,"Joao");
        Sale sale2 = new Sale(2,items2,"Roger");
        ArrayList<Sale> sales = new ArrayList<>();
        sales.add(sale1);
        sales.add(sale2);
        ArrayList<Customer> customers = new ArrayList<>();
        customers.add(new Customer("a","a","a"));
        customers.add(new Customer("a","a","a"));
        customers.add(new Customer("a","a","a"));
        ArrayList<Salesman> salesmen = new ArrayList<>();
        salesmen.add(new Salesman("a","Joao",10));
        salesmen.add(new Salesman("a","Roger",10));
        salesReport = new SalesReport(customers,sales,salesmen);
        Mockito.when(fileDecoder.decodeFile(any())).thenReturn(salesReport);

    }

    @Test
    public void worstSalesmanEverShouldReturnTheSalesmanWithLowestTotalSalesTest() {
        String outputReport = salesReportDataInterpreter.analyzeData(Paths.get("a"));
        assertTrue(outputReport.contains("Worst salesman ever: Joao"));

    }

    @Test
    public void CustomerAmountShouldCountTheCustomerAmountTest() {
        String outputReport = salesReportDataInterpreter.analyzeData(Paths.get("a"));
        assertTrue(outputReport.contains("Customer amount: 3 "));

    }

    @Test
    public void SalesmanAmountShouldCountTheSalesmanAmountTest() {
        String outputReport = salesReportDataInterpreter.analyzeData(Paths.get("a"));
        assertTrue(outputReport.contains("ID of the most expensive sale: 2"));

    }

    @Test
    public void IDofTheMostExpensiveSaleShouldBeOfTheMostExpensiveSaleTest() {
        String outputReport = salesReportDataInterpreter.analyzeData(Paths.get("a"));
        assertTrue(outputReport.contains("Salesman amount: 2 "));

    }

}