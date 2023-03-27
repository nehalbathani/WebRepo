package com.aflac.aims.tph.web.dao;

import java.util.List;

import com.aflac.aims.tph.web.model.ManualTradesReportBean;
import com.aflac.aims.tph.web.model.TradeInReportBean;
import com.aflac.aims.tph.web.model.TradeReportBean;
import com.aflac.aims.tph.web.model.TradeReportBondBean;
import com.aflac.aims.tph.web.model.TradeReportFXBean;


public interface ReportDAO {
	List<TradeReportBean> getTradeHeaderBean(int tradeRefNo, int touchCount,String dest,String tranType) throws Exception;

	List<TradeReportBondBean> getBondTradeReportBean(int tradeRefNo, int touchCount,String dest) throws Exception;

	List<TradeReportFXBean> getFXTradeReportBean(int tradeRefNo, int touchCount,String dest) throws Exception;
    
	List<ManualTradesReportBean> getManualTradeReportForID(int id) throws Exception;
	
	List<TradeInReportBean> getSingleDayTradeInReport() throws Exception;
}
