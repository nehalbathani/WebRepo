package com.aflac.aims.tph.web.utils;

import java.text.NumberFormat;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.view.document.AbstractExcelView;

import com.aflac.aims.tph.web.model.ManualTradesReportBean;

public class ExcelManualReportView extends AbstractExcelView{

	
	@Override
	@RequestMapping(value="/ExcelReport")
	protected void buildExcelDocument(Map model, HSSFWorkbook workbook,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
 
		List<ManualTradesReportBean> ManualTrades = (List<ManualTradesReportBean>) model.get("ManualTrades");
		//create a wordsheet
		HSSFSheet sheet = workbook.createSheet("Manual Trades Report");
 
		HSSFCellStyle cellStyle = workbook.createCellStyle();
		 cellStyle.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
		
		 HSSFCellStyle cellStyle1 = workbook.createCellStyle();
		 cellStyle1.setWrapText(true);
		HSSFRow header = sheet.createRow(0);
		header.setRowStyle(cellStyle1);
		header.createCell(0).setCellValue("Transaction Type");
		header.createCell(1).setCellValue("Currency");
		header.createCell(2).setCellValue("FX Forwards Trade Date");
		header.createCell(3).setCellValue("End Date");
		header.createCell(4).setCellValue("Notional Principal($)");
		header.createCell(5).setCellValue("Contra. FX Rate");
		header.createCell(6).setCellValue("Spot Rate");
		header.createCell(7).setCellValue("Notional Principal * Contra. FX Rate");
		header.createCell(8).setCellValue("Notional Principal * Spot Rate");
		header.createCell(9).setCellValue("Broker Name");
		int rowNum = 1;
		for (ManualTradesReportBean entry : ManualTrades) {
			//create the row data
			Float notionalPrincipal;
			String currency=null;
			if(entry.getFROM_CURRENCY()!=null && entry.getFROM_CURRENCY().trim().compareTo(constants.CodeUSD)==0)
			{
				notionalPrincipal=entry.getFROM_AMOUNT();
				currency=entry.getFROM_CURRENCY();
			}
			else if(entry.getTO_CURRENCY()!=null && entry.getTO_CURRENCY().trim().compareTo(constants.CodeUSD)==0)
			{
				notionalPrincipal=entry.getTO_AMOUNT();
				currency=entry.getTO_CURRENCY();
			}
			else
			{
				notionalPrincipal=new Float(0);
			}
			
			NumberFormat numberFormat = NumberFormat.getInstance();
			 
		    numberFormat.setMinimumFractionDigits(2);
		    numberFormat.setMaximumFractionDigits(2);
		 
		    numberFormat.setMinimumIntegerDigits(1);
		    numberFormat.setMaximumIntegerDigits(10);
		    
		    numberFormat.setGroupingUsed(true);
		    
		    NumberFormat numberFormat1 = NumberFormat.getInstance();
			 
		    numberFormat1.setMinimumFractionDigits(8);
		    numberFormat1.setMaximumFractionDigits(8);
		 
		    numberFormat1.setMinimumIntegerDigits(1);
		    numberFormat1.setMaximumIntegerDigits(10);
		 
		    numberFormat1.setGroupingUsed(true);
			
			HSSFRow row = sheet.createRow(rowNum++);
			
			if(entry.getTRAN_TYPE()!=null){row.createCell(0).setCellValue(entry.getTRAN_TYPE());}
			if(currency!=null){row.createCell(1).setCellValue(currency);}
			if(entry.getTRADE_DATE()!=null){row.createCell(2).setCellValue(entry.getTRADE_DATE().toString().substring(0, 10));}
			if(entry.getSETTLE_DATE()!=null){row.createCell(3).setCellValue(entry.getSETTLE_DATE().toString().substring(0, 10));}
			row.createCell(4).setCellValue(numberFormat.format(notionalPrincipal));
			String fx_rate="";
			if(entry.getFX_RATE()!=null){fx_rate=numberFormat1.format(entry.getFX_RATE());}
			row.createCell(5).setCellValue(fx_rate);
			String spot_rate="";
			if(entry.getSPOT_RATE()!=null){spot_rate=numberFormat1.format(entry.getSPOT_RATE());}
			row.createCell(6).setCellValue(spot_rate);
			String fx_np="";
			if(notionalPrincipal!=null && entry.getFX_RATE()!=null){fx_np=numberFormat.format(notionalPrincipal*entry.getFX_RATE());}
			row.createCell(7).setCellValue(fx_np);			
			String spot_np="";
			if(notionalPrincipal!=null && entry.getSPOT_RATE()!=null){spot_np=numberFormat.format(notionalPrincipal*entry.getSPOT_RATE());}
			row.createCell(8).setCellValue(spot_np);
			if(entry.getBROKER()!=null){row.createCell(9).setCellValue(entry.getBROKER());}

						
			row.getCell(4).setCellStyle(cellStyle);
			row.getCell(5).setCellStyle(cellStyle);
			row.getCell(6).setCellStyle(cellStyle);
			row.getCell(7).setCellStyle(cellStyle);
			row.getCell(8).setCellStyle(cellStyle);
			
		}
	}
}
