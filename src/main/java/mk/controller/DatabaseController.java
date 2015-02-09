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
    
    @RequestMapping(value="/create_database", method=RequestMethod.GET)
    @ResponseBody
    public String createDatabase(){
        return this.databaseService.createDatabase();
    }
    
    @RequestMapping(value="/get_databases", method=RequestMethod.GET, produces="application/json")
    @ResponseBody
    public String getDatabases(){
        return this.databaseService.getDatabases();
    }
    
    @RequestMapping(value="/define_class/{apikey}", method=RequestMethod.POST, consumes="application/json", produces="application/json")
    @ResponseBody
    public String defineClass(@PathVariable String apikey, @RequestBody String classJSON){
        return this.databaseService.defineClass(apikey, classJSON);
    }
    
    @RequestMapping(value="/destroy_class/{apikey}/{classname}", method=RequestMethod.DELETE)
    @ResponseBody
    public int destroyClass(@PathVariable String apikey, @PathVariable String classname){
        this.databaseService.destroyClass(apikey, classname.toLowerCase());
        return 1;
    }
    
    @RequestMapping(value="/get_classes/{apikey}", method=RequestMethod.GET, produces="application/json")
    @ResponseBody
    public String getAllClasses(@PathVariable String apikey){
        return this.databaseService.getAllClasses(apikey);
    }
    
    @RequestMapping(value="/get_class/{apikey}/{classname}", method=RequestMethod.GET, produces="application/json")
    @ResponseBody
    public String getClass(@PathVariable String apikey, @PathVariable String classname){
        return this.databaseService.getClass(apikey, classname.toLowerCase());
    }
    
}
