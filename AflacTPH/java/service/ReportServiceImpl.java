package com.aflac.aims.tph.web.service;

import java.util.ArrayList;
import java.util.List;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRField;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aflac.aims.tph.web.dao.ReportDAO;
import com.aflac.aims.tph.web.model.ManualTradesReportBean;



@Service("ReportServiceImpl")
public class ReportServiceImpl implements ReportService {
	@Autowired
	ReportDAO reportDAO;
	
	
	@Override
	public JRDataSource getTradeHeaderDataSource(int tradeRefNo, int touchCount, String dest,String tranType) throws Exception {
		// TODO Auto-generated method stub
		try{
			JRDataSource ds = new JRBeanCollectionDataSource(reportDAO.getTradeHeaderBean(tradeRefNo,touchCount,dest,tranType)); 	 
			return ds;
		}
		catch(Exception e){
			throw e;
		}
	}


	@Override
	public JRDataSource getBondTradeSubreportDataSource(int tradeRefNo,
			int touchCount,String dest) throws Exception {
		// TODO Auto-generated method stub
		try{
			JRDataSource ds = new JRBeanCollectionDataSource(reportDAO.getBondTradeReportBean(tradeRefNo,touchCount,dest)); 	 
			return ds;
		}
		catch(Exception e){
			throw e;
		}
	}


	@Override
	public JRDataSource getFXTradeSubreportDataSource(int tradeRefNo,
			int touchCount,String dest) throws Exception {
		// TODO Auto-generated method stub
		try{
			JRDataSource ds = new JRBeanCollectionDataSource(reportDAO.getFXTradeReportBean(tradeRefNo,touchCount,dest)); 	 
			return ds;
		}
		catch(Exception e){
			throw e;
		}
	}
	
	@Override
	public JRDataSource getOutgoingManualTradeDataSource(int[] tradeIDs) throws Exception {
		// TODO Auto-generated method stub
		try {
			List<ManualTradesReportBean> manualTrades = new ArrayList();
			for (int id:tradeIDs) {
				manualTrades.addAll(reportDAO.getManualTradeReportForID(id));
			}
			JRDataSource ds = new JRBeanCollectionDataSource(manualTrades); 	 
			return ds;
		}
		catch(Exception e){
			//TODO Handle Exceptional Handling
			throw e;
		}
	}
	
	@Override
	public JRDataSource getSingleDayTradeInDataSource() throws Exception {
		// TODO Auto-generated method stub
		try {
			JRDataSource ds = new JRBeanCollectionDataSource(reportDAO.getSingleDayTradeInReport());
			return ds;
		}
		catch(Exception e){
			//TODO Handle Exceptional Handling
			throw e;
		}
	}
}
