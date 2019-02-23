package pl.itcraft.appstract.commons.orders;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import pl.itcraft.appstract.core.entities.AbstractorOrderRating;
import pl.itcraft.appstract.core.entities.Order;
import pl.itcraft.appstract.core.entities.User;

@Repository
public interface AbstractorOrderRatingRepository extends JpaRepository<AbstractorOrderRating, Long> {

	Optional<AbstractorOrderRating> findFirstByAbstractorAndOrder(User abstractor, Order order);
	
	@Query("SELECT AVG(aor.rate) FROM AbstractorOrderRating aor WHERE aor.abstractor = ?1")
	Optional<Double> getAverageAbstractorRating(User abstractor);
	
}
