package com.aflac.aims.tph.web.service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.aflac.aims.tph.web.model.FieldMappingBean;
import com.aflac.aims.tph.web.model.ShortOrderBean;
import com.aflac.aims.tph.web.model.TradeINBean;
import com.aflac.aims.tph.web.model.TradeOverviewBean;

@Service
public interface TradeInService {

	List<TradeINBean> getTradesForOrder(int orderRefNo, int touchCount) throws Exception;

	List<TradeINBean> getInTradesByID(int tradeRefNo) throws Exception;

	List<ShortOrderBean> getShortOrdersForSource(String source) throws Exception;

	List<TradeINBean> getFXTradesForSource(String source) throws Exception;

	List<TradeINBean> getFilteredFXTrades(Date tradeDate) throws Exception;

	Date getLatestTradeDate() throws Exception;

	//int reprocessTrade(int tradeRefNo, int touchCount);
	
	HashMap<String, Integer> reprocessTrade(int [] tradeRefNos, int [] touchCounts);
	
	//int cancelTrade(int tradeRefNo);
	
	HashMap<String, Integer> cancelTrade(int [] tradeSeqNos);

	List<FieldMappingBean> getSourceValueforFieldMap(
			List<FieldMappingBean> fieldMap, int tradeRefNo, int touchCount) throws Exception;

	List<TradeINBean> applyFullStatus(List<TradeINBean> tradeList) throws Exception;

	List<TradeOverviewBean> getFilteredTrades(Map<String, Object> filterMap)
			throws Exception;

	List<TradeOverviewBean> filterByDates(List<TradeOverviewBean> tradeList,
			Date fromDate, Date toDate) throws Exception;


}
