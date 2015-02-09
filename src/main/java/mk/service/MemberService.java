package mk.service;

import java.util.List;
import mk.domain.Member;
import mk.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class MemberService {
    
    @Autowired
    private MemberRepository memberRepository;
    
    public Member getAuthenticatedPerson() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return memberRepository.findByUsername(authentication.getName());
    }
    
    public boolean isAuthenticated(){
        return SecurityContextHolder.getContext().getAuthentication().isAuthenticated();
    }

    public String register(String username, String password1, String password2, String email) {
        Member m=new Member();
        if(password1.equals(password2) && password1.length()>6){
            if(this.memberRepository.findByUsername(username)==null){
                m.setUsername(username);
                m.setPassword(password1);
                m.setRole("member");
                m.setEmail(email);
                this.memberRepository.save(m);
                return "OK";
            }
        }
        return "FAIL";
    }
    
}
