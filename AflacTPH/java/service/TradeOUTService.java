package com.aflac.aims.tph.web.service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Service;

import com.aflac.aims.tph.web.model.FieldMappingBean;
import com.aflac.aims.tph.web.model.ManualTradesReportBean;
import com.aflac.aims.tph.web.model.TradeOUTBean;
import com.aflac.aims.tph.web.model.TradeSummaryBean;

@Service
public interface TradeOUTService {
public List<TradeOUTBean> getOUTTrades() throws Exception;

public List<TradeOUTBean> getOutTradeDetailsByID(int tradeRefNo, String str_tran_type, String str_dest, int touchCount, String source) throws Exception;

public List<TradeSummaryBean> filterByDates(List<TradeSummaryBean> trades,
		Date fromDate, Date toDate) throws Exception;

//public int resendTrade(int tradeRefNo, String str_trade_type, int touchCount, String str_dest);

public HashMap<String, Integer> resendTrade(int [] tradeRefNo, String [] str_trade_type, int [] touchCount, String str_dest);

//public int discardTrade(int tradeRefNo, String str_trade_type, int touchCount, String str_dest);

public HashMap<String, Integer> discardTrade(int [] tradeRefNo, String [] str_trade_type, int [] touchCount, String str_dest);

public List<TradeOUTBean> filterFXTradesByDates(List<TradeOUTBean> trades,
		Date fromDate, Date toDate) throws Exception;

public List<TradeOUTBean> getFXTradesForSource(
		String source) throws Exception;

public List<ManualTradesReportBean> getManualTradesForReport(int[] tradeIDs) throws Exception;

public List<TradeSummaryBean> applyDisplayStatus(List<TradeSummaryBean> trades) throws Exception;

public List<FieldMappingBean> getDestinationValueforFieldMap(
		List<FieldMappingBean> fieldMap, int tradeRefNo, int touchCount) throws Exception;

public List<TradeOUTBean> applyFullStatus(List<TradeOUTBean> tradeList) throws Exception;
public String getErrorMsg(int tradeRefNo, String tranType, String dest);
}