package com.aflac.aims.tph.web.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.aflac.aims.tph.web.model.FieldMappingBean;
import com.aflac.aims.tph.web.model.FieldMappingValueBean;

@Service
public interface MappingService {

	List<FieldMappingBean> getFieldMap(String source, String datatype) throws Exception;

	List<FieldMappingValueBean> getFieldMappingValueForTrade(int tradeRefNo,int touch_count) throws Exception;

	List<FieldMappingValueBean> getFieldMappingValueForSecurity(int secRefNo) throws Exception;

	List<String> getDestinationsForTrade(int tradeRefNo, int touchCount) throws Exception;

	List<String> getDestinationsForSecurity(int secRefNo) throws Exception;

	

}
