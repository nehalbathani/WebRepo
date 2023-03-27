package com.aflac.aims.tph.web.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.aflac.aims.tph.web.model.DashboardBean;
import com.aflac.aims.tph.web.model.SecurityDashboardBean;
import com.aflac.aims.tph.web.model.TradeDashboardBean;
@Service
public interface DashboardService {

	public List<TradeDashboardBean> getTradeDashboardForSource() throws Exception;

	public List<SecurityDashboardBean> getSecurityDashboardForSource() throws Exception;
	
	public List<DashboardBean> getIncomingTradesDashboardData() throws Exception;
	
	public List<DashboardBean> getOutgoingTradesDashboardData(String destination) throws Exception;
	
	public List<DashboardBean> getIncomingSecurityDashboardData() throws Exception;
	
	public List<DashboardBean> getOutgoingSecurityDashboardData(String destination) throws Exception;
	
	public Integer getHeartBeatForSystem(String systemName) throws Exception;

	public Integer getDataStatusForSystem(String systemName, String cntType) throws Exception;
	
}
