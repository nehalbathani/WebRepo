package com.aflac.aims.tph.web.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.aflac.aims.tph.web.model.SecurityOutBean;
import com.aflac.aims.tph.web.model.TagValueBean;

public interface SecurityOutDAO {

	List<SecurityOutBean> getFilteredSecurityList(Map<String, Object> filterMap)throws Exception ;

	List<TagValueBean> getSecurityDetailsById(int secId, String dest) throws Exception;

	int resendSecurity(int secRefNo,String dest);

	int discardSecurity(int secRefNo,String dest);

	List<SecurityOutBean> getOutSecuritiesByID(int secRefNo) throws Exception;
	public String getErrorMsg(int secRefNo,  String dest);

}
