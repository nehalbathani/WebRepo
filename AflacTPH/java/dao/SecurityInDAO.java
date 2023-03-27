package com.aflac.aims.tph.web.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.aflac.aims.tph.web.model.SecurityInBean;
import com.aflac.aims.tph.web.model.TagValueBean;

@Repository
public interface SecurityInDAO {

	List<SecurityInBean> getFilteredSecurityList(Map<String, Object> filterMap) throws Exception;

	List<TagValueBean> getSecurityDetailsById(int SecId) throws Exception;

	SecurityInBean getSecurityInBeanById(int SecId) throws Exception;

	int cancelSecurity(int secRefNo);

	int reprocessSecurity(int secRefNo);

}
