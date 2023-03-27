package com.aflac.aims.tph.web.dao;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.jdbc.support.rowset.ResultSetWrappingSqlRowSet;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import com.aflac.aims.tph.web.model.SecurityOutBean;
import com.aflac.aims.tph.web.model.TagValueBean;

@Repository
public class SecurityOutDAOImpl extends CommonDAO implements SecurityOutDAO {

	private static final String SQL_OUT_SECURITY_LIST = "SELECT  [SOURCE]"+
      ",[SOURCE_OUT] "+
      ",[SEC_ID] "+
      ",[CUSIP] "+
      ",[SEC_GROUP] "+
      ",[SEC_TYPE_CD] "+
      ",[ISSUER_CD] "+
      ",[SYMB_POOL] "+
      ",[DESCRIPTION1] "+
      ",[DESCRIPTION2] "+
      ",[CPN_RATE] "+
      ",[MATURITY_DATE] "+
      ",[ISSUE_DATE] "+
      ",[PMT_SCHD] "+
      ",[ACCRUAL] "+
      ",[TAX_STATUS] "+
      ",[PRN_CURR] "+
      ",[INC_CURR] "+
      ",[PRICE_SRC] "+
      ",[EXCHANGE] "+
      ",[COUNTRY] "+
      ",CASE WHEN [TPH_STATUS]='COMPLETE' THEN 'Processed' WHEN [TPH_STATUS]='FAIL' THEN 'Failed' WHEN [TPH_STATUS]='READY' Then 'Pending' WHEN [TPH_STATUS]='MANUAL' then 'Manual' END TPH_STATUS "+
      ",[LAST_UPD_DATE] "+
  " FROM [dbo].[TPH_SECURITY_OUT]";

	@Override
	public List<SecurityOutBean> getFilteredSecurityList(
			Map<String, Object> filterMap) throws Exception{
		// TODO Auto-generated method stub
		try{
			String SQL_FILTERED_OUT_SECURITY=SQL_OUT_SECURITY_LIST;
			int filterCount=0;
			Object[] sqlargs = new Object[filterMap.size()];
			
			Iterator iter = filterMap.entrySet().iterator();
			 if(iter.hasNext()){ SQL_FILTERED_OUT_SECURITY= SQL_FILTERED_OUT_SECURITY+" WHERE ";}
			//adding filters to query
			 while (iter.hasNext()) {
				Map.Entry filterEntry = (Map.Entry) iter.next();
				//System.out.println(filterEntry.getKey() + " : " + filterEntry.getValue().toString());
				if(filterEntry.getValue()!=null && filterEntry.getValue().toString().trim()!=""){
				String columnName=filterEntry.getKey().toString();
				Object filterValue=filterEntry.getValue();
				SQL_FILTERED_OUT_SECURITY= SQL_FILTERED_OUT_SECURITY+" "+columnName+"=? AND";
				 sqlargs[filterCount]=new Object();
				 //preparing argument list for SQL parameter binding
				 sqlargs[filterCount]=filterEntry.getValue();
				 //System.out.println("args["+filterCount+"]="+sqlargs[filterCount]);
				 filterCount++;
				}
			}
			//removing last end
			if(SQL_FILTERED_OUT_SECURITY.substring(SQL_FILTERED_OUT_SECURITY.length() - 3).compareToIgnoreCase("AND")==0){
				SQL_FILTERED_OUT_SECURITY = SQL_FILTERED_OUT_SECURITY.substring(0, SQL_FILTERED_OUT_SECURITY.length() - 3);
			}
			//System.out.println("executing below query \n " +SQL_FILTERED_OUT_SECURITY);
			
			return get(SQL_FILTERED_OUT_SECURITY, sqlargs, SecurityOutBean.class);	
			}
			catch(Exception e){
				throw e;
			}
			
	}

	@Override
	public List<TagValueBean> getSecurityDetailsById(int secId,String dest) throws Exception{
		// TODO Auto-generated method stub
		
		try{
			SqlRowSet results=	this.jdbcTemplate.queryForRowSet(SQL_OUT_SECURITY_LIST+" WHERE SEC_ID="+secId+" AND SOURCE_OUT='"+dest.trim()+"'");
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
	public int resendSecurity(int secRefNo,String dest) {
		// TODO Auto-generated method stub
		try{
			SimpleJdbcCall resendCall=new SimpleJdbcCall(this.jdbcTemplate);
			resendCall.setProcedureName("P_ResendSecurity");
			resendCall.declareParameters(new SqlOutParameter("status", Types.INTEGER));
			Map<String, Object> out= resendCall.execute(new MapSqlParameterSource().addValue("secID", secRefNo).addValue("dest", dest));
			return (Integer) out.get("status");
		}
		catch(Exception e){
				return -1;
		}
	}

	@Override
	public int discardSecurity(int secRefNo,String dest) {
		// TODO Auto-generated method stub
		try{
			SimpleJdbcCall discardCall=new SimpleJdbcCall(this.jdbcTemplate);
			discardCall.setProcedureName("P_DiscardSecurity");
			discardCall.declareParameters(new SqlOutParameter("status", Types.INTEGER));
			Map<String, Object> out= discardCall.execute(new MapSqlParameterSource().addValue("secID", secRefNo).addValue("dest", dest));
			return (Integer) out.get("status");
		}
		catch(Exception e){
				return -1;
		}
	}

	@Override
	public List<SecurityOutBean> getOutSecuritiesByID(int secRefNo) throws Exception{
		try{
			Object[] args=new Object[1];
			args[0]=secRefNo;

			return get(SQL_OUT_SECURITY_LIST+" WHERE SEC_ID=?",args,SecurityOutBean.class );
		}
		catch(Exception e){
			throw e;
		}
	}

	private String SQL_GET_ERROR_MESSAGE="  SELECT t1.ERR_MSG "+
			" FROM [TPH_DEST_SEC_ERR] t1"+
			" LEFT OUTER JOIN [TPH_DEST_SEC_ERR] t2"+
			" ON t1.TPH_SEC_ID = t2.TPH_SEC_ID AND t1.DEST = t2.DEST AND ((t1.TIME_STAMP < t2.TIME_STAMP)) "+
			" WHERE t2.TPH_SEC_ID IS NULL AND t1.TPH_SEC_ID=?  AND t1.DEST=?";

@Override
public String getErrorMsg(int secRefNo,  String dest){
	// TODO Auto-generated method stub
	try{
		Object[] args=new Object[2];
		args[0]=secRefNo;
		args[1]=dest;
		
		return this.jdbcTemplate.queryForObject(SQL_GET_ERROR_MESSAGE, args, String.class);
	}
	catch(Exception e){
		return "Error not found in database!";
	}
}

	
}
