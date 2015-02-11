package mk.repository;

import java.util.List;
import mk.domain.RESTJoin;
import mk.domain.RESTObject;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RESTJoinRepository extends JpaRepository<RESTJoin, Long> {
    List<RESTJoin> findByJoinedObj(RESTObject childObj);
}
