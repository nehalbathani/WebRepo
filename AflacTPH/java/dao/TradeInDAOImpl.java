package com.aflac.aims.tph.web.dao;

import java.sql.Types;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Repository;

import com.aflac.aims.tph.web.model.FieldMappingBean;
import com.aflac.aims.tph.web.model.ShortOrderBean;
import com.aflac.aims.tph.web.model.TradeINBean;
import com.aflac.aims.tph.web.model.TradeOverviewBean;
import com.aflac.aims.tph.web.utils.constants;

@Repository
public class TradeInDAOImpl extends CommonDAO implements TradeInDAO {
	protected static Logger logger = LoggerFactory.getLogger(TradeInDAOImpl.class);
	
	private static final String SQL_LATEST_TRADE_DATE="select max(trade_date) from [dbo].[TPH_TRADE_IN]";
	private static final String SQL_TRADE_LIST_BY_ID="SELECT [TPH_TRD_SEQ_NO]"+
		      ",[SOURCE]"+
		      ",[TRAN_TYPE]"+
		      ",[TRAN_TYPE1]"+
		      ",[TRAN_TYPE2]"+
		      ",[ACCT_CD]"+
		      ",[ORD_REF_NO]"+
		      ",[TRD_REF_NO]"+
		      ",[CUSIP]"+
		      ",[TICKER]"+
		      ",[SEDOL]"+
		      ",[ISIN]"+
		      ",[SEC_GROUP]"+
		      ",[SEC_TYPE_CD]"+
		      ",[BROKER_CD]"+
		      ",[TRADE_DATE]"+
		      ",[SETTLE_DATE]"+
		      ",[ACCRUAL_DT]"+
		      ",[ACCT_DESIG]"+
		      ",[BROKER]"+
		      ",[BROKER_REASON]"+
		      ",[COLLAT_EXPO_TYPE]"+
		      ",[COUNTERPARTY_CODE]"+
		      ",[CPN_TYPE]"+
		      ",[DESC_INSTMT]"+
		      ",[DESK]"+
		      ",[DESK_AKA]"+
		      ",[DESK_TYPE]"+
		      ",[DTM_2A7]"+
		      ",[EFF_TERM_DATE]"+
		      ",[EXECUTION_TIME]"+
		      ",[EXECUTION_TYPE]"+
		      ",[EXEC_TIME_SOURCE]"+
		      ",[EXTERNAL_ACCOUNT_ID]"+
		      ",[EX_SUB_BROKER_ID]"+
		      ",[FIRST_PAY_DT]"+
		      ",[FX_FWD_BUY_AMOUNT]"+
		      ",[FX_FWD_PAY_SETTLE_LOCATION]"+
		      ",[FX_FWD_PRICE]"+
		      ",[FX_FWD_RCV_SETTLE_LOCATION]"+
		      ",[FX_FWD_SEC_TYPE]"+
		      ",[FX_FWD_SELL_AMOUNT]"+
		      ",[FX_PAY_AMT]"+
		      ",[FX_PAY_CURR]"+
		      ",[FX_PAY_SETTLE_LOCATION]"+
		      ",[FX_PRICE]"+
		      ",[FX_PRICE_SPOT]"+
		      ",[FX_RCV_AMT]"+
		      ",[FX_RCV_CURR]"+
		      ",[FX_RCV_SETTLE_LOCATION]"+
		      ",[INT_AT_MATURITY]"+
		      ",[MATURITY]"+
		      ",[MBSCC_CUSIP]"+
		      ",[MBSCC_TRAN_TYPE]"+
		      ",[MTG_SUBTYPE]"+
		      ",[MULTI_FUND_ID]"+
		      ",[NOVATION_TYPE]"+
		      ",[ORD_PM]"+
		      ",[ORIG_INVNUM]"+
		      ",[OTHER_FUND]"+
		      ",[OTHER_INVNUM]"+
		      ",[PLACEMENT_NUM]"+
		      ",[PORTFOLIOS_PORTFOLIO_NAME]"+
		      ",[PREPAY_RATE]"+
		      ",[PREPAY_TYPE]"+
		      ",[REPO_SETTLE_DATE]"+
		      ",[REPO_TRAN_TYPE]"+
		      ",[ROLL_END_DATE]"+
		      ",[ROLL_FEE]"+
		      ",[ROLL_FWD_INTEREST]"+
		      ",[ROLL_FWD_PRICE]"+
		      ",[ROLL_FWD_PRINCIPAL]"+
		      ",[ROLL_INVNUM1]"+
		      ",[ROLL_INVNUM2]"+
		      ",[SETTLE_EXCH_RATE]"+
		      ",[SM_COUPON_FREQ]"+
		      ",[SM_PRINCIPAL_FREQ]"+
		      ",[STRATEGY_ID]"+
		      ",[SUB_BROKER_ID]"+
		      ",[TBA_CUSIP]"+
		      ",[TBA_CUSIP9]"+
		      ",[TBA_INVNUM]"+
		      ",[TBA_TRD_DATE]"+
		      ",[TOUCH_COUNT]"+
		      ",[TRADE_INTERFACE_INFO]"+
		      ",[TRAN_TYPE_DERIV]"+
		      ",[TRD_AUTH_TIME]"+
		      ",[TRD_BROKER_AKA]"+
		      ",[TRD_COMMISSION]"+
		      ",[TRD_COMMISSION_RATE]"+
		      ",[TRD_COMMISSION_TYPE]"+
		      ",[TRD_CONFIRMED_BY]"+
		      ",[TRD_CONVEXITY]"+
		      ",[TRD_COUNTERPARTY]"+
		      ",[TRD_COUPON]"+
		      ",[TRD_CURRENCY]"+
		      ",[TRD_DIRTY_PRICE]"+
		      ",[TRD_DISCOUNT]"+
		      ",[TRD_DROP_RATE]"+
		      ",[TRD_DURATION]"+
		      ",[TRD_ENTRY_TIME]"+
		      ",[TRD_EXCHANGE_RATE]"+
		      ",[TRD_EXTERNAL_ACCOUNT_ID]"+
		      ",[TRD_EXTERNAL_ACCOUNT_LOC]"+
		      ",[TRD_EX_BROKER_DESK]"+
		      ",[TRD_EX_COMMISSION]"+
		      ",[TRD_EX_DESK_TYPE]"+
		      ",[TRD_FACTOR]"+
		      ",[TRD_FACT_DT]"+
		      ",[TRD_FIFO_TYPE]"+
		      ",[TRD_FINANCING_RATE]"+
		      ",[TRD_FLAGS]"+
		      ",[TRD_INTEREST]"+
		      ",[TRD_LOCATION]"+
		      ",[TRD_MODIFIED_BY]"+
		      ",[TRD_MODIFY_DATE]"+
		      ",[TRD_MODIFY_TIME]"+
		      ",[TRD_ORIG_ENTRY_DATE]"+
		      ",[TRD_ORIG_FACE]"+
		      ",[TRD_OTHER_FEE]"+
		      ",[TRD_PRICE]"+
		      ",[TRD_PRICING_INDEX]"+
		      ",[TRD_PRICING_SPREAD]"+
		      ",[TRD_PRINCIPAL]"+
		      ",[TRD_PURPOSE]"+
		      ",[TRD_REVIEWED_BY]"+
		      ",[TRD_REVIEW_TIME]"+
		      ",[TRD_SERIES_NUM]"+
		      ",[TRD_SETTLE_LOCATION]"+
		      ",[TRD_SETTLE_TEMPLATE_NAME]"+
		      ",[TRD_STATUS]"+
		      ",[TRD_TBA_ALLOWED_VARIANCE]"+
		      ",[TRD_TD_PAR]"+
		      ",[TRD_TRADER]"+
		      ",[TRD_VERSION]"+
		      ",[TRD_YIELD]"+
		      ",[TRD_YIELD_TO_CALL]"+
		      ",[UNITS]"+
		      ",[LAST_UPD_USER]"+
		      ",[LAST_UPD_DATE]"+ 
		      "FROM [dbo].[TPH_TRADE_IN] where TPH_TRD_SEQ_NO=?";
	
private static final String SQL_FX_TRADE_LIST_FOR_SOURCE= "SELECT [TPH_TRD_SEQ_NO]"+
      ",[SOURCE]"+
      ",[TRAN_TYPE]"+
      ",[TRAN_TYPE1]"+
      ",[TRAN_TYPE2]"+
      ",[ACCT_CD]"+
      ",[ORD_REF_NO]"+
      ",[TRD_REF_NO]"+
      ",[CUSIP]"+
      ",[TICKER]"+
      ",[SEDOL]"+
      ",[ISIN]"+
      ",[SEC_GROUP]"+
      ",[SEC_TYPE_CD]"+
      ",[BROKER_CD]"+
      ",[TRADE_DATE]"+
      ",[SETTLE_DATE]"+
      ",[ACCRUAL_DT]"+
      ",[ACCT_DESIG]"+
      ",[BROKER]"+
      ",[BROKER_REASON]"+
      ",[COLLAT_EXPO_TYPE]"+
      ",[COUNTERPARTY_CODE]"+
      ",[CPN_TYPE]"+
      ",[DESC_INSTMT]"+
      ",[DESK]"+
      ",[DESK_AKA]"+
      ",[DESK_TYPE]"+
      ",[DTM_2A7]"+
      ",[EFF_TERM_DATE]"+
      ",[EXECUTION_TIME]"+
      ",[EXECUTION_TYPE]"+
      ",[EXEC_TIME_SOURCE]"+
      ",[EXTERNAL_ACCOUNT_ID]"+
      ",[EX_SUB_BROKER_ID]"+
      ",[FIRST_PAY_DT]"+
      ",[FX_FWD_BUY_AMOUNT]"+
      ",[FX_FWD_PAY_SETTLE_LOCATION]"+
      ",[FX_FWD_PRICE]"+
      ",[FX_FWD_RCV_SETTLE_LOCATION]"+
      ",[FX_FWD_SEC_TYPE]"+
      ",[FX_FWD_SELL_AMOUNT]"+
      ",[FX_PAY_AMT]"+
      ",[FX_PAY_CURR]"+
      ",[FX_PAY_SETTLE_LOCATION]"+
      ",[FX_PRICE]"+
      ",[FX_PRICE_SPOT]"+
      ",[FX_RCV_AMT]"+
      ",[FX_RCV_CURR]"+
      ",[FX_RCV_SETTLE_LOCATION]"+
      ",[INT_AT_MATURITY]"+
      ",[MATURITY]"+
      ",[MBSCC_CUSIP]"+
      ",[MBSCC_TRAN_TYPE]"+
      ",[MTG_SUBTYPE]"+
      ",[MULTI_FUND_ID]"+
      ",[NOVATION_TYPE]"+
      ",[ORD_PM]"+
      ",[ORIG_INVNUM]"+
      ",[OTHER_FUND]"+
      ",[OTHER_INVNUM]"+
      ",[PLACEMENT_NUM]"+
      ",[PORTFOLIOS_PORTFOLIO_NAME]"+
      ",[PREPAY_RATE]"+
      ",[PREPAY_TYPE]"+
      ",[REPO_SETTLE_DATE]"+
      ",[REPO_TRAN_TYPE]"+
      ",[ROLL_END_DATE]"+
      ",[ROLL_FEE]"+
      ",[ROLL_FWD_INTEREST]"+
      ",[ROLL_FWD_PRICE]"+
      ",[ROLL_FWD_PRINCIPAL]"+
      ",[ROLL_INVNUM1]"+
      ",[ROLL_INVNUM2]"+
      ",[SETTLE_EXCH_RATE]"+
      ",[SM_COUPON_FREQ]"+
      ",[SM_PRINCIPAL_FREQ]"+
      ",[STRATEGY_ID]"+
      ",[SUB_BROKER_ID]"+
      ",[TBA_CUSIP]"+
      ",[TBA_CUSIP9]"+
      ",[TBA_INVNUM]"+
      ",[TBA_TRD_DATE]"+
      ",[TOUCH_COUNT]"+
      ",[TRADE_INTERFACE_INFO]"+
      ",[TRAN_TYPE_DERIV]"+
      ",[TRD_AUTH_TIME]"+
      ",[TRD_BROKER_AKA]"+
      ",[TRD_COMMISSION]"+
      ",[TRD_COMMISSION_RATE]"+
      ",[TRD_COMMISSION_TYPE]"+
      ",[TRD_CONFIRMED_BY]"+
      ",[TRD_CONVEXITY]"+
      ",[TRD_COUNTERPARTY]"+
      ",[TRD_COUPON]"+
      ",[TRD_CURRENCY]"+
      ",[TRD_DIRTY_PRICE]"+
      ",[TRD_DISCOUNT]"+
      ",[TRD_DROP_RATE]"+
      ",[TRD_DURATION]"+
      ",[TRD_ENTRY_TIME]"+
      ",[TRD_EXCHANGE_RATE]"+
      ",[TRD_EXTERNAL_ACCOUNT_ID]"+
      ",[TRD_EXTERNAL_ACCOUNT_LOC]"+
      ",[TRD_EX_BROKER_DESK]"+
      ",[TRD_EX_COMMISSION]"+
      ",[TRD_EX_DESK_TYPE]"+
      ",[TRD_FACTOR]"+
      ",[TRD_FACT_DT]"+
      ",[TRD_FIFO_TYPE]"+
      ",[TRD_FINANCING_RATE]"+
      ",[TRD_FLAGS]"+
      ",[TRD_INTEREST]"+
      ",[TRD_LOCATION]"+
      ",[TRD_MODIFIED_BY]"+
      ",[TRD_MODIFY_DATE]"+
      ",[TRD_MODIFY_TIME]"+
      ",[TRD_ORIG_ENTRY_DATE]"+
      ",[TRD_ORIG_FACE]"+
      ",[TRD_OTHER_FEE]"+
      ",[TRD_PRICE]"+
      ",[TRD_PRICING_INDEX]"+
      ",[TRD_PRICING_SPREAD]"+
      ",[TRD_PRINCIPAL]"+
      ",[TRD_PURPOSE]"+
      ",[TRD_REVIEWED_BY]"+
      ",[TRD_REVIEW_TIME]"+
      ",[TRD_SERIES_NUM]"+
      ",[TRD_SETTLE_LOCATION]"+
      ",[TRD_SETTLE_TEMPLATE_NAME]"+
      ",[TRD_STATUS]"+
      ",[TRD_TBA_ALLOWED_VARIANCE]"+
      ",[TRD_TD_PAR]"+
      ",[TRD_TRADER]"+
      ",[TRD_VERSION]"+
      ",[TRD_YIELD]"+
      ",[TRD_YIELD_TO_CALL]"+
      ",[UNITS]"+
      ",[LAST_UPD_USER]"+
      ",[LAST_UPD_DATE]"+
      "FROM [dbo].[TPH_TRADE_IN] where SOURCE=? and STATUS='"+constants.TRADE_STATUS1_M+"'";
	
private static final String SQL_TRADE_LIST_FOR_ORDER= "SELECT [TPH_TRD_SEQ_NO]"+
      ",[SOURCE]"+
      ",[TRAN_TYPE]"+
      ",[TRAN_TYPE1]"+
      ",[TRAN_TYPE2]"+
      ",[ACCT_CD]"+
      ",[ORD_REF_NO]"+
      ",[TRD_REF_NO]"+
      ",[CUSIP]"+
      ",[TICKER]"+
      ",[SEDOL]"+
      ",[ISIN]"+
      ",[SEC_GROUP]"+
      ",[SEC_TYPE_CD]"+
      ",[BROKER_CD]"+
      ",[TRADE_DATE]"+
      ",[SETTLE_DATE]"+
      ",[ACCRUAL_DT]"+
      ",[ACCT_DESIG]"+
      ",[BROKER]"+
      ",[BROKER_REASON]"+
      ",[COLLAT_EXPO_TYPE]"+
      ",[COUNTERPARTY_CODE]"+
      ",[CPN_TYPE]"+
      ",[DESC_INSTMT]"+
      ",[DESK]"+
      ",[DESK_AKA]"+
      ",[DESK_TYPE]"+
      ",[DTM_2A7]"+
      ",[EFF_TERM_DATE]"+
      ",[EXECUTION_TIME]"+
      ",[EXECUTION_TYPE]"+
      ",[EXEC_TIME_SOURCE]"+
      ",[EXTERNAL_ACCOUNT_ID]"+
      ",[EX_SUB_BROKER_ID]"+
      ",[FIRST_PAY_DT]"+
      ",[FX_FWD_BUY_AMOUNT]"+
      ",[FX_FWD_PAY_SETTLE_LOCATION]"+
      ",[FX_FWD_PRICE]"+
      ",[FX_FWD_RCV_SETTLE_LOCATION]"+
      ",[FX_FWD_SEC_TYPE]"+
      ",[FX_FWD_SELL_AMOUNT]"+
      ",[FX_PAY_AMT]"+
      ",[FX_PAY_CURR]"+
      ",[FX_PAY_SETTLE_LOCATION]"+
      ",[FX_PRICE]"+
      ",[FX_PRICE_SPOT]"+
      ",[FX_RCV_AMT]"+
      ",[FX_RCV_CURR]"+
      ",[FX_RCV_SETTLE_LOCATION]"+
      ",[INT_AT_MATURITY]"+
      ",[MATURITY]"+
      ",[MBSCC_CUSIP]"+
      ",[MBSCC_TRAN_TYPE]"+
      ",[MTG_SUBTYPE]"+
      ",[MULTI_FUND_ID]"+
      ",[NOVATION_TYPE]"+
      ",[ORD_PM]"+
      ",[ORIG_INVNUM]"+
      ",[OTHER_FUND]"+
      ",[OTHER_INVNUM]"+
      ",[PLACEMENT_NUM]"+
      ",[PORTFOLIOS_PORTFOLIO_NAME]"+
      ",[PREPAY_RATE]"+
      ",[PREPAY_TYPE]"+
      ",[REPO_SETTLE_DATE]"+
      ",[REPO_TRAN_TYPE]"+
      ",[ROLL_END_DATE]"+
      ",[ROLL_FEE]"+
      ",[ROLL_FWD_INTEREST]"+
      ",[ROLL_FWD_PRICE]"+
      ",[ROLL_FWD_PRINCIPAL]"+
      ",[ROLL_INVNUM1]"+
      ",[ROLL_INVNUM2]"+
      ",[SETTLE_EXCH_RATE]"+
      ",[SM_COUPON_FREQ]"+
      ",[SM_PRINCIPAL_FREQ]"+
      ",[STRATEGY_ID]"+
      ",[SUB_BROKER_ID]"+
      ",[TBA_CUSIP]"+
      ",[TBA_CUSIP9]"+
      ",[TBA_INVNUM]"+
      ",[TBA_TRD_DATE]"+
      ",[TOUCH_COUNT]"+
      ",[TRADE_INTERFACE_INFO]"+
      ",[TRAN_TYPE_DERIV]"+
      ",[TRD_AUTH_TIME]"+
      ",[TRD_BROKER_AKA]"+
      ",[TRD_COMMISSION]"+
      ",[TRD_COMMISSION_RATE]"+
      ",[TRD_COMMISSION_TYPE]"+
      ",[TRD_CONFIRMED_BY]"+
      ",[TRD_CONVEXITY]"+
      ",[TRD_COUNTERPARTY]"+
      ",[TRD_COUPON]"+
      ",[TRD_CURRENCY]"+
      ",[TRD_DIRTY_PRICE]"+
      ",[TRD_DISCOUNT]"+
      ",[TRD_DROP_RATE]"+
      ",[TRD_DURATION]"+
      ",[TRD_ENTRY_TIME]"+
      ",[TRD_EXCHANGE_RATE]"+
      ",[TRD_EXTERNAL_ACCOUNT_ID]"+
      ",[TRD_EXTERNAL_ACCOUNT_LOC]"+
      ",[TRD_EX_BROKER_DESK]"+
      ",[TRD_EX_COMMISSION]"+
      ",[TRD_EX_DESK_TYPE]"+
      ",[TRD_FACTOR]"+
      ",[TRD_FACT_DT]"+
      ",[TRD_FIFO_TYPE]"+
      ",[TRD_FINANCING_RATE]"+
      ",[TRD_FLAGS]"+
      ",[TRD_INTEREST]"+
      ",[TRD_LOCATION]"+
      ",[TRD_MODIFIED_BY]"+
      ",[TRD_MODIFY_DATE]"+
      ",[TRD_MODIFY_TIME]"+
      ",[TRD_ORIG_ENTRY_DATE]"+
      ",[TRD_ORIG_FACE]"+
      ",[TRD_OTHER_FEE]"+
      ",[TRD_PRICE]"+
      ",[TRD_PRICING_INDEX]"+
      ",[TRD_PRICING_SPREAD]"+
      ",[TRD_PRINCIPAL]"+
      ",[TRD_PURPOSE]"+
      ",[TRD_REVIEWED_BY]"+
      ",[TRD_REVIEW_TIME]"+
      ",[TRD_SERIES_NUM]"+
      ",[TRD_SETTLE_LOCATION]"+
      ",[TRD_SETTLE_TEMPLATE_NAME]"+
      ",[TRD_STATUS]"+
      ",[TRD_TBA_ALLOWED_VARIANCE]"+
      ",[TRD_TD_PAR]"+
      ",[TRD_TRADER]"+
      ",[TRD_VERSION]"+
      ",[TRD_YIELD]"+
      ",[TRD_YIELD_TO_CALL]"+
      ",[UNITS]"+
      ",[LAST_UPD_USER]"+
      ",[LAST_UPD_DATE]"+ ",CASE WHEN [STATUS]='P' THEN 'Processed' WHEN [STATUS]='F' THEN 'Failed' WHEN [STATUS]='N' Then 'Pending' WHEN [STATUS]='D' then 'Deleted' WHEN [STATUS]='M' then 'Manual' END STATUS" +
      " FROM [dbo].[TPH_TRADE_IN] where TPH_TRD_SEQ_NO=? AND TOUCH_COUNT=?";

private static final String SQL_SHORT_TRADE_FOR_SOURCE = "SELECT [ord].SOURCE as source"+
		",[ord].[TRAN_TYPE] as transactionType"+
		",[ord].[TRD_REF_NO] as orderRefNo"+
      ",[ord].[CUSIP]"+
      ",[sec].[SEC_GROUP] as securityGroup"+
      ",[sec].[SEC_TYPE_CD] as securityType"+
      ",[sec].[SEC_NAME] as securityName"+
      ",[ord].[BROKER_CD] as brokerCode"+
      ",[ord].[TRD_TD_PAR] as executionQty"+
      ",[ord].[TRD_PRICE] as executionPrice"+
      ",[ord].[TRD_ORIG_FACE] as executionAmount"+
      ",[ord].[TRD_STATUS] as orderStatus"+
  " FROM [dbo].[TPH_TRADE_IN] ord  left outer join [dbo].[TPH_SECURITY] sec"+
  		" on rtrim(ltrim([sec].[CUSIP]))=rtrim(ltrim([ord].[CUSIP])) "+
  		" WHERE rtrim(ltrim(ord.[SOURCE]))=?";

private static final String SQL_SHORT_FXTRADE_FOR_SOURCE = "SELECT [ord].SOURCE as source"+
		",[ord].[TRAN_TYPE] as transactionType"+
		",[ord].[TRD_REF_NO] as orderRefNo"+
      ",[ord].[CUSIP]"+
      ",[sec].[SEC_GROUP] as securityGroup"+
      ",[sec].[SEC_TYPE_CD] as securityType"+
      ",[sec].[SEC_NAME] as securityName"+
      ",[ord].[BROKER_CD] as brokerCode"+
      ",[ord].[TRD_TD_PAR] as executionQty"+
      ",[ord].[TRD_PRICE] as executionPrice"+
      ",[ord].[TRD_ORIG_FACE] as executionAmount"+
      ",[ord].[TRD_STATUS] as orderStatus"+
  " FROM [dbo].[TPH_TRADE_IN] ord  left outer join [dbo].[TPH_SECURITY] sec"+
  		" on rtrim(ltrim([sec].[CUSIP]))=rtrim(ltrim([ord].[CUSIP])) WHERE [sec].[SEC_GROUP]='"+constants.SEC_GROUP_FX+"'"+
  		" AND rtrim(ltrim(ord.[SOURCE]))=?";

private static final String SQL_FILTERED_FX_TRADE_LIST = "SELECT [TPH_TRD_SEQ_NO]"+
	      ",[SOURCE]"+
	      ",[TRAN_TYPE]"+
	      ",[TRAN_TYPE1]"+
	      ",[TRAN_TYPE2]"+
	      ",[ACCT_CD]"+
	      ",[ORD_REF_NO]"+
	      ",[TRD_REF_NO]"+
	      ",[CUSIP]"+
	      ",[TICKER]"+
	      ",[SEDOL]"+
	      ",[ISIN]"+
	      ",[SEC_GROUP]"+
	      ",[SEC_TYPE_CD]"+
	      ",[BROKER_CD]"+
	      ",[TRADE_DATE]"+
	      ",[SETTLE_DATE]"+
	      ",[ACCRUAL_DT]"+
	      ",[ACCT_DESIG]"+
	      ",[BROKER]"+
	      ",[BROKER_REASON]"+
	      ",[COLLAT_EXPO_TYPE]"+
	      ",[COUNTERPARTY_CODE]"+
	      ",[CPN_TYPE]"+
	      ",[DESC_INSTMT]"+
	      ",[DESK]"+
	      ",[DESK_AKA]"+
	      ",[DESK_TYPE]"+
	      ",[DTM_2A7]"+
	      ",[EFF_TERM_DATE]"+
	      ",[EXECUTION_TIME]"+
	      ",[EXECUTION_TYPE]"+
	      ",[EXEC_TIME_SOURCE]"+
	      ",[EXTERNAL_ACCOUNT_ID]"+
	      ",[EX_SUB_BROKER_ID]"+
	      ",[FIRST_PAY_DT]"+
	      ",[FX_FWD_BUY_AMOUNT]"+
	      ",[FX_FWD_PAY_SETTLE_LOCATION]"+
	      ",[FX_FWD_PRICE]"+
	      ",[FX_FWD_RCV_SETTLE_LOCATION]"+
	      ",[FX_FWD_SEC_TYPE]"+
	      ",[FX_FWD_SELL_AMOUNT]"+
	      ",[FX_PAY_AMT]"+
	      ",[FX_PAY_CURR]"+
	      ",[FX_PAY_SETTLE_LOCATION]"+
	      ",[FX_PRICE]"+
	      ",[FX_PRICE_SPOT]"+
	      ",[FX_RCV_AMT]"+
	      ",[FX_RCV_CURR]"+
	      ",[FX_RCV_SETTLE_LOCATION]"+
	      ",[INT_AT_MATURITY]"+
	      ",[MATURITY]"+
	      ",[MBSCC_CUSIP]"+
	      ",[MBSCC_TRAN_TYPE]"+
	      ",[MTG_SUBTYPE]"+
	      ",[MULTI_FUND_ID]"+
	      ",[NOVATION_TYPE]"+
	      ",[ORD_PM]"+
	      ",[ORIG_INVNUM]"+
	      ",[OTHER_FUND]"+
	      ",[OTHER_INVNUM]"+
	      ",[PLACEMENT_NUM]"+
	      ",[PORTFOLIOS_PORTFOLIO_NAME]"+
	      ",[PREPAY_RATE]"+
	      ",[PREPAY_TYPE]"+
	      ",[REPO_SETTLE_DATE]"+
	      ",[REPO_TRAN_TYPE]"+
	      ",[ROLL_END_DATE]"+
	      ",[ROLL_FEE]"+
	      ",[ROLL_FWD_INTEREST]"+
	      ",[ROLL_FWD_PRICE]"+
	      ",[ROLL_FWD_PRINCIPAL]"+
	      ",[ROLL_INVNUM1]"+
	      ",[ROLL_INVNUM2]"+
	      ",[SETTLE_EXCH_RATE]"+
	      ",[SM_COUPON_FREQ]"+
	      ",[SM_PRINCIPAL_FREQ]"+
	      ",[STRATEGY_ID]"+
	      ",[SUB_BROKER_ID]"+
	      ",[TBA_CUSIP]"+
	      ",[TBA_CUSIP9]"+
	      ",[TBA_INVNUM]"+
	      ",[TBA_TRD_DATE]"+
	      ",[TOUCH_COUNT]"+
	      ",[TRADE_INTERFACE_INFO]"+
	      ",[TRAN_TYPE_DERIV]"+
	      ",[TRD_AUTH_TIME]"+
	      ",[TRD_BROKER_AKA]"+
	      ",[TRD_COMMISSION]"+
	      ",[TRD_COMMISSION_RATE]"+
	      ",[TRD_COMMISSION_TYPE]"+
	      ",[TRD_CONFIRMED_BY]"+
	      ",[TRD_CONVEXITY]"+
	      ",[TRD_COUNTERPARTY]"+
	      ",[TRD_COUPON]"+
	      ",[TRD_CURRENCY]"+
	      ",[TRD_DIRTY_PRICE]"+
	      ",[TRD_DISCOUNT]"+
	      ",[TRD_DROP_RATE]"+
	      ",[TRD_DURATION]"+
	      ",[TRD_ENTRY_TIME]"+
	      ",[TRD_EXCHANGE_RATE]"+
	      ",[TRD_EXTERNAL_ACCOUNT_ID]"+
	      ",[TRD_EXTERNAL_ACCOUNT_LOC]"+
	      ",[TRD_EX_BROKER_DESK]"+
	      ",[TRD_EX_COMMISSION]"+
	      ",[TRD_EX_DESK_TYPE]"+
	      ",[TRD_FACTOR]"+
	      ",[TRD_FACT_DT]"+
	      ",[TRD_FIFO_TYPE]"+
	      ",[TRD_FINANCING_RATE]"+
	      ",[TRD_FLAGS]"+
	      ",[TRD_INTEREST]"+
	      ",[TRD_LOCATION]"+
	      ",[TRD_MODIFIED_BY]"+
	      ",[TRD_MODIFY_DATE]"+
	      ",[TRD_MODIFY_TIME]"+
	      ",[TRD_ORIG_ENTRY_DATE]"+
	      ",[TRD_ORIG_FACE]"+
	      ",[TRD_OTHER_FEE]"+
	      ",[TRD_PRICE]"+
	      ",[TRD_PRICING_INDEX]"+
	      ",[TRD_PRICING_SPREAD]"+
	      ",[TRD_PRINCIPAL]"+
	      ",[TRD_PURPOSE]"+
	      ",[TRD_REVIEWED_BY]"+
	      ",[TRD_REVIEW_TIME]"+
	      ",[TRD_SERIES_NUM]"+
	      ",[TRD_SETTLE_LOCATION]"+
	      ",[TRD_SETTLE_TEMPLATE_NAME]"+
	      ",[TRD_STATUS]"+
	      ",[TRD_TBA_ALLOWED_VARIANCE]"+
	      ",[TRD_TD_PAR]"+
	      ",[TRD_TRADER]"+
	      ",[TRD_VERSION]"+
	      ",[TRD_YIELD]"+
	      ",[TRD_YIELD_TO_CALL]"+
	      ",[UNITS]"+
	      ",[LAST_UPD_USER]"+
	      ",[LAST_UPD_DATE]"+
	      "FROM [dbo].[TPH_TRADE_IN] where TRADE_DATE=? and SEC_GROUP='"+constants.SEC_GROUP_FX+"'";
		
	

	@Override
	public List<TradeINBean> getTradeListforOrder(int orderRefId,int touchCount) throws Exception{
		try {
		Object[] args=new Object[2];
		args[0]=new Integer(orderRefId);
		args[1]=new Integer(touchCount);
		return get(SQL_TRADE_LIST_FOR_ORDER,args,TradeINBean.class);
		}
		catch(Exception e){
			throw e;
		}
	}

	@Override
	public List<TradeINBean> getTradeListByID(int tradeRefNo) throws Exception{
		// TODO Auto-generated method stub
		try {
			Object[] args=new Object[1];
			args[0]=new Integer(tradeRefNo);
			return get(SQL_TRADE_LIST_BY_ID,args,TradeINBean.class);
			}
			catch(Exception e){
				throw e;
			}
	}

	@Override
	public List<ShortOrderBean> getShortTradesForSource(String string) throws Exception{
		// TODO Auto-generated method stub
		try {
			Object[] args=new Object[1];
			args[0]=string;
			return get(SQL_SHORT_TRADE_FOR_SOURCE,args,ShortOrderBean.class);
			}
			catch(Exception e){
				throw e;
			}
	}

	@Override
	public List<TradeINBean> getFXTradeListForSource(String source) throws Exception{
		// TODO Auto-generated method stub
		try {
			Object[] args=new Object[1];
			args[0]=source;
			return get(SQL_FX_TRADE_LIST_FOR_SOURCE,args,TradeINBean.class);
			}
			catch(Exception e){
				throw e;
			}
	}

	@Override
	public List<TradeINBean> getFilteredFXTradeList(Date tradeDate) throws Exception{
		// TODO Auto-generated method stub
		try {
			Object[] args=new Object[1];
			args[0]=tradeDate;
			return get(SQL_FILTERED_FX_TRADE_LIST,args,TradeINBean.class);
			}
			catch(Exception e){
				throw e;
			}	
	}
	
	@Override
	public Date getLatestTradeDate() throws Exception{
		try{
			
			return this.jdbcTemplate.queryForObject(SQL_LATEST_TRADE_DATE,Date.class);
			}
			catch(Exception e){
				throw e;
			}
	}

	//public static final String SQL_REPROCESS_TRADE="UPDATE TPH_TRADE_IN SET STATUS='"+constants.TRADE_STATUS1_N+"'" +
	//												"WHERE TRD_REF_NO=? AND TOUCH_COUNT=?";	
	
	@Override
	public int reprocessTrade(int tradeRefNo, int touchCount) {
		// TODO Auto-generated method stub
		try{
			SimpleJdbcCall reprocessCall=new SimpleJdbcCall(this.jdbcTemplate);
			reprocessCall.setProcedureName("P_ReprocessTrade");
			reprocessCall.declareParameters(new SqlOutParameter("status", Types.INTEGER));
			Map<String, Object> out= reprocessCall.execute(new MapSqlParameterSource().addValue("trdRefNo", tradeRefNo).addValue("touchCnt", touchCount));
			logger.info("status is:" + out.get("status"));
			return (Integer) out.get("status");
		}
		catch(Exception e){
			logger.error("Exception:", e);
			return -1;
		}
	}
	
	@Override
	public int cancelTrade(int tradeSeqNo) {
		// TODO Auto-generated method stub
		try {
			SimpleJdbcCall discardCall = new SimpleJdbcCall(this.jdbcTemplate);
			discardCall.setProcedureName("P_DeleteTrade");
			discardCall.declareParameters(new SqlOutParameter("status", Types.INTEGER));
			Map<String, Object> out = discardCall.execute(new MapSqlParameterSource().addValue(
							                                            "trdID", tradeSeqNo));
			return (Integer) out.get("status");
		} catch(Exception e) {
			logger.error("Exception:", e);
			return -1;
		}
	}
		
	@Override
	public List<FieldMappingBean> getSourceValueForFieldMap(
			List<FieldMappingBean> fieldMap, int tradeRefNo, int touchCount) throws Exception{
		// TODO Auto-generated method stub
		for(FieldMappingBean mapEntry : fieldMap){
			String SQL_GET_FIELD_VALUE="SELECT "+mapEntry.getSourceTag()+" FROM TPH_TRADE_IN WHERE TPH_TRD_SEQ_NO=? AND TOUCH_COUNT=?";
			Object[] args=new Object[2];
			args[0]=tradeRefNo;
			args[1]=touchCount;
			mapEntry.setSourceTagValue(this.jdbcTemplate.queryForObject(SQL_GET_FIELD_VALUE, args, Object.class));
		}
		return fieldMap;
	}

	@Override
	public TradeINBean getInTradeByID(int tradeRefNo, int touch_count) throws Exception{
		// TODO Auto-generated method stub
		try{
			String SQL_GET_TRADE_BY_ID=SQL_TRADE_LIST_BY_ID+" AND TOUCH_COUNT=?";
			Object[] args=new Object[2];
			args[0]=tradeRefNo;
			args[1]=touch_count;
			logger.info(SQL_GET_TRADE_BY_ID);
			//System.out.println(SQL_GET_TRADE_BY_ID);
			return get(SQL_GET_TRADE_BY_ID, args, TradeINBean.class).get(0);
		}
		catch(Exception e){
			throw e;
		}
	}
	
	@Override
	public List<TradeOverviewBean> getFilteredTradeList(
			Map<String, Object> filterMap) throws Exception{
		try{
			String SQL_FILTER_TRADES="SELECT  TPH_TRD_SEQ_NO,TRD_REF_NO,source, TRAN_TYPE as transactionType, cusip, TRADE_DATE as tradeDate, SETTLE_DATE as settleDate,acct_cd ,status"+
					" ,TOUCH_COUNT ,CREATE_DATE , BROKER_CD as brokerCode, TRD_CURRENCY as executionCurrency, TRD_TD_PAR as executionQty, TRD_PRICE as executionPrice , TRD_PRINCIPAL+TRD_INTEREST as executionAmount"+
					" , FX_PAY_CURR as fixedCurrency , FX_RCV_CURR as floatCurrency , SEC_GROUP as securityGroup , SEC_TYPE_CD as securityType, DESC_INSTMT as securityName  ,"+
					" CASE WHEN [STATUS]='P' THEN 'Processed' WHEN [STATUS]='F' THEN 'Failed' WHEN [STATUS]='N' Then 'Pending' WHEN [STATUS]='D' then 'Deleted' WHEN [STATUS]='M' then 'Manual' END STATUS"+
					" FROM TPH_TRADE_IN  ";
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
		 if(iter.hasNext()){ SQL_FILTER_TRADES= SQL_FILTER_TRADES+" WHERE ";}
		//adding filters to query
		 while (iter.hasNext()) {
			Map.Entry filterEntry = (Map.Entry) iter.next();
			//System.out.println(filterEntry.getKey() + " : " + filterEntry.getValue().toString());
			if(filterEntry.getValue()!=null && filterEntry.getValue().toString().trim()!=""){
			String columnName=filterEntry.getKey().toString();
			Object filterValue=filterEntry.getValue();
			SQL_FILTER_TRADES= SQL_FILTER_TRADES+" "+columnName+"=? AND";
			 
			 //preparing argument list for SQL parameter binding
			 sqlargs[filterCount]=filterEntry.getValue();
			 filterCount++;
			}
		}
		//removing last end
		if(SQL_FILTER_TRADES.substring(SQL_FILTER_TRADES.length() - 3).compareToIgnoreCase("AND")==0){
			SQL_FILTER_TRADES = SQL_FILTER_TRADES.substring(0, SQL_FILTER_TRADES.length() - 3);
		}
		//System.out.println("executing below query \n " +SQL_FILTER_TRADES);
		
		return get(SQL_FILTER_TRADES, sqlargs, TradeOverviewBean.class);	
	}
	catch(Exception e){
		throw e;
		}
	}
}
