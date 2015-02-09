package mk.domain;

import java.io.Serializable;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class RESTObject  implements Serializable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    private Long classID;
    
    private String apiKey;
    
    private String className;
    
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RESTAttribute> attributes;

    public Long getClassID() {
        return classID;
    }

    public void setClassID(Long classID) {
        this.classID = classID;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public List<RESTAttribute> getAttributes() {
        return attributes;
    }

    public void setAttributes(List<RESTAttribute> attributes) {
        this.attributes = attributes;
    }
    
}
