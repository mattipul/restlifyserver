package mk.service;

import com.google.gson.Gson;
import java.util.List;
import mk.domain.RESTAttribute;
import mk.domain.RESTJoin;
import mk.domain.RESTObject;
import mk.repository.RESTAttributeRepository;
import mk.repository.RESTClassRepository;
import mk.repository.RESTDatabaseRepository;
import mk.repository.RESTJoinRepository;
import mk.repository.RESTObjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import static org.springframework.data.jpa.domain.AbstractPersistable_.id;
import org.springframework.stereotype.Service;

@Service
public class RestlifyDeleteService {
    
    @Autowired
    private MemberService memberService;

    @Autowired
    private RESTDatabaseRepository restDatabaseRepository;

    @Autowired
    private RESTClassRepository restClassRepository;

    @Autowired
    private RESTObjectRepository restObjectRepository;

    @Autowired
    private RESTAttributeRepository restAttributeRepository;

    @Autowired
    private RESTJoinRepository restJoinRepository;

    @Autowired
    private ValidatorService validatorService;
    
    @Autowired
    private RestlifyGetService getService;

    public String delete(String apiKey, String className, Long classID) {
        if(this.validatorService.isAuth(apiKey)){
            String json = "{}";
            RESTObject obj = this.restObjectRepository.findByApiKeyAndClassNameAndClassID(apiKey, className, classID);
            if (obj != null) {
                Gson gson = new Gson();
                json = gson.toJson(this.getService.restObjectToJSON(obj, apiKey));
                List<RESTJoin> joins = this.restJoinRepository.findByJoinedObj(obj);
                for (RESTJoin join : joins) {
                    RESTAttribute attr = join.getAttr();
                    if (attr.getListObjects() != null) {
                        List<RESTObject> joinedObjects = attr.getListObjects();
                        joinedObjects.remove(obj);
                        attr.setListObjects(joinedObjects);
                    }
                    attr.setJoinObj(null);
                    this.restAttributeRepository.save(attr);
                    this.restJoinRepository.save(join);
                }
                List<RESTJoin> joinsPr = this.restJoinRepository.findByParentObj(obj);
                this.restJoinRepository.delete(joinsPr);
                this.restJoinRepository.delete(joins);
                this.restObjectRepository.delete(obj);
            }
            return json;
        }
        return "{\"errorCode\":0}";
    }
    
}
