package level.mapping;
public interface Functionnal{
    
}
interface withReturn extends Functionnal{
    
}
interface noReturn extends Functionnal{
    
}


@FunctionalInterface
interface noReturn0 extends noReturn{
    
    void run(Object caller);  
}
@FunctionalInterface
interface noReturn1  extends noReturn{
    void run(Object caller,Object param1);  
}
@FunctionalInterface
interface noReturn2 extends noReturn{
    void run(Object caller,Object param1,Object param2);  
}
@FunctionalInterface
interface noReturn3 extends noReturn{
    void run(Object caller,Object param1,Object param2,Object param3);  
}



@FunctionalInterface
interface withReturn0<A> extends withReturn{
    Object run(A caller);  
    
}
@FunctionalInterface
interface withReturn1<A,B> extends withReturn{
    Object run(A caller,B param1);  
}
@FunctionalInterface
interface withReturn2<A,B,C> extends withReturn{
    Object run(A caller,B param1,C  param2);  
}
@FunctionalInterface
interface withReturn3 extends withReturn{
    Object run(Object caller,Object param1,Object param2,Object param3);  
}

