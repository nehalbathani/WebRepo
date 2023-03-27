package com.aflac.aims.tph.web.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aflac.aims.tph.web.dao.AdminDAO;
import com.aflac.aims.tph.web.model.AIMSEnvBean;
import com.aflac.aims.tph.web.model.FTPBean;
import com.aflac.aims.tph.web.model.FieldNameMappingBean;
import com.aflac.aims.tph.web.model.RoleBean;
import com.aflac.aims.tph.web.model.TPHCodeBean;
import com.aflac.aims.tph.web.model.UserBean;
import com.aflac.aims.tph.web.model.UserRoleBean;

@Service
public class AdminServiceImpl implements AdminService{
	
	@Autowired
	private AdminDAO adminDAO;
	
	@Override
	public List<FTPBean> getFTPData() throws Exception {
		return adminDAO.getFTPData();
	}

	@Override
	public List<FieldNameMappingBean> getFieldNameMappingData()
			throws Exception {
		return adminDAO.getFieldNameMappingData();
	}

	@Override
	public List<TPHCodeBean> getTPHCode() throws Exception {
		return adminDAO.getTPHCode();
	}

	@Override
	public int saveOrDeleteFieldMapping(String fieldName,
			String clientCode, String clientValue, String tphValue, String action) throws Exception {
		return adminDAO.saveOrDeleteFieldMapping(fieldName, clientCode, clientValue, tphValue, action);
	}

	@Override
	public List<AIMSEnvBean> getToggleValue() throws Exception {
		return adminDAO.getToggleValue();
	}

	@Override
	public int updateToggleValue(String paramSystCd, String paramVal)
			throws Exception {
		adminDAO.updateToggleValue(paramSystCd, paramVal);
		return 0;
	}

	/*@Override
	public List<UserRoleBean> getAdminUserRoles() throws Exception {
		return adminDAO.getAdminUserRoles();
	}*/
	
	@Override
	public List<RoleBean> getRoles() throws Exception {
		return adminDAO.getRoles();
	}

	@Override
	public List<UserBean> getAdminUsers() throws Exception {
		return adminDAO.getAdminUsers();
	}

	@Override
	public int updateUserRole(String userId, int roleId, String action) throws Exception {
		return adminDAO.updateUserRole(userId, roleId, action);
	}

	@Override
	public List<UserRoleBean> getUserRoles(String userCD) throws Exception {
		return adminDAO.getAdminUserRoles(userCD);
	}
}
