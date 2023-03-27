package com.aflac.aims.tph.web.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.aflac.aims.tph.web.model.IssuerBean;

@Repository
public class IssuerDAOImpl extends CommonDAO implements IssuerDAO{

	private static final String SQL_GET_ISSUER_IN_DETAIL="SELECT distinct i.TPH_ISSUER_SEQ_NO,i.ISSUER_ID as ISSUER_CD,NAME as DESCRIPTION,COUNTRY,STATE, MOODY_RATE, S_P_RATE from TPH_ISSUER_IN i join "
			+"(SELECT r1.ISSUER_ID,r1.SOURCE_FILE_ID,r1.SOURCE, r1.rating_value as MOODY_RATE, r2.rating_value as S_P_RATE FROM [dbo].TPH_ISSUER_RATING_IN r1 join [dbo].TPH_ISSUER_RATING_IN r2 on r1.ISSUER_ID=r2.ISSUER_ID where r1.SOURCE_FILE_ID=r2.SOURCE_FILE_ID and r1.SOURCE=r2.SOURCE and r1.AGENCY=1 and r2.AGENCY=2 and r1.ISSUER_ID=?) r "
			+"on i.ISSUER_ID = r.ISSUER_ID and i.SOURCE = r.SOURCE AND i.SOURCE_FILE_ID=r.SOURCE_FILE_ID "
			+"and i.SOURCE=? ";
		
	private static final String SQL_GET_ISSUER_OUT_DETAIL = "SELECT TOP 1 TPH_ISSUER_SEQ_NO, ISSUER_CD, DESCRIPTION, COUNTRY, STATE, MOODY_RATE, S_P_RATE, INT_RATE FROM TPH_ISSUER_OUT WHERE ISSUER_CD=? AND SOURCE_OUT=? order by TPH_ISSUER_SEQ_NO Desc";

	@Override
	public List<IssuerBean> getIssuerInDetails(String paramIssuerCD,String source) throws Exception{
		// TODO Auto-generated method stub
		try{
			Object[] args=new Object[2];
			args[0]=paramIssuerCD;
			args[1]=source;
			return get(SQL_GET_ISSUER_IN_DETAIL,args,IssuerBean.class);
		}
		catch(Exception e){
			throw e;
		}
	}
	
	@Override
	public List<IssuerBean> getIssuerOutDetails(String paramIssuerCD,String paramDest) throws Exception{
		// TODO Auto-generated method stub
		try{
			Object[] args=new Object[2];
			args[0]=paramIssuerCD;
			args[1]=paramDest;
			return get(SQL_GET_ISSUER_OUT_DETAIL,args,IssuerBean.class);
		}
		catch(Exception e){
			throw e;
		}
	}
	
	

}
