package mk.domain;

import java.util.List;

public class ClassJSON {
    
    private String name;
    
    private List<AttributeJSON> attributes;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<AttributeJSON> getAttributes() {
        return attributes;
    }

    public void setAttributes(List<AttributeJSON> attributes) {
        this.attributes = attributes;
    }
    
    
    
}
