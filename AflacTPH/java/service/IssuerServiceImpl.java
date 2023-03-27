package com.aflac.aims.tph.web.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aflac.aims.tph.web.dao.IssuerDAO;
import com.aflac.aims.tph.web.model.IssuerBean;

@Service("IssuerServiceImpl")
public class IssuerServiceImpl implements IssuerService{

	@Autowired
	IssuerDAO issuerDAO;
	
	@Override
	public List<IssuerBean> getIssuerInDetail(String paramIssuerCD, String source) throws Exception{
		// TODO Auto-generated method stub
		try{
			return issuerDAO.getIssuerInDetails(paramIssuerCD,source);
		}
		catch(Exception e){
			throw e;
		}
	}
	
	@Override
	public List<IssuerBean> getIssuerOutDetail(String paramIssuerCD, String paramDest) throws Exception{
		// TODO Auto-generated method stub
		try{
			return issuerDAO.getIssuerOutDetails(paramIssuerCD,paramDest);
		}
		catch(Exception e){
			throw e;
		}
	}

	
}
