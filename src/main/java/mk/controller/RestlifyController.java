package mk.controller;

import java.util.Map;
import mk.service.RestlifyDeleteService;
import mk.service.RestlifyGetService;
import mk.service.RestlifySaveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class RestlifyController {

    @Autowired
    private RestlifySaveService saveService;

    @Autowired
    private RestlifyGetService getService;

    @Autowired
    private RestlifyDeleteService deleteService;

    @RequestMapping(value = "/api/{apiKey}/{className}", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
    @ResponseBody
    public String save(@PathVariable String apiKey,
            @PathVariable String className,
            @RequestBody String json) {
        return this.saveService.save(apiKey, className.toLowerCase(), json);
    }

    @RequestMapping(value = "/api/{apiKey}/{className}", method = RequestMethod.GET, produces = "application/json", consumes = "application/json")
    @ResponseBody
    public String getAll(@PathVariable String apiKey,
            @PathVariable String className,
            @RequestParam Map<String, String> params) {
        return this.getService.getAll(apiKey, className.toLowerCase(), params);
    }

    @RequestMapping(value = "/api/{apiKey}/{className}/{id}", method = RequestMethod.GET, produces = "application/json", consumes = "application/json")
    @ResponseBody
    public String get(@PathVariable String apiKey,
            @PathVariable String className,
            @PathVariable Long id) {
        return this.getService.get(apiKey, className.toLowerCase(), id);
    }

    @RequestMapping(value = "/api/{apiKey}/{className}/{id}", method = RequestMethod.DELETE, produces = "application/json")
    @ResponseBody
    public String delete(@PathVariable String apiKey, @PathVariable String className, @PathVariable Long id) {
        String ret = this.deleteService.delete(apiKey, className.toLowerCase(), id);
        return ret;
    }

}
