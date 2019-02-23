package pl.itcraft.appstract.commons.users;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pl.itcraft.appstract.core.entities.User;
import pl.itcraft.appstract.core.enums.UserRole;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
	Optional<User> findFirstByActivationToken(String token);
	Optional<User> findFirstByRole(UserRole role);
	
	@Query("SELECT u FROM User u WHERE u.role = 'ABSTRACTOR' AND u.enabled = TRUE AND u.deleted = FALSE AND u.accountActivated = TRUE AND u.notifications = TRUE")
	List<User> findAbstractorsForNotifications();
}
