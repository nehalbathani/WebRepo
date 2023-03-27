package com.aflac.aims.tph.web.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aflac.aims.tph.web.dao.TradeSummaryDAO;
import com.aflac.aims.tph.web.model.TradeSummaryBean;

@Service("TradSummaryServiceImpl")
public class TradeSummaryServiceImpl implements TradeSummaryService {

	@Autowired
	private TradeSummaryDAO tradeSummaryDAO;
	
	@Override
	public List<TradeSummaryBean> getTradesSummary()  throws Exception{
		// TODO Auto-generated method stub
		
		List<TradeSummaryBean> ls = tradeSummaryDAO.getTradeSummaryList();
		
		return ls;
		
		
	}

	@Override
	public List<TradeSummaryBean> getTradesSummaryBySource(String source)  throws Exception{
		// TODO Auto-generated method stub
		return tradeSummaryDAO.getTradeSummaryListForSource(source);
	}

	@Override
	public List<TradeSummaryBean> getFilteredTradeSummary(Date tradeDate)  throws Exception{
		// TODO Auto-generated method stub
		return tradeSummaryDAO.getFilteredTSList(tradeDate);
	}

	@Override
	public List<TradeSummaryBean> getFilteredTrades(
			Map<String, Object> filterMap) throws Exception {
		// TODO Auto-generated method stub
		
		//logger.info("Fetching FilteredTrade sumary for CAMRA");
		return tradeSummaryDAO.getFilteredTrades(filterMap);
	}

}
