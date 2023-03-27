package com.aflac.aims.tph.web.dao;

import java.sql.Types;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.aflac.aims.tph.web.exception.CustomGenericException;
import com.aflac.aims.tph.web.model.AIMSEnvBean;
import com.aflac.aims.tph.web.model.FTPBean;
import com.aflac.aims.tph.web.model.FieldNameMappingBean;
import com.aflac.aims.tph.web.model.RoleBean;
import com.aflac.aims.tph.web.model.TPHCodeBean;
import com.aflac.aims.tph.web.model.UserBean;
import com.aflac.aims.tph.web.model.UserRoleBean;
import com.aflac.aims.tph.web.utils.constants;

@Repository
public class AdminDAOImpl extends CommonDAO implements AdminDAO{
	
	protected static Logger logger = LoggerFactory.getLogger(AdminDAOImpl.class);
	
	private static final String GET_FTP_DATA_SQL = "select [STATUS_ID], [CLIENT_CODE], [FILE_CODE], [FILE_NAME], [TASK], [STATUS], [TIME_STAMP]"
			+ " from [dbo].[AIMS_FILE_STATUS]";

	private static final String GET_AIMS_ENVIRONT_SQL = "select [PARAM_SYST_CD],[PARAM_NAME],[PARAM_VAL],[TIME_STAMP] from [dbo].[AIMS_ENVIRONMENT] where [PARAM_NAME] = 'TOGGLE' and PARAM_SYST_CD = 'CAMRAJP' Or PARAM_SYST_CD = 'CAMRAUS' Or PARAM_SYST_CD =   'IVT'";
	
	private static final String GET_FIELD_MAPPING_DATA_SQL = "select [FIELD_NAME] ,[DATA_TYPE],[CLIENT_CD_VALUE],[TPH_CD_VALUE],[DIRECTION],[LAST_UPD_USER],[TIME_STAMP],[CUST_CD]"
			+ " from [dbo].[TPH_CODE_TRANSLATION]";
	
	private static final String GET_TPH_CODE_DATA_SQL = "SELECT distinct FIELD_NAME FROM TPH_CODE";
	
	/*private static final String GET_USER_ROLES_SQL = "select [ROLE_ID], [ROLENAME] "
			+ " from [dbo].[TPH_USER_ROLES]";*/
	
	private static final String GET_USER_ROLES_FOR_AUSER_SQL = "select [ROLE_ID], [USER_CD] "
			+ " from [dbo].[TPH_USER_ROLE]";
	
	private static final String GET_USERS_SQL = "select [USER_CD],[USER_NAME],[EMAIL_ADDR],[USER_PASSWORD],[PASSWORD_EXPIRE_DT],[SCRAMBLE_IND],[LOCK_IND],[LAST_LOGON_TIME],[LOGIN_FAIL_COUNT],[LOGIN_FAIL_TIME],[ACTIVE_FLAG] "
				+ " from [dbo].[TPH_USER]";
	
	private static final String GET_ROLES_SQL = "select [ROLE_ID], [ROLE_NAME], [LAST_UPD_USER], [LAST_UPD_DATE] "
			+ " from [dbo].[TPH_ROLE]";
	
	@Override
	public List<FTPBean> getFTPData() throws Exception {		
		return get(GET_FTP_DATA_SQL, FTPBean.class);
	}

	@Override
	public List<FieldNameMappingBean> getFieldNameMappingData() throws Exception {
		return get(GET_FIELD_MAPPING_DATA_SQL, FieldNameMappingBean.class);
	}

	@Override
	public List<TPHCodeBean> getTPHCode() throws Exception {
		return get(GET_TPH_CODE_DATA_SQL, TPHCodeBean.class);
	}

	@Override
	public int saveOrDeleteFieldMapping(String fieldName,
			String clientCode, String clientValue, String tphValue, String action)
			throws Exception {
		SimpleJdbcCall saveDataCall=new SimpleJdbcCall(this.jdbcTemplate);
		saveDataCall.setProcedureName("P_AdminCodeXlation");
		
		System.out.println(fieldName + " " +clientCode + " " +clientValue + " " +tphValue + " " +action);
		
		MapSqlParameterSource inputSource = new MapSqlParameterSource();
		inputSource.addValue("FIELD_NAME", fieldName);
		inputSource.addValue("CUST_CD", clientCode);
		inputSource.addValue("CLIENT_CD_VALUE", clientValue);
		inputSource.addValue("TPH_CD_VALUE", tphValue);
		inputSource.addValue("ACTION", action);
		
		saveDataCall.declareParameters(new SqlOutParameter("RETVAL", Types.INTEGER));
		saveDataCall.declareParameters(new SqlOutParameter("RETMSG", Types.VARCHAR));
		
		Map<String, Object> out= saveDataCall.execute(inputSource);
		return (Integer) out.get("RETVAL");
	}

	@Override
	public List<AIMSEnvBean> getToggleValue() throws Exception {
		return get(GET_AIMS_ENVIRONT_SQL, AIMSEnvBean.class);
	}

	@Override
	public int updateToggleValue(String paramSystCd, String paramVal) throws Exception {
		SimpleJdbcCall saveDataCall=new SimpleJdbcCall(this.jdbcTemplate);
		saveDataCall.setProcedureName("P_UpdateEnvVariable");
		
		System.out.println(paramSystCd + " " +paramVal);
		
		MapSqlParameterSource inputSource = new MapSqlParameterSource();
		inputSource.addValue("paramVal", paramVal);
		inputSource.addValue("paramSystCd", paramSystCd);
		inputSource.addValue("paramName", "TOGGLE");

		saveDataCall.execute(inputSource);
		
		return 0;
	}

	/*@Override
	public List<UserRoleBean> getAdminUserRoles() throws Exception {
		return get(GET_USER_ROLES_SQL, UserRoleBean.class);
	}*/
	
	@Override
	public List<RoleBean> getRoles() throws Exception {
		return get(GET_ROLES_SQL, RoleBean.class);
	}

	@Override
	public List<UserBean> getAdminUsers() throws Exception {
		return get(GET_USERS_SQL, UserBean.class);
	}

	@Override
	@Transactional(rollbackForClassName="UserRoleBean")
	public int updateUserRole(String userId, int roleId, String action) throws Exception {
		SimpleJdbcCall saveDataCall=new SimpleJdbcCall(this.jdbcTemplate);
		saveDataCall.setProcedureName("P_AdminUserRole");//P_AdminUpdateUserRole
		
		//System.out.println(userId + " " +roleId);
		
		MapSqlParameterSource inputSource = new MapSqlParameterSource();
		inputSource.addValue("USER_ID", userId);
		inputSource.addValue("ROLE_ID", roleId);
		inputSource.addValue("ACTION", action);
		
		saveDataCall.declareParameters(new SqlOutParameter("RETVAL", Types.INTEGER));
		saveDataCall.declareParameters(new SqlOutParameter("RETMSG", Types.VARCHAR));
		
		Map<String, Object> out= saveDataCall.execute(inputSource);
		return (Integer) out.get("RETVAL");
	}

	@Override
	public List<UserRoleBean> getAdminUserRoles(String userCD) throws Exception {
		return get(GET_USER_ROLES_FOR_AUSER_SQL + " where USER_CD = '" + userCD + "'", UserRoleBean.class);
	}
}
