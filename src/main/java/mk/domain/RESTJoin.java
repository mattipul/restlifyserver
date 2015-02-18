package mk.domain;

import java.io.Serializable;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class RESTJoin  implements Serializable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    @ManyToOne(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    private RESTObject parentObj;
    
    @ManyToOne(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    private RESTAttribute attr;
    
    @ManyToOne(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    private RESTObject joinedObj;

    public RESTObject getParentObj() {
        return parentObj;
    }

    public void setParentObj(RESTObject parentObj) {
        this.parentObj = parentObj;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public RESTAttribute getAttr() {
        return attr;
    }

    public void setAttr(RESTAttribute attr) {
        this.attr = attr;
    }

    public RESTObject getJoinedObj() {
        return joinedObj;
    }

    public void setJoinedObj(RESTObject joinedObj) {
        this.joinedObj = joinedObj;
    }
    
}
