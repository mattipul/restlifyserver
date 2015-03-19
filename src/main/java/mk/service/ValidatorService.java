package mk.service;

import java.util.List;
import mk.domain.Member;
import mk.domain.RESTDatabase;
import mk.repository.RESTDatabaseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ValidatorService {
    
    @Autowired
    private MemberService memberService;
    
    @Autowired
    private RESTDatabaseRepository restDatabaseRepository;
    
    public boolean valid(String str) {
        String valids = "1234567890qwertyuiopasdfghjklzxcvbnmQWERTYUIOPASDFGHJKLZXCVBNM_";
        if(str.length()==0){
            return false;
        }
        for (int i = 0; i < str.length(); i++) {
            if (!valids.contains("" + str.charAt(i))) {
                return false;
            }
        }
        return true;
    }
    
    public boolean isAuthenticated() {
        return this.memberService.isAuthenticated();
    }

    public boolean isAuth(String apikey) {
        if (this.memberService.isAuthenticated()) {
            Member m = this.memberService.getAuthenticatedPerson();
            List<RESTDatabase> dbs = this.restDatabaseRepository.findByOwner(m.getUsername());
            if (dbs != null) {
                for (RESTDatabase db : dbs) {
                    if (db.getApiKey().equals(apikey)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
    
}
