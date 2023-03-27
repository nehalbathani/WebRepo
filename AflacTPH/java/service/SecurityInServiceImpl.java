package com.aflac.aims.tph.web.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aflac.aims.tph.web.dao.SecurityInDAO;
import com.aflac.aims.tph.web.model.SecurityInBean;
import com.aflac.aims.tph.web.model.TagValueBean;

@Service
public class SecurityInServiceImpl implements SecurityInService{

	@Autowired
	private SecurityInDAO securityInDAO;
	
	@Override
	public List<SecurityInBean> getFilteredSecurity(
			Map<String, Object> filterMap) throws Exception{
		// TODO Auto-generated method stub
		return securityInDAO.getFilteredSecurityList(filterMap);
	}

	@Override
	public List<SecurityInBean> filterByDates(
			List<SecurityInBean> securityList, Date fromDate) throws Exception{
		// TODO Auto-generated method stub
		List<SecurityInBean> filteredSecurity=new ArrayList<SecurityInBean>();
			for(SecurityInBean sec:securityList){
				if(!fromDate.after(sec.getTIME_STAMP())){
					filteredSecurity.add(sec);
				}
			}
			return filteredSecurity;
		
	}
	
	@Override
	public List<TagValueBean> getSecurityDetailsForSecId(int secID) throws Exception{
		return securityInDAO.getSecurityDetailsById(secID);
	}
		
	@Override
	public HashMap<String, Integer> reprocessSecurity(int [] secRefNos) {
		// TODO Auto-generated method stub
		HashMap<String, Integer> resultMap = new HashMap<String, Integer>(10);
		try {
			int ind = 0;
			for (int id:secRefNos) {
				int status = securityInDAO.reprocessSecurity(id);
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
	public HashMap<String, Integer> cancelSecurity(int [] secRefNos) {
		// TODO Auto-generated method stub
		HashMap<String, Integer> resultMap = new HashMap<String, Integer>(10);
		try {
			int ind = 0;
			for (int id:secRefNos) {
				int status = securityInDAO.cancelSecurity(id);
				String val = String.valueOf(id) + ":" + String.valueOf(ind);
				resultMap.put(val, new Integer(status));
				ind++;
			}
		}
		catch(Exception e){
			throw e;
		}
		return resultMap;
	}
}