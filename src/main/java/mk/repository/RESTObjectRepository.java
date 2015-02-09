package mk.repository;

import java.util.List;
import mk.domain.RESTObject;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RESTObjectRepository extends JpaRepository<RESTObject, Long> {
    RESTObject findByClassID(Long classID);
    List<RESTObject> findByApiKeyAndClassName(String apikey, String className);
    RESTObject findByApiKeyAndClassNameAndClassID(String apikey, String classname, Long classID);
}
