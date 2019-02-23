package pl.itcraft.appstract.commons.orders;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pl.itcraft.appstract.core.entities.Order;
import pl.itcraft.appstract.core.entities.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
	Optional<Order> findFirstByIdAndAbstractor(Long id, User abstractor);
	
//	Optional<Order> findById(Long id);

	@Query("select o from Order o where o.abstractor = ?1 and (o.open = 'true')")
	List<Order> findAllActiveByAbstractor(User user);
	
	@Query("select o from Order o where o.abstractor is not null and (o.open = 'true')")
	List<Order> findAllWithAssignedAbstractor();
}
