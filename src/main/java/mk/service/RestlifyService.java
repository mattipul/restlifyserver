package mk.service;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import mk.domain.Member;
import mk.domain.RESTAttribute;
import mk.domain.RESTAttributeDefinition;
import mk.domain.RESTClass;
import mk.domain.RESTDatabase;
import mk.domain.RESTObject;
import mk.repository.RESTAttributeRepository;
import mk.repository.RESTClassRepository;
import mk.repository.RESTDatabaseRepository;
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
        for (int i = 0; i < obj.getAttributes().size(); i++) {
            RESTAttribute attr = obj.getAttributes().get(i);

            if (attr.getType().equals("int")
                    || attr.getType().equals("double")
                    || attr.getType().equals("string")
                    || attr.getType().equals("boolean")) {
                if (attr.isList()) {

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
                        jsonMap.put(attr.getKey(), "undefined");
                    }
                } else {
                    RESTObject classObj = attr.getJoinObj();
                    if (classObj != null) {
                        RESTObject listObj = attr.getJoinObj();
                        listObj = this.restObjectRepository.findByApiKeyAndClassNameAndClassID(listObj.getApiKey(), listObj.getClassName(), listObj.getClassID());
                        HashMap<String, Object> objMap = restObjectToJSON(listObj, apikey);
                        jsonMap.put(attr.getKey(), objMap);
                    } else {
                        jsonMap.put(attr.getKey(), "undefined");
                    }
                }
            }
            /*if (attr.getType().equals("int")
             || attr.getType().equals("double")
             || attr.getType().equals("string")
             || attr.getType().equals("boolean")) {
             jsonMap.put(attr.getKey(), attr.getValue());
             } else if (attr.getType().equals("list")) {
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
             jsonMap.put(attr.getKey(), "undefined");
             }
             } else {
             RESTObject classObj = attr.getJoinObj();
             if (classObj != null) {
             jsonMap.put(attr.getKey(), restObjectToJSON(classObj, apikey));
             } else {
             jsonMap.put(attr.getKey(), "undefined");
             }
             }*/
        }
        jsonMap.put("id", obj.getClassID());

        return jsonMap;
    }

    public String getAll(String apikey, String classname) {
        String json = "[";
        List<RESTObject> objects = this.restObjectRepository.findAll();
        List<RESTObject> apiobjs = new ArrayList<>();
        for (RESTObject obj : objects) {
            if (obj.getApiKey().equals(apikey) && obj.getClassName().equals(classname)) {
                apiobjs.add(obj);
            }
        }
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

    public String get(String apikey, String classname, Long id) {
        String json = "{}";
        List<RESTObject> objects = this.restObjectRepository.findAll();
        RESTObject apiobj = null;
        for (RESTObject obj : objects) {
            if (obj.getApiKey().equals(apikey) && obj.getClassName().equals(classname) && obj.getClassID() == id) {
                apiobj = obj;
            }
        }
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

    /*public RESTObject createObject(String apikey, String classname, Long id, HashMap<String, String> jsonMap) {
     RESTObject obj = new RESTObject();
     obj.setApiKey(apikey);
     obj.setClassName(classname);
     RESTClass cls = this.restClassRepository.findByApiKeyAndClassName(apikey, classname);
     if (id != -1L) {
     obj.setClassID(id);
     } else {
     obj.setClassID(cls.getIdRunner() + 1);
     cls.setIdRunner(cls.getIdRunner() + 1);
     }
     List<RESTAttribute> attrs = new ArrayList<>();
     for (String key : jsonMap.keySet()) {
     RESTAttribute a = new RESTAttribute();
     String attrType = this.getAttributeType(cls, key);
     if (!attrType.isEmpty()) {
     a.setKey(key);
     if(attrType.equals("int") ||
     attrType.equals("double") ||
     attrType.equals("boolean") ||
     attrType.equals("string")){
                    
     }else if(attrType.equals("list")){
                    
     }else{
                    
     }
                
     a.setValue(jsonMap.get(key));
     a.setType(attrType);
     this.restAttributeRepository.save(a);
     attrs.add(a);
     }
     }
     obj.setAttributes(attrs);
     this.restClassRepository.save(cls);
     this.restObjectRepository.save(obj);
     return obj;
     }

     public void saveObject(String apikey, String classname, Long id, HashMap<String, String> jsonMap, boolean newObject) {
     if (id == -1L && newObject == true) {
     this.createObject(apikey, classname, id, jsonMap);
     } else if (newObject == false) {
     RESTObject obj = this.restObjectRepository.findByApiKeyAndClassNameAndClassID(apikey, classname, id);
     RESTClass cls = this.restClassRepository.findByApiKeyAndClassName(apikey, classname);
     List<RESTAttribute> attrs = obj.getAttributes();
     for (String key : jsonMap.keySet()) {
     String attrType = this.getAttributeType(cls, key);
     if (!attrType.isEmpty()) {
     for (RESTAttribute a : attrs) {
     if (a.getKey().equals(key)) {
     a.setValue(jsonMap.get(key));
     a.setType(attrType);
     this.restAttributeRepository.save(a);
     }
     }
     }
     this.restObjectRepository.save(obj);
     }
     }
     }*/
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
            }
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
                            //JATKOA!
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
                                        RESTObject refObj = this.routeSave(apikey, attrType, objArr.get(i).getAsJsonObject());
                                        this.restObjectRepository.save(refObj);
                                        listArr.add(refObj);
                                    }
                                }
                                attr.setListObjects(listArr);
                            }
                        } else {
                            if (jsobj.get(attrDef.getKey()).isJsonObject()) {
                                RESTObject refObj = this.routeSave(apikey, attrType, jsobj.get(attrDef.getKey()).getAsJsonObject());
                                attr.setKey(attrDef.getKey());
                                attr.setType(attrType);
                                attr.setList(false);
                                attr.setJoinObj(refObj);
                            }
                        }
                    }

                    /*if (attrType.equals("int")
                     || attrType.equals("double")
                     || attrType.equals("boolean")
                     || attrType.equals("string")) {
                     attr.setKey(attrDef.getKey());
                     attr.setType(attrType);
                     attr.setValue(jsobj.get(attrDef.getKey()).getAsString());
                     } else if (attrDef.isList()) {
                     if (jsobj.get(attrDef.getKey()).isJsonArray()) {
                     JsonArray objArr = jsobj.get(attrDef.getKey()).getAsJsonArray();
                     attr.setKey(attrDef.getKey());
                     attr.setType(attrType);
                     List<RESTObject> listArr = new ArrayList<>();
                     for (int i = 0; i < objArr.size(); i++) {
                     if (objArr.get(i).isJsonObject()) {
                     RESTObject refObj = this.routeSave(apikey, classname, objArr.get(i).getAsJsonObject());
                     this.restObjectRepository.save(refObj);
                     listArr.add(refObj);
                     }
                     }
                     attr.setListObjects(listArr);
                     }
                     } else {
                     if (jsobj.get(attrDef.getKey()).isJsonObject()) {
                     RESTObject refObj = this.routeSave(apikey, classname, jsobj.get(attrDef.getKey()).getAsJsonObject());
                     attr.setKey(attrDef.getKey());
                     attr.setType(attrType);
                     attr.setJoinObj(refObj);
                     }
                     }*/
                    attrs.add(attr);
                    this.restAttributeRepository.save(attr);
                }
            }
            obj.setAttributes(attrs);
            obj.setApiKey(apikey);
            obj.setClassName(classname);
        }

        this.restObjectRepository.save(obj);

        this.restClassRepository.save(cls);
        return obj;
    }

    public RESTObject routeSave(String apikey, String classname, JsonElement jsobj) {
        RESTObject retObj = null;
        boolean hasID = jsobj.getAsJsonObject().has("id");
        if (hasID) {
            Long objID = jsobj.getAsJsonObject().get("id").getAsLong();
            List<RESTObject> objects = this.restObjectRepository.findAll();
            RESTObject apiobj = null;
            for (RESTObject obj : objects) {
                if (obj.getApiKey().equals(apikey) && obj.getClassName().equals(classname) && obj.getClassID() == objID) {
                    apiobj = obj;
                }
            }
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

    public String save(String apikey, String classname, String body) {
        if (this.isAuth(apikey)) {
            String json = body;
            Gson gson = new Gson();
            //java.lang.reflect.Type stringStringMap = new TypeToken<Map<String, String>>() {
            //}.getType();
            //HashMap<String, String> jsonMap = gson.fromJson(body, stringStringMap);
            JsonElement jsobj = new JsonParser().parse(json);
            if(jsobj.isJsonObject()){
                this.routeSave(apikey, classname, jsobj);
            }
            else if(jsobj.isJsonArray()){
                JsonArray jsarr=jsobj.getAsJsonArray();
                for(JsonElement e:jsarr){
                    this.routeSave(apikey, classname, e);
                }
            }
            return json;
        } else {
            return "FAIL";
        }
    }

    public String delete(String apikey, String classname, Long id) {
        if (this.isAuth(apikey)) {
            String json = "{}";
            RESTObject obj = this.restObjectRepository.findByApiKeyAndClassNameAndClassID(apikey, classname, id);
            if (obj != null) {
                Gson gson = new Gson();
                json = gson.toJson(restObjectToJSON(obj, apikey));
                /* if(obj.getAttributes()!=null){
                 for(RESTAttribute attr:obj.getAttributes()){
                 attr.setJoinObj(null);
                 attr.setListObjects(null);
                 this.restAttributeRepository.save(attr);
                 this.restAttributeRepository.delete(attr);
                 }
                 }
                 obj.setAttributes(null);
                 this.restObjectRepository.save(obj);*/
                this.restObjectRepository.delete(obj);
            }
            return json;
        } else {
            return "FAIL";
        }
    }

}
