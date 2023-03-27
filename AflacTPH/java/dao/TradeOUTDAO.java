package com.aflac.aims.tph.web.dao;

import java.util.List;

import com.aflac.aims.tph.web.model.FieldMappingBean;
import com.aflac.aims.tph.web.model.ManualTradesReportBean;
import com.aflac.aims.tph.web.model.TradeOUTBean;

public interface TradeOUTDAO {

	public List<TradeOUTBean> getTradeOUTBeanList() throws Exception;

	public List<TradeOUTBean> getOutTradeByID(int tradeRefNo, String tranType, String dest,int touchCount, String source) throws Exception;

	public int resendTrade(int tradeRefNo, String str_trade_type, int touchCount, String dest);

	public int discardTrade(int tradeRefNo, String str_trade_type,int touchCount, String dest);

	public List<TradeOUTBean> getManualTrades(String source) throws Exception;

	public List<ManualTradesReportBean> getManualTradeReportForID(
			int id) throws Exception;

	public List<FieldMappingBean> getDestinationValuesForMap(
			List<FieldMappingBean> fieldMap, int tradeRefNo,int touchCount) throws Exception;

	public boolean isExistAtDest(int tradeRefNo, int touchCount) throws Exception;

	public List<TradeOUTBean> getOutTradesByID(int tradeRefNo,String tran_TYPE, int touch_count) throws Exception;
	public String getErrorMsg(int tradeRefNo, String tranType, String dest);
}
