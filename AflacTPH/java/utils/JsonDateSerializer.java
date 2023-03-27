package com.aflac.aims.tph.web.utils;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.SerializerProvider;


public class JsonDateSerializer extends JsonSerializer<Date> {
    private static final String dateFormat = ("yyyy-MM-dd HH:mm:ss");
    
    public JsonDateSerializer() {
    	
    }
    
    @Override
    public void serialize(Date date, JsonGenerator gen, SerializerProvider provider)
            throws IOException, JsonProcessingException {
    	//System.out.println("date:" + date);
    	SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);
        String formattedDate = formatter.format(date);
        //System.out.println("date and formattedDate:" + date + ":::" + formattedDate);
        gen.writeString(formattedDate);
    }
}
