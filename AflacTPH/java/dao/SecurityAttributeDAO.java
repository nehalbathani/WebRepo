package com.aflac.aims.tph.web.dao;

import java.util.List;

import com.aflac.aims.tph.web.model.SecurityAttributeBean;

public interface SecurityAttributeDAO {

	List<SecurityAttributeBean> getSecAttrsIn(int secID, String source) throws Exception;
	List<SecurityAttributeBean> getSecAttrsOut(int secID, String paramDest) throws Exception;

}
