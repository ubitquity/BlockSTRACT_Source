package pl.itcraft.appstract.admin.users;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import pl.itcraft.appstract.admin.Constants;
import pl.itcraft.appstract.commons.orders.AbstractorOrderRatingRepository;
import pl.itcraft.appstract.core.api.error.ApiException;
import pl.itcraft.appstract.core.api.error.RC;
import pl.itcraft.appstract.core.dto.PageableDto;
import pl.itcraft.appstract.core.entities.AbstractRate;
import pl.itcraft.appstract.core.entities.ServiceType;
import pl.itcraft.appstract.core.entities.User;
import pl.itcraft.appstract.core.enums.UserRole;
import pl.itcraft.appstract.core.user.UserService;
import pl.itcraft.appstract.core.utils.UtilsBean;
import pl.itcraft.appstract.core.validation.AppValidator;

@Service
@Transactional(readOnly = true)
public class AbstractorsListService {

	@PersistenceContext
	private EntityManager em;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private UtilsBean utilsBean;
	
	@Autowired
	private AppValidator validator;
	
	@Autowired
	private AbstractorOrderRatingRepository abstractorOrderRatingRepository;

	public Page<AbstractorsListRowDto> getList(PageableDto pageableDto) {
		Pageable pageable = pageableDto.getPageable(Constants.DEFAULT_PAGE_SIZE);
		String query = "SELECT u FROM User u"
				+ " WHERE u.deleted = FALSE AND u.role = :role ORDER BY ";
		if (pageable.getSort() == null) {
			query += "u.firstName";
		} else {
			List<String> sorts = new ArrayList<>();
			pageable.getSort().forEach(o -> {
				sorts.add("u." + o.getProperty() + " " + o.getDirection().name());
			});
			query += StringUtils.join(sorts, ",");
		}
		List<AbstractorsListRowDto> list =  em.createQuery(query, User.class)
			.setParameter("role", UserRole.ABSTRACTOR)
			.setFirstResult(pageable.getOffset())
			.setMaxResults(pageable.getPageSize())
			.getResultList()
			.stream()
			.map(AbstractorsListRowDto::new)
			.collect(Collectors.toList());
		
		long count = getAbstractorsCount();
		return new PageImpl<AbstractorsListRowDto>(list, pageable, count);
	}
	
	private long getAbstractorsCount() {
		List<Long> results = em.createQuery("SELECT COUNT(u) FROM User u WHERE u.deleted = FALSE AND u.role = :role", Long.class)
			.setParameter("role", UserRole.ABSTRACTOR)
			.getResultList();
		return results.isEmpty() ? 0 : results.get(0);
	}

	public AbstractorListDetailsDto getDetails(Long userId) {
		User user = userService.findById(userId);
		if (user == null) {
			throw new ApiException(RC.RESOURCE_NOT_FOUND, "No such user");
		}
		if (user.getRole().isReadOnly()) {
			throw new ApiException(RC.FORBIDDEN, "Useris readonly");
		}
		return new AbstractorListDetailsDto(user, getAverageRatingOrZero(user));
	}

	@Transactional
	public AbstractorListDetailsDto updateDetails(AbstractorProfileUpdateDto dto) {
		long userId = utilsBean.getCurrentUser().getId();
		dto.setUserId(userId);
		validator.validate(dto);
		User user = userService.findById(userId);
		user.updateAbstractor(dto.getFirstName(), dto.getLastName(), dto.getEmail(), dto.getBankAccount(), dto.getCompanyName(), dto.getWeeklyAvailability(), dto.isNotifications());
		em.createQuery("DELETE FROM AbstractRate ar WHERE ar.user = :user").setParameter("user", user).executeUpdate();
		em.createNativeQuery("DELETE FROM abstractor_counties ac WHERE ac.abstractor_id = :abstractorId").setParameter("abstractorId", user.getId()).executeUpdate();
		for (AbstractorProfileUpdateDto.AbstractorProfileAbstractRateDto abstractRateDto : dto.getAbstractRates()) {
			ServiceType serviceType = em.getReference(ServiceType.class, abstractRateDto.getServiceTypeId());
			AbstractRate rate = new AbstractRate(user, serviceType, abstractRateDto.getRate());
			em.persist(rate);
		}
		for (long countyId : dto.getCountyIds()) {
			em.createNativeQuery("INSERT INTO abstractor_counties(abstractor_id, county_id) VALUES(:abstractorId, :countyId)")
				.setParameter("abstractorId", user.getId())
				.setParameter("countyId", countyId)
				.executeUpdate();
		}
		return new AbstractorListDetailsDto(user, getAverageRatingOrZero(user));
	}

	private double getAverageRatingOrZero(User user) {
		Optional<Double> averageRating = abstractorOrderRatingRepository.getAverageAbstractorRating(user);
		double averageRate = averageRating.isPresent() ? averageRating.get() : 0;
		return averageRate;
	}

}
