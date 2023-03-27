package com.aflac.aims.tph.web.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aflac.aims.tph.web.dao.SecurityOutDAO;
import com.aflac.aims.tph.web.model.SecurityOutBean;
import com.aflac.aims.tph.web.model.TagValueBean;
import com.aflac.aims.tph.web.utils.constants;

@Service
public class SecurityOutServiceImpl implements SecurityOutService {

	@Autowired
	private SecurityOutDAO securityOutDAO;
	
	@Override
	public List<SecurityOutBean> getFilteredSecurity (
			Map<String, Object> filterMap) throws Exception{
		// TODO Auto-generated method stub
		return securityOutDAO.getFilteredSecurityList(filterMap);
	}

	
	@Override
	public List<SecurityOutBean> filterByDates(
			List<SecurityOutBean> securityList, Date fromDate) throws Exception{
		// TODO Auto-generated method stub
		List<SecurityOutBean> filteredSecurity=new ArrayList<SecurityOutBean>();
		try{
			for(SecurityOutBean sec:securityList){
				if(!fromDate.after(sec.getLAST_UPD_DATE())){
					filteredSecurity.add(sec);
				}
			}
			return filteredSecurity;
		}
		catch(Exception e){
			throw e;
		}
		
	}


	@Override
	public List<TagValueBean> getSecurityDetailsForSecId(int secId, String dest) throws Exception{
		// TODO Auto-generated method stub
		return securityOutDAO.getSecurityDetailsById(secId,dest);
	}


	@Override
	public HashMap<String, Integer> resendSecurity(int [] secRefNos, String dest) {
		// TODO Auto-generated method stub
		HashMap<String, Integer> resultMap = new HashMap<String, Integer>(10);
		try {
			int ind = 0;
			for (int id:secRefNos) {
				int status = securityOutDAO.resendSecurity(id, dest);
				String val = String.valueOf(id) + ":" + String.valueOf(ind);
				resultMap.put(val, new Integer(status));
				ind++;
			}
		}
		catch(Exception e){
			//TODO Handle Exceptional Handling
			throw e;
		}
		return resultMap;
	}
	
	@Override
	public HashMap<String, Integer> discardSecurity(int [] secRefNos, String dest) {
		// TODO Auto-generated method stub
		HashMap<String, Integer> resultMap = new HashMap<String, Integer>(10);
		try {
			int ind = 0;
			for (int id:secRefNos) {
				int status = securityOutDAO.discardSecurity(id, dest);
				String val = String.valueOf(id) + ":" + String.valueOf(ind);
				resultMap.put(val, new Integer(status));
				ind++;
			}
		}
		catch(Exception e){
			//TODO Handle Exceptional Handling
			throw e;
		}
		return resultMap;
	}
	
	@Override
	public String getErrorMsg(int secRefNo, String dest) {
		// TODO Auto-generated method stub
		try{
			return securityOutDAO.getErrorMsg(secRefNo,dest);
		}
		catch(Exception e){
			return "Service failed to return error message!";
		}
	}	
}