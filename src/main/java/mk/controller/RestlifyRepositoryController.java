package mk.controller;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.ProcessBuilder.Redirect.Type;
import java.util.HashMap;
import java.util.Map;
import mk.service.RestlifyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class RestlifyRepositoryController {
    
    @Autowired
    private RestlifyService restlifyService;
    
    @RequestMapping(value="/api/{apikey}/{classname}s", method=RequestMethod.GET, produces="application/json")
    @ResponseBody
    public String getAll(@PathVariable String apikey, @PathVariable String classname) {
        return this.restlifyService.getAll(apikey, classname.toLowerCase());
    }

    @RequestMapping(value="/api/{apikey}/{classname}s/{id}", method=RequestMethod.GET, produces="application/json")
    @ResponseBody
    public String get(@PathVariable String apikey, @PathVariable String classname, @PathVariable Long id) {
        return this.restlifyService.get(apikey, classname.toLowerCase(), id);
    }

    @RequestMapping(value="/api/{apikey}/{classname}s/{id}", method=RequestMethod.DELETE, produces="application/json")
    @ResponseBody
    public String delete(@PathVariable String apikey, @PathVariable String classname, @PathVariable Long id) {
        return this.restlifyService.delete(apikey, classname.toLowerCase(), id);
    }    

    @RequestMapping(value="/api/{apikey}/{classname}s", method=RequestMethod.POST, produces="application/json")
    @ResponseBody
    public String save(@PathVariable String apikey, @PathVariable String classname, @RequestBody String body) {
        return this.restlifyService.save(apikey, classname.toLowerCase(), body);
    }
    
    /*@RequestMapping(value="/search/{apikey}/{classname}", produces="application/json")
    @ResponseBody
    public String search(@PathVariable String apikey, @PathVariable String classname, @RequestBody String body){
        return this.restlifyService.search(apikey, classname, body);
    }*/
    
}
