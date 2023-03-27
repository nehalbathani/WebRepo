package com.aflac.aims.tph.web.service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.aflac.aims.tph.web.model.SecurityInBean;
import com.aflac.aims.tph.web.model.TagValueBean;

@Service
public interface SecurityInService {

	List<SecurityInBean> getFilteredSecurity(Map<String, Object> filterMap) throws Exception;

	List<SecurityInBean> filterByDates(List<SecurityInBean> securityList,
			Date fromDate) throws Exception;

	List<TagValueBean> getSecurityDetailsForSecId(int secID)throws Exception ;
	
	//int reprocessSecurity(int secRefNo);
	
	HashMap<String, Integer> reprocessSecurity(int [] secRefNo);

	//int cancelSecurity(int secRefNo);
	
	HashMap<String, Integer> cancelSecurity(int [] secRefNo);
}
