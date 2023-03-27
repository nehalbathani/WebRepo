package com.aflac.aims.tph.web.service;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;

@Service
public interface CommonService {

	Date getCAMRAProcessDate() throws Exception;

	List<?> applyStatusForDisplay(List<?> data) throws Exception;
}
