package com.aflac.aims.tph.web.service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.aflac.aims.tph.web.model.SecurityOutBean;
import com.aflac.aims.tph.web.model.TagValueBean;


public interface SecurityOutService {

	List<SecurityOutBean> getFilteredSecurity(Map<String, Object> filterMap) throws Exception;

	List<SecurityOutBean> filterByDates(List<SecurityOutBean> securityList,
			Date fromDate) throws Exception;

	List<TagValueBean> getSecurityDetailsForSecId(int secId, String paramDest) throws Exception;

	//int resendSecurity(int secRefNo,String str_dest);
	HashMap<String, Integer> resendSecurity(int [] secRefNos, String dest);

	//int discardSecurity(int secRefNo,String str_dest);
	
	HashMap<String, Integer> discardSecurity(int [] secRefNos, String strDest);
	
	String getErrorMsg(int secRefNo,String dest);
}
