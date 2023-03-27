package com.aflac.aims.tph.web.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.aflac.aims.tph.web.model.TradeSummaryBean;

public interface TradeSummaryDAO {
	public List<TradeSummaryBean> getTradeSummaryList() throws Exception;

	public List<TradeSummaryBean> getTradeSummaryListForSource(String source) throws Exception;

	public List<TradeSummaryBean> getFilteredTSList(Date tradeDate) throws Exception;

	public List<TradeSummaryBean> getFilteredTrades(
			Map<String, Object> filterMap) throws Exception;


}
