package com.aflac.aims.tph.web.dao;

import java.util.List;
import java.util.Map;

import com.aflac.aims.tph.web.model.OrderBean;
import com.aflac.aims.tph.web.model.ShortOrderBean;

public interface OrderDAO {

	public List<OrderBean> getOrderList() throws Exception;

	public List<ShortOrderBean> getOrderListBySource(String source) throws Exception;

	public List<ShortOrderBean> getFilteredShortOrderList(Map<String, Object> filterMap) throws Exception;

}
