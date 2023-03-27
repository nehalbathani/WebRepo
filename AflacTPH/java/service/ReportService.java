package com.aflac.aims.tph.web.service;

import net.sf.jasperreports.engine.JRDataSource;

import org.springframework.stereotype.Service;

@Service
public interface ReportService {
	JRDataSource getTradeHeaderDataSource(int tradeRefNo, int touchCount, String dest, String tranType) throws Exception;

	JRDataSource getBondTradeSubreportDataSource(int tradeRefNo, int touchCount, String dest) throws Exception;

	JRDataSource getFXTradeSubreportDataSource(int tradeRefNo, int touchCount, String dest) throws Exception;
	
	JRDataSource getOutgoingManualTradeDataSource(int[] tradeIDs) throws Exception;
	
	JRDataSource getSingleDayTradeInDataSource() throws Exception;
}
