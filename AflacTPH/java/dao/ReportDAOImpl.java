package com.aflac.aims.tph.web.dao;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.aflac.aims.tph.web.model.ManualTradesReportBean;
import com.aflac.aims.tph.web.model.TradeInReportBean;
import com.aflac.aims.tph.web.model.TradeReportBean;
import com.aflac.aims.tph.web.model.TradeReportBondBean;
import com.aflac.aims.tph.web.model.TradeReportFXBean;

@Repository
public class ReportDAOImpl extends CommonDAO implements ReportDAO {
	
	protected static Logger logger = LoggerFactory.getLogger(ReportDAOImpl.class);
	
	private static final String SQL_GET_IN_REPORT_TRADE_BEAN = "SELECT TPH_TRD_SEQ_NO, SOURCE,	TRAN_TYPE,"+
							"SEC_GROUP, SEC_TYPE_CD, TOUCH_COUNT, EXECUTION_TIME, DESC_INSTMT, TRD_COUPON,"+
							"MATURITY, TRD_REF_NO, CUSIP FROM TPH_TRADE_IN WHERE TPH_TRD_SEQ_NO=? AND TOUCH_COUNT= ?";
	private static final String SQL_GET_OUT_REPORT_TRADE_BEAN = "SELECT TPH_TRD_SEQ_NO, SOURCE_IN as SOURCE,	TRAN_TYPE, t.SEC_GROUP, s.SEC_TYPE_CD, TOUCH_COUNT,  s.DESCRIPTION1 as DESC_INSTMT, s.CPN_RATE as TRD_COUPON,"+
			" t.MATURITY_DATE as MATURITY, EXT_SYS_REF_NO as TRD_REF_NO, t.CUSIP FROM (SELECT SEC_ID, SEC_TYPE_CD, CUSIP, DESCRIPTION1, CPN_RATE FROM TPH_SECURITY_OUT WHERE SOURCE_OUT=?) s right outer join TPH_TRADE_OUT t on s.CUSIP=t.CUSIP "+
			" WHERE (s.SEC_ID IS NULL OR s.SEC_ID IN (SELECT MAX(s2.SEC_ID) FROM TPH_SECURITY_OUT s2 WHERE s2.CUSIP=t.CUSIP))	AND TPH_TRD_SEQ_NO=? AND TOUCH_COUNT= ? AND t.source_OUT=? AND t.TRAN_TYPE=?";
					
	@Override
	public List<TradeReportBean> getTradeHeaderBean(int tradeRefNo, int touchCount,String dest,String tranType) throws Exception {
		// TODO Auto-generated method stub
		try{
			if(dest.equalsIgnoreCase("")){
			Object[] args=new Object[2];
			args[0]=tradeRefNo;
			args[1]=touchCount;
			
			return get(SQL_GET_IN_REPORT_TRADE_BEAN, args, TradeReportBean.class);
			}
			else{
				Object[] args=new Object[5];
				args[0]=dest;
				args[1]=tradeRefNo;
				args[2]=touchCount;
				args[3]=dest;
				args[4]=tranType;
				
				return get(SQL_GET_OUT_REPORT_TRADE_BEAN, args, TradeReportBean.class);
			}
		}
		catch(Exception e){
			throw e;
		}
	}

	private static final String SQL_GET_IN_REPORT_BOND_TRADE_BEAN = "SELECT "+
			 " s.CUSIP,	s.ISIN,	s.CPN_TYPE, s.COUPON_FIX, s.MATURITY, s.COUP_FREQ,t.SM_COUPON_FREQ, s.ACCRUAL_DT,	t.TRADE_DATE, t.SETTLE_DATE,"+
			 " t.COUNTERPARTY_CODE, t.TRD_COUNTERPARTY, s.ISSUE_DT, s.MIN_TRD_SIZE, s.DATE_CONV, s.FIRST_PAY_DT,"+
			 " s.AMT, s.FLAG_ERISA, s.FLAG_144A, s.NTL_FLAG, t.TRD_ORIG_FACE, t.TRD_FACTOR, t.TRD_FACT_DT,"+
			 " t.TRD_TD_PAR, t.TRD_PRICE, t.TRD_PRINCIPAL, t.TRD_INTEREST, t.TRD_CURRENCY, t.TRD_COMMISSION,"+
			 " t.TRD_EXCHANGE_RATE, t.TRD_DISCOUNT, t.PREPAY_RATE, t.TRD_YIELD, t.TRD_DURATION, t.TRD_CONVEXITY,"+ 
			 " t.EXECUTION_TIME, t.EXEC_TIME_SOURCE, t.BROKER_CD, t.BROKER, t.TRD_SETTLE_LOCATION, t.ACCT_CD "+
			 " from TPH_SECURITY_IN s right outer join TPH_TRADE_IN t on s.CUSIP=t.CUSIP "+
			 " WHERE (s.TPH_SEC_SEQ_NO IS NULL OR s.TPH_SEC_SEQ_NO IN (SELECT MAX(s2.TPH_SEC_SEQ_NO) FROM TPH_SECURITY_IN s2 "+
			 " WHERE s2.CUSIP=t.CUSIP))	"+
			 " AND t.TPH_TRD_SEQ_NO=? AND t.TOUCH_COUNT= ?";

	private static final String SQL_GET_OUT_REPORT_BOND_TRADE_BEAN="SELECT  s.CUSIP,	s.ISIN,	s.FLOAT_RATE as CPN_TYPE, s.CPN_RATE as COUPON_FIX, s.MATURITY_DATE as MATURITY, s.INT_DIV_FREQ as COUP_FREQ, t.TRADE_DATE, t.SETTLE_DATE,"+
			  " s.ISSUE_DATE as ISSUE_DT,  s.ACCRUAL as DATE_CONV, t.EXEC_QTY as TRD_TD_PAR, t.EXEC_PRICE as TRD_PRICE, t.EXEC_AMT as TRD_PRINCIPAL, t.ACCR_INT_AMT as TRD_INTEREST,  t.COMMISSION_AMT as TRD_COMMISSION,"+
			  " t.EXCH_RATE as TRD_EXCHANGE_RATE, t.DISCOUNT as TRD_DISCOUNT, t.BROKER, t.SETL_LOC as TRD_SETTLE_LOCATION, t.ACCT_CD "+
			  " from TPH_SECURITY_OUT s right outer join TPH_TRADE_OUT t on s.CUSIP=t.CUSIP "+
			  " WHERE (s.SEC_ID IS NULL OR s.SEC_ID IN (SELECT MAX(s2.SEC_ID) FROM TPH_SECURITY_OUT s2 "+
			  " WHERE s2.CUSIP=t.CUSIP))	"+
			  " AND t.TPH_TRD_SEQ_NO=? AND t.TOUCH_COUNT= ?  AND t.source_OUT=?";
	@Override
	public List<TradeReportBondBean> getBondTradeReportBean(int tradeRefNo,	int touchCount, String dest) throws Exception {
		// TODO Auto-generated method stub
		try{
			if(dest.equalsIgnoreCase("")){
			Object[] args=new Object[2];
			args[0]=tradeRefNo;
			args[1]=touchCount;
			
			return get(SQL_GET_IN_REPORT_BOND_TRADE_BEAN, args, TradeReportBondBean.class);
			}
			else{
				Object[] args=new Object[3];
				args[0]=tradeRefNo;
				args[1]=touchCount;
				args[2]=dest;
				return get(SQL_GET_OUT_REPORT_BOND_TRADE_BEAN, args, TradeReportBondBean.class);
			}
		}
		catch(Exception e){
			throw e;
		}
	}

	private static final String SQL_GET_IN_REPORT_FX_TRADE_BEAN="SELECT SOURCE, CUSIP,	TICKER,	TRADE_DATE,"+
	" SETTLE_DATE, BROKER_CD, BROKER, TRAN_TYPE, FX_RCV_CURR, FX_PAY_CURR, FX_PRICE, FX_RCV_AMT, FX_FWD_BUY_AMOUNT, FX_FWD_SELL_AMOUNT, "+
	" FX_PAY_AMT, EXECUTION_TIME, EXEC_TIME_SOURCE, TRD_TRADER FROM TPH_TRADE_IN WHERE TPH_TRD_SEQ_NO=? AND TOUCH_COUNT= ?";
	private static final String SQL_GET_OUT_REPORT_FX_TRADE_BEAN="SELECT SOURCE_IN as SOURCE, s.CUSIP,	TRADE_DATE,"+
	 " SETTLE_DATE,  BROKER, TRAN_TYPE, FROM_CURRENCY as FX_RCV_CURR, TO_CURRENCY as FX_PAY_CURR, FX_RATE as FX_PRICE, FROM_AMOUNT as FX_RCV_AMT,"+
	 " TO_AMOUNT as FX_PAY_AMT  from TPH_SECURITY_OUT s right outer join TPH_TRADE_OUT t on s.CUSIP=t.CUSIP "+
	" WHERE (s.SEC_ID IS NULL OR s.SEC_ID IN (SELECT MAX(s2.SEC_ID) FROM TPH_SECURITY_OUT s2 WHERE s2.CUSIP=t.CUSIP))	"+
	" AND t.TPH_TRD_SEQ_NO=? AND t.TOUCH_COUNT= ?  AND t.source_OUT=?";
	@Override
	public List<TradeReportFXBean> getFXTradeReportBean(int tradeRefNo,
			int touchCount,String dest) throws Exception {
		// TODO Auto-generated method stub
		try{
			if(dest.equalsIgnoreCase("")){
			Object[] args=new Object[2];
			args[0]=tradeRefNo;
			args[1]=touchCount;
			
			return get(SQL_GET_IN_REPORT_FX_TRADE_BEAN, args, TradeReportFXBean.class);
			}
			else{
				Object[] args=new Object[3];
				args[0]=tradeRefNo;
				args[1]=touchCount;
				args[2]=dest;
				return get(SQL_GET_OUT_REPORT_FX_TRADE_BEAN, args, TradeReportFXBean.class);
				
			}
		}
		catch(Exception e){
			throw e;
		}
	}
    
	//TODO It should be only one call to database not each call for each ID
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
    public List<ManualTradesReportBean> getManualTradeReportForID(int id) throws Exception {
    	// TODO Auto-generated method stub
        try {
        	Object[] args = new Object[1];
            args[0] = id;
            return get(SQL_GET_MANUAL_REPORT_BEAN,args, ManualTradesReportBean.class);	
        } catch(Exception e) {
        	throw e;
        }
    }
    
  	private static final String SQL_GET_SINGLE_DAY_TRADE_IN_REPORT_BEAN = "SELECT  TPH_TRD_SEQ_NO, "
  			+ "TRD_REF_NO, source,"
  			+ "TRAN_TYPE as transactionType, cusip,"
  			+ "TRADE_DATE as tradeDate,"
  			+ "SETTLE_DATE as settleDate, acct_cd, status,"
  			+ "TOUCH_COUNT, CREATE_DATE, BROKER_CD as brokerCode,"
  			+ "TRD_CURRENCY as executionCurrency,"
  			+ "TRD_TD_PAR as executionQty,"
  			+ "TRD_PRICE as executionPrice,"
  			+ "TRD_PRINCIPAL + TRD_INTEREST as executionAmount,"
  			+ "FX_PAY_CURR as fixedCurrency,"
  			+ "FX_RCV_CURR as floatCurrency,"
  			+ "SEC_GROUP as securityGroup,"
  			+ "SEC_TYPE_CD as securityType,"
  			+ "DESC_INSTMT as securityName,"
  			+ "CASE WHEN [STATUS]='P' THEN 'Processed' WHEN [STATUS]='F' "
  			+ "THEN 'Failed' WHEN [STATUS]='N' Then 'Pending' "
  			+ "WHEN [STATUS]='D' then 'Deleted' "
  			+ "WHEN [STATUS]='M' then 'Manual' END STATUS "
  			+ "FROM TPH_TRADE_IN "
  			//+ "WHERE TRADE_DATE = '2013-11-21 00:00:00.000';";
  	        + "WHERE convert(varchar(10), CREATE_DATE, 111) = "
  	        + "convert(varchar(10), getDATE(), 111);";
  			
      @Override
      public List<TradeInReportBean> getSingleDayTradeInReport() throws Exception {
      	// TODO Auto-generated method stub
          try {
        	  logger.info("SQL-QUERY:\n" + SQL_GET_SINGLE_DAY_TRADE_IN_REPORT_BEAN);
        	  List<TradeInReportBean> lst = get(SQL_GET_SINGLE_DAY_TRADE_IN_REPORT_BEAN, TradeInReportBean.class);
        	  for (TradeInReportBean obj:lst) {
        		  logger.info("VALUE:" + obj.getTPH_TRD_SEQ_NO() + ":" + obj.getTRD_REF_NO() + ":" + obj.getsource());
        	  }        	  
              return get(SQL_GET_SINGLE_DAY_TRADE_IN_REPORT_BEAN, TradeInReportBean.class);	
          } catch(Exception e) {
          	throw e;
          }
      }
}