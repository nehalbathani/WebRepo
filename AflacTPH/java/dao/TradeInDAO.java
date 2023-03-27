package com.aflac.aims.tph.web.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.aflac.aims.tph.web.model.FieldMappingBean;
import com.aflac.aims.tph.web.model.ShortOrderBean;
import com.aflac.aims.tph.web.model.TradeINBean;
import com.aflac.aims.tph.web.model.TradeOverviewBean;

public interface TradeInDAO {
	public List<TradeINBean> getTradeListforOrder(int orderRefId, int touchCount) throws Exception;

	public List<TradeINBean> getTradeListByID(int tradeRefNo) throws Exception;

	public List<ShortOrderBean> getShortTradesForSource(String string) throws Exception;

	public List<TradeINBean> getFXTradeListForSource(String source) throws Exception;

	public List<TradeINBean> getFilteredFXTradeList(Date tradeDate)throws Exception ;
	public Date getLatestTradeDate() throws Exception;

	public int reprocessTrade(int tradeRefNo, int touchCount);

	public int cancelTrade(int tradeRefNo);

	public List<FieldMappingBean> getSourceValueForFieldMap(
			List<FieldMappingBean> fieldMap, int tradeRefNo, int touchCount) throws Exception;

	public TradeINBean getInTradeByID(int tradeRefNo, int touch_count) throws Exception;

	List<TradeOverviewBean> getFilteredTradeList(Map<String, Object> filterMap)
			throws Exception;

	}
