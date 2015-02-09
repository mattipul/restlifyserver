package mk.repository;

import mk.domain.RESTAttributeDefinition;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RESTAttributeDefinitionRepository extends JpaRepository<RESTAttributeDefinition, Long> {
    
}
