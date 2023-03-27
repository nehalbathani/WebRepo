package com.aflac.aims.tph.web.dao;

import java.util.List;

import com.aflac.aims.tph.web.model.AIMSEnvBean;
import com.aflac.aims.tph.web.model.FTPBean;
import com.aflac.aims.tph.web.model.FieldNameMappingBean;
import com.aflac.aims.tph.web.model.RoleBean;
import com.aflac.aims.tph.web.model.TPHCodeBean;
import com.aflac.aims.tph.web.model.UserBean;
import com.aflac.aims.tph.web.model.UserRoleBean;

/**
 * Admin related database handling
 */
public interface AdminDAO {
	public List<FTPBean> getFTPData() throws Exception;
	public List<AIMSEnvBean> getToggleValue() throws Exception;
	public int updateToggleValue(String paramSystCd, String paramVal) throws Exception;
	public List<FieldNameMappingBean> getFieldNameMappingData() throws Exception;
	public List<TPHCodeBean> getTPHCode() throws Exception;
	public int saveOrDeleteFieldMapping(String fieldName,
			String clientCode, String clientValue, String tphValue, String action) throws Exception;
	
	//public List<UserRoleBean> getAdminUserRoles() throws Exception ;
	public List<RoleBean> getRoles() throws Exception;
	public List<UserBean> getAdminUsers() throws Exception;
	public int updateUserRole(String userId, int roleId, String action) throws Exception;
	public List<UserRoleBean> getAdminUserRoles(String userCD) throws Exception;
}
