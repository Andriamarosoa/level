package io.github.andriamarosoa.helper;

import java.lang.reflect.Method;

public class Info {
    private Method m;
    
    public boolean isArray(){
        return m.getReturnType().isArray();
    }
    public String getName(){
        String methodName=m.getName().substring(3);
        String rep=(methodName.charAt(0)+"").toLowerCase()+methodName.substring(1);
        if (this.isArray())
            rep+="[]";
        return rep;
    }
    public Class returnType(){
        Class c=m.getReturnType();
        while (c.isArray())
            c=c.getComponentType();
        return c;
    }
    
    public Info(Method m){
        this.m=m;
    }
}
