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

    public boolean isAuthenticated() {
        return SecurityContextHolder.getContext().getAuthentication().isAuthenticated();
    }

    public String register(String username, String password1, String password2, String email) {
        Member m = new Member();
        if (!username.isEmpty() && !email.isEmpty() && !password1.isEmpty() && !password2.isEmpty()) {
            if (password1.equals(password2)) {
                if (password1.length() > 6) {
                    if (this.memberRepository.findByUsername(username) == null) {
                        m.setUsername(username);
                        m.setPassword(password1);
                        m.setRole("member");
                        m.setEmail(email);
                        this.memberRepository.save(m);
                        return "{\"success\":1}";
                    }else{
                        return "{\"success\":0, \"errorMessage\":\"Username exists already..\"}";
                    }
                }else{
                    return "{\"success\":0, \"errorMessage\":\"Min. password length 7 characters.\"}";
                }
            }else{
                return "{\"success\":0, \"errorMessage\":\"Both passwords have to match.\"}";
            }
        }else{
            return "{\"success\":0, \"errorMessage\":\"Empty parameters.\"}";
        }
    }

    public String changePassword(String password1, String password2) {
        if (this.isAuthenticated()) {
            Member m = this.getAuthenticatedPerson();
            if (password1.equals(password2)) {
                if (password1.length() > 6) {
                    m.setPassword(password1);
                    this.memberRepository.save(m);
                } else {
                    return "{\"success\":0, \"errorMessage\":\"Min. length 7 characters.\"}";
                }
            } else {
                return "{\"success\":0, \"errorMessage\":\"Both passwords have to match.\"}";
            }
        }
        return "{\"success\":0, \"errorMessage\":\"Authenticated?\"}";
    }

}
