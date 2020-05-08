package com.neves_eduardo.data_analysis_challenge.decoder;

public enum DataTypes {

    SALESMAN("001"),CUSTOMER("002"),SALE("003");

    private String code;

    public String getCode()
    {
        return this.code;
    }

    DataTypes(String code)
    {
        this.code = code;
    }

}
