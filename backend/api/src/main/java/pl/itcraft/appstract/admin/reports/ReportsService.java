package pl.itcraft.appstract.admin.reports;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pl.itcraft.appstract.core.enums.OrderInternalStatus;
import pl.itcraft.appstract.core.utils.UtilsBean;

@Service
public class ReportsService {

	@PersistenceContext
	private EntityManager em;
	
	@Autowired
	private UtilsBean utilsBean;
	
	public MostResponsiveAbstractorDto getMostResponsiveAbstractor() {
		List<?> results = em.createNativeQuery(
				"SELECT u.id, u.first_name||' '||u.last_name AS name, EXTRACT(EPOCH FROM AVG(o.accepted_by_abstractor_time - o.created_at)) * 1000 AS accept_time "
				+ "FROM orders o JOIN users u ON o.abstractor_id = u.id "
				+ "GROUP BY 1,2 ORDER BY 3 ASC"
		).setMaxResults(1).getResultList();
		
		if (results.isEmpty()) {
			return new MostResponsiveAbstractorDto();
		}
		Object[] row = (Object[]) results.get(0);
		return new MostResponsiveAbstractorDto(((Number) row[0]).longValue(), (String) row[1], ((Number) row[2]).longValue());
	}
	
	public AbstractorWithAverageCostDto getCheapestAbstractor() {
		return getCheapestOrMostExpensiveAbstractor(true);
	}
	
	public AbstractorWithAverageCostDto getMostExpensiveAbstractor() {
		return getCheapestOrMostExpensiveAbstractor(false);
	}

	private AbstractorWithAverageCostDto getCheapestOrMostExpensiveAbstractor(boolean cheapest) {
		List<?> results = em.createNativeQuery(
				"SELECT u.id, u.first_name||' '||u.last_name AS name, AVG(o.final_cost) AS final_cost_average "
				+ "FROM orders o JOIN users u ON o.abstractor_id = u.id "
				+ "WHERE o.internal_status = :paid "
				+ "GROUP BY 1,2 ORDER BY 3 " + (cheapest ? "ASC" : "DESC")
		).setParameter("paid", OrderInternalStatus.PAID.name()).setMaxResults(1).getResultList();
		
		if (results.isEmpty()) {
			return new AbstractorWithAverageCostDto();
		}
		Object[] row = (Object[]) results.get(0);
		return new AbstractorWithAverageCostDto(((Number) row[0]).longValue(), (String) row[1], ((BigDecimal) row[2]).setScale(2, RoundingMode.HALF_EVEN));
	}
	
	public BigDecimal getAverageCost(Date startDate, int months) {
		DateTime startDT = new DateTime(startDate).withDayOfMonth(1).withTimeAtStartOfDay();
		Date start = startDT.toDate();
		Date end = startDT.plusMonths(months).toDate();
		
		List<Double> results = em.createQuery("SELECT AVG(o.finalCost) FROM Order o WHERE o.internalStatus = :paid AND o.createdAt >= :start AND o.createdAt < :end", Double.class)
			.setParameter("paid", OrderInternalStatus.PAID)
			.setParameter("start", start)
			.setParameter("end", end)
			.getResultList();
		
		return extractBigDecimalResultFromDouble(results);
	}
	
	public BigDecimal getTotalIncome() {
		List<BigDecimal> results = em.createQuery("SELECT SUM(o.finalCost) FROM Order o WHERE o.internalStatus = :paid AND o.abstractor = :abstractor", BigDecimal.class)
			.setParameter("paid", OrderInternalStatus.PAID)
			.setParameter("abstractor", utilsBean.getCurrentUser())
			.getResultList();
		
		return extractBigDecimalResult(results);
	}
	
	public int getProjectsWon() {
		List<Number> results = em.createQuery("SELECT COUNT(o) FROM Order o WHERE o.abstractor = :abstractor AND o.internalStatus = :paidStatus", Number.class)
			.setParameter("abstractor", utilsBean.getCurrentUser())
			.setParameter("paidStatus", OrderInternalStatus.PAID)
			.getResultList();
		
		return extractIntResult(results);
	}
	
	public int getProjectsDeclinedOrRecalled(boolean recalled) {
		List<?> results = em.createNativeQuery("SELECT COUNT(ado) FROM abstractor_declined_orders ado WHERE ado.abstractor_id = :abstractorId AND recalled = :recalled")
				.setParameter("abstractorId", utilsBean.getCurrentUser().getId())
				.setParameter("recalled", recalled)
				.getResultList();
		
		return extractIntResultFromList(results);
	}
	
	private BigDecimal extractBigDecimalResultFromDouble(List<Double> results) {
		if (results.isEmpty() || results.get(0) == null) {
			return BigDecimal.ZERO;
		}
		return BigDecimal.valueOf(results.get(0)).setScale(2, RoundingMode.HALF_EVEN);
	}
	
	private BigDecimal extractBigDecimalResult(List<BigDecimal> results) {
		if (results.isEmpty() || results.get(0) == null) {
			return BigDecimal.ZERO;
		}
		return results.get(0).setScale(2, RoundingMode.HALF_EVEN);
	}
	
	private int extractIntResult(List<Number> results) {
		if (results.isEmpty() || results.get(0) == null) {
			return 0;
		}
		return results.get(0).intValue();
	}
	
	private int extractIntResultFromList(List<?> results) {
		if (results.isEmpty() || results.get(0) == null) {
			return 0;
		}
		return ((Number)results.get(0)).intValue();
	}
	
}
