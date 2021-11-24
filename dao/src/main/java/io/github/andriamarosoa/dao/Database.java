package io.github.andriamarosoa.dao;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.andriamarosoa.dao.annotation.Table;
import io.github.andriamarosoa.entity.Model;
import io.github.andriamarosoa.helper.Tracer;
import io.github.andriamarosoa.mapping.Function;
import io.github.andriamarosoa.mapping.Mapping;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;



public class Database {
    private static final Map <String,Source> map=init();
    public static Connection connect() throws SQLException, ClassNotFoundException{
        String dbURL = "jdbc:sqlserver://localhost\\sqlexpress";
        Properties properties = new Properties();
        properties.put("user", "sa");
        properties.put("password", "123456");
        properties.put("databaseName", "hjra");
        DriverManager.registerDriver(new com.microsoft.sqlserver.jdbc.SQLServerDriver());
        return DriverManager.getConnection(dbURL, properties);
    }
    public static Source[] source() throws Throwable{
        

        String sql="select "+ 
        "i.table_name ,"+
        "i.column_name ,"+
        "t.constraint_type,"+
        "OBJECT_NAME (fk.referenced_object_id) AS rf_table,"+
        "COL_NAME(fc.referenced_object_id, fc.referenced_column_id) AS rf_column "+
        "from information_schema.columns i "+
        "left join information_schema.constraint_column_usage c  on i.table_name=c.table_name and i.column_name=c.column_name "+
        "left join information_schema.table_constraints t on c.constraint_name=t.constraint_name "+
        "left join sys.foreign_keys as fk on fk.name=c.constraint_name "+
        "left join sys.foreign_key_columns AS fc ON fk.object_id = fc.constraint_object_id ";

        Connection c=Database.connect();
        PreparedStatement ps=c.prepareStatement(sql);
        ResultSet r=ps.executeQuery();
        
        List <Source> list=new ArrayList();
       
        
        while(r.next()){
               
            Source s=new Source();
            Column[] col={new Column()};
            s.setColumn(col);
            s.setName(r.getString("table_name"));
            col[0].setName(r.getString("column_name"));
            col[0].setIsPrimaryKey(false);
            col[0].setIsForeignKey(false);
            if ("PRIMARY KEY".equals(r.getString("constraint_type")))
                col[0].setIsPrimaryKey(true);
            else if ("FOREIGN KEY".equals(r.getString("constraint_type"))){
                col[0].setIsForeignKey(true);
                col[0].setReferences(new Column());
                col[0].getReferences().setName(r.getString("rf_column"));
                Source rfTable=new Source();
                col[0].getReferences().setSource(rfTable);
                rfTable.setName(r.getString("rf_table"));
            }
            list.add(s);
        }
        Source [] rep=list.toArray(new Source[list.size()]);
        
      
        
        c.close();
        return Tracer.groupBy(rep);
    }
    public static Map<String,Source> init(){
        Source ss[] = null;
        try {
            ss = source();
        } catch (Throwable ex) {
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
        }
        Map <String,Source>map=new HashMap();
        
        
        for (Source s:ss)
            if (s!=null)
            map.put(s.getName(), s);
        
       
        
        
        return map;
    }
    public static Source source(String name) throws Exception{
        Source rep= map.get(name);
        if (rep==null) throw new Exception ("la source(view/table)"+name+" n'existe pas");
        
        return rep;
    }
    public static Column join(Source a,Source b){
        for(Column c:a.getColumn())
            if(c.isForeignKey() && c.getReferences().getSource().equals(b))
                return c;
        return null;
    }
    public static String joinAsString(Source a,Source b){
        Column c=join(a,b);
        return a.getName()+"."+c.getName()+"="+b.getName()+"."+c.getReferences().getName();
    }
    
    public static String joinAsString(String a,String b) throws Exception{
        Source a1=source(a);
        Source b1=source(b);
        
        
        
        
        Column c=join(a1,b1);
        
        return a1.getName()+"."+c.getName()+"="+b1.getName()+"."+c.getReferences().getName();
    }
    
    
    public  static String join(Model m,String rep) throws Throwable{
        List<Function> list=Mapping.getter(m.getClass());
        
        
        for(Function f:list){
            Object o=f.invoke(m);
            if(o!=null && o instanceof Model){
                rep+=" join "+table((Model) o);
                rep+=" on "+ Database.joinAsString(table(m), table((Model) o));
                rep+=join((Model) o,"");
            }
        }
        
        return rep;
    }
    public static String join(Model m) throws Throwable{
        return "select * from "+join(m,table(m));
    }
    
    public static String table(Model m){
        if(m.getClass().getAnnotation(Table.class)==null) return null;
        Table t=m.getClass().getAnnotation(Table.class);
        return t.select();
    }
    
    public static <T>T[] find(Connection c,T model,String sql) throws SQLException, JsonProcessingException{
        PreparedStatement ps=c.prepareStatement(sql);
        ResultSet r=ps.executeQuery();
        
        ResultSetMetaData info=r.getMetaData();
        String rep="[";
        ObjectMapper m=new ObjectMapper();
        while(r.next()){
            String s=m.writerWithDefaultPrettyPrinter().writeValueAsString(model);
            for (int i=0;i<info.getColumnCount();i++)
                if(r.getString(i+1)!=null){
                    s=s.replace(info.getColumnName(i+1)+"\" : null",info.getColumnName(i+1)+"\" :  \""+ r.getString(i+1)+"\"");
                    s=s.replace(info.getColumnName(i+1)+"\" : 0",info.getColumnName(i+1)+"\" :  \""+ r.getString(i+1)+"\"");
                }
            rep+=s+",\n";
        }
        c.close();
        return (T[]) Tracer.convert((rep+="]").replace(",\n]", "]"),java.lang.reflect.Array.newInstance(model.getClass(), 0).getClass());
      
    }
    
    
}
