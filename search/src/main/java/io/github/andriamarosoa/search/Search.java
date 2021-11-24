
package io.github.andriamarosoa.search;

import io.github.andriamarosoa.dao.Database;
import io.github.andriamarosoa.entity.*;
import io.github.andriamarosoa.helper.Tracer;
import java.util.Map;


public class Search {
    public static String search(Model model,Map<String ,Object> operateur)throws Exception, Throwable{
        Map<String,Object>map= Tracer.flat(Tracer.map((Model)model));
        Map<String,Object>op=Tracer.flat(operateur);
        String condition="where 1=1";
        for (String key:op.keySet()){
            Object[] o=map.keySet().stream().filter(s->s.replace("Operateur_", "").equals(key.replace("Operateur_", ""))).toArray();
            if (o.length>0){
                String s=(String) o[0];
                Operateur p=Operateur.get((int)op.get(key));
                condition+=" and "+s +" "+ p.getSigne() +" '"+map.get(s)+"'";
            }
        }
        return Database.join(model)+" "+condition;
    }
    public static String last(String str){
        if(!str.contains(".")) return str;
        return str.substring(str.lastIndexOf(".")+1);
    }
}
