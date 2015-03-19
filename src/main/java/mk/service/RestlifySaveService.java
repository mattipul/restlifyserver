package mk.service;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.util.ArrayList;
import java.util.List;
import javax.transaction.Transactional;
import mk.domain.RESTAttribute;
import mk.domain.RESTAttributeDefinition;
import mk.domain.RESTClass;
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
public class RestlifySaveService {

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

    public RESTJoin createJoin(RESTAttribute attr,
            RESTObject obj,
            RESTObject refObj) {
        RESTJoin objJoin = new RESTJoin();
        objJoin.setAttr(attr);
        objJoin.setParentObj(obj);
        objJoin.setJoinedObj(refObj);
        return objJoin;
    }

    public RESTAttribute createAttributeObject(String apiKey, RESTAttributeDefinition def, JsonArray objArr) {
        RESTAttribute attr = new RESTAttribute();
        attr.setKey(def.getKey());
        attr.setList(true);
        attr.setType(def.getType());
        List<RESTObject> listArr = new ArrayList<>();
        for (int i = 0; i < objArr.size(); i++) {
            if (objArr.get(i).isJsonObject()) {
                JsonObject linkObj = objArr.get(i).getAsJsonObject();
                RESTObject refObj = null;
                if (linkObj.has("id")) {
                    refObj = this.refFromJSON(apiKey, def.getType(), linkObj);
                } else {
                    refObj = this.routeSave(apiKey, def.getType(), linkObj);
                }
                if (refObj != null) {
                    this.restObjectRepository.save(refObj);
                    listArr.add(refObj);
                }
            }
        }
        attr.setListObjects(listArr);
        return attr;
    }

    public RESTAttribute createAttributeObject(String apiKey, RESTAttributeDefinition def, JsonObject linkObj) {
        RESTAttribute attr = new RESTAttribute();
        RESTObject refObj = null;
        if (linkObj.has("id")) {
            refObj = this.refFromJSON(apiKey, def.getType(), linkObj);
        } else {
            refObj = this.routeSave(apiKey, def.getType(), linkObj);
        }
        attr.setKey(def.getKey());
        attr.setType(def.getType());
        attr.setList(false);
        if (refObj != null) {
            this.restObjectRepository.save(refObj);
            attr.setJoinObj(refObj);
        }
        return attr;
    }

    public RESTAttribute createAttributeList(RESTAttributeDefinition def, String value) {
        RESTAttribute attr = new RESTAttribute();
        attr.setList(true);
        attr.setKey(def.getKey());
        attr.setList(true);
        attr.setType(def.getType());
        attr.setListPrimObjects(value);
        return attr;
    }

    public RESTAttribute createAttributePrimitive(RESTAttributeDefinition def, String value) {
        RESTAttribute attr = new RESTAttribute();
        attr.setKey(def.getKey());
        attr.setType(def.getType());
        attr.setList(false);
        attr.setValue(value);
        return attr;
    }

    public RESTObject createObject(String apikey,
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
            }

            List<RESTJoin> objJoins = new ArrayList<>();
            List<RESTAttribute> attrs = new ArrayList<>();
            for (RESTAttributeDefinition attrDef : cls.getAttributes()) {
                RESTAttribute attr = new RESTAttribute();
                String attrType = attrDef.getType();
                if (jsobj.has(attrDef.getKey())) {
                    if (attrType.equals("int")
                            || attrType.equals("double")
                            || attrType.equals("boolean")
                            || attrType.equals("string")) {
                        if (attrDef.isList()) {
                            JsonArray objArr = jsobj.get(attrDef.getKey()).getAsJsonArray();
                            attr = this.createAttributeList(attrDef, gson.toJson(objArr));
                        } else {
                            attr = this.createAttributePrimitive(attrDef, jsobj.get(attrDef.getKey()).getAsString());
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

    public RESTObject routeSave(String apikey, String classname, JsonElement jsobj) {
        RESTObject retObj = null;
        boolean hasID = jsobj.getAsJsonObject().has("id");
        if (hasID) {
            Long objID = jsobj.getAsJsonObject().get("id").getAsLong();
            RESTObject apiobj = this.restObjectRepository.findByApiKeyAndClassNameAndClassID(apikey, classname, objID);
            if (apiobj == null) {
                //this.objectCreate(apikey, classname, jsobj, -1L, true);
            } else {
                retObj = this.createObject(apikey, classname, jsobj.getAsJsonObject(), apiobj.getClassID(), false);
            }
        } else {
            retObj = this.createObject(apikey, classname, jsobj.getAsJsonObject(), -1L, true);
        }
        return retObj;
    }

    @Transactional
    public String save(String apiKey, String className, String json) {
        if (this.validatorService.isAuth(apiKey)) {
            if (this.restClassRepository.findByApiKeyAndClassName(apiKey, className) != null) {
                Gson gson = new Gson();
                JsonElement jsonElement = new JsonParser().parse(json);
                if (jsonElement.isJsonObject()) {
                    RESTObject restObject = this.routeSave(apiKey, className, jsonElement);
                } else if (jsonElement.isJsonArray()) {

                }
            }
        }
        return "{\"errorCode\":0}";
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

}
