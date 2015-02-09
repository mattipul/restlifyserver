package mk.service;

import com.google.gson.Gson;
import mk.domain.RESTObject;
import mk.repository.RESTAttributeRepository;
import mk.repository.RESTClassRepository;
import mk.repository.RESTObjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class JoinService {
    
    @Autowired
    private RESTClassRepository restClassRepository;

    @Autowired
    private RESTObjectRepository restObjectRepository;

    @Autowired
    private RESTAttributeRepository restAttributeRepository;
    
    public String getJoinObject(String apikey, String classname, Long id){
        RESTObject obj=this.restObjectRepository.findByApiKeyAndClassNameAndClassID(apikey, classname, id);
        Gson gson=new Gson();
        return gson.toJson(gson);
    }
    
}
