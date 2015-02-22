package mk.controller;

import java.util.Map;
import mk.service.DatabaseService;
import mk.service.RestlifyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class RestlifyRepositoryController {

    @Autowired
    private RestlifyService restlifyService;

    @Autowired
    private DatabaseService databaseService;

    //MUISTA PRIMITIIVIEN LISTAT
    @RequestMapping(value = "/api/{apikey}", method = RequestMethod.GET, produces = "application/json")
    public String getStructure(Model model, @PathVariable String apikey) {
        model.addAttribute("db", this.databaseService.getDatabaseStructureList(apikey));
        return "/WEB-INF/views/api.jsp";
    }

    @RequestMapping(value = "/api/{apikey}/{classname}", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public String getAll(@PathVariable String apikey, @PathVariable String classname, @RequestParam Map<String, String> params) {
        String ret = this.restlifyService.getAll(apikey, classname.toLowerCase(), params);
        if (ret.length() > 2) {
            return ret;
        } else {
            return ret;
        }
    }

    @RequestMapping(value = "/api/{apikey}/{classname}/{id}", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public String get(@PathVariable String apikey, @PathVariable String classname, @PathVariable Long id) {
        String ret = this.restlifyService.get(apikey, classname.toLowerCase(), id);
        if (ret.length() > 2) {
            return ret;
        } else {
            return ret;
        }
    }

    @RequestMapping(value = "/api/{apikey}/{classname}/{id}", method = RequestMethod.DELETE, produces = "application/json")
    @ResponseBody
    public String delete(@PathVariable String apikey, @PathVariable String classname, @PathVariable Long id) {
        String ret = this.restlifyService.delete(apikey, classname.toLowerCase(), id);
        if (ret.length() > 2) {
            return ret;
        } else {
            return ret;
        }
    }

    @RequestMapping(value = "/api/{apikey}/{classname}", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public String save(@PathVariable String apikey, @PathVariable String classname, @RequestBody String body) {
        String ret = this.restlifyService.save(apikey, classname.toLowerCase(), body);
        if (ret.length() > 2) {
            return ret;
        } else {
            return ret;
        }
    }

    /*@RequestMapping(value="/search/{apikey}/{classname}", method=RequestMethod.GET, produces="application/json")
     @ResponseBody
     public String search(@PathVariable String apikey, @PathVariable String classname, @RequestParam Map<String, String> parameters){
     return this.restlifyService.search(apikey, classname, parameters);
     }
     */
}
