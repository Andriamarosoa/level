
package io.github.andriamarosoa.search;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;


public class Operateur {
    
    private int id;
    private String label;
    private String signe;
    private String name;
    private String type;
    private static Operateur []all=new Operateur[0];
    
    //setter
    public void setType(String type) {
        this.type = type;
    }
    public void setLabel(String label) {
        this.label = label;
    }
    public void setSigne(String signe) {
        this.signe = signe;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setId(int id) {
        this.id = id;
    }

    
    
    //getter
    public int getId() {
        return id;
    }
    public String getLabel() {
        return label;
    }
    public String getSigne() {
        return signe;
    }
    public String getName() {
        return name;
    }
    public String getType() {
        return type;
    }
   
    
    
    
    public Operateur(){}
    
    public static void init() throws IOException{
        ObjectMapper objectMapper = new ObjectMapper();  
        
        InputStream is = Operateur.class.getClassLoader().getResourceAsStream("operateur.json");
        all=objectMapper.readValue(is, Operateur[].class);   
       
    }
    
    
    
    
    public static Operateur [] get(String type) throws IOException{
        if (all.length==0) init();
        List<Operateur> op=new ArrayList();
        for (int i=0;i<all.length;i++)
            if (all[i].getType().compareTo(type)==0)
                op.add(all[i]);
        return op.toArray(new Operateur[op.size()]);
    }
    public static Operateur get(int id) throws IOException{
        if (all.length==0) init();
        for (int i=0;i<all.length;i++)
            if (all[i].getId()==id)
                return all[i];
        return null;
    }
    public String toString(){
        return "id: "+this.getId()+", "+
               "name: "+this.getName()+", "+
               "signe: "+this.getSigne()+", "+
               "label: "+this.getLabel()+", "+
               "type: "+this.getType();
    }
}
