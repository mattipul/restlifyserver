package mk.profiles;


import java.util.List;
import javax.annotation.PostConstruct;
import mk.domain.Member;
import mk.domain.RESTAttribute;
import mk.domain.RESTAttributeDefinition;
import mk.domain.RESTClass;
import mk.domain.RESTObject;
import mk.repository.MemberRepository;
import mk.repository.RESTAttributeDefinitionRepository;
import mk.repository.RESTAttributeRepository;
import mk.repository.RESTClassRepository;
import mk.repository.RESTObjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile(value = {"dev", "default"})
public class DevProfile {
    
    @Autowired
    private MemberRepository memberRepository;
    
    @Autowired
    private RESTClassRepository restClassRepository;
    
    @Autowired
    private RESTObjectRepository restObjectRepository;
    
    @Autowired
    private RESTAttributeRepository restAttributeRepository;
    
    @Autowired
    private RESTAttributeDefinitionRepository restAttributeDefinitionRepository;
    
    private RESTClass createClass(String apikey, String name){
        RESTClass classObj=new RESTClass();
        classObj.setApiKey(apikey);
        classObj.setClassName(name);
        classObj.setIdRunner(1L);
        this.restClassRepository.save(classObj);
        return classObj;
    }
    
    private RESTObject createObject(Long id, String apikey, String className, List<RESTAttribute> attrs){
        RESTObject rObj=new RESTObject();
        rObj.setApiKey(apikey);
        rObj.setAttributes(attrs);
        rObj.setClassName(className);
        rObj.setClassID(id);
        return rObj;
    }
    
    private RESTAttribute createAttribute(String apikey, String key, String value, String type, boolean list){
        RESTAttribute rAttr=new RESTAttribute();
        rAttr.setKey(key);
        rAttr.setType(type);
        rAttr.setValue(value);
        rAttr.setList(list);
        this.restAttributeRepository.save(rAttr);
        return rAttr;
    }
    
    private RESTAttributeDefinition createAttributeDef(String apikey, String key, String type, boolean list){
        RESTAttributeDefinition rAttr=new RESTAttributeDefinition();
        rAttr.setKey(key);
        rAttr.setType(type);
        rAttr.setList(list);
        this.restAttributeDefinitionRepository.save(rAttr);
        return rAttr;
    }
    
    @PostConstruct
    public void init(){
        
        Member m=new Member();
        m.setUsername("mattipul");
        m.setPassword("killis");
        this.memberRepository.save(m);
        /*
        RESTClass memberClass=this.createClass("apikey", "member");
        memberClass.setIdRunner(2L);
        List<RESTAttributeDefinition> memberObjAttrsDefs=new ArrayList<>();
        memberObjAttrsDefs.add(this.createAttributeDef("apikey", "name","string", false));
        memberObjAttrsDefs.add(this.createAttributeDef("apikey", "age", "int", false));
        memberObjAttrsDefs.add(this.createAttributeDef("apikey", "image", "image", true));
        memberClass.setAttributes(memberObjAttrsDefs);
        this.restClassRepository.save(memberClass);
        
        
        RESTObject memberObj=this.createObject(1L, "apikey", "member", null);
        List<RESTAttribute> memberObjAttrs=new ArrayList<RESTAttribute>();
        memberObjAttrs.add(this.createAttribute("apikey", "name", "Matti Pulli", "string", false));
        memberObjAttrs.add(this.createAttribute("apikey", "age", "23", "int", false));
        RESTAttribute imgAttr=this.createAttribute("apikey", "image", "", "image", true);
        memberObjAttrs.add(imgAttr);
        memberObj.setAttributes(memberObjAttrs);
        this.restObjectRepository.save(memberObj);
        
        
        
        
        
        
        RESTClass imageClass=this.createClass("apikey", "image");
        imageClass.setIdRunner(2L);
        List<RESTAttributeDefinition> imgAttrDefs=new ArrayList<>();
        imgAttrDefs.add(this.createAttributeDef("apikey", "src", "string", false));
        imageClass.setAttributes(imgAttrDefs);
        this.restClassRepository.save(imageClass);        
                
        RESTObject imageObj=this.createObject(1L, "apikey", "image", null);
        List<RESTAttribute> imageObjAttrs=new ArrayList<RESTAttribute>();
        imageObjAttrs.add(this.createAttribute("apikey", "src", "http://sejase.com", "string", false));
        imageObj.setAttributes(imageObjAttrs);
        List<RESTObject> imgArr=new ArrayList<>();
        imgArr.add(imageObj);
        imgAttr.setListObjects(imgArr);    
        this.restObjectRepository.save(imageObj);
        
        this.restAttributeRepository.save(imgAttr);
        this.restObjectRepository.save(memberObj);
                */
    }

}
