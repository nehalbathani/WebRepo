package com.aflac.aims.tph.web.dao;

import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;


@Repository
public class CommonDAO {
    
    protected JdbcTemplate jdbcTemplate;
    protected SimpleJdbcInsert simpleJdbcInsert;
    protected SimpleJdbcCall simpleJdbcCall;
    protected NamedParameterJdbcTemplate npJdbcTemplate;
    
    

    @Autowired
    public void setDataSource(DataSource ds){
        this.jdbcTemplate = new JdbcTemplate(ds);
        this.simpleJdbcInsert = new SimpleJdbcInsert(ds);
        this.simpleJdbcCall = new SimpleJdbcCall(ds);
        this.npJdbcTemplate = new NamedParameterJdbcTemplate(ds);
    }
    
    @SuppressWarnings("unchecked")
    public  <T> List<T> get(String query, Class<T> cls){
        
    return this.jdbcTemplate.query(query, new BeanPropertyRowMapper(cls));
    
    }
    
    @SuppressWarnings("unchecked")
    public  <T> List<T> get(String query,  Object[] args, Class<T> cls){
        
    return this.jdbcTemplate.query(query,args, new BeanPropertyRowMapper(cls));
    
    }
   /* 
    public <T> List<T> get(String query,SqlParameterSource sps){
        GenericMapper<T> gm = new GenericMapper<T>();        
        gm.setBeanFactory(bf);
        gm.setDataEncrypted(isDataEncrypted);
        return npJdbcTemplate.query(query,sps,gm);
    }
    
    public <T> List<T> get(String query,BeanFactory<T> bf,Object[] args,boolean isDataEncrypted){
        GenericMapper<T> gm = new GenericMapper<T>();        
        gm.setBeanFactory(bf);
        gm.setDataEncrypted(isDataEncrypted);
        return jdbcTemplate.query(query,gm,args);
    }
    
    public <T> List<T> get(String query,Class<T> c,SqlParameterSource sps){             
        return npJdbcTemplate.queryForList(query, sps, c);
    }
    
    @SuppressWarnings("unchecked")
    public <T> List<T> get(String query,BeanFactory<T> bf,boolean isDataEncrypted,
            HashMap<String,QueryParameterBean> andMatchingQueryParamsMap, 
            HashMap<String,QueryParameterBean> orMatchingQueryParamsMap){
        GenericResultSetExtractor<T> grse = new GenericResultSetExtractor<T>();        
        grse.setBeanFactory(bf);
        grse.setDataEncrypted(isDataEncrypted);
        grse.setAndMatchingParams(andMatchingQueryParamsMap);
        grse.setOrMatchingParams(orMatchingQueryParamsMap);
        return (List<T>)jdbcTemplate.query(query,grse);
    }
    
    @SuppressWarnings("unchecked")
    public <T> List<T> get(String query,BeanFactory<T> bf,SqlParameterSource sps,
            boolean isDataEncrypted, HashMap<String,QueryParameterBean> andMatchingQueryParamsMap, 
            HashMap<String,QueryParameterBean> orMatchingQueryParamsMap){
        GenericResultSetExtractor<T> grse = new GenericResultSetExtractor<T>();        
        grse.setBeanFactory(bf);
        grse.setDataEncrypted(isDataEncrypted);
        grse.setAndMatchingParams(andMatchingQueryParamsMap);
        grse.setOrMatchingParams(orMatchingQueryParamsMap);
        return (List<T>)npJdbcTemplate.query(query,sps,grse);
    }
    
    @SuppressWarnings("unchecked")
    public <T> List<T> get(String query, BeanFactory<T> bf, Object[] args,
            boolean isDataEncrypted, HashMap<String,QueryParameterBean> andMatchingQueryParamsMap, 
            HashMap<String,QueryParameterBean> orMatchingQueryParamsMap){
        GenericResultSetExtractor<T> grse = new GenericResultSetExtractor<T>();        
        grse.setBeanFactory(bf);
        grse.setDataEncrypted(isDataEncrypted);
        grse.setAndMatchingParams(andMatchingQueryParamsMap);
        grse.setOrMatchingParams(orMatchingQueryParamsMap);
        return (List<T>)jdbcTemplate.query(query,grse,args);
    }
        
    public <T> T getObject(String query,BeanFactory<T> bf,SqlParameterSource sps,boolean isDataEncrypted){
        GenericMapper<T> gm = new GenericMapper<T>();        
        gm.setBeanFactory(bf);
        gm.setDataEncrypted(isDataEncrypted);
        try{
            return npJdbcTemplate.queryForObject(query,sps,gm);
        } catch(EmptyResultDataAccessException e){
            return null;
        }
    }
    
    public <T> T getObject(String query,BeanFactory<T> bf,Object[] args,boolean isDataEncrypted){
        GenericMapper<T> gm = new GenericMapper<T>();        
        gm.setBeanFactory(bf);
        gm.setDataEncrypted(isDataEncrypted);
        try{
            return jdbcTemplate.queryForObject(query,gm,args);
        } catch(EmptyResultDataAccessException e){
            return null;
        }
    }
    
    public <T> T getObject(String query,BeanFactory<T> bf,boolean isDataEncrypted){
        GenericMapper<T> gm = new GenericMapper<T>();        
        gm.setBeanFactory(bf);
        gm.setDataEncrypted(isDataEncrypted);
        try{
            return jdbcTemplate.queryForObject(query,gm);
        } catch(EmptyResultDataAccessException e){
            return null;
        }
    }
    
    public <T> T getObject(String query,Class<T> c,Object[] params){
        try{
            return (T)jdbcTemplate.queryForObject(query, c, params);
        } catch(EmptyResultDataAccessException e){
            return null;
        }
    }
    
    public <T> T getObject(String query,Class<T> c,SqlParameterSource sps){
        try{
            return npJdbcTemplate.queryForObject(query, sps, c);
        } catch(EmptyResultDataAccessException e){
            return null;
        }
    }
    
    public <T> T getObject(String query,Class<T> c){        
        try{
            return jdbcTemplate.queryForObject(query,c);
        } catch(EmptyResultDataAccessException e){
            return null;
        }
    }
    
    public int get(String query){
        return jdbcTemplate.queryForInt(query);
    }
    
    public int get(String query,SqlParameterSource sps){
        return npJdbcTemplate.queryForInt(query,sps);
    }
    
    public int update(String query){
        return jdbcTemplate.update(query);
    }
    
    public int update(String query,SqlParameterSource sps){
        return npJdbcTemplate.update(query,sps);
    }
    
    public int update(String query,Map<String,?> paramMap){
        return npJdbcTemplate.update(query,paramMap);
    }
    
    public int[] batchUpdate(NamedParameterJdbcTemplate npJdbcTemplate,String query,List<?> objList){
        SqlParameterSource[] batch = SqlParameterSourceUtils.createBatch(objList.toArray());
        return npJdbcTemplate.batchUpdate(query, batch);
    }
    
    public int insert(SimpleJdbcInsert sji,Map<String,Object> map){
        return sji.execute(map);
    }
    
    public int insert(SimpleJdbcInsert sji,SqlParameterSource sps){
        return sji.execute(sps);
    }
    
    public long insertAndReturnId(SimpleJdbcInsert sji,Map<String,Object> map){        
            return sji.executeAndReturnKey(map).longValue();        
    }
    
    public Map<String,Object> callProc(SimpleJdbcCall simpleJdbcCall,String procedureName,Map<String,Object> map){
        simpleJdbcCall = simpleJdbcCall.withProcedureName(procedureName);
        return simpleJdbcCall.execute(map);        
    }
    
    public Map<String,Object> callProc(SimpleJdbcCall simpleJdbcCall,String procedureName,SqlParameterSource inSps){
        simpleJdbcCall = simpleJdbcCall.withProcedureName(procedureName);        
        return simpleJdbcCall.execute(inSps);        
    }
    
    public <T> T callFunction(SimpleJdbcCall simpleJdbcCall,String functionName,Map<String,Object> map,Class<T> returnType){
        simpleJdbcCall = simpleJdbcCall.withFunctionName(functionName);
        return simpleJdbcCall.executeFunction(returnType, map);
    }
    
    public <T> T callFunction(SimpleJdbcCall simpleJdbcCall,String functionName,SqlParameterSource inSps,Class<T> returnType){
        simpleJdbcCall = simpleJdbcCall.withFunctionName(functionName);
        return simpleJdbcCall.executeFunction(returnType, inSps);
    }
    
    public Map<String,Object> callProcWithoutMetaData(SimpleJdbcCall simpleJdbcCall,String procedureName,SqlParameterSource inSps,String[] inParams,SqlParameter ... sp){
        simpleJdbcCall = simpleJdbcCall.withProcedureName(procedureName).withoutProcedureColumnMetaDataAccess().useInParameterNames(inParams).declareParameters(sp);
        return simpleJdbcCall.execute(inSps);        
    }
    
    @SuppressWarnings("unchecked")
    public <T> T callFunction(SimpleJdbcCall simpleJdbcCall,String functionName,Class<T> returnType,SqlParameterSource inSps){
        simpleJdbcCall = simpleJdbcCall.withFunctionName(functionName);
        return (T)simpleJdbcCall.execute(returnType,inSps);        
    }
    */

		
    
    
}