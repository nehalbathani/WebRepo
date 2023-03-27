package com.aflac.aims.tph.web.dao;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.aflac.aims.tph.web.model.OrderBean;
import com.aflac.aims.tph.web.model.ShortOrderBean;

@Repository
public class OrderDAOImpl extends CommonDAO implements OrderDAO {
	
	private static final String SQL_GET_ORDERS_BY_SOURCE="SELECT [ord].SOURCE as source"+
							",[ord].[TRAN_TYPE] as transactionType"+
							",[ord].[ORDER_REF_NO] as orderRefNo"+
					      ",[ord].[CUSIP]"+
						",[ord].[TRADE_DATE] as tradeDate"+
						",[ord].[SETTLE_DATE] as settleDate"+
					      ",[sec].[SEC_GROUP] as securityGroup"+
					      ",[sec].[SEC_TYPE_CD] as securityType"+
					      ",[sec].[SEC_NAME] as securityName"+
					      ",[ord].[BROKER_CD] as brokerCode"+
					      ",[ord].[EXEC_QTY] as executionQty"+
					      ",[ord].[EXEC_PRICE] as executionPrice"+
					      ",[ord].[EXEC_AMT] as executionAmount"+
					      ",[ord].[ORD_STATUS] as orderStatus"+
					  " FROM [dbo].[TPH_ORDER] ord  left outer join [dbo].[TPH_SECURITY] sec"+
					  		" on rtrim(ltrim([sec].[CUSIP]))=rtrim(ltrim([ord].[CUSIP])) "+
					  		" WHERE rtrim(ltrim(ord.[SOURCE]))=?";
			
	private  static final String SQL_GET_ORDER_LIST = "SELECT [SOURCE] as source"+
      ",[TRAN_TYPE] as transactionType"+
      ",[ORDER_REF_NO] as orderRefNo"+
      ",[CUSIP]"+
      ",[TICKER] as Ticker"+
      ",[SEDOL]"+
      ",[ISIN]"+
      ",[SEC_GROUP] as securityGroup"+
      ",[SEC_TYPE_CD] as securityTypeCode"+
      ",[BROKER_CD] as brokerCode"+
      ",[TRADE_DATE] as tradeDate"+
      ",[SETTLE_DATE] as settleDate"+
      ",[EXCH_CD] as exchangeCode"+
      ",[EXEC_QTY] as executionQty"+
      ",[EXEC_PRICE] as executionPrice"+
      ",[EXEC_AMT] as executionAmount"+
      ",[DELIVERY_TYPE] as deliveryType"+
      ",[PRIN_BASE_FX] as principalBaseFX"+
      ",[PRIN_SETTLE_FX] as principalSettleFX"+
      ",[DELIV_DATE] as deliveryDate"+
      ",[ORIG_FACE] as originalFaceValue"+
      ",[INT_AMT] as interestAmount"+
      ",[COMMISION_AMT] as commisionAmount "+
      ",[COMMISION_RATE] as commisionRate"+
      ",[FEE1_TYPE] as fee1Type"+
      ",[FEE1_AMT] as fee1Amount"+
      ",[FEE2_TYPE] as fee2Type"+
      ",[FEE2_AMT] as fee2Amount"+
      ",[NET_AMT] as netAmount"+
      ",[CONVEXITY] as convexity"+
      ",[EXEC_YIELD] as executionYield"+
      ",[SEC_FACTOR] as securityFactor"+
      ",[EFF_DURATION] as effectiveDuration"+
      ",[EFF_DATE] as effectiveDate"+
      ",[COMMENTS] as comments"+
      ",[ORD_STATUS] as orderStatus"+
      ",[CNCL_REASON] as cancelReason"+
      ",[EXT_SEC_ID] as externalSecurityId"+
      ",[LAST_UPD_USER] as lastUpdateUser"+
      ",[LAST_UPD_DATE] as lastUpdateDate"+
 " FROM [dbo].[TPH_ORDER]";
	
	@Override
	public List<OrderBean> getOrderList() throws Exception{
		try{
		return get(SQL_GET_ORDER_LIST, OrderBean.class);	
		}
		catch(Exception e){
			throw e;
		}
		
	}
	
	
	//get orders for specified source
	@Override
	public List<ShortOrderBean> getOrderListBySource(String source) throws Exception{
		try{
			Object[] args=new Object[1];
			args[0]=source;
		return get(SQL_GET_ORDERS_BY_SOURCE, args, ShortOrderBean.class);	
		}
		catch(Exception e){
			throw e;
		}
	}


	@Override
	public List<ShortOrderBean> getFilteredShortOrderList(
			Map<String, Object> filterMap) throws Exception{
		try{
			String SQL_FILTER_ORDERS="	SELECT  source, transactionType, orderRefNo"+
				     " ,o.CUSIP as cusip"+
					" , tradeDate"+
					" , settleDate"+
					" ,o.acct_cd"+
					" ,o.status"+
					" ,o.TOUCH_COUNT"+
					" ,o.CREATE_DATE"+
					" , brokerCode"+
					" , executionCurrency"+
					" , executionQty"+
					" , executionPrice"+
					" , executionAmount"+
					" , fixedCurrency"+
					" , floatCurrency"+
					" , orderStatus"+
					" ,[sec].[SEC_GROUP] as securityGroup"+
					" ,[sec].[SEC_TYPE_CD] as securityType"+
					" ,[sec].[SEC_NAME] as securityName from "+
					" (SELECT [ord].SOURCE as source"+
					" ,[ord].[TRAN_TYPE] as transactionType"+
					" ,[ord].[ORDER_REF_NO] as orderRefNo"+
					" ,[ord].[CUSIP]"+
					" ,[ord].[TRADE_DATE] as tradeDate"+
					" ,[ord].[SETTLE_DATE] as settleDate"+
					" ,[tr].ACCT_CD "+
					" ,[tr].STATUS"+
					" ,[tr].TRD_CURRENCY as executionCurrency"+
					" ,[tr].FX_RCV_CURR as floatCurrency"+
					" ,[tr].FX_PAY_CURR as fixedCurrency"+
					" ,[tr].CREATE_DATE "+
					" ,[ord].TOUCH_COUNT "+
					" ,[ord].[BROKER_CD] as brokerCode"+
					" ,[ord].[EXEC_QTY] as executionQty"+
					" ,[ord].[EXEC_PRICE] as executionPrice"+
					" ,[ord].[EXEC_AMT] as executionAmount"+
					" ,[ord].[ORD_STATUS] as orderStatus"+
					" FROM [dbo].[TPH_ORDER] ord, TPH_TRADE_IN tr where tr.TPH_TRD_SEQ_NO=ord.ORDER_REF_NO and tr.TOUCH_COUNT=ord.TOUCH_COUNT) o left outer join [dbo].[TPH_SECURITY] sec"+
					" on rtrim(ltrim([sec].[CUSIP]))=rtrim(ltrim([o].[CUSIP]))  ";
	/*	String SQL_FILTER_ORDERS="SELECT [ord].SOURCE as source"+
				",[ord].[TRAN_TYPE] as transactionType"+
				",[ord].[ORDER_REF_NO] as orderRefNo"+
		      ",[ord].[CUSIP]"+
			",[ord].[TRADE_DATE] as tradeDate"+
			",[ord].[SETTLE_DATE] as settleDate"+
		      ",[sec].[SEC_GROUP] as securityGroup"+
		      ",[sec].[SEC_TYPE_CD] as securityType"+
		      ",[sec].[SEC_NAME] as securityName"+
		      ",[ord].[BROKER_CD] as brokerCode"+
		      ",[ord].[EXEC_QTY] as executionQty"+
		      ",[ord].[EXEC_PRICE] as executionPrice"+
		      ",[ord].[EXEC_AMT] as executionAmount"+
		      ",[ord].[ORD_STATUS] as orderStatus"+
		  " FROM [dbo].[TPH_ORDER] ord  left outer join [dbo].[TPH_SECURITY] sec"+
		  		" on rtrim(ltrim([sec].[CUSIP]))=rtrim(ltrim([ord].[CUSIP])) ";
		*/
		int filterCount=0;
		Object[] sqlargs=new Object[filterMap.size()];
		
		Iterator iter = filterMap.entrySet().iterator();
		 if(iter.hasNext()){ SQL_FILTER_ORDERS= SQL_FILTER_ORDERS+" WHERE ";}
		//adding filters to query
		 while (iter.hasNext()) {
			Map.Entry filterEntry = (Map.Entry) iter.next();
			System.out.println(filterEntry.getKey() + " : " + filterEntry.getValue().toString());
			if(filterEntry.getValue()!=null && filterEntry.getValue().toString().trim()!=""){
			String columnName=filterEntry.getKey().toString();
			Object filterValue=filterEntry.getValue();
			 SQL_FILTER_ORDERS= SQL_FILTER_ORDERS+" "+columnName+"=? AND";
			 
			 //preparing argument list for SQL parameter binding
			 sqlargs[filterCount]=filterEntry.getValue();
			 filterCount++;
			}
		}
		//removing last end
		if(SQL_FILTER_ORDERS.substring(SQL_FILTER_ORDERS.length() - 3).compareToIgnoreCase("AND")==0){
			SQL_FILTER_ORDERS = SQL_FILTER_ORDERS.substring(0, SQL_FILTER_ORDERS.length() - 3);
		}
		System.out.println("executing below query \n " +SQL_FILTER_ORDERS);
		
		return get(SQL_FILTER_ORDERS, sqlargs, ShortOrderBean.class);	
		}
		// TODO Auto-generated method stub
	catch(Exception e){
		throw e;
		}
	}
	
	
}
