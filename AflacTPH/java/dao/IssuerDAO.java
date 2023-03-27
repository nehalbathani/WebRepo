package com.aflac.aims.tph.web.dao;

import java.util.List;

import com.aflac.aims.tph.web.model.IssuerBean;

public interface IssuerDAO {
	
	List<IssuerBean> getIssuerInDetails(String paramIssuerCD, String source) throws Exception;

	List<IssuerBean> getIssuerOutDetails(String paramIssuerCD, String paramDest) throws Exception;
	
}
