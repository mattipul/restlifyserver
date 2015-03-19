package mk.controller;

import mk.service.DatabaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class DatabaseController {
    
    @Autowired
    private DatabaseService databaseService;
    
    @RequestMapping(value="/get_databases", method=RequestMethod.GET, produces="application/json")
    @ResponseBody
    public String getDatabases(){
        return this.databaseService.getDatabases();
    }
    
    @RequestMapping(value="/create_database", method=RequestMethod.POST, produces="application/json", consumes="application/json")
    @ResponseBody
    public String createDatabase(@RequestBody String json){
        return this.databaseService.createDatabase(json);
    }
    
    @RequestMapping(value="/edit_database/{apiKey}", method=RequestMethod.POST, produces="application/json", consumes="application/json")
    @ResponseBody
    public String editDatabase(@PathVariable String apiKey, @RequestBody String json){
        return this.databaseService.editDatabase(apiKey, json);
    }
    
    @RequestMapping(value="/define_classes/{apiKey}", method=RequestMethod.POST, produces="application/json", consumes="application/json")
    @ResponseBody
    public String defineClasses(@PathVariable String apiKey, @RequestBody String json){
        return this.databaseService.defineClasses(apiKey, json);
    }
    
    @RequestMapping(value="/get_class/{apiKey}/{className}", method=RequestMethod.GET)
    @ResponseBody
    public String getClass(@PathVariable String apiKey, @PathVariable String className ){
        return this.databaseService.getClass(apiKey, className);
    }
    
    @RequestMapping(value="/get_classes/{apiKey}", method=RequestMethod.GET)
    @ResponseBody
    public String getClasses(@PathVariable String apiKey ){
        return this.databaseService.getClasses(apiKey);
    }
    
    @RequestMapping(value="/delete_class/{apiKey}/{className}", method=RequestMethod.DELETE)
    @ResponseBody
    public String deleteClass(@PathVariable String apiKey, @PathVariable String className ){
        return this.databaseService.deleteClass(apiKey, className);
    }
    
}
