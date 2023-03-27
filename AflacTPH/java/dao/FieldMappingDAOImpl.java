package com.aflac.aims.tph.web.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.aflac.aims.tph.web.model.FieldMappingBean;
import com.aflac.aims.tph.web.model.TagMapBean;

@Repository
public class FieldMappingDAOImpl extends CommonDAO implements FieldMappingDAO{

	private static final String SQL_GET_FIELD_MAPPING = "SELECT [SOURCE]"+
									      " ,[SOURCE_TAG]"+
									      " ,[GROUP_NAME]"+
									      " ,[DEST]"+
									      " ,[DEST_TAG]"+
									      " ,[BUSINESS_NAME]"+
									      " ,[DATA_TYPE]"+
									  " FROM [dbo].[TPH_TAG_MAPPING] WHERE [SOURCE]=? AND DATA_TYPE=?";
	private static final String SQL_GET_DEST_TRADE = "SELECT [DEST_CD] FROM TPH_FUND_DEST "+
					"WHERE ACCT_CD IN (SELECT ACCT_CD FROM TPH_TRADE_IN WHERE TPH_TRD_SEQ_NO=? AND TOUCH_COUNT=?) "+
					"AND SOURCE IN (SELECT SOURCE FROM TPH_TRADE_IN WHERE TPH_TRD_SEQ_NO=? AND TOUCH_COUNT=?)";

	@Override
	public List<FieldMappingBean> getFieldMap(String source, String datatype)  throws Exception{
		// TODO Auto-generated method stub
		try{
			Object[] args=new Object[2];
			args[0]=source;
			args[1]=datatype;

		    return get(SQL_GET_FIELD_MAPPING,args,FieldMappingBean.class);
		}
		catch(Exception e){
			throw e;
		}
		
	}

	@Override
	public List<TagMapBean> getTagMap(String source, String mapDatatype)  throws Exception{
		// TODO Auto-generated method stub
		try{
			Object[] args=new Object[2];
			args[0]=source;
			args[1]=mapDatatype;

		    return get(SQL_GET_FIELD_MAPPING,args,TagMapBean.class);
		}
		catch(Exception e){
			throw e;
		}
	}

	@Override
	public List<String> getDestinationForTrade(int tradeRefNo, int touchCount) {
		// TODO Auto-generated method stub
		try{
			Object[] args=new Object[4];
			args[0]=tradeRefNo;
			args[1]=touchCount;
			args[2]=tradeRefNo;
			args[3]=touchCount;

		    return get(SQL_GET_DEST_TRADE,args,String.class);
		}
		catch(Exception e){
			throw e;
		}
	}

}
