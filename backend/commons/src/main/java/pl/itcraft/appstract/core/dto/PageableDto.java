package pl.itcraft.appstract.core.dto;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.util.StringUtils;

import io.swagger.annotations.ApiParam;

public class PageableDto {
	@ApiParam(value="Zero-based page index", defaultValue="0")
	private Integer page = 0;
	
	@ApiParam(value="Page size", defaultValue="10")
	private Integer size = 10;
	
	@ApiParam(value="Column name and direction, comma separated", defaultValue="name,asc")
	private String sort = "";
	
	public Pageable getPageable(int defaultSize) {
		if (size == null) {
			size = defaultSize;
		}
		List<Order> allOrders = new ArrayList<Sort.Order>();
		String[] elements = sort.split(",");
		Direction direction = elements.length == 0 ? null : Direction.fromStringOrNull(elements[elements.length - 1]);
		for (int i = 0; i < elements.length; i++) {
			if (i == elements.length - 1 && direction != null) {
				continue;
			}
			String property = elements[i];
			if (!StringUtils.hasText(property)) {
				continue;
			}
			allOrders.add(new Order(direction, property));
		}
		Sort s = allOrders.isEmpty() ? null : new Sort(allOrders);
		return new PageRequest(page, size, s);
	}

	public void setPage(Integer page) {
		if (page != null && page >= 0) {
			this.page = page;
		}
	}
	public void setSize(Integer size) {
		if (size != null && size >= 0) {
			this.size = size;
		}
	}
	public void setSort(String sort) {
		this.sort = sort == null ? "" : sort;
	}
	
	public Integer getSize() {
		return size;
	}
	public Integer getPage() {
		return page;
	}
	public String getSort() {
		return sort;
	}
}
