package com.aflac.aims.tph.web.service;

import java.util.Date;
import java.util.List;

import org.apache.commons.beanutils.PropertyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aflac.aims.tph.web.dao.DashboardDAO;

@Service("CommonServiceImpl")
public class CommonServiceImpl implements CommonService{

	@Autowired
	private DashboardDAO dashboardDAO;
	
	@Override
	public Date getCAMRAProcessDate()  throws Exception{
		return dashboardDAO.getCAMRAlastProcessDate();
	}

	@Override
	public List<?> applyStatusForDisplay(List<?> data)  throws Exception{
		// TODO Auto-generated method stub
	//	PropertyUtils.setProperty(data, "status", value);
		return null;
	}
	
	
}
