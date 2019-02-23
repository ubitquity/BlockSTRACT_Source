package pl.itcraft.appstract.admin.counties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.itcraft.appstract.commons.counties.CountyRepository;
import pl.itcraft.appstract.core.dto.NamedIdDto;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CountiesService {

	@Autowired
	private CountyRepository countyRepository;

	public List<NamedIdDto> getAll() {
		return countyRepository.findAll().stream().map(county -> new NamedIdDto(county.getId(), county.getName())).collect(Collectors.toList());
	}
}
