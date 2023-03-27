package com.aflac.aims.tph.web.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aflac.aims.tph.web.dao.OrderDAO;
import com.aflac.aims.tph.web.dao.TradeInDAO;
import com.aflac.aims.tph.web.model.OrderBean;
import com.aflac.aims.tph.web.model.ShortOrderBean;
import com.aflac.aims.tph.web.model.TradeINBean;
import com.aflac.aims.tph.web.utils.constants;

@Service("OrderServiceImpl")
public class OrderServiceImpl implements OrderService{

	@Autowired
	private OrderDAO orderDAO;

	@Autowired
	TradeInDAO tradeInDAO;
	
	
	protected static Logger logger = LoggerFactory.getLogger("OrderService");
	
	@Override
	public List<OrderBean> getOrders() throws Exception{
		return orderDAO.getOrderList();
	}
	
	@Override
	public List<ShortOrderBean> getOrdersForSource(String source) throws Exception{
		try{
			logger.debug("Fetch order list for source");
			List<ShortOrderBean> orderList=orderDAO.getOrderListBySource(source.trim());
			for(ShortOrderBean order:orderList){
			 List<TradeINBean> trade=tradeInDAO.getTradeListforOrder(order.getOrderRefNo(),order.getTOUCH_COUNT());
			 order.setExecutionCurrency(trade.get(0).getTRD_CURRENCY());
			 if(trade.get(0) != null){
				 order.setFixedCurrency(trade.get(0).getFX_PAY_CURR());
				 order.setFloatCurrency(trade.get(0).getFX_RCV_CURR());
			 }
			}
			return orderList;
		}
		catch(Exception ex){
			throw ex;
		}
		
		
	}

	@Override
	public List<ShortOrderBean> getFilteredShortOrders(	Map<String, Object> filterMap) throws Exception{
		// TODO Auto-generated method stub
		logger.debug("Fetch filtered orderList");
		return orderDAO.getFilteredShortOrderList(filterMap);
	}

	@Autowired
	 private  TradeInService tradeInService;
	
	@Override
	public List<ShortOrderBean> attachStatus(List<ShortOrderBean> orderList) throws Exception{
		// TODO Auto-generated method stub
		try{
		for(ShortOrderBean ord:orderList){
    		ord.setStatus(constants.TRADE_STATUS1_P);
    		List<TradeINBean> tradeList=tradeInService.getTradesForOrder(ord.getOrderRefNo(),ord.getTOUCH_COUNT());
    		 if(tradeList.get(0) != null){
    		ord.setExecutionCurrency(tradeList.get(0).getTRD_CURRENCY());
    		 ord.setFixedCurrency(tradeList.get(0).getFX_PAY_CURR());
			 ord.setFloatCurrency(tradeList.get(0).getFX_RCV_CURR());
    		
    		 }
    		for(TradeINBean tr:tradeList){
    			if(tr.getSTATUS().equals(constants.TRADE_STATUS1_M) && ord.getStatus().equals(constants.TRADE_STATUS1_P)){
    				ord.setStatus(constants.TRADE_STATUS1_M);
    			}
    			if(tr.getSTATUS().equals(constants.TRADE_STATUS1_F)){
    			ord.setStatus(constants.TRADE_STATUS1_F);
    			}
    		}
    		
    	}
		return orderList;
		
		}
		catch(Exception e){
			throw e;
		}
		
	}

	@Override
	public List<ShortOrderBean> filterByStatus(List<ShortOrderBean> orderList,
			String paramStatus) throws Exception{
		// TODO Auto-generated method stub
		List<ShortOrderBean> filteredOrders=new ArrayList<ShortOrderBean>();
		try{
			for(ShortOrderBean ord:orderList){
				if(ord.getStatus().compareTo(paramStatus)==0){
					filteredOrders.add(ord);
				}
			}
			return filteredOrders;
		}
		catch(Exception e){
			throw e;
		}
		
	}

	@Override
	public List<ShortOrderBean> filterByDates(List<ShortOrderBean> orderList,
			Date fromDate, Date toDate) throws Exception{
		// TODO Auto-generated method stub
		List<ShortOrderBean> filteredOrders=new ArrayList<ShortOrderBean>();
		try{
			for(ShortOrderBean ord:orderList){
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

	@Override
	public List<ShortOrderBean> applyFullStatus(List<ShortOrderBean> orderList) throws Exception{
		// TODO Auto-generated method stub
		try{
			for(ShortOrderBean ord:orderList){
				if(ord.getStatus()!=null){
					if(ord.getStatus().equals(constants.TRADE_STATUS1_P)){
						ord.setStatus(constants.TRADE_STATUS_FULL_P);
					}
					if(ord.getStatus().equals(constants.TRADE_STATUS1_F)){
						ord.setStatus(constants.TRADE_STATUS_FULL_F);
					}
					if(ord.getStatus().equals(constants.TRADE_STATUS1_M)){
						ord.setStatus(constants.TRADE_STATUS_FULL_M);
					}
					if(ord.getStatus().equals(constants.TRADE_STATUS1_N)){
						ord.setStatus(constants.TRADE_STATUS_FULL_N);
					}
					if(ord.getStatus().equals(constants.TRADE_STATUS1_D)){
						ord.setStatus(constants.TRADE_STATUS_FULL_D);
					}
				}
			}
			return orderList;
			}
			catch(Exception e){
				throw e;
			}
	}
	
}
