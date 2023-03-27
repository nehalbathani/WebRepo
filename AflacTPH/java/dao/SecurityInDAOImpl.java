package com.aflac.aims.tph.web.dao;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.jdbc.support.rowset.ResultSetWrappingSqlRowSet;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import com.aflac.aims.tph.web.model.SecurityInBean;
import com.aflac.aims.tph.web.model.TagValueBean;

@Repository
public class SecurityInDAOImpl extends CommonDAO implements SecurityInDAO {
	
	protected static Logger logger = LoggerFactory.getLogger(SecurityInDAOImpl.class);

	private static final String SQL_IN_SECURITY_LIST = "SELECT [TPH_SEC_SEQ_NO], "+
		        "[SOURCE], "+
		       "[CUSIP], "+
		       "[ASSET_STATUS_TYPE], "+
		        "[ACCRUAL_DT], "+
		        "[AMT], "+
		       "[AMT_ISU], "+
		        "[ANNOUNCE_DT], "+
		        "[ASSET_BENCHMARK], "+
		        "[ASSET_STATUS], "+
		       "[CALC_TYPE], "+
		        "[CALL_MANDATORY], "+
		        "[CALL_TYPE], "+
		        "[CAPITAL_TYPE], "+
		        "[CD_INSTMT_TYPE], "+
		        "[CHANGE_DT], "+
		        "[COUNTRY], "+
		       "[COUPON_FIX], "+
		        "[COUP_FREQ], "+
		        "[CPN_TYPE], "+
		        "[CURRENCY], "+
		        "[CUSIP_TYPE], "+
		       "[CUSTODIAN_FEE], "+
		       "[DATE_CONV], "+
		        "[DESC_INSTMT], "+
		        "[DESC_INSTMT2], "+
		        "[EXCHANGE], "+
		        "[EXERCISE_TYPE], "+
		        "[EXP_DT], "+
		        "[EXPECTED_MATURITY], "+
		        "[FIRST_PAY_DT], "+
		        "[FIRST_SETTLE_DT], "+
		       "[FLAG_144A], "+
		        "[ISSUER_ID], "+
		        "[ISSUE_DT], "+
		       "[ISSUE_PRICE], "+
		       "[ISSUE_YIELD], "+
		        "[LAST_REGULAR_PMT], "+
		        "[MARKET], "+
		        "[MATURITY], "+
		        "[MODIFIED_BY], "+
		        "[SEC_TYPE], "+
		        "[SM_SEC_GROUP], "+
		        "[SM_SEC_TYPE], "+
		        "[TICKER], "+
		        "CASE WHEN [STATUS]='P' THEN 'Processed' WHEN [STATUS]='F' THEN 'Failed' WHEN [STATUS]='N' Then 'Pending' WHEN [STATUS]='D' then 'Deleted' WHEN [STATUS]='M' then 'Manual' END STATUS, "+
		        "[TIME_STAMP]"+
				" From [dbo].[TPH_SECURITY_IN]";
	@Override
	public List<SecurityInBean> getFilteredSecurityList(
			Map<String, Object> filterMap) throws Exception{
		// TODO Auto-generated method stub
		try{
		String SQL_FILTERED_IN_SECURITY=SQL_IN_SECURITY_LIST;
		int filterCount=0;
		Object[] sqlargs = new Object[filterMap.size()];
		
		Iterator iter = filterMap.entrySet().iterator();
		 if(iter.hasNext()){ SQL_FILTERED_IN_SECURITY= SQL_FILTERED_IN_SECURITY+" WHERE ";}
		//adding filters to query
		 while (iter.hasNext()) {
			Map.Entry filterEntry = (Map.Entry) iter.next();
			//System.out.println(filterEntry.getKey() + " : " + filterEntry.getValue().toString());
			if(filterEntry.getValue()!=null && filterEntry.getValue().toString().trim()!=""){
			String columnName=filterEntry.getKey().toString();
			Object filterValue=filterEntry.getValue();
			SQL_FILTERED_IN_SECURITY= SQL_FILTERED_IN_SECURITY+" "+columnName+"=? AND";
			 sqlargs[filterCount]=new Object();
			 //preparing argument list for SQL parameter binding
			 sqlargs[filterCount]=filterEntry.getValue();
			 //System.out.println("args["+filterCount+"]="+sqlargs[filterCount]);
			 filterCount++;
			}
		}
		//removing last end
		if(SQL_FILTERED_IN_SECURITY.substring(SQL_FILTERED_IN_SECURITY.length() - 3).compareToIgnoreCase("AND")==0){
			SQL_FILTERED_IN_SECURITY = SQL_FILTERED_IN_SECURITY.substring(0, SQL_FILTERED_IN_SECURITY.length() - 3);
		}
		//System.out.println("executing below query \n " +SQL_FILTERED_IN_SECURITY);
		return get(SQL_FILTERED_IN_SECURITY, sqlargs, SecurityInBean.class);	
		}
		catch(Exception e){
			throw e;
			//	return null;
		}
		
		
	}
	
	@Override
	public List<TagValueBean> getSecurityDetailsById(int SecId)  throws Exception{
		try{
		SqlRowSet results=	this.jdbcTemplate.queryForRowSet(SQL_IN_SECURITY_LIST+" WHERE TPH_SEC_SEQ_NO="+SecId);
		ResultSet rs=((ResultSetWrappingSqlRowSet) results).getResultSet();
		ResultSetMetaData rsmt=rs.getMetaData();
		List<TagValueBean> securityDetail=new ArrayList<TagValueBean>();
		
		if(rs.next()){
		for(int i=1;i<rsmt.getColumnCount();i++){
			TagValueBean tag=new TagValueBean();
			tag.setTagName(rsmt.getColumnName(i));
			tag.setTagValue(rs.getObject(i));
			securityDetail.add(tag);
		}
		}
		return securityDetail;
		}
		catch(Exception e){
			throw e;
		}
	}

	
	@Override
	public int cancelSecurity(int secRefNo) {
		// TODO Auto-generated method stub
		try{
			SimpleJdbcCall discardCall=new SimpleJdbcCall(this.jdbcTemplate);
			discardCall.setProcedureName("P_DeleteSecurity");
			discardCall.declareParameters(new SqlOutParameter("status", Types.INTEGER));
			Map<String, Object> out= discardCall.execute(new MapSqlParameterSource().
					                                            addValue("secID", secRefNo));
			return (Integer) out.get("status");
		}
		catch(Exception e){
				e.printStackTrace();
				return -1;
		}
	}
	
	@Override
	public int reprocessSecurity(int secRefNo) {
		// TODO Auto-generated method stub
		try{
			SimpleJdbcCall reprocessCall=new SimpleJdbcCall(this.jdbcTemplate);
			reprocessCall.setProcedureName("P_ReprocessSecurity");
			reprocessCall.declareParameters(new SqlOutParameter("status", Types.INTEGER));
			Map<String, Object> out = reprocessCall.execute(new MapSqlParameterSource().addValue("secRefNo", secRefNo));
			return (Integer) out.get("status");
		}
		catch(Exception e){
				logger.error("Exception:", e);
				return -1;
		}
	}

	@Override
	public SecurityInBean getSecurityInBeanById(int SecId) throws Exception{
		try{
			String SQL_IN_SECURITY_LIST_qry = SQL_IN_SECURITY_LIST + " where TPH_SEC_SEQ_NO=?";
			Object[] args=new Object[1];
			args[0]=SecId;
			
			return get(SQL_IN_SECURITY_LIST_qry, args, SecurityInBean.class).get(0);
		}
		catch(Exception e){
			throw e;
		}
	}
	
	
}
