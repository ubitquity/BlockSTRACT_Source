package pl.itcraft.appstract.core.utils;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.persistence.TypedQuery;

public class QueryUtils {
	
	public static <T> List<T> list(TypedQuery<T> query, Integer offset, Integer limit) {
		int firstResult = offset != null ? offset : DatatablesInDtoUtils.DEFAULT_START;
		int maxResults  = limit != null ? limit : DatatablesInDtoUtils.DEFAULT_LENGTH;
		
		return query.setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
	}
	
	public static <T, Q> List<Q> map(TypedQuery<T> query, Integer offset, Integer limit, Function<? super T,? extends Q> mapper) {
		return list(query, offset, limit)
			.stream()
			.map(mapper)
			.collect(Collectors.toList());
	}
}
