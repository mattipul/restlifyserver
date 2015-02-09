package mk.repository;

import mk.domain.RESTAttribute;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RESTAttributeRepository extends JpaRepository<RESTAttribute, Long> {
    
}
