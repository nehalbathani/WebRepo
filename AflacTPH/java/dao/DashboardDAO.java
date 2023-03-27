package com.aflac.aims.tph.web.dao;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.aflac.aims.tph.web.model.DashboardBean;
import com.aflac.aims.tph.web.model.HeartBeatStatusBean;
import com.aflac.aims.tph.web.model.IncomingSecurityCountsBean;
import com.aflac.aims.tph.web.model.IncomingTradeCountsBean;
import com.aflac.aims.tph.web.model.OutgoingCAMRAJPSecurityCountsBean;
import com.aflac.aims.tph.web.model.OutgoingCAMRAJPTradeCountsBean;
import com.aflac.aims.tph.web.model.OutgoingCAMRAUSSecurityCountsBean;
import com.aflac.aims.tph.web.model.OutgoingCAMRAUSTradeCountsBean;
import com.aflac.aims.tph.web.model.StatusIndicatorBean;

@Repository
public interface DashboardDAO {
	public int getCntForProcessedTrades(String source);

	public int getCntForTotalTrades(String string);

//	public int getCntForInprocessTrades(String source);

	public int getCntForFailedTrades(String source);

	public int getCntForTotalCamraJPTrades(String source);

	public int getCntForFailedCamraJPTrades(String source);

	public int getCntForProcessedCamraJPTrades(String source);

	public int getCntForTotalCamraUSTrades(String source);

	public int getCntForFailedCamraUSTrades(String source);

	public int getCntForProcessedCamraUSTrades(String source);

	public int getCntForManualTrades(String source);

	public int getCntForPendingCamraJPTrades(String source);

	public int getCntForManualCamraJPTrades(String source);

	public int getCntForPendingCamraUSTrades(String source);

	public int getCntForManualCamraUSTrades(String source);

	public List<String> getAccountListForSource(String source) throws Exception;

	public List<IncomingTradeCountsBean> getTradeCounts(String source) throws Exception;

	public Date getCAMRAlastProcessDate() throws Exception;

	List<OutgoingCAMRAJPTradeCountsBean> getCAMRAJPTradeCounts(String source) throws Exception;

	List<OutgoingCAMRAUSTradeCountsBean> getCAMRAUSTradeCounts(String source) throws Exception;

	public List<IncomingSecurityCountsBean> getInSecurityCounts(String source) throws Exception;

	public List<OutgoingCAMRAJPSecurityCountsBean> getOutCAMRAJPSecurityCounts(String source) throws Exception;
	
	public List<OutgoingCAMRAUSSecurityCountsBean> getOutCAMRAUSSecurityCounts(String source) throws Exception;

	public List<DashboardBean> getIncomingTradeCounts() throws Exception;
	
	public List<DashboardBean> getOutgoingTradeCounts(String destination) throws Exception;
	
	public List<DashboardBean> getIncomingSecurityCounts() throws Exception;
	
	public List<DashboardBean> getOutgoingSecurityCounts(String destination) throws Exception;
	
	public HeartBeatStatusBean getHeartBeatStatus(String systemName);

	public StatusIndicatorBean getDataStatusIndicatorBean(String systemName, String cntType) throws Exception;
}
