package io.github.andriamarosoa.helper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import com.github.wnameless.json.unflattener.JsonUnflattener;
import com.github.wnameless.json.flattener.JsonFlattener;
import io.github.andriamarosoa.entity.Model;
import io.github.andriamarosoa.mapping.Function;
import io.github.andriamarosoa.mapping.Mapping;
import java.util.LinkedHashMap;





public class Tracer {
    private static ObjectMapper mapper=init();
    
    public static ObjectMapper init(){
        ObjectMapper mapper=new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true);
        mapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
        //mapper.setSerializationInclusion(Include.NON_NULL);
        return mapper; 
    }
    public static String describe(Object o) throws JsonProcessingException{
        return mapper.writeValueAsString(o);
    }
    public static <T>T convert(String value,Class<T> model ) throws JsonProcessingException{
        return mapper.readValue(value, model);
    }
    public static Map<String,Object>map(Model model){
        Map<String, Object> map = mapper.convertValue(model, Map.class);
        return map;
    }
    public static <T>T mapTo(Map m,Class<T> model){
        return mapper.convertValue(m, model);
    }
    public static Map<String,Object> groupBy(Model [] list,Map<String,Object> map,String name) throws Exception, Throwable{
        
        if (list!=null && list[0]!=null){
        List<Function> method=Mapping.getter( list[0].getClass() );
        for(Model model:list){
            String index=name.replace("[]", "[\"xxx"+model.id()+"\"]");
   
            map.put(index,map(model));
            for (Function m:method){
                Info f=new Info(m.getMethod());
                
                if (Model.class.isAssignableFrom(f.returnType()) ){
                   
                    Object o=m.invoke(model);
                    if(o!=null){
                    Model [] l=new Model[1];
                    Map t=(Map)map.get(index);
                    
                    t.put(f.getName().replace("[]", ""),new LinkedHashMap());
                    if(f.isArray())
                        l=(Model[]) o;
                    else
                        l[0]=(Model)o;
                    groupBy(l,map,index+"."+f.getName());
                    }
                }
            }
            
        
        }
        }
        return map;
    }
    public static <T>T[] groupBy(T ... model) throws Exception, Throwable{
        
        //System.out.println();
        Map <String,Object> map=Tracer.groupBy((Model[]) model,new LinkedHashMap(),model.getClass().getSimpleName());
        
        
        
        map=JsonUnflattener.unflattenAsMap(map);
        
        
        reduce(map);
        
        
        List<Map<String,Object>> list=(List<Map<String, Object>>) map.get(model.getClass().getSimpleName().replace("[]", ""));
        return mapper.convertValue(list, (Class<T[]>) model.getClass());
      
    }
    public static boolean isArray(Map<String,Object>map){
        for (String s:map.keySet())
            if (s.startsWith("xxx")) return true;
            else return false;
        return false;
    }
    public static void reduce(Map<String,Object>map){
        for (String s:map.keySet()){
            if (map.get(s) instanceof Map){
                Map<String,Object> m=(Map<String,Object>) map.get(s);
                if (isArray(m))
                    map.put(s,m.values().stream().collect(Collectors.toList()));
                reduce(m);
            }

        }


    }
    public static Map<String,Object> flat(Map<String,Object> map) throws JsonProcessingException, JsonProcessingException{
        return JsonFlattener.flattenAsMap(mapper.writeValueAsString(map));
    }
    
}
