package mk.service;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import mk.domain.AttributeJSON;
import mk.domain.ClassJSON;
import mk.domain.Member;
import mk.domain.RESTAttributeDefinition;
import mk.domain.RESTClass;
import mk.domain.RESTDatabase;
import mk.repository.RESTAttributeDefinitionRepository;
import mk.repository.RESTClassRepository;
import mk.repository.RESTDatabaseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DatabaseService {
    
    @Autowired
    private MemberService memberService;
    
    @Autowired
    private RESTClassRepository restClassRepository;
    
    @Autowired
    private RESTAttributeDefinitionRepository restAttributeDefinitionRepository;
    
    @Autowired
    private RESTDatabaseRepository restDatabaseRepository;
    
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
    
    public int destroyClass(String apikey, String classname) {
        if (this.isAuth(apikey)) {
            RESTClass delClass = this.restClassRepository.findByApiKeyAndClassName(apikey, classname);
            if (delClass != null) {
                for (RESTAttributeDefinition attrDef : delClass.getAttributes()) {
                    this.restAttributeDefinitionRepository.delete(attrDef);
                }               
                RESTDatabase rdb=this.restDatabaseRepository.findByApiKey(apikey);
                List<RESTClass> clss=rdb.getClasses();
                clss.remove(delClass);
                rdb.setClasses(clss);
                this.restClassRepository.delete(delClass);
                this.restDatabaseRepository.save(rdb);
            }
            return 1;
        } else {
            return 0;
        }
    }
    
    public String defineClass(String apikey, String classJSON) {
        if (this.isAuth(apikey)) {
            if (this.restDatabaseRepository.findByApiKey(apikey) != null) {
                JsonElement jsobj = new JsonParser().parse(classJSON);
                if (jsobj.isJsonObject()) {
                    JsonObject classObj = jsobj.getAsJsonObject();
                    if (classObj.has("name") && classObj.has("attributes")) {
                        if (classObj.get("name").isJsonPrimitive() && classObj.get("attributes").isJsonArray()) {
                            String className = classObj.get("name").getAsString().toLowerCase();
                            JsonArray attrArr = classObj.get("attributes").getAsJsonArray();
                            this.destroyClass(apikey, className);
                            RESTClass restClass = new RESTClass();
                            restClass.setApiKey(apikey);
                            restClass.setIdRunner(1L);
                            restClass.setClassName(className);
                            List<RESTAttributeDefinition> attrDefs = new ArrayList<>();
                            for (JsonElement attrElement : attrArr) {
                                if (attrElement.isJsonObject()) {
                                    JsonObject attrObj = attrElement.getAsJsonObject();
                                    if (attrObj.has("key") && attrObj.has("type") && attrObj.has("list")) {
                                        if (attrObj.get("key").isJsonPrimitive()
                                                && attrObj.get("type").isJsonPrimitive()
                                                && attrObj.get("list").isJsonPrimitive()) {
                                            String attrKey = attrObj.get("key").getAsString().toLowerCase();
                                            String attrType = attrObj.get("type").getAsString().toLowerCase();
                                            boolean attrList = attrObj.get("list").getAsBoolean();
                                            RESTAttributeDefinition attrDef = new RESTAttributeDefinition();
                                            attrDef.setKey(attrKey);
                                            attrDef.setType(attrType);
                                            attrDef.setList(attrList);
                                            this.restAttributeDefinitionRepository.save(attrDef);
                                            attrDefs.add(attrDef);
                                        }
                                    }
                                }
                            }
                            restClass.setAttributes(attrDefs);
                            this.restClassRepository.save(restClass);
                            RESTDatabase restDB = this.restDatabaseRepository.findByApiKey(apikey);
                            List<RESTClass> classArr = new ArrayList<>();
                            classArr.addAll(restDB.getClasses());
                            classArr.add(restClass);
                            restDB.setClasses(classArr);
                            this.restDatabaseRepository.save(restDB);
                            
                        }
                    }
                }
            }
            return classJSON;
        } else {
            return "FAIL";
        }
    }
    
    public String createDatabase() {
        if (this.memberService.isAuthenticated()) {
            String apikey = UUID.randomUUID().toString();
            if (this.restDatabaseRepository.findByApiKey(apikey) == null) {
                RESTDatabase restDatabase = new RESTDatabase();
                restDatabase.setApiKey(apikey);
                Member m=this.memberService.getAuthenticatedPerson();
                restDatabase.setOwner(m.getUsername());
                this.restDatabaseRepository.save(restDatabase);
            } else {
                return "FAIL";
            }
            return apikey;
        } else {
            return "FAIL";
        }
    }
    
    public String classToJSON(RESTClass cls) {
        String json = "{}";
        if (cls != null) {
            Gson gson = new Gson();
            ClassJSON classJSON = new ClassJSON();
            classJSON.setName(cls.getClassName());
            List<AttributeJSON> attrs = new ArrayList<>();
            for (RESTAttributeDefinition def : cls.getAttributes()) {
                AttributeJSON attrJSON = new AttributeJSON();
                attrJSON.setKey(def.getKey());
                attrJSON.setType(def.getType());
                attrJSON.setList(def.isList());
                attrs.add(attrJSON);
            }
            classJSON.setAttributes(attrs);
            json = gson.toJson(classJSON, ClassJSON.class);
        }
        return json;
    }
    
    public String getAllClasses(String apikey) {
        if (this.isAuth(apikey)) {
            String json = "";
            RESTDatabase restDB = this.restDatabaseRepository.findByApiKey(apikey);
            List<RESTClass> classes = restDB.getClasses();
            if (classes != null) {
                for (int i = 0; i < classes.size(); i++) {
                    RESTClass cls = classes.get(i);
                    json += this.classToJSON(cls);
                    if (i < classes.size() - 1) {
                        json += ",";
                    }
                }
            }
            return "[" + json + "]";
        } else {
            return "FAIL";
        }
    }
    
    public String getClass(String apikey, String classname) {
        if (this.isAuth(apikey)) {
            String json = "{}";
            RESTClass cls = this.restClassRepository.findByApiKeyAndClassName(apikey, classname);
            if (cls != null) {
                json = this.classToJSON(cls);
            }
            return json;
        }
        return "FAIL";
    }
    
    public String getDatabases() {
        Member m = this.memberService.getAuthenticatedPerson();
        List<RESTDatabase> dbs = this.restDatabaseRepository.findByOwner(m.getUsername());
        if (dbs != null) {
            List<String> keys=new ArrayList<>();
            for(RESTDatabase db:dbs){
                keys.add(db.getApiKey());
            }
            Gson gson = new Gson();
            return gson.toJson(keys);
        }
        return "[]";
    }
    
}
