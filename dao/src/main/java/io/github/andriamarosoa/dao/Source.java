package io.github.andriamarosoa.dao;

import io.github.andriamarosoa.entity.Model;
import io.github.andriamarosoa.entity.annotation.Id;






public class Source extends Model{
    
    private String name;
    private Column [] column;
    
    //setter
    public void setName(String sourceName) {
        this.name = sourceName;
    }
    public void setColumn(Column[] column) {
        this.column = column;
    }
    
    //getter
    @Id
    public String getName() {
        return name;
    }
    public Column[] getColumn() {
        return column;
    }
    
}
