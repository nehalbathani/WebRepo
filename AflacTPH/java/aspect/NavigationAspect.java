package com.aflac.aims.tph.web.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;
import org.springframework.ui.ModelMap;

import com.aflac.aims.tph.web.utils.constants;

@Aspect
public class NavigationAspect {
	
	@Before("execution(* com.aflac.aims.tph.web.controller.EMFTradesController.*(..)) && args(model,..)")
	public void setNavVarsForIncomingTrade(JoinPoint joinPoint, ModelMap model) {
		model.addAttribute("system","EXT_MNG"); 
    	model.addAttribute("view","TRADE");
		
		System.out.println("Before Method : " + joinPoint.getSignature().getName());
		
	}
	
	@Before("execution(* com.aflac.aims.tph.web.controller.CAMRATradesController.*(..)) && args(model,paramDest,..)")
	public void setNavVarsForOutgoingTrade(JoinPoint joinPoint, ModelMap model,String paramDest) {
		model.addAttribute("view","TRADE");
		if(paramDest!=null && paramDest.equals(constants.SOURCE_OUT_CAMRA_JP)){
		     model.addAttribute("system","CAMRAJP");
		     model.addAttribute("title","CAMRA(JP) Security");					
  		}
		if(paramDest!=null && paramDest.equals(constants.SOURCE_OUT_CAMRA_US)){
		      model.addAttribute("system","CAMRAUS");
		      model.addAttribute("title","CAMRA(US) Security");
  		}
		if(paramDest!=null && paramDest.equals(constants.SOURCE_OUT_IVT)){
		      model.addAttribute("system","IVT");
		      model.addAttribute("title","IVT Security");
  		}
		System.out.println("Before Method : " + joinPoint.getSignature().getName());
		
	}
	
	@Before("execution(* com.aflac.aims.tph.web.controller.EMFSecurityController.*(..)) && args(model,..)")
	public void setNavVarsForIncomingSecurity(JoinPoint joinPoint, ModelMap model) {
		model.addAttribute("system","EXT_MNG"); 
    	model.addAttribute("view","SECURITY");
		
		System.out.println("Before Method : " + joinPoint.getSignature().getName());
		
	}
	
	@Before("execution(* com.aflac.aims.tph.web.controller.CAMRASecurityController.*(..)) && args(model,paramDest,..)")
	public void setNavVarsForOutgoingSecurity(JoinPoint joinPoint, ModelMap model,String paramDest) {
		model.addAttribute("view","SECURITY");
		if(paramDest!=null && paramDest.equals(constants.SOURCE_OUT_CAMRA_JP)){
		     model.addAttribute("system","CAMRAJP");
		     model.addAttribute("title","CAMRA(JP) Security");					
  		}
		if(paramDest!=null && paramDest.equals(constants.SOURCE_OUT_CAMRA_US)){
		      model.addAttribute("system","CAMRAUS");
		      model.addAttribute("title","CAMRA(US) Security");
  		}
		if(paramDest!=null && paramDest.equals(constants.SOURCE_OUT_IVT)){
		      model.addAttribute("system","IVT");
		      model.addAttribute("title","IVT Security");
  		}
		System.out.println("Before Method : " + joinPoint.getSignature().getName());
		
	}
	
	
}
