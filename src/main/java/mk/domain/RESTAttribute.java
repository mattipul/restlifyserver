package mk.domain;

import java.io.Serializable;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;

@Entity
public class RESTAttribute  implements Serializable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    private String key;
    
    private String value;
    
    private String type;
    
    private boolean list;
    
    @ManyToOne(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    private RESTObject joinObj;

    private String listPrimObjects;
    
    @ManyToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    private List<RESTObject> listObjects;

    public String getListPrimObjects() {
        return listPrimObjects;
    }

    public void setListPrimObjects(String listPrimObjects) {
        this.listPrimObjects = listPrimObjects;
    }

    public boolean isList() {
        return list;
    }

    public void setList(boolean list) {
        this.list = list;
    }

    public RESTObject getJoinObj() {
        return joinObj;
    }

    public void setJoinObj(RESTObject joinObj) {
        this.joinObj = joinObj;
    }

    public List<RESTObject> getListObjects() {
        return listObjects;
    }

    public void setListObjects(List<RESTObject> listObjects) {
        this.listObjects = listObjects;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Object getValue() {
        if(this.type.equals("int")){
            return Integer.parseInt(value);
        }
        else if(this.type.equals("double")){
            return Double.parseDouble(value);
        }
        else if(this.type.equals("string")){
            return value;
        }
        else if(this.type.equals("boolean")){
            return Boolean.getBoolean(value);
        }       
        else{
            return Long.parseLong(value);
        }
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
    
    
}
