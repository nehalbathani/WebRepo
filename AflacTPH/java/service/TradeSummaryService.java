package com.aflac.aims.tph.web.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.aflac.aims.tph.web.model.TradeSummaryBean;

@Service
public interface TradeSummaryService {
	public List<TradeSummaryBean> getTradesSummary() throws Exception;

	public List<TradeSummaryBean> getTradesSummaryBySource(String string) throws Exception;

	public List<TradeSummaryBean> getFilteredTradeSummary(Date tradeDate) throws Exception;

	public List<TradeSummaryBean> getFilteredTrades(
			Map<String, Object> filterMap) throws Exception;
}
