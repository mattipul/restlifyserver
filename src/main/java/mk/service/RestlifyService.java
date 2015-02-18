package mk.service;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.transaction.Transactional;
import mk.domain.Member;
import mk.domain.RESTAttribute;
import mk.domain.RESTAttributeDefinition;
import mk.domain.RESTClass;
import mk.domain.RESTDatabase;
import mk.domain.RESTJoin;
import mk.domain.RESTObject;
import mk.repository.RESTAttributeRepository;
import mk.repository.RESTClassRepository;
import mk.repository.RESTDatabaseRepository;
import mk.repository.RESTJoinRepository;
import mk.repository.RESTObjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RestlifyService {

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

    public boolean isAuth(String apikey) {
        if (this.memberService.isAuthenticated()) {
            Member m = this.memberService.getAuthenticatedPerson();
            List<RESTDatabase> dbs = this.restDatabaseRepository.findByOwner(m.getUsername());
            if (dbs != null) {
                for (RESTDatabase db : dbs) {
                    if (db.getApiKey().equals(apikey)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private HashMap<String, Object> restObjectToJSON(RESTObject obj, String apikey) {
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

    public RESTObject objectCreate(String apikey,
            String classname,
            JsonObject jsobj,
            Long id,
            boolean createNew) {
        Gson gson = new Gson();
        RESTObject obj = null;
        RESTClass cls = this.restClassRepository.findByApiKeyAndClassName(apikey, classname);
        if (cls != null) {
            if (createNew) {
                obj = new RESTObject();
                obj.setClassID(cls.getIdRunner());
                cls.setIdRunner(cls.getIdRunner() + 1);
            } else {
                obj = this.restObjectRepository.findByApiKeyAndClassNameAndClassID(apikey, classname, id);
                if (obj.getAttributes() != null) {
                    this.restAttributeRepository.delete(obj.getAttributes());
                }

                //this.restObjectRepository.save(obj);
            }
            List<RESTJoin> objJoins = new ArrayList<>();
            List<RESTAttribute> attrs = new ArrayList<>();
            for (RESTAttributeDefinition attrDef : cls.getAttributes()) {
                RESTAttribute attr = new RESTAttribute();              
                if (jsobj.has(attrDef.getKey())) {
                    String attrType = attrDef.getType();

                    if (attrType.equals("int")
                            || attrType.equals("double")
                            || attrType.equals("boolean")
                            || attrType.equals("string")) {
                        if (attrDef.isList()) {
                            attr.setList(true);
                            JsonArray objArr = jsobj.get(attrDef.getKey()).getAsJsonArray();
                            attr.setKey(attrDef.getKey());
                            attr.setList(true);
                            attr.setType(attrType);
                            attr.setListPrimObjects(gson.toJson(objArr));
                        } else {
                            attr.setKey(attrDef.getKey());
                            attr.setType(attrType);
                            attr.setList(false);
                            attr.setValue(jsobj.get(attrDef.getKey()).getAsString());
                        }
                    } else {
                        if (attrDef.isList()) {
                            if (jsobj.get(attrDef.getKey()).isJsonArray()) {
                                JsonArray objArr = jsobj.get(attrDef.getKey()).getAsJsonArray();
                                attr.setKey(attrDef.getKey());
                                attr.setList(true);
                                attr.setType(attrType);
                                List<RESTObject> listArr = new ArrayList<>();
                                for (int i = 0; i < objArr.size(); i++) {
                                    if (objArr.get(i).isJsonObject()) {
                                        JsonObject linkObj = objArr.get(i).getAsJsonObject();
                                        RESTObject refObj = null;
                                        if (linkObj.has("id")) {
                                            refObj = this.refFromJSON(apikey, attrType, linkObj);
                                        } else {
                                            refObj = this.routeSave(apikey, attrType, linkObj);
                                        }
                                        if (refObj != null) {
                                            this.restObjectRepository.save(refObj);
                                            RESTJoin objJoin = new RESTJoin();
                                            objJoin.setParentObj(obj);
                                            objJoin.setAttr(attr);
                                            objJoin.setJoinedObj(refObj);
                                            objJoins.add(objJoin);
                                            listArr.add(refObj);
                                        }
                                    }
                                }
                                attr.setListObjects(listArr);
                            }
                        } else {
                            if (jsobj.get(attrDef.getKey()).isJsonObject()) {
                                JsonObject linkObj = jsobj.get(attrDef.getKey()).getAsJsonObject();
                                RESTObject refObj = null;
                                if (linkObj.has("id")) {
                                    refObj = this.refFromJSON(apikey, attrType, linkObj);
                                } else {
                                    refObj = this.routeSave(apikey, attrType, linkObj);
                                }
                                attr.setKey(attrDef.getKey());
                                attr.setType(attrType);
                                attr.setList(false);
                                if (refObj != null) {
                                    this.restObjectRepository.save(refObj);
                                    RESTJoin objJoin = new RESTJoin();
                                    objJoin.setAttr(attr);
                                    objJoin.setParentObj(obj);
                                    objJoin.setJoinedObj(refObj);
                                    objJoins.add(objJoin);
                                    attr.setJoinObj(refObj);
                                }
                            }
                        }
                    }
                    attrs.add(attr);
                    this.restAttributeRepository.save(attr);
                    
                }
            }
            obj.setAttributes(attrs);
            obj.setApiKey(apikey);
            obj.setClassName(classname);
            this.restObjectRepository.save(obj);
            this.restJoinRepository.save(objJoins);
            this.restClassRepository.save(cls);
        }

        return obj;
    }

    public RESTObject refFromJSON(String apikey, String classname, JsonElement jsobj) {
        if (jsobj.isJsonObject()) {
            JsonObject jsonObj = jsobj.getAsJsonObject();
            if (jsonObj.has("id")) {
                if (jsonObj.get("id").isJsonPrimitive()) {
                    Long id = jsonObj.get("id").getAsLong();
                    return this.restObjectRepository.findByApiKeyAndClassNameAndClassID(apikey, classname, id);
                }
            }
        }
        return null;
    }

    public RESTObject routeSave(String apikey, String classname, JsonElement jsobj) {
        RESTObject retObj = null;
        boolean hasID = jsobj.getAsJsonObject().has("id");
        if (hasID) {
            Long objID = jsobj.getAsJsonObject().get("id").getAsLong();
            RESTObject apiobj = this.restObjectRepository.findByApiKeyAndClassNameAndClassID(apikey, classname, objID);
            if (apiobj == null) {
                //this.objectCreate(apikey, classname, jsobj, -1L, true);
            } else {
                retObj = this.objectCreate(apikey, classname, jsobj.getAsJsonObject(), apiobj.getClassID(), false);
            }
        } else {
            retObj = this.objectCreate(apikey, classname, jsobj.getAsJsonObject(), -1L, true);
        }
        return retObj;
    }

    @Transactional
    public String save(String apikey, String classname, String body) {
        if (this.isAuth(apikey)) {
            Gson gson = new Gson();
            JsonElement jsobj = new JsonParser().parse(body);
            if (jsobj.isJsonObject()) {
                RESTObject retObj = this.routeSave(apikey, classname, jsobj);
                if(retObj!=null){
                    return this.get(apikey, classname, retObj.getClassID());
                }else{
                    return "{}";
                }
            } else if (jsobj.isJsonArray()) {
                JsonArray jsarr = jsobj.getAsJsonArray();
                String json = "";
                for (int i = 0; i < jsarr.size(); i++) {
                    JsonElement e = jsarr.get(i);
                    RESTObject retObj = this.routeSave(apikey, classname, e);
                    if(retObj!=null){
                        json += this.get(apikey, classname, retObj.getClassID());
                    }else{
                        json +="{}";
                    }
                    if (i < jsarr.size() - 1) {
                        json += ",";
                    }
                }
                return "[" + json + "]";
            }
            return "{}";
        } else {
            return "{\"success\":0}";
        }
    }

    @Transactional
    public String delete(String apikey, String classname, Long id) {
        if (this.isAuth(apikey)) {
            String json = "{}";
            RESTObject obj = this.restObjectRepository.findByApiKeyAndClassNameAndClassID(apikey, classname, id);
            if (obj != null) {
                Gson gson = new Gson();
                json = gson.toJson(restObjectToJSON(obj, apikey));
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
        } else {
            return "{\"success\":0}";
        }
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
