package mk.service;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import javax.transaction.Transactional;
import mk.domain.AttributeJSON;
import mk.domain.ClassJSON;
import mk.domain.Member;
import mk.domain.RESTAttributeDefinition;
import mk.domain.RESTClass;
import mk.domain.RESTDatabase;
import mk.domain.RESTObject;
import mk.repository.RESTAttributeDefinitionRepository;
import mk.repository.RESTAttributeRepository;
import mk.repository.RESTClassRepository;
import mk.repository.RESTDatabaseRepository;
import mk.repository.RESTObjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DatabaseService {

    @Autowired
    private MemberService memberService;

    @Autowired
    private RestlifyService restlifyService;

    @Autowired
    private RESTObjectRepository restObjectRepository;

    @Autowired
    private RESTAttributeRepository restAttributeRepository;

    @Autowired
    private RESTClassRepository restClassRepository;

    @Autowired
    private RESTAttributeDefinitionRepository restAttributeDefinitionRepository;

    @Autowired
    private RESTDatabaseRepository restDatabaseRepository;

    public boolean valid(String str) {
        String valids = "1234567890qwertyuiopasdfghjklzxcvbnmQWERTYUIOPASDFGHJKLZXCVBNM_";
        if(str.length()==0){
            return false;
        }
        for (int i = 0; i < str.length(); i++) {
            if (!valids.contains("" + str.charAt(i))) {
                return false;
            }
        }
        return true;
    }

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

    @Transactional
    public String destroyClass(String apikey, String classname) {
        if (this.isAuth(apikey)) {
            RESTClass delClass = this.restClassRepository.findByApiKeyAndClassName(apikey, classname);
            if (delClass != null) {
                for (RESTAttributeDefinition attrDef : delClass.getAttributes()) {
                    this.restAttributeDefinitionRepository.delete(attrDef);
                }
                RESTDatabase rdb = this.restDatabaseRepository.findByApiKey(apikey);
                List<RESTClass> clss = rdb.getClasses();
                clss.remove(delClass);
                rdb.setClasses(clss);
                List<RESTObject> objs = this.restObjectRepository.findByApiKeyAndClassName(apikey, classname);
                for (RESTObject obj : objs) {
                    this.restlifyService.delete(obj.getApiKey(), obj.getClassName(), obj.getClassID());
                }
                this.restClassRepository.delete(delClass);
                this.restDatabaseRepository.save(rdb);
                return "{\"success\":1}";
            }
            return "{\"success\":0}";
        } else {
            return "{\"success\":0}";
        }
    }

    @Transactional
    public String define(String apikey, String classJSON) {
        String json = "{\"success\":0}";
        if (this.restDatabaseRepository.findByApiKey(apikey) != null) {
            JsonElement jsobj = new JsonParser().parse(classJSON);
            Gson gson = new Gson();
            if (jsobj.isJsonArray()) {
                JsonArray arr = jsobj.getAsJsonArray();
                String jsonArr = "";
                for (int i = 0; i < arr.size(); i++) {   
                    RESTClass jsCls=this.defineClass(apikey, arr.get(i));
                    if(jsCls!=null){
                        jsonArr += this.classToJSON(jsCls);
                    }else{
                        return "{\"success\":0}";
                    }
                    if (i < arr.size() - 1) {
                        jsonArr += ",";
                    }
                }
                return "[" + jsonArr + "]";
            }
            if (jsobj.isJsonObject()) {
                RESTClass jsCls=this.defineClass(apikey, jsobj.getAsJsonObject());
                if(jsCls!=null){
                    return this.classToJSON(jsCls);
                }else{
                    return "{\"success\":0}";
                }
            }
        }
        return json;
    }

    public RESTClass defineClass(String apikey, JsonElement jsonEl) {
        if (this.isAuth(apikey)) {
            if (this.restDatabaseRepository.findByApiKey(apikey) != null) {
                JsonElement jsobj = jsonEl;
                if (jsobj.isJsonObject()) {
                    JsonObject classObj = jsobj.getAsJsonObject();
                    if (classObj.has("name") && classObj.has("attributes")) {
                        if (classObj.get("name").isJsonPrimitive() && classObj.get("attributes").isJsonArray()) {
                            String className = classObj.get("name").getAsString().toLowerCase();
                            if (this.restObjectRepository.findByApiKeyAndClassName(apikey, className).size()==0) {
                                if (this.valid(className)) {
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
                                                    if (this.valid(attrKey) && this.valid(attrType) && !attrKey.equals("id")) {
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
                                    }
                                    restClass.setAttributes(attrDefs);
                                    this.restClassRepository.save(restClass);
                                    RESTDatabase restDB = this.restDatabaseRepository.findByApiKey(apikey);
                                    List<RESTClass> classArr = new ArrayList<>();
                                    classArr.addAll(restDB.getClasses());
                                    classArr.add(restClass);
                                    restDB.setClasses(classArr);
                                    this.restDatabaseRepository.save(restDB);
                                    return restClass;
                                } else {
                                    return null;
                                }
                            } else {
                                return null;
                            }
                        }
                    }
                }
            }
        }
        return null;
    }

    @Transactional
    public String createDatabase(String dbJSON) {
        if (this.memberService.isAuthenticated()) {
            while (true) {
                String apikey = UUID.randomUUID().toString().substring(0, 5);
                if (this.restDatabaseRepository.findByApiKey(apikey) == null) {
                    JsonElement jsel = new JsonParser().parse(dbJSON);
                    if(jsel.isJsonObject()){
                        JsonObject jsonObj=jsel.getAsJsonObject();
                        if(jsonObj.has("name") && jsonObj.has("description")){
                            String name=jsonObj.get("name").getAsString();
                            String desc=jsonObj.get("description").getAsString();
                            RESTDatabase restDatabase = new RESTDatabase();
                            restDatabase.setApiKey(apikey);
                            restDatabase.setName(name);
                            restDatabase.setDescription(desc);
                            Member m = this.memberService.getAuthenticatedPerson();
                            restDatabase.setOwner(m.getUsername());
                            this.restDatabaseRepository.save(restDatabase);
                            return "{\"apiKey\":\"" + apikey + "\"}";
                        }else{
                            break;
                        }
                    }else{
                        break;
                    }
                }
            }
        } 
        return "{\"success\":0}";     
    }

    public String classToJSON(RESTClass cls) {
        String json = "{\"success:0\"}";
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

    @Transactional
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
            return "{\"success\":0}";
        }
    }

    @Transactional
    public String getClass(String apikey, String classname) {
        if (this.isAuth(apikey)) {
            String json = "{}";
            RESTClass cls = this.restClassRepository.findByApiKeyAndClassName(apikey, classname);
            if (cls != null) {
                json = this.classToJSON(cls);
            }
            return json;
        }
        return "{\"success\":0}";
    }

    @Transactional
    public String getDatabases() {
        Member m = this.memberService.getAuthenticatedPerson();
        List<RESTDatabase> dbs = this.restDatabaseRepository.findByOwner(m.getUsername());
        if (dbs != null) {
            List<String> keys = new ArrayList<>();
            for (RESTDatabase db : dbs) {
                keys.add(db.getApiKey());
            }
            Gson gson = new Gson();
            return gson.toJson(keys);
        }
        return "[]";
    }

    public String dropDatabase(String apikey) {
        /* if(this.isAuth(apikey)){
         RESTDatabase restDB=this.restDatabaseRepository.findByApiKey(apikey);
         this.restDatabaseRepository.delete(restDB);
         return this.getDatabases();
         }else{
         return "FAIL";
         }*/
        return "{\"success\":0}";
    }

    @Transactional
    public String getDatabaseStructure(String apikey) {
        String json = "";
        RESTDatabase restDB = this.restDatabaseRepository.findByApiKey(apikey);
        if (restDB != null) {
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
        }
        return "[" + json + "]";
    }

    public RESTDatabase getDatabaseStructureList(String apikey) {
        RESTDatabase restDB = this.restDatabaseRepository.findByApiKey(apikey);
        return restDB;
    }

}
