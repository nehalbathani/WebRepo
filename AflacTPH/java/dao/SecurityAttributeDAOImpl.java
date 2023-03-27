package com.aflac.aims.tph.web.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.aflac.aims.tph.web.model.SecurityAttributeBean;

@Repository
public class SecurityAttributeDAOImpl extends CommonDAO implements SecurityAttributeDAO{

	private static final String SQL_GET_SECURITY_ATTRIBUTES_IN ="(select 'SINK' as ATTRIB_TYPE,SINK_DATE as DATE1, SINK_PRICE as PRICE, SINK_AMOUNT as AMOUNT from TPH_SECURITY_SINK_IN where TPH_SEC_SEQ_NO=? and SOURCE=?) "+
			"UNION	(select CASE WHEN PUTCALL_TYPE='C' THEN 'CALL' WHEN PUTCALL_TYPE='P' THEN 'PUT' ELSE PUTCALL_TYPE END as ATTRIB_TYPE, PUTCALL_DATE as DATE1,PUTCALL_PRICE as PRICE, null as AMOUNT from TPH_SECURITY_PUTCALL_IN where TPH_SEC_SEQ_NO=? and SOURCE=?)";

	private static final String SQL_GET_SECURITY_ATTRIBUTES_OUT = "SELECT ATTRIB_TYPE, DATE1, PRICE, AMOUNT FROM TPH_SEC_ATTRIB_OUT WHERE SEC_ID=? AND SOURCE_OUT=?";

	@Override
	public List<SecurityAttributeBean> getSecAttrsIn(int secID, String source)  throws Exception{
		// TODO Auto-generated method stub
		try{
			Object[] args=new Object[4];
			args[0]=secID;
			args[1]=source;
			args[2]=secID;
			args[3]=source;
			return get(SQL_GET_SECURITY_ATTRIBUTES_IN,args,SecurityAttributeBean.class);
		}
		catch(Exception e){
			throw e;
		}
	}
	
	@Override
	public List<SecurityAttributeBean> getSecAttrsOut(int secID, String paramDest)  throws Exception{
		// TODO Auto-generated method stub
		try{
			Object[] args=new Object[2];
			args[0]=secID;
			args[1]=paramDest;
			return get(SQL_GET_SECURITY_ATTRIBUTES_OUT,args,SecurityAttributeBean.class);
		}
		catch(Exception e){
			throw e;
		}
	}

}
