package level.mapping;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import level.mapping.annotation.Id;
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
            Logger.getLogger(Model.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        } catch (Throwable ex) {
            Logger.getLogger(Model.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }
    
    public  String id() throws Exception, Throwable{    
        List<Function> list= Mapping.getter(this.getClass());
        String rep="";
        for ( Function m:list)
            if (m.getMethod().isAnnotationPresent(Id.class))
            rep+=m.invoke(this);
        return rep;
    }
 }