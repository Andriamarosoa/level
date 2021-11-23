package level.mapping;

import java.lang.invoke.CallSite;
import java.lang.invoke.LambdaMetafactory;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Method;
import org.apache.commons.lang3.ClassUtils;

public class Loader {
    private static Class noReturnType[]={noReturn0.class,noReturn1.class,noReturn2.class,noReturn3.class};
    private static Class withReturnType[]={withReturn0.class,withReturn1.class,withReturn2.class,withReturn3.class};
    private static Class functionnal(boolean returnType,int nbParameter){
        if (returnType)
            return withReturnType[nbParameter];
        return noReturnType[nbParameter];
    }
    static withReturn2<Loader,withReturn0,Object[]> w0=Loader ::runs;
    static withReturn2<Loader,withReturn1,Object[]> w1=Loader ::runs;
    static withReturn2<Loader,withReturn3,Object[]> w2=Loader ::runs;
    static withReturn2<Loader,withReturn2,Object[]> w3=Loader ::runs;

    static withReturn2<Loader,noReturn0,Object[]> n0=Loader ::runs;
    static withReturn2<Loader,noReturn1,Object[]> n1=Loader ::runs;
    static withReturn2<Loader,noReturn2,Object[]> n2=Loader ::runs;
    static withReturn2<Loader,noReturn3,Object[]> n3=Loader ::runs;


    static withReturn2 [] w={w0,w1,w2,w3};
    static withReturn2 [] n={n0,n1,n2,n3};
        
    public  static <T extends noReturn>Object run(T l,Object ... param){
        System.out.println("haha");
        n[param.length-1].run(new Loader(), l, param);
        return null;
    }
    public  static <T extends withReturn>Object run(T l,Object ... param){
        return w[param.length-1].run(new Loader(), l, param);
    }
    public  static <T extends Functionnal>Object run(T l,Object ... param){
        if (noReturn.class.isInstance(l))
            return n[param.length-1].run(new Loader(), l, param);
        return w[param.length-1].run(new Loader(), l, param);
    }
    public  Object runs(withReturn0 l,Object ... param){
        return l.run(param[0]);
    }
    public  Object runs(withReturn1 l,Object ... param){
        return l.run(param[0],param[1]);
    }
    public  Object runs(withReturn2 l,Object ... param){
        return l.run(param[0],param[1],param[2]);
    }
    public  Object runs(withReturn3 l,Object ... param){
        return l.run(param[0],param[1],param[2],param[3]);
    }
    	
    public  Object runs(noReturn0 l,Object ... param){
        l.run(param[0]);
        return null;
    }
    public  Object runs(noReturn1 l,Object ... param){
        l.run(param[0],param[1]);
        return null;
    }
    public  Object runs(noReturn2 l,Object ... param){
        l.run(param[0],param[1],param[2]);
        return null;
    }
    public  Object runs(noReturn3 l,Object ... param){
        l.run(param[0],param[1],param[2],param[3]);
        return null;
    }

    
    
    public static <T extends Functionnal>T method(Method m) throws Exception, Throwable{
        Class returnType=m.getReturnType() == void.class ?void.class :Object.class;
        Class<T> functionnal=functionnal(returnType != void.class,m.getParameterTypes().length);
        
        
        MethodHandles.Lookup lookup = MethodHandles.lookup();
        Class param[]=ClassUtils.primitivesToWrappers(m.getParameterTypes());
        MethodHandle mt=lookup.unreflect(m);

        CallSite site = LambdaMetafactory.metafactory(lookup,
                "run",
                MethodType.methodType(functionnal),
                MethodType.methodType(returnType, mt.type().generic().parameterArray()), // Function::apply signature
                mt,
                MethodType.methodType(m.getReturnType(), m.getDeclaringClass(),param) // Person::getName signature
        );
        
        return functionnal.cast(site.getTarget().invoke());
  
    }
    
    

}
