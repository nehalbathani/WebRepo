package com.aflac.aims.tph.web.dao;

import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.aflac.aims.tph.web.model.TradeSummaryBean;

@Repository
public class TradeSummaryDAOImpl extends CommonDAO implements TradeSummaryDAO {

	private static final String SQL_TRADE_STATUS_LIST =
			"select tr.TPHTradeNo,tr.source_in as source,tr.CUSIP, sec.DESCRIPTION1 as securityName, sec.SEC_GROUP as securityGroup, sec.SEC_TYPE_CD as securityType, " +
			" tr.tradeStatus,tr.tradeType,tr.tradeDate,tr.settleDate,tr.broker from (select tout.TPH_TRD_SEQ_NO as [TPHTradeNo],tout.source_in,tout.CUSIP,tstat.TRD_STATUS as [tradeStatus]"+
			" ,tout.TRAN_TYPE as [tradeType], tout.TRADE_DATE as [tradeDate],tout.SETTLE_DATE as [settleDate],tout.BROKER as [broker] " +
			" from [dbo].TPH_TRADE_OUT tout LEFT OUTER JOIN dbo.TPH_TRADE_STATUS tstat on tout.TPH_TRD_SEQ_NO=tstat.TPH_TRD_SEQ_NO)  tr " +
			" LEFT OUTER JOIN dbo.TPH_SECURITY_OUT sec on tr.CUSIP=sec.CUSIP";
/*	private static final String SQL_TRADE_STATUS_FOR_SOURCE = "select tr.TPHTradeNo,tr.SOURCE_IN as [source],tr.CUSIP, sec.SEC_NAME as securityName, sec.SEC_GROUP as securityGroup, sec.SEC_TYPE_CD as securityType, " +
			" tr.tradeStatus,tr.tradeType,tr.tradeDate,tr.settleDate,tr.broker from (select tout.TPH_TRD_SEQ_NO as [TPHTradeNo],tout.source_in,tout.CUSIP,tstat.TRD_STATUS as [tradeStatus]"+
			" ,tout.TRAN_TYPE as [tradeType], tout.TRADE_DATE as [tradeDate],tout.SETTLE_DATE as [settleDate],tout.BROKER as [broker] " +
			" from [dbo].TPH_TRADE_OUT tout LEFT OUTER JOIN dbo.TPH_TRADE_STATUS tstat on tout.TPH_TRD_SEQ_NO=tstat.TPH_TRD_SEQ_NO " +
			"WHERE tout.SOURCE_IN=?)  tr " +
			" LEFT OUTER JOIN dbo.TPH_SECURITY sec on tr.CUSIP=sec.CUSIP";*/

	private static final String SQL_TRADE_STATUS_FOR_SOURCE =	" select tr.TPHTradeNo,tr.SOURCE_IN as [source],tr.CUSIP, sec.SEC_NAME as securityName, sec.SEC_GROUP as securityGroup, sec.SEC_TYPE_CD as securityType, "+
 			 " tr.tradeStatus,tr.tradeType,tr.tradeDate,tr.settleDate,tr.broker, tr.exportStatus from (select tout.TPH_TRD_SEQ_NO as [TPHTradeNo],tout.source_in,tout.CUSIP,tstat.TRD_STATUS as [tradeStatus] "+
			 " ,tout.TRAN_TYPE as [tradeType], tout.TRADE_DATE as [tradeDate],tout.SETTLE_DATE as [settleDate],tout.BROKER as [broker], tstat.EXP_STATUS as [exportStatus] "+
			 " from [dbo].TPH_TRADE_OUT tout LEFT OUTER JOIN  "+
			 " (select T1.TPH_TRD_SEQ_NO,T1.SOURCE_IN,T1.SOURCE_OUT,T1.TRD_STATUS,T1.EXP_STATUS "+
			 " from TPH_TRADE_STATUS T1 where not exists "+
			 " (select T2.TPH_TRD_SEQ_NO,T2.SOURCE_IN,T2.SOURCE_OUT,T2.TRD_STATUS,T2.EXP_STATUS from TPH_TRADE_STATUS T2 "+ 
			 " where  t1.TPH_TRD_SEQ_NO= t2.TPH_TRD_SEQ_NO "+
			 " AND t1.LAST_UPD_DATE < t2.LAST_UPD_DATE  )) tstat on tout.TPH_TRD_SEQ_NO=tstat.TPH_TRD_SEQ_NO  "+
			" WHERE tout.SOURCE_IN=?)  tr "+
			" LEFT OUTER JOIN dbo.TPH_SECURITY sec on tr.CUSIP=sec.CUSIP";

	private static final String SQL_FILTERED_TRADE_STATUS_LIST = " select tr.TPHTradeNo,tr.SOURCE_IN as [source],tr.CUSIP, sec.SEC_NAME as securityName, sec.SEC_GROUP as securityGroup, sec.SEC_TYPE_CD as securityType, "+
 			 " tr.tradeStatus,tr.tradeType,tr.tradeDate,tr.settleDate,tr.broker,tr.exportStatus from (select tout.TPH_TRD_SEQ_NO as [TPHTradeNo],tout.source_in,tout.CUSIP,tstat.TRD_STATUS as [tradeStatus] "+
			 " ,tout.TRAN_TYPE as [tradeType], tout.TRADE_DATE as [tradeDate],tout.SETTLE_DATE as [settleDate],tout.BROKER as [broker], tstat.EXP_STATUS as [exportStatus] "+
			 " from [dbo].TPH_TRADE_OUT tout LEFT OUTER JOIN  "+
			 " (select T1.TPH_TRD_SEQ_NO,T1.SOURCE_IN,T1.SOURCE_OUT,T1.TRD_STATUS,T1.EXP_STATUS "+
			 " from TPH_TRADE_STATUS T1 where not exists "+
			 " (select T2.TPH_TRD_SEQ_NO,T2.SOURCE_IN,T2.SOURCE_OUT,T2.TRD_STATUS,T2.EXP_STATUS from TPH_TRADE_STATUS T2 "+ 
			 " where  t1.TPH_TRD_SEQ_NO= t2.TPH_TRD_SEQ_NO "+
			 " AND t1.LAST_UPD_DATE < t2.LAST_UPD_DATE  )) tstat on tout.TPH_TRD_SEQ_NO=tstat.TPH_TRD_SEQ_NO  )  tr "+
			" LEFT OUTER JOIN dbo.TPH_SECURITY sec on tr.CUSIP=sec.CUSIP" +
			" WHERE tr.tradeDate=?";
			
			
	@Override
	public List<TradeSummaryBean> getTradeSummaryList()  throws Exception{
		// TODO Auto-generated method stub
		try{
				return get(SQL_TRADE_STATUS_LIST,TradeSummaryBean.class);
			}
			catch(Exception e){
				throw e;
			}
	}
	@Override
	public List<TradeSummaryBean> getTradeSummaryListForSource(String source)  throws Exception{
		// TODO Auto-generated method stub
		try{
			Object[] args=new Object[1];
			args[0]=source;
			return get(SQL_TRADE_STATUS_FOR_SOURCE,args,TradeSummaryBean.class);
		}
		catch(Exception e){
			throw e;
		}
	}
	@Override
	public List<TradeSummaryBean> getFilteredTSList(Date tradeDate)  throws Exception{
		// TODO Auto-generated method stub
		try{
			Object[] args=new Object[1];
			args[0]=tradeDate;
			return get(SQL_FILTERED_TRADE_STATUS_LIST,args,TradeSummaryBean.class);
		}
		catch(Exception e){
			throw e;
		}
		
		
	}
	@Override
	public List<TradeSummaryBean> getFilteredTrades(
			Map<String, Object> filterMap)  throws Exception{
		// TODO Auto-generated method stub
		try{
			String SQL_FILTERED_TRADES="select TPH_TRD_SEQ_NO as TPHTradeNo, "+
					" tout.TOUCH_COUNT,tout.SOURCE_OUT as [destination], "+
					 " SOURCE_IN as [source], "+
					  " ACCT_CD,tout.CUSIP, sec.description1 as securityName, "+ 
					  " tout.SEC_GROUP as securityGroup, sec.SEC_TYPE_CD as securityType, "+ 
					  " tout.TRAN_TYPE as tradeType, "+
					  " tout.TRADE_DATE as tradeDate,tout.SETTLE_DATE as settleDate, "+
			  " tout.broker, tout.FROM_AMOUNT as fromAmount,  "+
			  "  tout.TO_AMOUNT as toAmount, tout.FROM_CURRENCY as fromCurrency, "+
			  " tout.TO_CURRENCY as toCurrency, "+
			  " tout.EXEC_QTY, "+
			  " tout.EXEC_PRICE, "+
			  " tout.EXEC_AMT+tout.ACCR_INT_AMT as EXEC_AMT, "+
		      " CASE WHEN tout.TPH_STATUS='COMPLETE' THEN 'Processed' WHEN tout.TPH_STATUS='FAIL' "+
		    		  "  THEN 'Failed' WHEN tout.TPH_STATUS='READY' Then 'Pending' "+
							  "  WHEN tout.TPH_STATUS='MANUAL' then 'Manual' END TPH_STATUS "+
							  "  from [dbo].TPH_TRADE_OUT tout "+
			  " LEFT OUTER JOIN "+
			  " (SELECT s.SEC_TYPE_CD, s.DESCRIPTION1, s.source_out,s.CUSIP FROM TPH_SECURITY_OUT s WHERE s.SEC_ID IN  "+
				" (select max(SEC_ID) as SEC_ID from TPH_SECURITY_OUT group by CUSIP, SOURCE_OUT))sec "+
			  " on tout.CUSIP=sec.CUSIP and tout.source_out=sec.source_out";
			/*String SQL_FILTERED_TRADES="select TPH_TRD_SEQ_NO as TPHTradeNo,SOURCE_IN as [source],source_out as [destination],ACCT_CD,tout.CUSIP, sec.SEC_NAME as securityName, sec.SEC_GROUP as securityGroup, sec.SEC_TYPE_CD as securityType,"+ 
			" tout.TRAN_TYPE as tradeType,tout.TRADE_DATE as tradeDate,tout.SETTLE_DATE as settleDate, tout.broker, TPH_STATUS  from "+
			 " [dbo].TPH_TRADE_OUT tout"+ 
			" LEFT OUTER JOIN dbo.TPH_SECURITY sec on tout.CUSIP=sec.CUSIP";*/
		/*String SQL_FILTERED_TRADES=
		" select tr.TPHTradeNo,tr.SOURCE_IN as [source],tr.source_out as [destination],tr.CUSIP, sec.SEC_NAME as securityName, sec.SEC_GROUP as securityGroup, sec.SEC_TYPE_CD as securityType, "+
		 " tr.tradeStatus,tr.tradeType,tr.tradeDate,tr.settleDate,tr.broker, tr.exportStatus from (select tout.TPH_TRD_SEQ_NO as [TPHTradeNo],tout.source_in,tout.source_out,tout.CUSIP,tstat.TRD_STATUS as [tradeStatus] "+
		 " ,tout.TRAN_TYPE as [tradeType], tout.TRADE_DATE as [tradeDate],tout.SETTLE_DATE as [settleDate],tout.BROKER as [broker], tstat.EXP_STATUS as [exportStatus] "+
		 " from [dbo].TPH_TRADE_OUT tout LEFT OUTER JOIN  "+
		 " (select T1.TPH_TRD_SEQ_NO,T1.SOURCE_IN,T1.SOURCE_OUT,T1.TRD_STATUS,T1.EXP_STATUS "+
		 " from TPH_TRADE_STATUS T1 where not exists "+
		 " (select T2.TPH_TRD_SEQ_NO,T2.SOURCE_IN,T2.SOURCE_OUT,T2.TRD_STATUS,T2.EXP_STATUS from TPH_TRADE_STATUS T2 "+ 
		 " where  t1.TPH_TRD_SEQ_NO= t2.TPH_TRD_SEQ_NO "+
		 " AND t1.LAST_UPD_DATE < t2.LAST_UPD_DATE  )) tstat on tout.TPH_TRD_SEQ_NO=tstat.TPH_TRD_SEQ_NO  "+
		" )  tr "+
		" LEFT OUTER JOIN dbo.TPH_SECURITY sec on tr.CUSIP=sec.CUSIP";*/
		int filterCount=0;
		Object[] sqlargs = new Object[filterMap.size()];
		
		Iterator iter = filterMap.entrySet().iterator();
		 if(iter.hasNext()){ SQL_FILTERED_TRADES= SQL_FILTERED_TRADES+" WHERE ";}
		//adding filters to query
		 while (iter.hasNext()) {
			Map.Entry filterEntry = (Map.Entry) iter.next();
			System.out.println(filterEntry.getKey() + " : " + filterEntry.getValue().toString());
			if(filterEntry.getValue()!=null && filterEntry.getValue().toString().trim()!=""){
			String columnName=filterEntry.getKey().toString();
			Object filterValue=filterEntry.getValue();
			SQL_FILTERED_TRADES= SQL_FILTERED_TRADES+" "+columnName+"=? AND";
			 sqlargs[filterCount]=new Object();
			 //preparing argument list for SQL parameter binding
			 sqlargs[filterCount]=filterEntry.getValue();
			 System.out.println("args["+filterCount+"]="+sqlargs[filterCount]);
			 filterCount++;
			}
		}
		//removing last end
		if(SQL_FILTERED_TRADES.substring(SQL_FILTERED_TRADES.length() - 3).compareToIgnoreCase("AND")==0){
			SQL_FILTERED_TRADES = SQL_FILTERED_TRADES.substring(0, SQL_FILTERED_TRADES.length() - 3);
		}
		System.out.println("executing below query \n " +SQL_FILTERED_TRADES);
		
		return get(SQL_FILTERED_TRADES, sqlargs, TradeSummaryBean.class);	
		}
		// TODO Auto-generated method stub
		catch(Exception e){
			throw e;
		}
	}
	

}
