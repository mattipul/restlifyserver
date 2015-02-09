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
public class RESTClass implements Serializable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    private String apiKey;
    
    private String className;
    
    private String xml;
    
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RESTAttributeDefinition> attributes;
    
    private Long idRunner;

    public List<RESTAttributeDefinition> getAttributes() {
        return attributes;
    }

    public void setAttributes(List<RESTAttributeDefinition> attributes) {
        this.attributes = attributes;
    }

    public Long getIdRunner() {
        return idRunner;
    }

    public void setIdRunner(Long idRunner) {
        this.idRunner = idRunner;
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

    public String getXml() {
        return xml;
    }

    public void setXml(String xml) {
        this.xml = xml;
    }
    
}
