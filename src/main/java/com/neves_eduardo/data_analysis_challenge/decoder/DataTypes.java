package com.neves_eduardo.data_analysis_challenge.decoder;

public enum DataTypes {
    SALESMAN(001),CUSTOMER(002),SALE(003);

    private int code;

    public double getCode()
    {
        return this.code;
    }

    DataTypes(int code)
    {
        this.code = code;
    }
}
