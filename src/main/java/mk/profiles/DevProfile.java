package mk.profiles;


import javax.annotation.PostConstruct;
import mk.domain.Member;
import mk.repository.MemberRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile(value = {"dev", "default"})
public class DevProfile {
    
    @Autowired
    private MemberRepository memberRepository;
    
    @PostConstruct
    public void init(){
        Member m=new Member();
        m.setUsername("mattipul");
        m.setPassword("killis");
        this.memberRepository.save(m);
    }
   
}
