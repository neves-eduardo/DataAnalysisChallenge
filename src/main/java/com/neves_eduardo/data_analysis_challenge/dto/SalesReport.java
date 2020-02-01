package com.neves_eduardo.data_analysis_challenge.dto;

import java.util.List;

public class SalesReport {
    private List<Customer> customers;
    private List<Sale> sales;
    private List<Salesman> salesmen;

    public SalesReport(List<Customer> customers, List<Sale> sales, List<Salesman> salesmen) {
        this.customers = customers;
        this.sales = sales;
        this.salesmen = salesmen;
    }

    public SalesReport() {
    }

    public List<Customer> getCustomers() {
        return customers;
    }

    public void setCustomers(List<Customer> customers) {
        this.customers = customers;
    }

    public List<Sale> getSales() {
        return sales;
    }

    public void setSales(List<Sale> sales) {
        this.sales = sales;
    }

    public List<Salesman> getSalesmen() {
        return salesmen;
    }

    public void setSalesmen(List<Salesman> salesmen) {
        this.salesmen = salesmen;
    }
}
