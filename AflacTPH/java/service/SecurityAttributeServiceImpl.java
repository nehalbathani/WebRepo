package com.aflac.aims.tph.web.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aflac.aims.tph.web.dao.SecurityAttributeDAO;
import com.aflac.aims.tph.web.model.SecurityAttributeBean;

@Service("SecurityAttributeServiceImpl")
public class SecurityAttributeServiceImpl implements SecurityAttributeService{

	@Autowired
	SecurityAttributeDAO secAttrDAO;
	@Override
	public List<SecurityAttributeBean> getSecAttributesIn(int secID,
			String source)  throws Exception{
		// TODO Auto-generated method stub
		try{
			return secAttrDAO.getSecAttrsIn(secID,source);
		}
		catch(Exception e){
			throw e;
		}
	}
	@Override
	public List<SecurityAttributeBean> getSecAttributesOut(int secID,
			String paramDest)  throws Exception{
		// TODO Auto-generated method stub
		try{
			return secAttrDAO.getSecAttrsOut(secID,paramDest);
		}
		catch(Exception e){
			throw e;
		}
	}

}
