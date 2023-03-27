package com.aflac.aims.tph.web.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.aflac.aims.tph.web.model.IssuerBean;

@Service
public interface IssuerService {
	
	List<IssuerBean> getIssuerInDetail(String paramIssuerCD, String source) throws Exception;

	List<IssuerBean> getIssuerOutDetail(String paramIssuerCD, String paramDest) throws Exception;

	



}
