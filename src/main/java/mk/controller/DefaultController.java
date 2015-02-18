package mk.controller;

import mk.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class DefaultController {
    
    @Autowired
    private MemberService memebrService;
    
    @RequestMapping(value="/", method=RequestMethod.GET)
    @ResponseBody
    public String root(){
        return "";
    }
    
    @RequestMapping(value="/register", method=RequestMethod.POST)
    @ResponseBody
    public String register(@RequestParam String username, 
            @RequestParam String password,
            @RequestParam String passwordagain,
            @RequestParam String email){
        return this.memebrService.register(username, password, passwordagain, email);
    }
    
    @RequestMapping(value="/change_password", method=RequestMethod.POST)
    @ResponseBody
    public String changePassword(@RequestParam String password,
            @RequestParam String passwordagain){
        return this.memebrService.changePassword( password, passwordagain);
    }
    
    @RequestMapping(value="/login", method=RequestMethod.GET, produces="application/json")
    @ResponseBody
    public String login(){
        return "{\"success\":0,\"errorMessage\":\"Not authenticated.\"}";
    }
    
    @RequestMapping(value="/logout", method=RequestMethod.GET)
    @ResponseBody
    public String logout(){
        return "Logged out.";
    }
    
}
