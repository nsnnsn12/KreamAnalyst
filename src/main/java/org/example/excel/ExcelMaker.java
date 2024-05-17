package org.example.excel;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.example.service.dto.AnalyzedReport;

import java.io.*;
import java.util.List;

public class ExcelMaker {
    public static String filePath = "C:\\kream_report";
    public static String fileNm = "poi_making_file_test.xlsx";
    private final static String[] headers = {"상품 번호"
                                            ,"크림 url"
                                            ,"상품명"
                                            ,"모델번호"
                                            ,"발매가"
                                            ,"출시일"
                                            ,"D-day 판매 입찰 수"
                                            ,"D-day 구매 입찰 수"
                                            ,"D-1 체결 거래량"
                                            ,"D-2 체결 거래량"
                                            ,"Daily 거래량 증감"
                                            ,"Daily 거래량 증감"
                                            ,"D-1~D-7 거래량"
                                            ,"D-8~D-14 거래량"
                                            ,"Weekly 거래량 증감"
                                            ,"Weekly 거래량 증감%"};
    private final XSSFWorkbook workbook;
    private final Sheet sheet;
    private final List<AnalyzedReport> items;

    public ExcelMaker(List<AnalyzedReport> items){
        workbook = new XSSFWorkbook();
        sheet = workbook.createSheet();
        this.items = items;
    }

    public void export(){
        drawHeader(sheet.createRow(0));

        for(int i = 0; i < items.size(); i++){
            drawBody(sheet.createRow(i+1), items.get(i));
        }
        try {
            FileOutputStream out = new FileOutputStream(new File(getFolder(), fileNm));
            workbook.write(out);
            out.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private File getFolder(){
        File folder = new File(filePath);
        if(!folder.exists()){
            folder.mkdir();
        }

        return folder;
    }

    private void drawHeader(Row row){
        for(int i = 0; i < headers.length; i++){
            row.createCell(i).setCellValue(headers[i]);
        }
    }

    private void drawBody(Row row, AnalyzedReport dto){
        row.createCell(0).setCellValue(dto.getProductId());
        row.createCell(1).setCellValue(dto.getUrl());
        row.createCell(2).setCellValue(dto.getProductName());
        row.createCell(3).setCellValue(dto.getModelNo());
        row.createCell(4).setCellValue(dto.getOriginalPrice());
        row.createCell(5).setCellValue(dto.getDateReleased());
        row.createCell(6).setCellValue(dto.getTotalSellingCount());
        row.createCell(7).setCellValue(dto.getTotalBuyingCount());
    }
}
