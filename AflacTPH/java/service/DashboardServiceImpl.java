package com.aflac.aims.tph.web.service;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aflac.aims.tph.web.dao.DashboardDAO;
import com.aflac.aims.tph.web.dao.TradeInDAO;
import com.aflac.aims.tph.web.model.DashboardBean;
import com.aflac.aims.tph.web.model.HeartBeatStatusBean;
import com.aflac.aims.tph.web.model.SecurityDashboardBean;
import com.aflac.aims.tph.web.model.StatusIndicatorBean;
import com.aflac.aims.tph.web.model.TradeDashboardBean;
import com.aflac.aims.tph.web.utils.constants;

@Service("DashboardServiceImpl")
public class DashboardServiceImpl implements DashboardService {
	
	@Autowired
	private DashboardDAO dashboardDAO;
	
	@Autowired
	private TradeInDAO tradeInDAO;

	@Override
	public List<TradeDashboardBean> getTradeDashboardForSource()  throws Exception{
		try{
		String[] sourcelist = {constants.SOURCE_GSAM,constants.SOURCE_BR};
		List<TradeDashboardBean> tradeDashboard = new ArrayList<TradeDashboardBean>();
		for(String source : sourcelist)
		{
			TradeDashboardBean tradeDashboardBean =new TradeDashboardBean();
			tradeDashboardBean.setSource(source);
			tradeDashboardBean.setCAMRAlastProcessDate(dashboardDAO.getCAMRAlastProcessDate());
			//List<String> accounts=dashboardDAO.getAccountListForSource(source);
			//List<IncomingTradeCountsBean> incomingTradeCounts=new ArrayList<IncomingTradeCountsBean>();
			//for(String acct:accounts){
			//	incomingTradeCounts=dashboardDAO.getTradeCounts(source);
			//}
			tradeDashboardBean.setIncomingTradeCountsForAccount(dashboardDAO.getTradeCounts(source));
			tradeDashboardBean.setOutgoingCAMRAJPTradeCountsForAccount(dashboardDAO.getCAMRAJPTradeCounts(source));
			tradeDashboardBean.setOutgoingCAMRAUSTradeCountsForAccount(dashboardDAO.getCAMRAUSTradeCounts(source));
			tradeDashboard.add(tradeDashboardBean);
			
			
		}
		return tradeDashboard;
		}
		catch(Exception e){
			throw e;
		}
	}
	

	@Override
	public List<SecurityDashboardBean> getSecurityDashboardForSource()  throws Exception{
		// TODO Auto-generated method stub
		try{
			String[] sourcelist = {constants.SOURCE_GSAM,constants.SOURCE_BR};
			List<SecurityDashboardBean> secDashboard = new ArrayList<SecurityDashboardBean>();
			for(String source : sourcelist)
			{
				SecurityDashboardBean secDashboardBean =new SecurityDashboardBean();
				secDashboardBean.setSource(source);
				secDashboardBean.setCAMRAlastProcessDate(dashboardDAO.getCAMRAlastProcessDate());
				
				secDashboardBean.setIncomingSecurityCounts(dashboardDAO.getInSecurityCounts(source));
				secDashboardBean.setOutgoingCAMRAJPSecurityCounts(dashboardDAO.getOutCAMRAJPSecurityCounts(source));
				secDashboardBean.setOutgoingCAMRAUSSecurityCounts(dashboardDAO.getOutCAMRAUSSecurityCounts(source));
				secDashboard.add(secDashboardBean);
			}
			return secDashboard;
			}
			catch(Exception e){
				throw e;
			}
	}
	
	@Override
	public List<DashboardBean> getIncomingTradesDashboardData()  throws Exception{
		try{
		List<DashboardBean> incomingTradesDashboardList = new ArrayList<DashboardBean>();
		incomingTradesDashboardList = dashboardDAO.getIncomingTradeCounts();
		return incomingTradesDashboardList;
		}
		catch(Exception e){
			throw e;
		}
	}

	@Override
	public List<DashboardBean> getOutgoingTradesDashboardData(String destination)  throws Exception{
		try{
			List<DashboardBean> outgoingTradesDashboardList = new ArrayList<DashboardBean>();
			outgoingTradesDashboardList = dashboardDAO.getOutgoingTradeCounts(destination);
			return outgoingTradesDashboardList;
			}
			catch(Exception e){
				throw e;
			}
	}
	
	@Override
	public List<DashboardBean> getIncomingSecurityDashboardData()  throws Exception{
		try{
		List<DashboardBean> incomingSecurityDashboardList = new ArrayList<DashboardBean>();
		incomingSecurityDashboardList = dashboardDAO.getIncomingSecurityCounts();
		return incomingSecurityDashboardList;
		}
		catch(Exception e){
			throw e;
		}
	}
	
	@Override
	public List<DashboardBean> getOutgoingSecurityDashboardData(String destination)  throws Exception{
		try{
			List<DashboardBean> outgoingSecurityDashboardList = new ArrayList<DashboardBean>();
			outgoingSecurityDashboardList = dashboardDAO.getOutgoingSecurityCounts(destination);
			return outgoingSecurityDashboardList;
			}
			catch(Exception e){
				throw e;
			}
	}


	@Override
	public Integer getHeartBeatForSystem(String systemName) throws Exception {
		try{
		HeartBeatStatusBean heartBeatStatus = dashboardDAO.getHeartBeatStatus(systemName);
			if(heartBeatStatus!=null){
				Integer Status = (heartBeatStatus.getStatus().equalsIgnoreCase("ALIVE"))?constants.HEARTBEAT_CODE_ALIVE:constants.HEARTBEAT_CODE_DEAD;
				Date currentDate=new Date();
				if(Status==2 && currentDate.getTime()-heartBeatStatus.getLastUpdTime().getTime()>(constants.HEARTBEAT_INTERVAL_DEAD)){
					Status=constants.HEARTBEAT_CODE_DEAD;
				}
				if(Status==2 && currentDate.getTime()-heartBeatStatus.getLastUpdTime().getTime()>(constants.HEARTBEAT_INTERVAL_STANDBY)){
					Status=constants.HEARTBEAT_CODE_STANDBY;
				}
				return Status;
			}
			else{
				return constants.HEARTBEAT_CODE_DEAD;
			}
		}
		catch(Exception e){
			throw e;
		}
	}


	@Override
	public Integer getDataStatusForSystem(String systemName, String cntType)
			throws Exception {
		// TODO Auto-generated method stub
		try{
			StatusIndicatorBean status=dashboardDAO.getDataStatusIndicatorBean(systemName,cntType);
			if(status.getFailedCnt().intValue()>0){
				return constants.INDICATOR_RED;
			}
			else if(status.getManualCnt().intValue()>0){
				return constants.INDICATOR_ORANGE;
			}
			else if(status.getPendingCnt().intValue()>0){
				return constants.INDICATOR_BLUE;
			}
			else if(status.getProcessedCnt().intValue()>0){
				return constants.INDICATOR_GREEN;
			}
			return null;
		}
		catch(Exception e){
			throw e;
		}
		
	}

}
