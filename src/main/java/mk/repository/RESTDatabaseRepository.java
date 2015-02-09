package mk.repository;

import java.util.List;
import mk.domain.RESTDatabase;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RESTDatabaseRepository extends JpaRepository<RESTDatabase, Long> {
    RESTDatabase findByApiKey(String apikey);
    List<RESTDatabase> findByOwner(String owner);
}
