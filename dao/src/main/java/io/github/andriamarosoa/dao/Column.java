package io.github.andriamarosoa.dao;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.github.andriamarosoa.entity.Model;
import io.github.andriamarosoa.entity.annotation.Id;


public class Column extends Model{
    private String name;
    private boolean isPrimaryKey;
    private boolean isForeignKey;
    private Source source;
    private Column references;
    
    //setter
    public void setName(String name) {
        this.name = name;
    }
    public void setIsPrimaryKey(boolean isPrimaryKey) {
        this.isPrimaryKey = isPrimaryKey;
    }
    public void setIsForeignKey(boolean isForeignKey) {
        this.isForeignKey = isForeignKey;
    }
    public void setSource(Source source) {
        this.source = source;
    }
    public void setReferences(Column references) {
        this.references = references;
    }
    
    
    //getter
    @Id
    public String getName() {
        return name;
    }
    @JsonIgnore
    public boolean isPrimaryKey() {
        return isPrimaryKey;
    }
    @JsonIgnore
    public boolean isForeignKey() {
      return references!=null;
    }
    public Source getSource() {
        return source;
    }
    public Column getReferences() {
        return references;
    }
    
    
    
}
