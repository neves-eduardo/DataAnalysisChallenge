package com.neves_eduardo.data_analysis_challenge.decoder;

import com.neves_eduardo.data_analysis_challenge.dao.BatFileDAO;
import com.neves_eduardo.data_analysis_challenge.dao.FileDAO;
import com.neves_eduardo.data_analysis_challenge.dto.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.hamcrest.*;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.Matchers.samePropertyValuesAs;
import static org.junit.Assert.*;
import static org.mockito.Matchers.any;

@RunWith(MockitoJUnitRunner.class)
public class SalesReportFileDecoderTest {
    @Mock
    FileDAO fileDAO;
    @InjectMocks
    private SalesReportFileDecoder salesReportFileDecoder = new SalesReportFileDecoder(fileDAO);
    private List<String> file;
    private static final double DELTA = 0.001;
    private SalesReport salesReport;

    @Before
    public void init() {
        salesReport = salesReportFileDecoder.decodeFile(Paths.get("/"));
        file = new ArrayList<>();
        file.add("001ç1234567891234çDiegoç50000");
        file.add("001ç3245678865434çRenatoç40000.99");
        file.add("002ç2345675434544345çJose da SilvaçRural");
        file.add("002ç2345675433444345çEduardoPereiraçRural");
        file.add("003ç10ç[1-10-100,2-30-2.50,3-40-3.10]çDiego");
        file.add("003ç08ç[1-34-10,2-33-1.50,3-40-0.10]çRenato");
        Mockito.when(fileDAO.readFile(Paths.get("/"))).thenReturn(file);
        salesReport = salesReportFileDecoder.decodeFile(Paths.get("/"));
    }

    @Test
    public void salesmanDecoderShouldConvertTheLineIntoAnObjectTest() {
        assertEquals(salesReport.getSalesmen().get(0).getClass(), Salesman.class);
        assertEquals(salesReport.getSalesmen().get(1).getClass(), Salesman.class);
        assertEquals("Diego", salesReport.getSalesmen().get(0).getName());
        assertEquals("Renato", salesReport.getSalesmen().get(1).getName());
        assertEquals("1234567891234", salesReport.getSalesmen().get(0).getCpf());
        assertEquals("3245678865434", salesReport.getSalesmen().get(1).getCpf());
        assertEquals(50000, salesReport.getSalesmen().get(0).getSalary(), DELTA);
        assertEquals(40000.99, salesReport.getSalesmen().get(1).getSalary(), DELTA);

    }

    @Test
    public void customerDecoderShouldConvertTheLineIntoAnObjectTest() {
        assertEquals(salesReport.getCustomers().get(0).getClass(), Customer.class);
        assertEquals(salesReport.getCustomers().get(1).getClass(), Customer.class);
        assertEquals("Jose da Silva", salesReport.getCustomers().get(0).getName());
        assertEquals("EduardoPereira", salesReport.getCustomers().get(1).getName());
        assertEquals("2345675434544345", salesReport.getCustomers().get(0).getCnpj());
        assertEquals("2345675433444345", salesReport.getCustomers().get(1).getCnpj());


    }

    @Test
    public void saleDecoderShouldConvertTheLineIntoAnObjectTest() {
        assertEquals(salesReport.getSales().get(0).getClass(), Sale.class);
        assertEquals(salesReport.getSales().get(1).getClass(), Sale.class);
        assertEquals(10, salesReport.getSales().get(0).getSaleId());
        assertEquals(8, salesReport.getSales().get(1).getSaleId());
        assertEquals("2345675434544345", salesReport.getCustomers().get(0).getCnpj());
        assertEquals("2345675433444345", salesReport.getCustomers().get(1).getCnpj());
        assertEquals("Diego", salesReport.getSales().get(0).getSalesmanName());
        assertEquals("Renato", salesReport.getSales().get(1).getSalesmanName());
        assertThat(salesReport.getSales().get(0).getItems().get(0), samePropertyValuesAs(new Item(1, 10, 100)));
        assertThat(salesReport.getSales().get(0).getItems().get(1), samePropertyValuesAs(new Item(2, 30, 2.50)));
        assertThat(salesReport.getSales().get(0).getItems().get(2), samePropertyValuesAs(new Item(3, 40, 3.10)));
        assertThat(salesReport.getSales().get(1).getItems().get(0), samePropertyValuesAs(new Item(1, 34, 10)));
        assertThat(salesReport.getSales().get(1).getItems().get(1), samePropertyValuesAs(new Item(2, 33, 1.50)));
        assertThat(salesReport.getSales().get(1).getItems().get(2), samePropertyValuesAs(new Item(3, 40, 0.10)));


    }
}