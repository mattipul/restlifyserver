package mk.repository;

import mk.domain.RESTClass;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RESTClassRepository extends JpaRepository<RESTClass, Long> {

    RESTClass findByApiKeyAndClassName(String apikey, String classname);
    
}
