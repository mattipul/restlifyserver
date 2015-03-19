package mk.service;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.util.ArrayList;
import java.util.HashMap;
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
    private RESTDatabaseRepository restDatabaseRepository;
    
    @Autowired
    private RestlifyDeleteService deleteService;

    @Autowired
    private MemberService memberService;

    @Autowired
    private ValidatorService validatorService;

    @Autowired
    private RESTObjectRepository restObjectRepository;

    @Autowired
    private RESTAttributeRepository restAttributeRepository;

    @Autowired
    private RESTClassRepository restClassRepository;

    @Autowired
    private RESTAttributeDefinitionRepository restAttributeDefinitionRepository;

    @Transactional
    public String getDatabases() {
        if (this.validatorService.isAuthenticated()) {
            Member m = this.memberService.getAuthenticatedPerson();
            List<RESTDatabase> dbs = this.restDatabaseRepository.findByOwner(m.getUsername());
            if (dbs != null) {
                List<HashMap<String, String>> keys = new ArrayList<>();
                for (RESTDatabase db : dbs) {
                    HashMap<String, String> dbkey = new HashMap<>();
                    dbkey.put("apiKey", db.getApiKey());
                    dbkey.put("name", db.getName());
                    dbkey.put("description", db.getDescription());
                    keys.add(dbkey);
                }
                Gson gson = new Gson();
                return gson.toJson(keys);
            }
            return "[]";
        }
        return "{\"errorCode\":0}";
    }

    @Transactional
    public String createDatabase(String json) {
        if (this.validatorService.isAuthenticated()) {
            JsonElement jsonElement = new JsonParser().parse(json);
            if (jsonElement.isJsonObject()) {
                JsonObject jsonObj = jsonElement.getAsJsonObject();
                if (jsonObj.has("name") && jsonObj.has("description")) {
                    String name = jsonObj.get("name").getAsString();
                    String desc = jsonObj.get("description").getAsString();
                    while (true) {
                        String apikey = UUID.randomUUID().toString().substring(0, 5);
                        if (this.restDatabaseRepository.findByApiKey(apikey) == null) {
                            RESTDatabase restDatabase = new RESTDatabase();
                            restDatabase.setApiKey(apikey);
                            restDatabase.setName(name);
                            restDatabase.setDescription(desc);
                            Member m = this.memberService.getAuthenticatedPerson();
                            restDatabase.setOwner(m.getUsername());
                            this.restDatabaseRepository.save(restDatabase);
                            return "{\"apiKey\":\"" + apikey + "\"}";
                        }
                    }
                }
            }
        }
        return "{\"errorCode\":0}";
    }

    @Transactional
    public String editDatabase(String apiKey, String json) {
        if (this.validatorService.isAuthenticated()) {
            RESTDatabase restDatabase = this.restDatabaseRepository.findByApiKey(apiKey);
            if (restDatabase != null) {
                JsonElement jsonElement = new JsonParser().parse(json);
                if (jsonElement.isJsonObject()) {
                    JsonObject jsonObj = jsonElement.getAsJsonObject();
                    if (jsonObj.has("name") && jsonObj.has("description")) {
                        String name = jsonObj.get("name").getAsString();
                        String desc = jsonObj.get("description").getAsString();
                        restDatabase.setName(name);
                        restDatabase.setDescription(desc);
                        this.restDatabaseRepository.save(restDatabase);
                    }
                }
            }
        }
        return "{\"errorCode\":0}";
    }

    public List<RESTAttributeDefinition> defineAttributes(JsonArray attributes) {
        List<RESTAttributeDefinition> definitions = new ArrayList<>();
        for (JsonElement attrElement : attributes) {
            if (attrElement.isJsonObject()) {
                JsonObject attrObj = attrElement.getAsJsonObject();
                if (attrObj.has("key") && attrObj.has("type") && attrObj.has("list")) {
                    if (attrObj.get("key").isJsonPrimitive()
                            && attrObj.get("type").isJsonPrimitive()
                            && attrObj.get("list").isJsonPrimitive()) {
                        String attrKey = attrObj.get("key").getAsString().toLowerCase();
                        String attrType = attrObj.get("type").getAsString().toLowerCase();
                        boolean attrList = attrObj.get("list").getAsBoolean();
                        if (this.validatorService.valid(attrKey) && this.validatorService.valid(attrType) && !attrKey.equals("id")) {
                            RESTAttributeDefinition attrDef = new RESTAttributeDefinition();
                            attrDef.setKey(attrKey);
                            attrDef.setType(attrType);
                            attrDef.setList(attrList);
                            this.restAttributeDefinitionRepository.save(attrDef);
                            definitions.add(attrDef);
                        }
                    }
                }
            }
        }
        return definitions;
    }

    public RESTClass defineClass(String apiKey, JsonElement jsonElement) {
        RESTClass restClass = new RESTClass();
        if (jsonElement.isJsonObject()) {
            JsonObject jsonObject = jsonElement.getAsJsonObject();
            if (jsonObject.has("name") && jsonObject.has("attributes")) {
                if (jsonObject.get("name").isJsonPrimitive()
                        && jsonObject.get("attributes").isJsonArray()) {
                    String name = jsonObject.get("name").getAsString();
                    JsonArray attributes = jsonObject.get("attributes").getAsJsonArray();
                    this.deleteClass(apiKey, name);
                    restClass.setIdRunner(1L);
                    restClass.setClassName(name);
                    restClass.setApiKey(apiKey);
                    restClass.setAttributes(this.defineAttributes(attributes));
                    this.restClassRepository.save(restClass);
                    RESTDatabase restDB = this.restDatabaseRepository.findByApiKey(apiKey);
                    List<RESTClass> classArr = new ArrayList<>();
                    classArr.addAll(restDB.getClasses());
                    classArr.add(restClass);
                    restDB.setClasses(classArr);
                    this.restDatabaseRepository.save(restDB);
                }
            }
        }
        return restClass;
    }

    @Transactional
    public String defineClasses(String apiKey, String json) {
        if (this.validatorService.isAuthenticated()) {
            JsonElement jsonElement = new JsonParser().parse(json);
            if (jsonElement.isJsonArray()) {
                JsonArray jsonArray = jsonElement.getAsJsonArray();
                List<RESTClass> restClasses = new ArrayList<>();
                boolean errors = false;
                for (JsonElement e : jsonArray) {
                    RESTClass restClass = this.defineClass(apiKey, e);
                    if (restClass != null) {
                        restClasses.add(restClass);
                    } else {
                        errors = true;
                    }
                }
                if (errors == false) {
                    return "{\"success\":1}";
                } else {
                    return "{\"errorCode\":0}";
                }
            }
        }
        return "{\"errorCode\":0}";
    }

    public String classToJSON(RESTClass cls) {
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
            return gson.toJson(classJSON, ClassJSON.class);
        }
        return null;
    }

    @Transactional
    public String getClass(String apiKey, String className) {
        if (this.validatorService.isAuthenticated()) {
            RESTClass cls = this.restClassRepository.findByApiKeyAndClassName(apiKey, className);
            if (cls != null) {
                return this.classToJSON(cls);
            }
        }
        return "{\"errorCode\":0}";
    }

    @Transactional
    public String getClasses(String apiKey) {
        if (this.validatorService.isAuthenticated()) {
            String json = "";
            RESTDatabase restDB = this.restDatabaseRepository.findByApiKey(apiKey);
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
        }
        return "{\"errorCode\":0}";
    }

    @Transactional
    public String deleteClass(String apiKey, String className) {
        if (this.validatorService.isAuthenticated()) {
            RESTClass delClass = this.restClassRepository.findByApiKeyAndClassName(apiKey, className);
            if (delClass != null) {
                for (RESTAttributeDefinition attrDef : delClass.getAttributes()) {
                    this.restAttributeDefinitionRepository.delete(attrDef);
                }
                RESTDatabase rdb = this.restDatabaseRepository.findByApiKey(apiKey);
                List<RESTClass> clss = rdb.getClasses();
                clss.remove(delClass);
                rdb.setClasses(clss);
                List<RESTObject> objs = this.restObjectRepository.findByApiKeyAndClassName(apiKey, className);
                for (RESTObject obj : objs) {
                    this.deleteService.delete(obj.getApiKey(), obj.getClassName(), obj.getClassID());
                }
                this.restClassRepository.delete(delClass);
                this.restDatabaseRepository.save(rdb);
                return "{\"success\":1}";
            }
            return "{\"success\":0}";
        }
        return "{\"errorCode\":0}";
    }

}
