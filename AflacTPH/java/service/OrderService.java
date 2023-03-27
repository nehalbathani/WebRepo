package com.aflac.aims.tph.web.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.aflac.aims.tph.web.model.OrderBean;
import com.aflac.aims.tph.web.model.ShortOrderBean;

@Service
public interface OrderService {
	public List<OrderBean> getOrders()throws Exception ;

	List<ShortOrderBean> getOrdersForSource(String source) throws Exception;

	public List<ShortOrderBean> getFilteredShortOrders(Map<String, Object> filterMap) throws Exception;

	public List<ShortOrderBean> attachStatus(List<ShortOrderBean> orderList) throws Exception;

	public List<ShortOrderBean> filterByStatus(List<ShortOrderBean> orderList,
			String paramStatus) throws Exception;

	public List<ShortOrderBean> filterByDates(List<ShortOrderBean> orderList,
			Date fromDate, Date toDate) throws Exception;

	public List<ShortOrderBean> applyFullStatus(List<ShortOrderBean> orderList) throws Exception;

	
	
}
