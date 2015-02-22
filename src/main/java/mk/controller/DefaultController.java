package mk.controller;

import mk.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

@Controller
public class DefaultController {
    
    @Autowired
    private MemberService memberService;
    
    @RequestMapping(value="/", method=RequestMethod.GET)
    public String root(){
        return "/WEB-INF/views/index.jsp";
    }

    @RequestMapping(value="/ok", method=RequestMethod.GET, produces="application/json")
    @ResponseBody
    public String loginOk(){
        return "{\"authenticated\":1}";
    }
    
    @RequestMapping(value = "/signup", method = RequestMethod.POST, produces="application/json")
    @ResponseBody
    public String signUp(@RequestParam String username,
            @RequestParam String password1,
            @RequestParam String password2,
            @RequestParam String email) {
        return this.memberService.register(username, password1, password2, email);
    }
    
    @RequestMapping(value="/register", method=RequestMethod.POST, produces="application/json")
    @ResponseBody
    public String register(@RequestParam String username, 
            @RequestParam String password,
            @RequestParam String passwordagain,
            @RequestParam String email){
        return this.memberService.register(username, password, passwordagain, email);
    }
    
    @RequestMapping(value="/change_password", method=RequestMethod.POST)
    @ResponseBody
    public String changePassword(@RequestParam String password,
            @RequestParam String passwordagain){
        return this.memberService.changePassword( password, passwordagain);
    }
    
    @RequestMapping(value="/login", method=RequestMethod.GET, produces="application/json")
    @ResponseBody
    public String login(){
        return "{\"success\":0,\"errorMessage\":\"Not authenticated.\"}";
    }
    
    @RequestMapping(value="/logout", method=RequestMethod.GET, produces="application/json")
    @ResponseBody
    public String logout(){
        return "";
    }
    
    @RequestMapping(value="/loggedout", method=RequestMethod.GET, produces="application/json")
    @ResponseBody
    public String loggedout(){
        return "";
    }
    
}
