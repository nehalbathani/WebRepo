package com.aflac.aims.tph.web.utils;

import java.util.List;

import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import freemarker.cache.TemplateLoader;
  
public class HtmlFreeMarkerConfigurer extends FreeMarkerConfigurer {  
  
    @Override  
    protected TemplateLoader getAggregateTemplateLoader(List<TemplateLoader> templateLoaders) {
        return new HtmlTemplateLoader(super.getAggregateTemplateLoader(templateLoaders));  
    }  
}  