package pl.itcraft.appstract.commons.counties;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.itcraft.appstract.core.entities.County;

import java.util.Optional;

@Repository
public interface CountyRepository extends JpaRepository<County, Long> {

	Optional<County> findFirstByName(String name);
}
