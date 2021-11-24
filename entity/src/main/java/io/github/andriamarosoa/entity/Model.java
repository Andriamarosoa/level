package io.github.andriamarosoa.entity;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.logging.Level;
import java.util.logging.Logger;
import io.github.andriamarosoa.entity.annotation.Id;
import java.lang.reflect.Method;
public class Model {
    private static final ObjectMapper mapper=init();
    private static ObjectMapper init(){
        ObjectMapper mapper=new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true);
        mapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
        return mapper;
    }
     
    @Override
    public String toString(){
        String rep="";
        
        
        try {
            rep=mapper.writeValueAsString(this);
        } catch (JsonProcessingException ex) {
            Logger.getLogger(Model.class.getName()).log(Level.SEVERE, null, ex);
        }
       
        return rep;
    }
    
    @Override
    public boolean equals(Object p){
        if (p==null) return false;
        if (p.getClass()!= this.getClass()) return false;
        try {
            return this.id().equals(((Model)p).id());
        } catch (Exception ex) {
            System.out.println("error lesy e : "+ex.getMessage());
            Logger.getLogger(Model.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        } 
    }
    
    public  String id() throws Exception{    
        Method method[]=this.getClass().getMethods();
        String rep="";
        for (Method m:method)
            if (m.getName().startsWith("get") && m.getParameterCount()==0 && m.isAnnotationPresent(Id.class))
                rep+=m.invoke(this);
        
        return rep;
    }
 }