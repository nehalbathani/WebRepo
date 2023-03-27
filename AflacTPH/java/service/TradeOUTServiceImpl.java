package com.aflac.aims.tph.web.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aflac.aims.tph.web.dao.TradeOUTDAO;
import com.aflac.aims.tph.web.model.FieldMappingBean;
import com.aflac.aims.tph.web.model.ManualTradesReportBean;
import com.aflac.aims.tph.web.model.TradeOUTBean;
import com.aflac.aims.tph.web.model.TradeSummaryBean;
import com.aflac.aims.tph.web.utils.constants;

@Service("TradeOUTServiceImpl")
public class TradeOUTServiceImpl implements TradeOUTService{
	protected static Logger logger = LoggerFactory.getLogger(TradeOUTServiceImpl.class);
	
	@Autowired
	private TradeOUTDAO tradeoutDAO;
	
	
	@Override
	public List<TradeOUTBean> getOUTTrades()  throws Exception{
		// TODO Auto-generated method stub
		List<TradeOUTBean> ls = tradeoutDAO.getTradeOUTBeanList();
		
		return ls;
	}


	@Override
	public List<TradeOUTBean> getOutTradeDetailsByID(int tradeRefNo, String tranType, String dest,int touchCount, String source)  throws Exception{
		// TODO Auto-generated method stub
		return tradeoutDAO.getOutTradeByID(tradeRefNo,tranType,dest,touchCount,source);
	}


	@Override
	public List<TradeSummaryBean> filterByDates(List<TradeSummaryBean> trades,
			Date fromDate, Date toDate)  throws Exception{
		// TODO Auto-generated method stub
		
		List<TradeSummaryBean> filteredTrades=new ArrayList<TradeSummaryBean>();
		try{
			for(TradeSummaryBean trd:trades){
				 Calendar cal = Calendar.getInstance();
			        cal.setTime(trd.getTradeDate());
			        cal.set(Calendar.HOUR_OF_DAY, 0);
			        cal.set(Calendar.MINUTE, 0);
			        cal.set(Calendar.SECOND, 0);
			        cal.set(Calendar.MILLISECOND, 0);
			        Date tradeDate=cal.getTime();
				if(!fromDate.after(tradeDate) && !toDate.before(tradeDate)){
					filteredTrades.add(trd);
				}
			}
			return filteredTrades;
		}
		catch(Exception e){
			throw e;
		}
	}

	/*@Override
	public int resendTrade(int tradeRefNo, String str_trade_type, int touchCount,String dest){
		// TODO Auto-generated method stub
		try {
			return tradeoutDAO.resendTrade(tradeRefNo,str_trade_type,touchCount,dest);
		} catch(Exception e){
			logger.error("Exception:", e);
			return -1;
		}
		
	}*/
	
	@Override
	public HashMap<String, Integer> resendTrade(int [] tradeRefNos, 
			                         String [] tradeTypeArr, int [] touchCounts, String dest) {
		// TODO Auto-generated method stub
		HashMap<String, Integer> resultMap = new HashMap<String, Integer>(10);
		try {
			int ind = 0;
			for (int id:tradeRefNos) {
				int status = tradeoutDAO.resendTrade(id, tradeTypeArr[ind], touchCounts[ind], dest);
				String val = String.valueOf(id) + ":" + String.valueOf(ind);
				resultMap.put(val, new Integer(status));
				ind++;
			}
		}
		catch(Exception e){
			//TODO Handle Exceptional Handling
			throw e;
		}
		return resultMap;
	}

	/*@Override
	public int discardTrade(int tradeRefNo, String str_trade_type,int touchCount,String dest) {
		// TODO Auto-generated method stub
		try {
			return tradeoutDAO.discardTrade(tradeRefNo,str_trade_type,touchCount,dest);
		} catch(Exception e) {
			logger.error("Exception:", e);
			return 0;
		}
	}*/
	
	@Override
	public HashMap<String, Integer> discardTrade(int [] tradeRefNos, 
			                         String [] tradeTypeArr, 
			                         int [] touchCounts, String dest) {
		// TODO Auto-generated method stub
		HashMap<String, Integer> resultMap = new HashMap<String, Integer>(10);
		try {
			int ind = 0;
			for (int id:tradeRefNos) {
				int status = tradeoutDAO.discardTrade(id, tradeTypeArr[ind], touchCounts[ind], dest);
				String val = String.valueOf(id) + ":" + String.valueOf(ind);
				resultMap.put(val, new Integer(status));
				ind++;
			}
		}
		catch(Exception e){
			//TODO Handle Exceptional Handling
			throw e;
		}
		return resultMap;
	}

/*
	@Override
	public List<TradeINBean> filterFXTradesByDates(List<TradeINBean> trades,
			Date fromDate, Date toDate) {

		List<TradeINBean> filteredTrades=new ArrayList<TradeINBean>();
		try{
			for(TradeINBean trd:trades){
				 Calendar cal = Calendar.getInstance();
			        cal.setTime(trd.getTRADE_DATE());
			        cal.set(Calendar.HOUR_OF_DAY, 0);
			        cal.set(Calendar.MINUTE, 0);
			        cal.set(Calendar.SECOND, 0);
			        cal.set(Calendar.MILLISECOND, 0);
			        Date tradeDate=cal.getTime();
				if(!fromDate.after(tradeDate) && !toDate.before(tradeDate)){
					filteredTrades.add(trd);
				}
			}
			return filteredTrades;
		}
		catch(Exception e){
			return null;
		}
	
	
		// TODO Auto-generated method stub
		
	}
*/

	@Override
	public List<TradeOUTBean> filterFXTradesByDates(List<TradeOUTBean> trades,
			Date fromDate, Date toDate)  throws Exception{
		// TODO Auto-generated method stub
		List<TradeOUTBean> filteredTrades=new ArrayList<TradeOUTBean>();
		try{
			for(TradeOUTBean trd:trades){
				 Calendar cal = Calendar.getInstance();
			        cal.setTime(trd.getTRADE_DATE());
			        cal.set(Calendar.HOUR_OF_DAY, 0);
			        cal.set(Calendar.MINUTE, 0);
			        cal.set(Calendar.SECOND, 0);
			        cal.set(Calendar.MILLISECOND, 0);
			        Date tradeDate=cal.getTime();
				if(!fromDate.after(tradeDate) && !toDate.before(tradeDate)){
					filteredTrades.add(trd);
				}
			}
			return filteredTrades;
		}
		catch(Exception e){
			throw e;
		}
	}


	@Override
	public List<TradeOUTBean> getFXTradesForSource(String source)  throws Exception{
		// TODO Auto-generated method stub
		return tradeoutDAO.getManualTrades(source);
	}


	@Override
	public List<ManualTradesReportBean> getManualTradesForReport(int[] tradeIDs)  throws Exception{
		// TODO Auto-generated method stub
		try{
		List<ManualTradesReportBean> trades=new ArrayList();
		for(int id:tradeIDs){
			trades.addAll(tradeoutDAO.getManualTradeReportForID(id));
		}
		return trades;
		}
		catch(Exception e){
			throw e;
		}
	}


	@Override
	public List<TradeSummaryBean> applyDisplayStatus(
			List<TradeSummaryBean> trades)  throws Exception{
		// TODO Auto-generated method stub
		
		try{
			for(TradeSummaryBean trd:trades){
				if(trd.getTPH_STATUS()!=null){
					if(trd.getTPH_STATUS().equals(constants.TRADE_OUT_STATUS_P)){
						trd.setTPH_STATUS(constants.TRADE_OUT_DISPLAY_STATUS_P);
					}
					if(trd.getTPH_STATUS().equals(constants.TRADE_OUT_STATUS_F)){
						trd.setTPH_STATUS(constants.TRADE_OUT_DISPLAY_STATUS_F);
					}
					if(trd.getTPH_STATUS().equals(constants.TRADE_OUT_STATUS_M)){
						trd.setTPH_STATUS(constants.TRADE_OUT_DISPLAY_STATUS_M);
					}
					if(trd.getTPH_STATUS().equals(constants.TRADE_OUT_STATUS_PND)){
						trd.setTPH_STATUS(constants.TRADE_OUT_DISPLAY_STATUS_PND);
					}
					
				}
			}
			return trades;
			}
			catch(Exception e){
				throw e;
			}
	}


	@Override
	public List<FieldMappingBean> getDestinationValueforFieldMap(
			List<FieldMappingBean> fieldMap, int tradeRefNo,int touchCount)  throws Exception{
		// TODO Auto-generated method stub
		if(tradeoutDAO.isExistAtDest(tradeRefNo,touchCount)==true){
		return tradeoutDAO.getDestinationValuesForMap(fieldMap,  tradeRefNo,touchCount);
		}
		else{
			return fieldMap;
		}
	}

	public List<TradeOUTBean> applyFullStatus(List<TradeOUTBean> tradeList)  throws Exception{
		// TODO Auto-generated method stub
		for(TradeOUTBean trade:tradeList){
			if(trade.getTPH_STATUS().equals(constants.TRADE_OUT_STATUS_P)){
				trade.setTPH_STATUS(constants.TRADE_OUT_DISPLAY_STATUS_P);
			}else if(trade.getTPH_STATUS().equals(constants.TRADE_OUT_STATUS_PND)){
				trade.setTPH_STATUS(constants.TRADE_OUT_DISPLAY_STATUS_PND);
			}else if(trade.getTPH_STATUS().equals(constants.TRADE_OUT_STATUS_F)){
				trade.setTPH_STATUS(constants.TRADE_OUT_DISPLAY_STATUS_F);
			}else if(trade.getTPH_STATUS().equals(constants.TRADE_OUT_STATUS_M)){
				trade.setTPH_STATUS(constants.TRADE_OUT_DISPLAY_STATUS_M);
			}else{
				
			}
		}
		return tradeList;
	}
	

	@Override
	public String getErrorMsg(int tradeRefNo, String tranType, String dest) {
		// TODO Auto-generated method stub
		try{
			return tradeoutDAO.getErrorMsg(tradeRefNo,tranType,dest);
		}
		catch(Exception e){
			return "Service failed to return error message!";
		}
	}
	
	
}
