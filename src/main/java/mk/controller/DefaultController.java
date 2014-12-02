package mk.controller;

import mk.domain.Entry;
import mk.repository.EntryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class DefaultController {
    
    @Autowired
    private EntryRepository entryRepository;
    
    @RequestMapping(value="/", method=RequestMethod.GET)
    public String root(){
        return "/WEB-INF/views/index.jsp";
    }
    
    @RequestMapping(value="/otayhteytta", method=RequestMethod.GET)
    public String rek(){
        return "/WEB-INF/views/otayhteytta.jsp";
    }
    
    @RequestMapping(value="/ok", method=RequestMethod.GET)
    public String ok(){
        return "/WEB-INF/views/ok.jsp";
    }
    
    @RequestMapping(value="/rek", method=RequestMethod.POST)
    public String submit(@RequestParam String yritys,
            @RequestParam String osoite,
            @RequestParam String kaupunki,
            @RequestParam String postinumero,
            @RequestParam String maa,
            @RequestParam String puhelinnumero,
            @RequestParam String sahkopostiosoite,
            @RequestParam String viesti){
        
        Entry e=new Entry();
        e.setKaupunki(kaupunki);
        e.setMaa(maa);
        e.setOsoite(osoite);
        e.setPostinumero(postinumero);
        e.setPuhelinnumero(puhelinnumero);
        e.setSahkopostiosoite(sahkopostiosoite);
        e.setViesti(viesti);
        e.setYritys(yritys);
        this.entryRepository.save(e);
        
        return "redirect:/ok";
    }
    
}
