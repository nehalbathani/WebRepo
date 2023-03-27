package com.aflac.aims.tph.web.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aflac.aims.tph.web.dao.TradeInDAO;
import com.aflac.aims.tph.web.model.FieldMappingBean;
import com.aflac.aims.tph.web.model.ShortOrderBean;
import com.aflac.aims.tph.web.model.TradeINBean;
import com.aflac.aims.tph.web.model.TradeOverviewBean;
import com.aflac.aims.tph.web.utils.constants;

@Service
public class TradeInServiceImpl implements TradeInService {

	@Autowired
	TradeInDAO tradeInDAO;
	
	@Override
	public List<TradeINBean> getTradesForOrder(int orderRefNo, int touchCount) throws Exception{
		return tradeInDAO.getTradeListforOrder(orderRefNo,touchCount);
	}

	@Override
	public Date getLatestTradeDate() throws Exception{
		return tradeInDAO.getLatestTradeDate();
	}
	
	@Override
	public List<TradeINBean> getInTradesByID(int tradeRefNo) throws Exception {
		// TODO Auto-generated method stub
		return tradeInDAO.getTradeListByID(tradeRefNo);
	}

	@Override
	public List<ShortOrderBean> getShortOrdersForSource(String source) throws Exception {
		// TODO Auto-generated method stub
		return tradeInDAO.getShortTradesForSource(source);
	}

	@Override
	public List<TradeINBean> getFXTradesForSource(String source) throws Exception {
		// TODO Auto-generated method stub
		return tradeInDAO.getFXTradeListForSource(source);
	}

	@Override
	public List<TradeINBean> getFilteredFXTrades(Date tradeDate) throws Exception {
		// TODO Auto-generated method stub
		return tradeInDAO.getFilteredFXTradeList(tradeDate);
	}
	
	@Override
	public HashMap<String, Integer> reprocessTrade(int [] tradeRefNos, int [] touchCounts) {
		// TODO Auto-generated method stub
		HashMap<String, Integer> resultMap = new HashMap<String, Integer>(10);
		try {
			int ind = 0;
			for (int id:tradeRefNos) {
				int tchCnt = touchCounts[ind];
				int status = tradeInDAO.reprocessTrade(id, tchCnt);
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
			
	@Override
	public HashMap<String, Integer> cancelTrade(int [] tradeSeqNos) {
		// TODO Auto-generated method stub
		HashMap<String, Integer> resultMap = new HashMap<String, Integer>(10);
		try {
			int ind = 0;
			for (int id:tradeSeqNos) {
				int status = tradeInDAO.cancelTrade(id);
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
	
	@Override
	public List<FieldMappingBean> getSourceValueforFieldMap(
			List<FieldMappingBean> fieldMap, int tradeRefNo,int touchCount)  throws Exception{
		// TODO Auto-generated method stub
		return tradeInDAO.getSourceValueForFieldMap(fieldMap,tradeRefNo,touchCount);
	}

	@Override
	public List<TradeOverviewBean> getFilteredTrades(	Map<String, Object> filterMap) throws Exception{
		// TODO Auto-generated method stub
		return tradeInDAO.getFilteredTradeList(filterMap);
	}

	
	@Override
	public List<TradeINBean> applyFullStatus(List<TradeINBean> tradeList)  throws Exception{
		// TODO Auto-generated method stub
		for(TradeINBean trade:tradeList){
			
			if(trade.getSTATUS().trim().equals(constants.TRADE_STATUS1_P)){
				trade.setSTATUS(constants.TRADE_OUT_DISPLAY_STATUS_P);
			}else if(trade.getSTATUS().trim().equals(constants.TRADE_STATUS1_N)){
				trade.setSTATUS(constants.TRADE_OUT_DISPLAY_STATUS_PND);
			}else if(trade.getSTATUS().trim().equalsIgnoreCase(constants.TRADE_STATUS1_F.trim())){
				System.out.println(trade.getSTATUS());
				trade.setSTATUS(constants.TRADE_OUT_DISPLAY_STATUS_F);
			}else if(trade.getSTATUS().trim().equals(constants.TRADE_STATUS1_M)){
				trade.setSTATUS(constants.TRADE_OUT_DISPLAY_STATUS_M);
			}else if(trade.getSTATUS().trim().equals(constants.TRADE_STATUS1_D)){
				trade.setSTATUS(constants.TRADE_OUT_DISPLAY_STATUS_D);
			}else{
				
			}
			
		}
		return tradeList;
	}
	
	@Override
	public List<TradeOverviewBean> filterByDates(List<TradeOverviewBean> tradeList,
			Date fromDate, Date toDate) throws Exception{
		// TODO Auto-generated method stub
		List<TradeOverviewBean> filteredOrders=new ArrayList<TradeOverviewBean>();
		try{
			for(TradeOverviewBean ord:tradeList){
				if(!fromDate.after(ord.getTradeDate()) && !toDate.before(ord.getTradeDate())){
					filteredOrders.add(ord);
				}
			}
			return filteredOrders;
		}
		catch(Exception e){
			throw e;
		}
	}
}
