package com.aflac.aims.tph.web.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.aflac.aims.tph.web.model.FieldMappingBean;
import com.aflac.aims.tph.web.model.TagMapBean;

@Repository
public interface FieldMappingDAO {

	List<FieldMappingBean> getFieldMap(String source, String datatype) throws Exception;

	List<TagMapBean> getTagMap(String source, String mapDatatype) throws Exception;

	List<String> getDestinationForTrade(int tradeRefNo, int touchCount);

}
