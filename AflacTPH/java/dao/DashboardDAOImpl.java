package com.aflac.aims.tph.web.dao;

import java.sql.Types;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
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
import com.aflac.aims.tph.web.utils.constants;

@Repository
public class DashboardDAOImpl extends CommonDAO implements DashboardDAO {
	
	protected static Logger logger = LoggerFactory
			.getLogger(DashboardDAOImpl.class);

	private static final String SQL_INCOMING_TRADE_COUNTS = "select source, SUM(1) totalCnt, sum(CASE WHEN status = N'N' THEN 1 ELSE 0 END) pendingCnt, sum(CASE WHEN status = N'P' THEN 1 ELSE 0 END) processedCnt, sum(CASE WHEN status = N'M' THEN 1 ELSE 0 END) manualCnt, sum(CASE WHEN status = N'D' THEN 1 ELSE 0 END) deletedCnt, sum(CASE WHEN status = N'F' THEN 1 ELSE 0 END) failedCnt from TPH_TRADE_IN a , [dbo].[AIMS_PARAMETER] b where b.PARAM_NAME = 'CAMRA_LAST_PROCESSED_DATE' and a.TRADE_DATE > convert(datetime,b.param_val,111)  group by source";
	
	private static final String SQL_OUTGOING_TRADE_COUNTS = "select source_in as source, SUM(1) totalCnt, sum(CASE WHEN tph_status = N'READY' THEN 1 ELSE 0 END) pendingCnt, sum(CASE WHEN tph_status = N'COMPLETE' THEN 1 ELSE 0 END) processedCnt, sum(CASE WHEN tph_status = N'MANUAL' THEN 1 ELSE 0 END) manualCnt, sum(CASE WHEN tph_status = N'DELETED' THEN 1 ELSE 0 END) deletedCnt, sum(CASE WHEN tph_status = N'FAIL' THEN 1 ELSE 0 END) failedCnt from TPH_TRADE_OUT a , [dbo].[AIMS_PARAMETER] b where b.PARAM_NAME = 'CAMRA_LAST_PROCESSED_DATE' and a.TRADE_DATE > convert(datetime,b.param_val,111) and source_out=? group by source_in";
	
	private static final String SQL_INCOMING_SECURITY_COUNTS = "select source, SUM(1) totalCnt, sum(CASE WHEN status = N'N' THEN 1 ELSE 0 END) pendingCnt, sum(CASE WHEN status = N'P' THEN 1 ELSE 0 END) processedCnt, sum(CASE WHEN status = N'M' THEN 1 ELSE 0 END) manualCnt, sum(CASE WHEN status = N'D' THEN 1 ELSE 0 END) deletedCnt, sum(CASE WHEN status = N'F' THEN 1 ELSE 0 END) failedCnt from TPH_SECURITY_IN a , [dbo].[AIMS_PARAMETER] b where b.PARAM_NAME = 'CAMRA_LAST_PROCESSED_DATE' and a.TIME_STAMP > convert(datetime,b.param_val,111)  group by source";
	
	private static final String SQL_OUTGOING_SECURITY_COUNTS_FOR_SOURCE = "select source, SUM(1) totalCnt, sum(CASE WHEN tph_status = N'READY' THEN 1 ELSE 0 END) pendingCnt, sum(CASE WHEN tph_status = N'COMPLETE' THEN 1 ELSE 0 END) processedCnt, sum(CASE WHEN tph_status = N'MANUAL' THEN 1 ELSE 0 END) manualCnt, sum(CASE WHEN tph_status = N'DELETED' THEN 1 ELSE 0 END) deletedCnt, sum(CASE WHEN tph_status = N'FAIL' THEN 1 ELSE 0 END) failedCnt from TPH_SECURITY_OUT a , [dbo].[AIMS_PARAMETER] b where b.PARAM_NAME = 'CAMRA_LAST_PROCESSED_DATE' and a.LAST_UPD_DATE > convert(datetime,b.param_val,111) and SOURCE_OUT=? group by source";
	
	private static final String SQL_HEARTBEAT_FOR_SYSTEM = "SELECT PARAM_VAL as status, TIME_STAMP as lastUpdTime FROM AIMS_ENVIRONMENT where param_name='status' and PARAM_SYST_CD=?";
	
	private static final String SQL_CNT_TOTAL_TRADES = "SELECT COUNT(*) FROM [DBO].[TPH_TRADE_IN] WHERE source=?";
	
	private static final String SQL_CNT_PROCESSED_TRADES = "select count(*)"+
			" from dbo.TPH_TRADE_IN tr where STATUS = '"+constants.TRADE_STATUS1_P+"' AND tr.source = ?";
	
	private static final String SQL_CNT_FAILED_TRADES = "select count(*)"+
			" from dbo.TPH_TRADE_IN tr where STATUS = '"+constants.TRADE_STATUS1_F+"' AND tr.source = ?";

	private static final String SQL_CNT_MANUAL_TRADES = "select count(*)"+
			" from dbo.TPH_TRADE_IN tr where STATUS = '"+constants.TRADE_STATUS1_M+"' AND tr.source = ?";


	
	private static final String SQL_PROCESSED_TRADES_FOR_SOURCE ="select TPH_TRD_SEQ_NO,SOURCE as source,TRD_REF_NO as tradeRefNo,TRAN_TYPE as tradeType,"+
							" SEC_GROUP as securityGroup,SEC_TYPE_CD as securityType,"+
							" TRADE_DATE as tradeDate, SETTLE_DATE as settleDate, BROKER as broker,"+
							" TRD_CURRENCY as currency, STATUS as status "+
			" from dbo.TPH_TRADE_IN where STATUS = '"+constants.TRADE_STATUS1_P+"' AND SOURCE = ?";

	private static final String SQL_FAILED_TRADES_FOR_SOURCE = "select TPH_TRD_SEQ_NO,SOURCE as source,TRD_REF_NO as tradeRefNo,TRAN_TYPE as tradeType,"+
							" SEC_GROUP as securityGroup,SEC_TYPE_CD as securityType,"+
							" TRADE_DATE as tradeDate, SETTLE_DATE as settleDate, BROKER as broker,"+
							" TRD_CURRENCY as currency, STATUS as status "+
			" from dbo.TPH_TRADE_IN where STATUS = '"+constants.TRADE_STATUS1_F+"' AND source = ?";

	private static final String SQL_ALL_TRADE_FOR_SOURCE = "SELECT TPH_TRD_SEQ_NO,SOURCE as source,TRD_REF_NO as tradeRefNo,TRAN_TYPE as tradeType,"+
						" SEC_GROUP as securityGroup,SEC_TYPE_CD as securityType,"+ 
						" TRADE_DATE as tradeDate, SETTLE_DATE as settleDate, BROKER as broker,"+
						" TRD_CURRENCY as currency, STATUS as status  FROM [DBO].[TPH_TRADE_IN] WHERE source=?";

	private static final String SQL_CNT_ALL_CAMRA_JP_TRADES = "select count(T1.TPH_TRD_SEQ_NO) "+
							" from TPH_TRADE_OUT t1"+
							" where t1.SOURCE_OUT='"+constants.SOURCE_OUT_CAMRA_JP+"' and t1.SOURCE_IN=?";
			
		
	private static final String SQL_CNT_FAILED_CAMRA_JP_TRADES = "select count(T1.TPH_TRD_SEQ_NO) "+
							" from TPH_TRADE_OUT t1 "+
							" where t1.SOURCE_OUT='"+constants.SOURCE_OUT_CAMRA_JP+"' and T1.TPH_STATUS='"+constants.TRADE_OUT_STATUS_F+"' "+
							" and t1.SOURCE_IN=?";
			
			
			
	private static final String SQL_CNT_PROCESSED_CAMRA_JP_TRADES = "select count(T1.TPH_TRD_SEQ_NO) "+
							" from TPH_TRADE_OUT t1 "+
							" where t1.SOURCE_OUT='"+constants.SOURCE_OUT_CAMRA_JP+"' and T1.TPH_STATUS='"+constants.TRADE_OUT_STATUS_P+"'" +
									"and t1.SOURCE_IN=? ";
	
	private static final String SQL_CNT_PENDING_CAMRA_JP_TRADES = "select count(T1.TPH_TRD_SEQ_NO) "+
			" from TPH_TRADE_OUT t1 "+
			" where t1.SOURCE_OUT='"+constants.SOURCE_OUT_CAMRA_JP+"' and T1.TPH_STATUS='"+constants.TRADE_OUT_STATUS_PND+"'" +
					"and t1.SOURCE_IN=? ";
	
	private static final String SQL_CNT_MANUAL_CAMRA_JP_TRADES = "select count(T1.TPH_TRD_SEQ_NO) "+
			" from TPH_TRADE_OUT t1 "+
			" where t1.SOURCE_OUT='"+constants.SOURCE_OUT_CAMRA_JP+"' and T1.TPH_STATUS='"+constants.TRADE_OUT_STATUS_M+"'" +
					"and t1.SOURCE_IN=? ";
	
	
	
	private static final String SQL_CNT_ALL_CAMRA_US_TRADES = "select count(T1.TPH_TRD_SEQ_NO) "+
			" from TPH_TRADE_OUT t1"+
			" where t1.SOURCE_OUT='"+constants.SOURCE_OUT_CAMRA_US+"' and t1.SOURCE_IN=?";


private static final String SQL_CNT_FAILED_CAMRA_US_TRADES = "select count(T1.TPH_TRD_SEQ_NO) "+
			" from TPH_TRADE_OUT t1 "+
			" where t1.SOURCE_OUT='"+constants.SOURCE_OUT_CAMRA_US+"' and T1.TPH_STATUS='"+constants.TRADE_OUT_STATUS_F+"' "+
			" and t1.SOURCE_IN=?";



private static final String SQL_CNT_PROCESSED_CAMRA_US_TRADES = "select count(T1.TPH_TRD_SEQ_NO) "+
			" from TPH_TRADE_OUT t1 "+
			" where t1.SOURCE_OUT='"+constants.SOURCE_OUT_CAMRA_US+"' and T1.TPH_STATUS='"+constants.TRADE_OUT_STATUS_P+"'" +
					"and t1.SOURCE_IN=? ";
private static final String SQL_CNT_PENDING_CAMRA_US_TRADES = "select count(T1.TPH_TRD_SEQ_NO) "+
		" from TPH_TRADE_OUT t1 "+
		" where t1.SOURCE_OUT='"+constants.SOURCE_OUT_CAMRA_US+"' and T1.TPH_STATUS='"+constants.TRADE_OUT_STATUS_PND+"'" +
				"and t1.SOURCE_IN=? ";

private static final String SQL_CNT_MANUAL_CAMRA_US_TRADES = "select count(T1.TPH_TRD_SEQ_NO) "+
		" from TPH_TRADE_OUT t1 "+
		" where t1.SOURCE_OUT='"+constants.SOURCE_OUT_CAMRA_US+"' and T1.TPH_STATUS='"+constants.TRADE_OUT_STATUS_M+"'" +
				"and t1.SOURCE_IN=? ";

private static final String SQL_GET_ACCOUNTS_FOR_SOURCE = "select distinct(ACCT_CD) from TPH_TRADE_IN where source=?";
	

		/*	
			
			"select count(T1.TPH_TRD_SEQ_NO) "+
			 " from TPH_TRADE_STATUS T1 where not exists "+
			  " (select T2.TPH_TRD_SEQ_NO,T2.SOURCE_IN,T2.SOURCE_OUT,T2.TRD_STATUS,T2.EXP_STATUS from TPH_TRADE_STATUS T2  "+
			  " where  t1.TPH_TRD_SEQ_NO= t2.TPH_TRD_SEQ_NO "+
			 " AND t1.LAST_UPD_DATE < t2.LAST_UPD_DATE  ) and t1.SOURCE_OUT='"+constants.SOURCE_OUT_CAMRA_JP+"' and t1.EXP_STATUS='"+constants.TRADE_OUT_STATUS_P+"' and t1.SOURCE_IN=?";

	private static final String SQL_CNT_ALL_CAMRA_US_TRADES = "select count(T1.TPH_TRD_SEQ_NO) "+
			 " from TPH_TRADE_STATUS T1 where not exists "+
			  " (select T2.TPH_TRD_SEQ_NO,T2.SOURCE_IN,T2.SOURCE_OUT,T2.TRD_STATUS,T2.EXP_STATUS from TPH_TRADE_STATUS T2  "+
			  " where  t1.TPH_TRD_SEQ_NO= t2.TPH_TRD_SEQ_NO "+
			 " AND t1.LAST_UPD_DATE < t2.LAST_UPD_DATE  ) and t1.SOURCE_OUT='"+constants.SOURCE_OUT_CAMRA_US+"' and t1.SOURCE_IN=?";

	private static final String SQL_CNT_FAILED_CAMRA_US_TRADES = "select count(T1.TPH_TRD_SEQ_NO) "+
			 " from TPH_TRADE_STATUS T1 where not exists "+
			  " (select T2.TPH_TRD_SEQ_NO,T2.SOURCE_IN,T2.SOURCE_OUT,T2.TRD_STATUS,T2.EXP_STATUS from TPH_TRADE_STATUS T2  "+
			  " where  t1.TPH_TRD_SEQ_NO= t2.TPH_TRD_SEQ_NO "+
			 " AND t1.LAST_UPD_DATE < t2.LAST_UPD_DATE  ) and t1.SOURCE_OUT='"+constants.SOURCE_OUT_CAMRA_US+"' and t1.EXP_STATUS='"+constants.TRADE_OUT_STATUS_F+"' and t1.SOURCE_IN=?";
;

	private static final String SQL_CNT_PROCESSED_CAMRA_US_TRADES = "select count(T1.TPH_TRD_SEQ_NO) "+
			 " from TPH_TRADE_STATUS T1 where not exists "+
			  " (select T2.TPH_TRD_SEQ_NO,T2.SOURCE_IN,T2.SOURCE_OUT,T2.TRD_STATUS,T2.EXP_STATUS from TPH_TRADE_STATUS T2  "+
			  " where  t1.TPH_TRD_SEQ_NO= t2.TPH_TRD_SEQ_NO "+
			 " AND t1.LAST_UPD_DATE < t2.LAST_UPD_DATE  ) and t1.SOURCE_OUT='"+constants.SOURCE_OUT_CAMRA_US+"' and t1.EXP_STATUS='"+constants.TRADE_OUT_STATUS_P+"' and t1.SOURCE_IN=?";


	
	/* private static final String SQL_CNT_PROCESSED_TRADES = "select count(*)"+
			" from dbo.TPH_TRADE_IN tr left outer join dbo.TPH_TRADE_STATUS st "+
			" ON  tr.TPH_TRD_SEQ_NO = st.TPH_TRD_SEQ_NO "+
			" where st.TRD_STATUS = "+"'Processed'"+" AND tr.source = ?";
	
	private static final String SQL_CNT_FAILED_TRADES = "select count(*)"+
			" from dbo.TPH_TRADE_IN tr left outer join dbo.TPH_TRADE_STATUS st "+
			" ON  tr.TPH_TRD_SEQ_NO = st.TPH_TRD_SEQ_NO "+
			" where st.TRD_STATUS = "+"'Failed'"+" AND tr.source = ?";

	private static final String SQL_CNT_INPROCESS_TRADES = "SELECT COUNT(*) " +
			" FROM [DBO].[TPH_TRADE_IN] " +
			" WHERE source=? AND TPH_TRD_SEQ_NO NOT IN " +
			"(SELECT TPH_TRD_SEQ_NO FROM [dbo].[TPH_TRADE_STATUS]);"; */
	
	/*private JdbcTemplate jdbcTemplate;
	
	 @Autowired
	    public void setDataSource(DataSource ds){
	        this.jdbcTemplate = new JdbcTemplate(ds);
	    }*/
	 
	@Override
	public int getCntForProcessedTrades(String source) {
		// TODO Auto-generated method stub
		try{
		Object[] args=new Object[1];
		
		args[0]=source;
		return this.jdbcTemplate.queryForInt(SQL_CNT_PROCESSED_TRADES,args);
		}
		catch(Exception e){
			logger.error("Exception:", e);
			return 0;
		}
	}

	@Override
	public int getCntForTotalTrades(String source) {
		// TODO Auto-generated method stub
		try{
		Object[] args=new Object[1];
		args[0]=source;
		return this.jdbcTemplate.queryForInt(SQL_CNT_TOTAL_TRADES,args);
		}
		catch(Exception e){
			logger.error("Exception:", e);
			return 0;
		}
	}

/*	@Override
	public int getCntForInprocessTrades(String source) {
		// TODO Auto-generated method stub
		try{
		Object[] args=new Object[1];
		args[0]=source;
		return this.jdbcTemplate.queryForInt(SQL_CNT_INPROCESS_TRADES,args);
		}
		catch(Exception e){
			logger.error("Exception:", e);
			return 0;
		}
	}
*/
	@Override
	public int getCntForFailedTrades(String source) {
		// TODO Auto-generated method stub
		try{
			Object[] args=new Object[1];
			args[0]=source;
			return this.jdbcTemplate.queryForInt(SQL_CNT_FAILED_TRADES,args);
		}
		catch(Exception e){
			logger.error("Exception:", e);
			return 0;
		}
	}

	

	@Override
	public int getCntForTotalCamraJPTrades(String source) {
		// TODO Auto-generated method stub
		try{
			Object[] args=new Object[1];
			args[0]=source;
			return this.jdbcTemplate.queryForInt(SQL_CNT_ALL_CAMRA_JP_TRADES,args);
		}
		catch(Exception e){
			logger.error("Exception:", e);
			return 0;
		}
		
	}

	@Override
	public int getCntForFailedCamraJPTrades(String source) {
		// TODO Auto-generated method stub
		try{
			Object[] args=new Object[1];
			args[0]=source;
			return this.jdbcTemplate.queryForInt(SQL_CNT_FAILED_CAMRA_JP_TRADES,args);
		}
		catch(Exception e){
			logger.error("Exception:", e);
			return 0;
		}
	}

	@Override
	public int getCntForProcessedCamraJPTrades(String source) {
		// TODO Auto-generated method stub
		try{
			Object[] args=new Object[1];
			args[0]=source;
			return this.jdbcTemplate.queryForInt(SQL_CNT_PROCESSED_CAMRA_JP_TRADES,args);
		}
		catch(Exception e){
			logger.error("Exception:", e);
			return 0;
		}
	}
	@Override
	public int getCntForPendingCamraJPTrades(String source) {
		// TODO Auto-generated method stub
		try{
			Object[] args=new Object[1];
			args[0]=source;
			return this.jdbcTemplate.queryForInt(SQL_CNT_PENDING_CAMRA_JP_TRADES,args);
		}
		catch(Exception e){
			logger.error("Exception:", e);
			return 0;
		}
	}
	
	@Override
	public int getCntForManualCamraJPTrades(String source) {
		// TODO Auto-generated method stub
		try{
			Object[] args=new Object[1];
			args[0]=source;
			return this.jdbcTemplate.queryForInt(SQL_CNT_MANUAL_CAMRA_JP_TRADES,args);
		}
		catch(Exception e){
			logger.error("Exception:", e);
			return 0;
		}
	}
	@Override
	public int getCntForTotalCamraUSTrades(String source) {
		// TODO Auto-generated method stub
		try{
			Object[] args=new Object[1];
			args[0]=source;
			return this.jdbcTemplate.queryForInt(SQL_CNT_ALL_CAMRA_US_TRADES,args);
		}
		catch(Exception e){
			logger.error("Exception:", e);
			return 0;
		}
	}

	@Override
	public int getCntForFailedCamraUSTrades(String source) {
		// TODO Auto-generated method stub
		try{
			Object[] args=new Object[1];
			args[0]=source;
			return this.jdbcTemplate.queryForInt(SQL_CNT_FAILED_CAMRA_US_TRADES,args);
		}
		catch(Exception e){
			logger.error("Exception:", e);
			return 0;
		}
	}

	@Override
	public int getCntForProcessedCamraUSTrades(String source) {
		// TODO Auto-generated method stub
		try{
			Object[] args=new Object[1];
			args[0]=source;
			return this.jdbcTemplate.queryForInt(SQL_CNT_PROCESSED_CAMRA_US_TRADES,args);
		}
		catch(Exception e){
			logger.error("Exception:", e);
			return 0;
		}
	}

	@Override
	public int getCntForPendingCamraUSTrades(String source) {
		// TODO Auto-generated method stub
		try{
			Object[] args=new Object[1];
			args[0]=source;
			return this.jdbcTemplate.queryForInt(SQL_CNT_PENDING_CAMRA_US_TRADES,args);
		}
		catch(Exception e){
			logger.error("Exception:", e);
			return 0;
		}
	}
	
	@Override
	public int getCntForManualCamraUSTrades(String source) {
		// TODO Auto-generated method stub
		try{
			Object[] args=new Object[1];
			args[0]=source;
			return this.jdbcTemplate.queryForInt(SQL_CNT_MANUAL_CAMRA_US_TRADES,args);
		}
		catch(Exception e){
			logger.error("Exception:", e);
			return 0;
		}
	}
	
	@Override
	public int getCntForManualTrades(String source) {
		// TODO Auto-generated method stub
		try{
			Object[] args=new Object[1];
			args[0]=source;
			return this.jdbcTemplate.queryForInt(SQL_CNT_MANUAL_TRADES,args);
		}
		catch(Exception e){
			logger.error("Exception:", e);
			return 0;
		}
		
	}

	@Override
	public List<String> getAccountListForSource(String source)  throws Exception{
		// TODO Auto-generated method stub
		try{
			Object[] args=new Object[1];
			args[0]=source;
		return this.jdbcTemplate.queryForList(SQL_GET_ACCOUNTS_FOR_SOURCE, args, String.class);
		}
		catch(Exception e)
		{
			throw e;
		}
	}

	private static final String SQL_TRADE_COUNTS_FOR_SOURCE_ACCOUNT_DATE="select source, acct_cd as accountCode,convert(datetime,b.param_val,111),"+
			" LEFT(CONVERT(VARCHAR, a.create_date, 120), 10) as RECD_DATE, " +
			" CAST(A.TRADE_date AS DATE) as TRADE_DATE,"+							
			" SUM(1) totalCnt,"+
								       " sum(CASE"+
								       "       WHEN  status = N'N'  THEN 1"+
								       "        ELSE 0"+
								       "       END) pendingCnt,"+
								       " sum(CASE"+
								       "        WHEN  status = N'P'  THEN 1"+
								       "        ELSE 0"+
								       "       END) processedCnt,"+
								       " sum(CASE"+
								       "        WHEN  status = N'F'  THEN 1"+
								       "        ELSE 0"+
								       "       END) failedCnt,"+
								       " sum(CASE"+
								       "        WHEN  status = N'M'  THEN 1"+
								       "        ELSE 0"+
								       "       END) manualCnt,"+
								       " sum(CASE"+
								       "        WHEN  status = N'D'  THEN 1"+
								       "        ELSE 0"+
								       "       END) deletedCnt"+
								       " from TPH_TRADE_IN a , [dbo].[AIMS_PARAMETER] b"+
								       " where b.PARAM_NAME = 'CAMRA_LAST_PROCESSED_DATE'"+
								       " and a.TRADE_DATE > convert(datetime,b.param_val,111) and source=? "+
								       " group by source,acct_cd,convert(datetime,b.param_val,111),LEFT(CONVERT(VARCHAR, a.create_date, 120), 10),CAST(A.TRADE_date AS DATE)"+
										" order by source,LEFT(CONVERT(VARCHAR, a.create_date, 120), 10),acct_cd,convert(datetime,b.param_val,111),CAST(A.TRADE_date AS DATE)";

	@Override
	public List<IncomingTradeCountsBean> getTradeCounts(String source)  throws Exception{
		// TODO Auto-generated method stub
		try{
			Object[] args=new Object[1];
			args[0]=source;
		    return get(SQL_TRADE_COUNTS_FOR_SOURCE_ACCOUNT_DATE,args,IncomingTradeCountsBean.class);
		}
		catch(Exception e){
			throw e;
		}
	}
	
	private static final String SQL_OUT_TRADE_COUNTS_FOR_SOURCE_ACCOUNT_DATE = "select source_in, source_out, acct_cd as accountCode,convert(datetime,b.param_val,111),"+
			" CAST(A.TRADE_date AS DATE) as TRADE_DATE,"+
			"  SUM(1) totalCnt,"+
			"   sum(CASE"+
			" 	 WHEN  tph_status = N'READY'  THEN 1"+
			" 	  ELSE 0"+
			" 	 END) pendingCnt,"+
			" 	 sum(CASE"+
			" 		 WHEN  tph_status = N'COMPLETE'  THEN 1"+
			"         ELSE 0"+
			"          END) processedCnt,"+
			"    sum(CASE"+
			" 	 WHEN  tph_status = N'FAIL'  THEN 1"+
			" 	 ELSE 0"+
			" 	 END) failedCnt,"+
			" 	 sum(CASE"+
			" 	 WHEN  tph_status = N'MANUAL'  THEN 1"+
			" 	  ELSE 0"+
			"  END) manualCnt"+
			" from TPH_TRADE_OUT a , [dbo].[AIMS_PARAMETER] b"+
			" where b.PARAM_NAME = 'CAMRA_LAST_PROCESSED_DATE'"+
			" and a.TRADE_DATE > convert(datetime,b.param_val,111)  and source_in=? and source_out=?  "+
			" group by source_in,source_out,acct_cd,convert(datetime,b.param_val,111),CAST(A.TRADE_date AS DATE)";	
	@Override
	public List<OutgoingCAMRAJPTradeCountsBean> getCAMRAJPTradeCounts(String source)  throws Exception{
		// TODO Auto-generated method stub
		try{
			Object[] args=new Object[2];
			args[0]=source;
			args[1]=constants.SOURCE_OUT_CAMRA_JP;

		    return get(SQL_OUT_TRADE_COUNTS_FOR_SOURCE_ACCOUNT_DATE,args,OutgoingCAMRAJPTradeCountsBean.class);
		}
		catch(Exception e){
			throw e;
		}
	}
	
	
	@Override
	public List<OutgoingCAMRAUSTradeCountsBean> getCAMRAUSTradeCounts(String source)  throws Exception{
		// TODO Auto-generated method stub
		try{
			Object[] args=new Object[2];
			args[0]=source;
			args[1]=constants.SOURCE_OUT_CAMRA_US;

		    return get(SQL_OUT_TRADE_COUNTS_FOR_SOURCE_ACCOUNT_DATE,args,OutgoingCAMRAUSTradeCountsBean.class);
		}
		catch(Exception e){
			throw e;
		}
	}
	
	
private static final String SQL_GET_CAMRA_PROCESS_DATE="select PARAM_VAL from [dbo].[AIMS_PARAMETER] where PARAM_NAME = 'CAMRA_LAST_PROCESSED_DATE'";


	@Override
	public Date getCAMRAlastProcessDate()  throws Exception{
		// TODO Auto-generated method stub
		try{
		DateFormat df = new SimpleDateFormat("yyyy/MM/dd"); 
        
        String strDate=this.jdbcTemplate.queryForObject(SQL_GET_CAMRA_PROCESS_DATE, String.class);
        return df.parse(strDate);
       
		}
		catch(Exception e){
			throw e;
		}
	}
	
	private static final String SQL_IN_SECURITY_COUNTS_FOR_SOURCE = "select source, SM_SEC_GROUP as securityType, convert(datetime,b.param_val,111),LEFT(CONVERT(VARCHAR, a.create_date, 120), 10) as RECD_DATE,"+
		       " SUM(1) totalCnt,"+
		       " sum(CASE"+
		             "  WHEN  status = N'N'  THEN 1"+
		              " ELSE 0"+
		              " END) pendingCnt,"+
		       " sum(CASE"+
		              " WHEN  status = N'P'  THEN 1"+
		              " ELSE 0"+
		              " END) processedCnt,"+
		       " sum(CASE"+
		              " WHEN  status = N'F'  THEN 1"+
		              " ELSE 0"+
		             " END) failedCnt,"+
		             " sum(CASE"+
		              " WHEN  status = N'M'  THEN 1"+
		             "  ELSE 0"+
		            "  END) manualCnt,"+ 
		          " sum(CASE"+
		              " WHEN  status = N'D'  THEN 1"+
		             "  ELSE 0"+
		            "  END) deletedCnt"+
		            " from TPH_SECURITY_IN a , [dbo].[AIMS_PARAMETER] b"+
		            " where b.PARAM_NAME = 'CAMRA_LAST_PROCESSED_DATE' AND SOURCE=?"+
		            " and a.TIME_STAMP > convert(datetime,b.param_val,111)"+
		            " group by source,LEFT(CONVERT(VARCHAR, a.create_date, 120), 10),SM_SEC_GROUP,convert(datetime,b.param_val,111)"+
					" order by source,LEFT(CONVERT(VARCHAR, a.create_date, 120), 10),SM_SEC_GROUP,convert(datetime,b.param_val,111)";


	@Override
	public List<IncomingSecurityCountsBean> getInSecurityCounts(String source)  throws Exception{
		// TODO Auto-generated method stub
		try{
			Object[] args=new Object[1];
			args[0]=source;
			return get(SQL_IN_SECURITY_COUNTS_FOR_SOURCE,args,IncomingSecurityCountsBean.class);
		}
		catch(Exception e){
			throw e;
		}	
	}

	private static final String SQL_OUT_SECURITY_COUNTS_FOR_SOURCE = "select source,SEC_GROUP as securityType,convert(datetime,b.param_val,111),"+
       " SUM(1) totalCnt,"+
       " sum(CASE"+
             " WHEN  tph_status = N'N'  THEN 1"+
             " ELSE 0"+
            " END) pendingCnt,"+
      " sum(CASE"+
             " WHEN  tph_status = N'COMPLETE'  THEN 1"+
             " ELSE 0"+
             " END) processedCnt,"+
       " sum(CASE"+
             " WHEN  tph_status = N'FAIL'  THEN 1"+
             " ELSE 0"+
            " END) failedCnt,"+
       " sum(CASE"+
              " WHEN  tph_status = N'MANUAL'  THEN 1"+
             " ELSE 0"+
             " END) manualCnt"+
       " from TPH_SECURITY_OUT a , [dbo].[AIMS_PARAMETER] b"+
       " where b.PARAM_NAME = 'CAMRA_LAST_PROCESSED_DATE' AND SOURCE=? AND SOURCE_OUT=?"+
       " and a.LAST_UPD_DATE > convert(datetime,b.param_val,111)"+
       " group by source,SEC_GROUP,convert(datetime,b.param_val,111)";

	
	@Override
	public List<OutgoingCAMRAJPSecurityCountsBean> getOutCAMRAJPSecurityCounts(String source)  throws Exception{
		// TODO Auto-generated method stub
		try{
			Object[] args=new Object[2];
			args[0]=source;
			args[1]=constants.SOURCE_OUT_CAMRA_JP;

		    return get(SQL_OUT_SECURITY_COUNTS_FOR_SOURCE,args,OutgoingCAMRAJPSecurityCountsBean.class);
		}
		catch(Exception e){
			throw e;
		}
	}
	
	@Override
	public List<OutgoingCAMRAUSSecurityCountsBean> getOutCAMRAUSSecurityCounts(String source)  throws Exception{
		// TODO Auto-generated method stub
		try{
			Object[] args=new Object[2];
			args[0]=source;
			args[1]=constants.SOURCE_OUT_CAMRA_US;

		    return get(SQL_OUT_SECURITY_COUNTS_FOR_SOURCE,args,OutgoingCAMRAUSSecurityCountsBean.class);
		}
		catch(Exception e){
			throw e;
		}
	}
	
	@Override
	public List<DashboardBean> getIncomingTradeCounts()  throws Exception{
		try{
		    return get(SQL_INCOMING_TRADE_COUNTS,DashboardBean.class);
		}
		catch(Exception e){
			throw e;
		}
	}
	

	@Override
	public List<DashboardBean> getOutgoingTradeCounts(String destination)  throws Exception{
		// TODO Auto-generated method stub
		try{
			Object[] args=new Object[1];
			args[0]=destination;
		    return get(SQL_OUTGOING_TRADE_COUNTS,args,DashboardBean.class);
		}
		catch(Exception e){
			throw e;
		}
	}

	@Override
	public List<DashboardBean> getIncomingSecurityCounts()  throws Exception{
		try{
		    return get(SQL_INCOMING_SECURITY_COUNTS,DashboardBean.class);
		}
		catch(Exception e){
			throw e;
		}
	}

	@Override
	public List<DashboardBean> getOutgoingSecurityCounts(String destination)  throws Exception{
		// TODO Auto-generated method stub 
		try{
			Object[] args=new Object[1];
			args[0]=destination;
		    return get(SQL_OUTGOING_SECURITY_COUNTS_FOR_SOURCE,args,DashboardBean.class);
		}
		catch(Exception e){
			throw e;
		}
	}

	@Override
	public HeartBeatStatusBean getHeartBeatStatus(String systemName) {
			// TODO Auto-generated method stub
			try{
				Object[] args=new Object[1];
				args[0]=systemName;
				System.out.println("SystemName:"+systemName);
				List<HeartBeatStatusBean> status=get(SQL_HEARTBEAT_FOR_SYSTEM,args,HeartBeatStatusBean.class);
				return status.get(0);
			}
			catch(Exception e){
				logger.error("Exception:", e);
				return null;
			}
	}

	@Override
	public StatusIndicatorBean getDataStatusIndicatorBean(String systemName,
			String cntType) throws Exception {
		// TODO Auto-generated method stub
		try {
			StatusIndicatorBean s = new StatusIndicatorBean();
			SimpleJdbcCall discardCall = new SimpleJdbcCall(this.jdbcTemplate);
			discardCall.setProcedureName("P_GetCount");
			discardCall.declareParameters(new SqlOutParameter("failedCnt", Types.INTEGER),
										new SqlOutParameter("manualCnt", Types.INTEGER),
										new SqlOutParameter("pendingCnt", Types.INTEGER),
										new SqlOutParameter("processedCnt", Types.INTEGER));
			Map<String, Object> out = discardCall.execute(new MapSqlParameterSource().addValue("sysRef", systemName).addValue("cntType", cntType));
			s.setFailedCnt((Integer) out.get("failedCnt"));
			s.setManualCnt((Integer) out.get("manualCnt"));
			s.setPendingCnt((Integer) out.get("pendingCnt"));
			s.setProcessedCnt((Integer) out.get("processedCnt"));
			return s;
		}
		catch(Exception e) {
			logger.error("Exception:", e);
			return null;
		}
	}
  
}
