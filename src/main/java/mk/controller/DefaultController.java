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
            @RequestParam String password1,
            @RequestParam String password2,
            @RequestParam String email){
        return this.memebrService.register(username, password1, password2, email);
    }
    
    @RequestMapping(value="/login", method=RequestMethod.GET)
    @ResponseBody
    public String login(){
        return "Authenticated?";
    }
    
    @RequestMapping(value="/logout", method=RequestMethod.GET)
    @ResponseBody
    public String logout(){
        return "Logged out.";
    }
    
}
