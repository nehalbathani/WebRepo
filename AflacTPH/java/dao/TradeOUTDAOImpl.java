package com.aflac.aims.tph.web.dao;

import java.sql.Types;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Repository;

import com.aflac.aims.tph.web.model.FieldMappingBean;
import com.aflac.aims.tph.web.model.ManualTradesReportBean;
import com.aflac.aims.tph.web.model.TradeOUTBean;
import com.aflac.aims.tph.web.utils.constants;
@Repository
public class TradeOUTDAOImpl extends CommonDAO implements TradeOUTDAO {
	protected static Logger logger = LoggerFactory.getLogger(TradeOUTDAOImpl.class);
	
	private static final String SQL_GET_TRADE_BY_ID = "SELECT tr.[TPH_TRD_SEQ_NO]" +
			  ",tr.[SOURCE_IN] " +
		      ",tr.[CUSIP]" +
			  ",tr.[TPH_STATUS]"+
		      ",tr.[SEC_GROUP]"+
			  ",sec.[SEC_TYPE_CD] as SEC_TYPE_CD"+
		      ",sec.DESCRIPTION1"+
		      ",tr.[ORIG_LOT]" +
		      ",tr.[ACCT_CD]" +
		      ",tr.[PORTFOLIO]" +
		      ",tr.[TRAN_TYPE]" +
		      ",tr.[ORIG_PRICE]" +
		      ",tr.[ORIG_QTY]" +
		      ",tr.[ACCR_INT_AMT]" +
		      ",tr.[FROM_AMOUNT]" +
		      ",tr.[TO_AMOUNT]" +
		      ",tr.[SETL_FLAG]" +
		      ",tr.[TRADE_DATE]" +
		      ",tr.[SETTLE_DATE]" +
		      ",tr.[ORIG_FACE]" +
		      ",tr.[FACTOR]" +
		      ",tr.[EXEC_QTY]" +
		      ",tr.[DISCOUNT]" +
		      ",tr.[EXEC_PRICE]" +
		      ",tr.[EXEC_AMT]" +
		      ",tr.[COMMISSION_AMT]" +
		      ",tr.[FEE_AMT]" +
		      ",tr.[ACCR_INT_AMT]" +
		      ",tr.[EXCH_RATE]" +
		      ",tr.[SALE_METHOD]" +
		      ",tr.[BRKR_YLD]" +
		      ",tr.[SP_BPS]" +
		      ",tr.[SP_YRS]" +
		      ",tr.[BROKER]" +
		      ",tr.[CUSTODIAN]" +
		      ",tr.[PMT_CODE]" +
		      ",tr.[USER_DESC1]" +
		      ",tr.[USER_DESC2]" +
		      ",tr.[ORIG_SETL_DATE]" +
		      ",tr.[ORIG_QTY]" +
		      ",tr.[ORIG_PRICE]" +
		      ",tr.[CURR_HOLD]" +
		      ",tr.[FOTO_TKT_NO]" +
		      ",tr.[CPN_RATE]" +
		      ",tr.[ISSUE_DATE]" +
		      ",tr.[MATURITY_DATE]" +
		      ",tr.[INTENT_CD]" +
		      ",tr.[DESC3]" +
		      ",tr.[FX_RATE] "+
		      ",tr.[SPOT_RATE] "+
		      ",tr.[DESC4]" +
		      ",tr.[SETL_TYPE]" +
		      ",tr.[UDF_CHAR1]" +
		      ",tr.[UDF_CHAR2]" +
		      ",tr.[EXT_SYS]" +
		      ",tr.[EXT_SYS_REF_NO]" +
		      ",tr.[AUTHOR]" +
		      ",tr.[UDF_DATE1]" +
		      ",tr.[UDF_DATE2]" +
		      ",tr.[TOUCH_COUNT]" +
		      ",tr.[IMPUTED_COM_AMT]" +
		      ",tr.[CLG_BROKER]" +
		      ",tr.[LAST_UPD_USER]" +
		      ",tr.[LAST_UPD_DATE]" +
		      ",tr.[FROM_CURRENCY]" +
		      ",tr.[TO_CURRENCY]" +
			  " FROM [dbo].[TPH_TRADE_OUT] tr LEFT OUTER JOIN  "+
			  " (SELECT s.SEC_TYPE_CD, s.DESCRIPTION1, s.source_out,s.CUSIP FROM TPH_SECURITY_OUT s WHERE s.SEC_ID IN "+
			  " (select max(SEC_ID) as SEC_ID from TPH_SECURITY_OUT group by CUSIP, SOURCE_OUT))sec "+
			  "   on tr.CUSIP=sec.CUSIP and tr.source_out=sec.source_out WHERE [TPH_TRD_SEQ_NO]=? AND TRAN_TYPE=? AND  tr.TOUCH_COUNT=? and tr.SOURCE_OUT=?";
	
	
	@Override
	public List<TradeOUTBean> getTradeOUTBeanList() throws Exception{
		try{
		return get(SQL_GET_OUT_TRADES, TradeOUTBean.class);	
		}
		catch(Exception e){
			throw e;
		}
		
	}



	@Override
	public List<TradeOUTBean> getOutTradeByID(int tradeRefNo, String tranType, String dest,int touchCount, String source)  throws Exception{
		// TODO Auto-generated method stub
		try{
			Object[] args=new Object[4];
			args[0]=new Integer(tradeRefNo);
			args[1]=tranType.trim();
			args[2]=new Integer(touchCount);
			args[3]=dest.trim();
			
			//System.out.println("Query to be executed\n" + SQL_GET_TRADE_BY_ID);
			return get(SQL_GET_TRADE_BY_ID,args, TradeOUTBean.class);	
			}
			catch(Exception e){
				throw e;
			}
	}

	@Override
	public int resendTrade(int tradeRefNo, String str_trade_type,int touchCount,String dest) {
		// TODO Auto-generated method stub
		try {
			SimpleJdbcCall resendCall = new SimpleJdbcCall(this.jdbcTemplate);
			resendCall.setProcedureName("P_ResendTrade");
			resendCall.declareParameters(new SqlOutParameter("status", Types.INTEGER));
		    Map<String, Object> out = resendCall.execute(new MapSqlParameterSource().addValue(
		    		                              "trdID", tradeRefNo).addValue(
		    		                              "tranType", str_trade_type).addValue(
		    		                              "touchCnt", touchCount).addValue("dest", dest));
		    return (Integer) out.get("status");
		}
		catch(Exception e){
			logger.error("Exception:", e);
			return -1;
		}
	}
	
	@Override
	public int discardTrade(int tradeRefNo, String str_trade_type,int touchCount,String dest) {
		// TODO Auto-generated method stub
		try {
			SimpleJdbcCall discardCall = new SimpleJdbcCall(this.jdbcTemplate);
			discardCall.setProcedureName("P_DiscardTrade");
			discardCall.declareParameters(new SqlOutParameter("status", Types.INTEGER));
			Map<String, Object> out = discardCall.execute(new MapSqlParameterSource().addValue(
					                             "trdID", tradeRefNo).addValue(
					                             "tranType", str_trade_type).addValue(
					                             "touchCnt", touchCount).addValue("dest", dest));
			return (Integer) out.get("status");
		} catch(Exception e) {
			logger.error("Exception:", e);
			return -1;
		}
	}
	
	private static final String SQL_GET_OUT_TRADES = "SELECT [TPH_TRD_SEQ_NO]"+
      " ,[SOURCE_IN]"+
      " ,[SOURCE_OUT]"+
      " ,[CUSIP]"+
      " ,[ORIG_LOT]"+
      " ,[ACCT_CD]"+
      " ,[PORTFOLIO]"+
      " ,[TRAN_TYPE]"+
      " ,[SETL_FLAG]"+
      " ,[TRADE_DATE]"+
      " ,[SETTLE_DATE]"+
      " ,[ORIG_FACE]"+
      " ,[FACTOR]"+
      " ,[EXEC_QTY]"+
      " ,[DISCOUNT]"+
      " ,[EXEC_PRICE]"+
      " ,[EXEC_AMT]"+
      " ,[COMMISSION_AMT]"+
      " ,[FEE_AMT]"+
      " ,[ACCR_INT_AMT]"+
      " ,[EXCH_RATE]"+
      " ,[SALE_METHOD]"+
      " ,[BRKR_YLD]"+
      " ,[SP_BPS]"+
      " ,[SP_YRS]"+
      " ,[BROKER]"+
      " ,[CUSTODIAN]"+
      " ,[PMT_CODE]"+
      " ,[USER_DESC1]"+
      " ,[USER_DESC2]"+
      " ,[ORIG_SETL_DATE]"+
      " ,[ORIG_QTY]"+
      " ,[ORIG_PRICE]"+
      " ,[CURR_HOLD]"+
      " ,[FOTO_TKT_NO]"+
      " ,[CPN_RATE]"+
      " ,[ISSUE_DATE]"+
      " ,[MATURITY_DATE]"+
      " ,[INTENT_CD]"+
      " ,[DESC3]"+
      " ,[DESC4]"+
      " ,[SETL_TYPE]"+
      " ,[UDF_CHAR1]"+
      " ,[UDF_CHAR2]"+
      " ,[EXT_SYS]"+
      " ,[EXT_SYS_REF_NO]"+
      " ,[AUTHOR]"+
      " ,[UDF_DATE1]"+
      " ,[UDF_DATE2]"+
      " ,[IMPUTED_COM_AMT]"+
      " ,[CLG_BROKER]"+
      " ,[INCOME_GEN_OPT]"+
      " ,[GAMORT_METHOD]"+
      " ,[SAMORT_METHOD]"+
      " ,[MAMORT_METHOD]"+
      " ,[TDISC_AMORT_METHOD]"+
      " ,[TPREM_AMORT_METHOD]"+
      " ,[PROSPECTIVE_YILD]"+
      " ,[ACCUM_INT]"+
      " ,[SETL_LOC]"+
      " ,[CPR]"+
      " ,[PSA]"+
      " ,[ANT_MANAGER]"+
      " ,[ANT_STRATEGY]"+
      " ,[ANT_PROFIT_CTR]"+
      " ,[ANT_HEDGE]"+
      " ,[USER_DEFINED3]"+
      " ,[USER_DEFINED4]"+
      " ,[GBASIS_DATE]"+
      " ,[SBASIS_DATE]"+
      " ,[MBASIS_DATE]"+
      " ,[TBASIS_DATE]"+
      " ,[GORIG_EX_RATE]"+
      " ,[SORIG_EX_RATE]"+
      " ,[MORIG_EX_RATE]"+
      " ,[TORIG_EX_RATE]"+
      " ,[CA_EVENT_NUM]"+
      " ,[GSETTLE_DATE]"+
      " ,[SSETTLE_DATE]"+
      " ,[MSETTLE_DATE]"+
      " ,[TSETTLE_DATE]"+
      " ,[GORIG_COST]"+
      " ,[SORIG_COST]"+
      " ,[MORIG_COST]"+
      " ,[TORIG_COST]"+
      " ,[GBOOK_VALUE]"+
      " ,[SBOOK_VALUE]"+
      " ,[MBOOK_VALUE]"+
      " ,[TBOOK_VALUE]"+
      " ,[GBEG_BV_CY]"+
      " ,[SBEG_BV_CY]"+
      " ,[MBEG_BV_CY]"+
      " ,[TBEG_BV_CY]"+
      " ,[GAAP_Y_N]"+
      " ,[STAT_Y_N]"+
      " ,[MGMT_Y_N]"+
      " ,[TAX_Y_N]"+
      " ,[SINTENT_CODE]"+
      " ,[MINTENT_CODE]"+
      " ,[TINTENT_CODE]"+
      " ,[GAMZ_DATE]"+
      " ,[SAMZ_DATE]"+
      " ,[MAMZ_DATE]"+
      " ,[TAMZ_DATE]"+
      " ,[DISC_MARGIN]"+
      " ,[QUOT_MARGIN]"+
      " ,[THAI_INDEX]"+
      " ,[NOM_TITLE_AMT]"+
      " ,[SEC_GROUP]"+
      " ,[TLOCATION]"+
      " ,[FROM_CURRENCY]"+
      " ,[FROM_AMOUNT]"+
      " ,[FROM_CUST]"+
      " ,[TO_CURRENCY]"+
      " ,[TO_AMOUNT]"+
      " ,[TO_CUST]"+
      " ,[FX_RATE]"+
      " ,[FROM_RATE]"+
      " ,[TO_RATE]"+
      " ,[CONTRACT_NUMBER]"+
      " ,[HEDGE]"+
      " ,[AMZ_YN]"+
      " ,[SPOT_RATE]"+
      " ,[TOUCH_COUNT]"+
      " ,[TPH_STATUS]"+
      " ,[LAST_UPD_USER]"+
      " ,[LAST_UPD_DATE]"+
      " ,[SEC_CHECK]"+
   " FROM [dbo].[TPH_TRADE_OUT]";
	
	@Override
	public List<TradeOUTBean> getManualTrades(String source)  throws Exception{
		// TODO Auto-generated method stub
		try{
			Object[] args=new Object[1];
			args[0]=source;
			
			return get(SQL_GET_OUT_TRADES + " WHERE source_in=? AND TPH_STATUS='"+constants.TRADE_OUT_STATUS_M+"'",args, TradeOUTBean.class);	
			}
			catch(Exception e){
				throw e;
			}
	}


	private static final String SQL_GET_MANUAL_REPORT_BEAN = "SELECT  [TPH_TRD_SEQ_NO]"+
									     " ,[SOURCE_IN]"+
									     " ,[SOURCE_OUT]"+
									     " ,[CUSIP]"+
									     " ,[ACCT_CD]"+
									     " ,[PORTFOLIO]"+
									     " ,[TRAN_TYPE]"+
									     " ,[TRADE_DATE]"+
									     " ,[SETTLE_DATE]"+
										  " ,[ORIG_PRICE]"+
										  " ,[FX_RATE]"+
										  " ,[SPOT_RATE]"+
										  " ,[FROM_CURRENCY]"+
										  " ,[TO_CURRENCY]"+
										  " ,[FROM_AMOUNT]"+
										  " ,[TO_AMOUNT]"+
										  " ,[BROKER]"+
										" FROM [dbo].[TPH_TRADE_OUT] where [TPH_TRD_SEQ_NO]=?";
	
	@Override
	public List<ManualTradesReportBean> getManualTradeReportForID(int id)  throws Exception{
		// TODO Auto-generated method stub
		try{
			Object[] args=new Object[1];
			args[0]=id;
			
			return get(SQL_GET_MANUAL_REPORT_BEAN,args, ManualTradesReportBean.class);	
			}
			catch(Exception e){
				throw e;
			}
	}

	@Override
	public List<FieldMappingBean> getDestinationValuesForMap(
			List<FieldMappingBean> fieldMap, int tradeRefNo, int touchCount)  throws Exception{
		// TODO Auto-generated method stub
		for(FieldMappingBean mapEntry : fieldMap){
			String SQL_GET_FIELD_VALUE="SELECT "+mapEntry.getDestTag()+" FROM TPH_TRADE_OUT WHERE TRAN_TYPE != 'DELETE' AND TPH_TRD_SEQ_NO=?";
			Object[] args=new Object[1];
			args[0]=tradeRefNo;
			mapEntry.setDestTagValue(this.jdbcTemplate.queryForObject(SQL_GET_FIELD_VALUE, args, Object.class));
		}
		return fieldMap;
	}


	private String SQL_GET_CAMRA_TRADE_EXIST_COUNT="SELECT count(*) from TPH_TRADE_OUT WHERE TPH_TRD_SEQ_NO=? AND TOUCH_COUNT=?";
	
	@Override
	public boolean isExistAtDest(int tradeRefNo,int touchCount)  throws Exception{
		// TODO Auto-generated method stub
		try{
			Object[] args=new Object[2];
			args[0]=tradeRefNo;
			args[1]=touchCount;
			if(this.jdbcTemplate.queryForInt(SQL_GET_CAMRA_TRADE_EXIST_COUNT,args)>0){
			return true;	
			}
			return false;
			
			}
			catch(Exception e){
				throw e;
			}
	}



	@Override
	public List<TradeOUTBean> getOutTradesByID(int tradeRefNo,
			String tranType, int touch_count)  throws Exception{
		// TODO Auto-generated method stub
		try{
			Object[] args=new Object[3];
			args[0]=tradeRefNo;
			args[1]=tranType;
			args[2]=touch_count;
			/*System.out.print("Trade ID"+tradeRefNo);
			System.out.print("Tran Type"+tranType);
			System.out.print("Touch Count"+touch_count);
			System.out.print(SQL_GET_OUT_TRADES+" WHERE TPH_TRD_SEQ_NO=? AND TRAN_TYPE=? AND TOUCH_COUNT=?");
			*/return get(SQL_GET_OUT_TRADES+" WHERE TPH_TRD_SEQ_NO=? AND TRAN_TYPE=? AND TOUCH_COUNT=?",args,TradeOUTBean.class );
		}
		catch(Exception e){
			throw e;
		}
	}

	private String SQL_GET_ERROR_MESSAGE="  SELECT t1.ERR_MSG"+
				" FROM [TPH_DEST_TRADE_ERR] t1"+
				" LEFT OUTER JOIN [TPH_DEST_TRADE_ERR] t2"+
				" ON t1.TPH_TRD_ID = t2.TPH_TRD_ID AND  t1.TRAN_TYPE = t2.TRAN_TYPE AND t1.DEST = t2.DEST AND ((t1.TIME_STAMP < t2.TIME_STAMP))   " +
				" WHERE t2.TPH_TRD_ID IS NULL AND t1.TPH_TRD_ID=? AND t1.TRAN_TYPE=? AND t1.DEST=?";
	
	@Override
	public String getErrorMsg(int tradeRefNo, String tranType, String dest) {
		// TODO Auto-generated method stub
		try{
			Object[] args=new Object[3];
			args[0]=tradeRefNo;
			args[1]=tranType;
			args[2]=dest;
			
			return this.jdbcTemplate.queryForObject(SQL_GET_ERROR_MESSAGE, args, String.class);
		}
		catch(Exception e){
			return "Error not found in database!";
		}
	}
	
	
}
