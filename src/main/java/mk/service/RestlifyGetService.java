package mk.service;

import com.google.gson.Gson;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.transaction.Transactional;
import mk.domain.RESTAttribute;
import mk.domain.RESTAttributeDefinition;
import mk.domain.RESTClass;
import mk.domain.RESTObject;
import mk.repository.RESTAttributeRepository;
import mk.repository.RESTClassRepository;
import mk.repository.RESTDatabaseRepository;
import mk.repository.RESTJoinRepository;
import mk.repository.RESTObjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RestlifyGetService {
    
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
    
    //return "{\"errorCode\":0}";
    
    public HashMap<String, Object> restObjectToJSON(RESTObject obj, String apikey) {
        String json = "{}";
        obj = this.restObjectRepository.findByApiKeyAndClassNameAndClassID(obj.getApiKey(), obj.getClassName(), obj.getClassID());
        HashMap<String, Object> jsonMap = new HashMap<>();
        if (obj != null) {
            for (int i = 0; i < obj.getAttributes().size(); i++) {
                RESTAttribute attr = obj.getAttributes().get(i);

                if (attr.getType().equals("int")
                        || attr.getType().equals("double")
                        || attr.getType().equals("string")
                        || attr.getType().equals("boolean")) {
                    if (attr.isList()) {
                        String listObjects = attr.getListPrimObjects();
                        if (listObjects != null) {
                            Gson gson = new Gson();
                            jsonMap.put(attr.getKey(), gson.fromJson(listObjects, Object[].class));
                        } else {
                            //jsonMap.put(attr.getKey(), "undefined");
                        }
                    } else {
                        jsonMap.put(attr.getKey(), attr.getValue());
                    }
                } else {
                    if (attr.isList()) {
                        List<RESTObject> listObjects = attr.getListObjects();
                        List<HashMap<String, Object>> listMaps = new ArrayList<>();
                        if (listObjects != null) {
                            for (int k = 0; k < listObjects.size(); k++) {
                                RESTObject listObj = listObjects.get(k);
                                listObj = this.restObjectRepository.findByApiKeyAndClassNameAndClassID(listObj.getApiKey(), listObj.getClassName(), listObj.getClassID());
                                HashMap<String, Object> objMap = restObjectToJSON(listObj, apikey);
                                listMaps.add(objMap);
                            }
                            jsonMap.put(attr.getKey(), listMaps);
                        } else {
                            //jsonMap.put(attr.getKey(), null);
                        }
                    } else {
                        RESTObject classObj = attr.getJoinObj();
                        if (classObj != null) {
                            RESTObject listObj = attr.getJoinObj();
                            listObj = this.restObjectRepository.findByApiKeyAndClassNameAndClassID(listObj.getApiKey(), listObj.getClassName(), listObj.getClassID());
                            HashMap<String, Object> objMap = restObjectToJSON(listObj, apikey);
                            jsonMap.put(attr.getKey(), objMap);
                        } else {
                            //jsonMap.put(attr.getKey(), null);
                        }
                    }
                }

            }
            jsonMap.put(
                "id", obj.getClassID());
        }
        

        return jsonMap;
    }

    @Transactional
    public String getAll(String apikey, String classname, Map<String, String> params) {
        String json = "[";
        List<RESTObject> apiobjs = this.search(apikey, classname, params);
        String jsonTest=(new Gson()).toJson(apiobjs);
        for (int i = 0; i < apiobjs.size(); i++) {
            RESTObject obj = apiobjs.get(i);
            Gson gson = new Gson();
            HashMap<String, Object> objMap = this.restObjectToJSON(obj, apikey);
            if (!objMap.isEmpty()) {
                json += gson.toJson(objMap);
            }
            if (i < apiobjs.size() - 1) {
                json += ",";
            }
        }
        json += "]";
        return json;
    }

    @Transactional
    public String get(String apikey, String classname, Long id) {
        String json = "{}";       
        RESTObject apiobj = this.restObjectRepository.findByApiKeyAndClassNameAndClassID(apikey, classname, id);
        String jsonTest=(new Gson()).toJson(apiobj);
        if (apiobj != null) {
            Gson gson = new Gson();
            HashMap<String, Object> objMap = this.restObjectToJSON(apiobj, apikey);
            if (!objMap.isEmpty()) {
                json = gson.toJson(objMap);
            }
        }
        return json;
    }

    public String getAttributeType(RESTClass cls, String key) {
        String type = "";
        for (RESTAttributeDefinition def : cls.getAttributes()) {
            if (def.getKey().equals(key)) {
                type = def.getType();
            }
        }
        return type;
    }
    
    public List<RESTObject> search(String apikey, String classname, Map<String, String> parameters) {
        List<RESTObject> objs = this.restObjectRepository.findByApiKeyAndClassName(apikey, classname);
        if (objs != null) {
            List<RESTObject> retObjs = new ArrayList<>();
            if (!parameters.isEmpty()) {
                for (RESTObject o : objs) {
                    int c = 0;
                    for (RESTAttribute attr : o.getAttributes()) {
                        if (parameters.containsKey(attr.getKey())) {
                            String attrVal = "" + attr.getValue();
                            if (attrVal.contains(parameters.get(attr.getKey()))) {
                                c++;
                            }
                        }
                    }
                    if (c == parameters.size()) {
                        retObjs.add(o);
                    }
                }
                return retObjs;
            } else {
                return objs;
            }

        }
        return (new ArrayList<>());
    }
    
}
