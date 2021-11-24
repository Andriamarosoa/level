package io.github.andriamarosoa.mapping;


import io.github.andriamarosoa.mapping.annotation.Column;
import java.lang.reflect.Method;



public class Function  {
    private Method method;
    private Functionnal functionnal;
    
    //setter 
    public void setMethod(Method method) {
        this.method = method;
    }
    public void setFunctionnal(Functionnal functionnal) {
        this.functionnal = functionnal;
    }
    //getter
    public Method getMethod() {
        return method;
    }
    public Functionnal getFunctionnal() {
        return functionnal;
    }
    
    public String getName(){
        return this.getMethod().isAnnotationPresent(Column.class) ?this.getMethod().getAnnotation(Column.class).name(): this.getMethod().getName();
    }
    public Class[] getParameterClass(){
        return this.getParameterClass();
    }
    public int getParameterCount(){
        return this.getParameterCount();
    }
    public Class getReturnType(){
        return this.getMethod().getReturnType();
    } 
    public Class getDeclaringClass(){
        return this.getMethod().getDeclaringClass();
    }
    
    public <T>Object invoke(Object ... param){
        return Loader.run(this.getFunctionnal(), param);
    }
    public boolean isGetter(){
        return (this.getMethod().getName().startsWith("get") && !this.getMethod().getName().equals("getClass") && this.getReturnType()!=Void.class && this.getMethod().getParameterCount()==0);
    }
    public boolean isSetter(){
        return (this.getMethod().getName().startsWith("set")  && this.getMethod().getParameterCount()>0 );
    }
}
