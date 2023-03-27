package com.aflac.aims.tph.web.service;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aflac.aims.tph.web.dao.FieldMappingDAO;
import com.aflac.aims.tph.web.dao.SecurityInDAO;
import com.aflac.aims.tph.web.dao.SecurityOutDAO;
import com.aflac.aims.tph.web.dao.TradeInDAO;
import com.aflac.aims.tph.web.dao.TradeOUTDAO;
import com.aflac.aims.tph.web.model.FieldMappingBean;
import com.aflac.aims.tph.web.model.FieldMappingValueBean;
import com.aflac.aims.tph.web.model.FieldTagBean;
import com.aflac.aims.tph.web.model.SecurityInBean;
import com.aflac.aims.tph.web.model.SecurityOutBean;
import com.aflac.aims.tph.web.model.TagMapBean;
import com.aflac.aims.tph.web.model.TradeINBean;
import com.aflac.aims.tph.web.model.TradeOUTBean;
import com.aflac.aims.tph.web.utils.constants;

@Service
public class MappingServiceImpl implements MappingService{

	@Autowired
	private FieldMappingDAO fieldMappingDAO;
	
	@Autowired
	private TradeInDAO tradeInDAO;
	
	@Autowired
	private TradeOUTDAO tradeOutDAO;
	
	@Autowired
	private SecurityInDAO secInDAO;
	
	@Override
	public List<FieldMappingBean> getFieldMap(String source, String datatype) throws Exception{
		// TODO Auto-generated method stub
		return fieldMappingDAO.getFieldMap(source,datatype);
	}

	@Override
	public List<FieldMappingValueBean> getFieldMappingValueForTrade(int tradeRefNo, int touch_count) throws Exception{
		// TODO Auto-generated method stub
		try{
			TradeINBean tradeInBean=tradeInDAO.getInTradeByID(tradeRefNo,touch_count);
			List<TradeOUTBean> tradeOutBeans=tradeOutDAO.getOutTradesByID(tradeRefNo, tradeInBean.getTRAN_TYPE(), touch_count);
			List<TagMapBean> tagMapping=fieldMappingDAO.getTagMap(tradeInBean.getSOURCE(),constants.FIELDMAP_DATATYPE_TRADE);
			List<FieldMappingValueBean> mappedValues= getFieldMapping(tagMapping);
			mappedValues=getTagValuesForTrade(tradeInBean, tradeOutBeans, mappedValues);
			return mappedValues;
		}
		catch(Exception e){
			throw e;
		}
	}


	private List<FieldMappingValueBean> getTagValuesForTrade(TradeINBean tradeInBean,
			List<TradeOUTBean> tradeOutBeans,
			List<FieldMappingValueBean> mappedValues) throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		// TODO Auto-generated method stub
		Class<?> noparams[] = {};
		Object[] noparm={};
		ListIterator<FieldMappingValueBean> itrValueMap=mappedValues.listIterator();
		while(itrValueMap.hasNext()){
			FieldMappingValueBean valueBean=(FieldMappingValueBean)itrValueMap.next();
			//System.out.println("=================Trade valueBean.getSourceTag() ==== " +valueBean.getSourceTag());
			Method method = TradeINBean.class.getDeclaredMethod("get"+valueBean.getSourceTag(), noparams);
			valueBean.setSourceTagValue(method.invoke(tradeInBean, noparm));
			
			HashMap<String,FieldTagBean> destMap=valueBean.getDestinationBean();
			ListIterator<TradeOUTBean> itrTradeOut=tradeOutBeans.listIterator();
			while(itrTradeOut.hasNext()){
				TradeOUTBean tradeOut=itrTradeOut.next();
				//System.out.println("================= " + tradeOut.getSOURCE_OUT());
				if(destMap.get(tradeOut.getSOURCE_OUT())!=null){
					FieldTagBean tagBean=destMap.get(tradeOut.getSOURCE_OUT());
					//System.out.println("=================Trade tagBean.getDestTag() ==== " +tagBean.getDestTag());
					Method methodOut = TradeOUTBean.class.getDeclaredMethod("get"+tagBean.getDestTag(), noparams);
					tagBean.setDestTagValue(methodOut.invoke(tradeOut, noparm));
					destMap.put(tradeOut.getSOURCE_OUT(), tagBean);
				}
			}
			//System.out.println("================= " + destMap.size() + " " + destMap.containsKey("CAMRAUS") + " " + destMap.containsKey("CAMRAJP"));
			valueBean.setDestinationBean(destMap);
			itrValueMap.set(valueBean);
			/*Iterator<?> it = destMap.entrySet().iterator();
			
			while(it.hasNext()){
				Map.Entry<String,FieldTagBean> pair=(Map.Entry<String, FieldTagBean>) it.next();
				String destination=pair.getKey();
				FieldTagBean tagBean=pair.getValue();
			}*/
		}
		return mappedValues;
	}

	
	private List<FieldMappingValueBean> getFieldMapping(List<TagMapBean> tagMapping) throws Exception{
		// TODO Auto-generated method stub
		
		List<FieldMappingValueBean> MappingValues=new ArrayList<FieldMappingValueBean>();
		
		for(TagMapBean mapBean: tagMapping){
				if(isSrcTagExist(mapBean.getSourceTag(),MappingValues)){
					MappingValues=addDestinationBean(mapBean,MappingValues);
				}
				else{
					FieldMappingValueBean valueBean=new FieldMappingValueBean();
					HashMap<String,FieldTagBean> destinationBean=new HashMap<String, FieldTagBean>();
					valueBean.setBusinessName(mapBean.getBusinessName());
					valueBean.setDataType(mapBean.getDataType());
					valueBean.setSourceTag(mapBean.getSourceTag());
					valueBean.setSource(mapBean.getSource());
					valueBean.setGroupName(mapBean.getGroupName());
						FieldTagBean destTag=new FieldTagBean();
						destTag.setDestTag(mapBean.getDestTag());
						destinationBean.put(mapBean.getDest(), destTag);
					valueBean.setDestinationBean(destinationBean);
					MappingValues.add(valueBean);
				}
		}
		
		return MappingValues;
	}

	private List<FieldMappingValueBean> addDestinationBean(TagMapBean mapBean,
			List<FieldMappingValueBean> mappingValues) throws Exception{
		// TODO Auto-generated method stub
		ListIterator<FieldMappingValueBean> itrMap=mappingValues.listIterator();
		while(itrMap.hasNext()){
			FieldMappingValueBean valueBean=(FieldMappingValueBean) itrMap.next();
			if(valueBean.getSourceTag().equals(mapBean.getSourceTag())){
				HashMap<String,FieldTagBean> destinationBean=valueBean.getDestinationBean();				
				FieldTagBean destTag=new FieldTagBean();
				destTag.setDestTag(mapBean.getDestTag());
				destinationBean.put(mapBean.getDest(), destTag);
				valueBean.setDestinationBean(destinationBean);
				itrMap.set(valueBean);
			}
			
		}
		return mappingValues;
	}

	private boolean isSrcTagExist(String sourceTag,
			List<FieldMappingValueBean> mappingValues) {
		// TODO Auto-generated method stub
		for(FieldMappingValueBean valueBean:mappingValues){
			if(valueBean.getSourceTag().equals(sourceTag)){
				return true;
			}
		}
		return false;
	}

	@Override
	public List<String> getDestinationsForTrade(int tradeRefNo, int touchCount) throws Exception{
		// TODO Auto-generated method stub
		try{
			/*TradeINBean tradeInBean=tradeInDAO.getInTradeByID(tradeRefNo,touchCount);
			List<TagMapBean> tagMapping=fieldMappingDAO.getTagMap(tradeInBean.getSOURCE(),constants.FIELDMAP_DATATYPE_TRADE);
			List<String> distDest=new ArrayList<String>();
			for(TagMapBean mapBean:tagMapping){
				Boolean flg_exist=false;
				for(String dest:distDest){
					if(dest.equals(mapBean.getDest())){
						flg_exist=true;
					}
				}
				if(flg_exist==false){
					distDest.add(mapBean.getDest());
				}
			}
			return distDest; */
			//TODO- Sudipto : Try using below code instead of above logic
			return fieldMappingDAO.getDestinationForTrade(tradeRefNo,touchCount);
		}
		catch(Exception e){
			throw e;
		}
			
	}

	@Override
	public List<String> getDestinationsForSecurity(int secRefNo) throws Exception{
		// TODO Auto-generated method stub
		try{
			SecurityInBean secInBean=secInDAO.getSecurityInBeanById(secRefNo);
			System.out.println(secInBean.getSOURCE() + " " + secInBean.getTPH_SEC_SEQ_NO());
			List<TagMapBean> tagMapping=fieldMappingDAO.getTagMap(secInBean.getSOURCE(),constants.FIELDMAP_DATATYPE_SECURITY);
			List<String> distDest=new ArrayList<String>();
			for(TagMapBean mapBean:tagMapping){
				Boolean flg_exist=false;
				for(String dest:distDest){
					if(dest.equals(mapBean.getDest())){
						flg_exist=true;
					}
				}
				if(flg_exist==false){
					distDest.add(mapBean.getDest());
				}
			}
			return distDest;
		}
		catch(Exception e){
			throw e;
		}
	}
	
	@Autowired
	SecurityInDAO securityInDAO;
	
	@Autowired
	SecurityOutDAO securityOutDAO;
	
	@Override
	public List<FieldMappingValueBean> getFieldMappingValueForSecurity(int secRefNo) throws Exception{
		// TODO Auto-generated method stub
		try{
			SecurityInBean secInBean=securityInDAO.getSecurityInBeanById(secRefNo);
			List<SecurityOutBean> secOutBeans=securityOutDAO.getOutSecuritiesByID(secRefNo);
			List<TagMapBean> tagMapping=fieldMappingDAO.getTagMap(secInBean.getSOURCE(),constants.FIELDMAP_DATATYPE_SECURITY);
			List<FieldMappingValueBean> mappedValues= getFieldMapping(tagMapping);
			mappedValues=getTagValuesForSecurity(secInBean, secOutBeans, mappedValues);
			return mappedValues;
		}
		catch(Exception e){
			throw e;
		}
	}
	


	private List<FieldMappingValueBean> getTagValuesForSecurity(SecurityInBean secInBean,
			List<SecurityOutBean> secOutBeans,
			List<FieldMappingValueBean> mappedValues) throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		// TODO Auto-generated method stub
		Class<?> noparams[] = {};
		Object[] noparm={};
		ListIterator<FieldMappingValueBean> itrValueMap=mappedValues.listIterator();
		while(itrValueMap.hasNext()){
			FieldMappingValueBean valueBean=(FieldMappingValueBean)itrValueMap.next();
			//System.out.println("=================Security valueBean.getSourceTag() ==== " +valueBean.getSourceTag());
			Method method = SecurityInBean.class.getDeclaredMethod("get"+valueBean.getSourceTag(), noparams);
			valueBean.setSourceTagValue(method.invoke(secInBean, noparm));
			
			HashMap<String,FieldTagBean> destMap=valueBean.getDestinationBean();
			ListIterator<SecurityOutBean> itrTradeOut=secOutBeans.listIterator();
			while(itrTradeOut.hasNext()){
				SecurityOutBean secOut=itrTradeOut.next();
				if(destMap.get(secOut.getSOURCE_OUT())!=null){
				FieldTagBean tagBean=destMap.get(secOut.getSOURCE_OUT());
				//System.out.println("=================Security tagBean.getDestTag() ==== " +tagBean.getDestTag());
				Method methodOut = SecurityOutBean.class.getDeclaredMethod("get"+tagBean.getDestTag(), noparams);
				tagBean.setDestTagValue(methodOut.invoke(secOut, noparm));
				destMap.put(secOut.getSOURCE_OUT(), tagBean);
				}
			}
			valueBean.setDestinationBean(destMap);
			itrValueMap.set(valueBean);
			/*Iterator<?> it = destMap.entrySet().iterator();
			
			while(it.hasNext()){
				Map.Entry<String,FieldTagBean> pair=(Map.Entry<String, FieldTagBean>) it.next();
				String destination=pair.getKey();
				FieldTagBean tagBean=pair.getValue();
			}*/
		}
		return mappedValues;
	}

	
	
}
