package mk.domain;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

@Entity
public class RESTJoin  implements Serializable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    @OneToOne
    private RESTAttribute attr;
    
    @OneToOne
    private RESTObject joinedObj;

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
