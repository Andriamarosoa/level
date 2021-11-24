package io.github.andriamarosoa.mapping;


import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import io.github.andriamarosoa.mapping.annotation.Column;


public class Mapping {
    private static Map<Class,Map<String,Function>> map=new HashMap();
    
    
     public static Class getClass(String name,String packageName) {
        Class cls=null;
        
        try {
            cls= Class.forName(packageName+"."+(name.replace(".class", "")));
        } catch (ClassNotFoundException ex) {
            System.out.println("erreur   "+ex.getMessage()+"huhuhu");
        }
        finally{
            return cls;
        }
    }
    
    public static List<Class> classes(String packageName){
        InputStream stream=ClassLoader.getSystemResourceAsStream(packageName.replaceAll("[.]", "/"));
        BufferedReader reader=new BufferedReader (new InputStreamReader(stream));
        List<Class> list= reader.lines().filter(line->line.endsWith(".class")).map(line->getClass(line,packageName)).collect(Collectors.toList());
        return list;
        
       
    }
    public static void init (Class model) throws Throwable{
        Map <String,Function> rep=new HashMap();
        Method[] m=model.getMethods();
        
        
        for (int i=0;i<m.length;i++){
            String name=m[i].isAnnotationPresent(Column.class) ?m[i].getAnnotation(Column.class).name(): m[i].getName();
            Function f=new Function();
            f.setFunctionnal(Loader.method(m[i]));
            f.setMethod(m[i]);
            rep.put(name, f);
            
        }
            
        map.put(model, rep);
    }
    public static void init(String packageName) throws Throwable{
        List<Class> cls=classes(packageName);
        for (int i=0;i<cls.size();i++)
            init(cls.get(i));
    
    }
    
    
    public static Function get(Class model,String name) throws Throwable{
        if (map.get(model)==null)init(model);
        return map.get(model).get(name);
    }
    
    public static List<Function> getter(Class model) throws Throwable{
        if (map.get(model)==null)init(model);
        List<Function> rep=new ArrayList();
        map.get(model).forEach((key,value)->{
            if (value.isGetter()) rep.add(value);
        });
        return rep;
    }
    public static List<Function> setter(Class model) throws Throwable{
        if (map.get(model)==null)init(model);
        List<Function> rep=new ArrayList();
        map.get(model).forEach((key,value)->{
            if (value.isSetter()) rep.add(value);
        });
        return rep;
    }
    
}
