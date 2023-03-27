package com.aflac.aims.tph.web.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.aflac.aims.tph.web.model.SecurityAttributeBean;

@Service
public interface SecurityAttributeService {

	List<SecurityAttributeBean> getSecAttributesIn(int secID, String source) throws Exception;
	List<SecurityAttributeBean> getSecAttributesOut(int secID, String paramDest)  throws Exception;

}
