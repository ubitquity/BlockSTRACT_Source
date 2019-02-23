package pl.itcraft.appstract.admin.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pl.itcraft.appstract.commons.services.ServiceRepository;
import pl.itcraft.appstract.core.dto.NamedIdDto;

@Service
public class ServicesService {

	@Autowired
	private ServiceRepository servicesRepository;

	public List<NamedIdDto> getAll() {
		return servicesRepository.findAll().stream().map(service -> new NamedIdDto(service.getId(), service.getName())).collect(Collectors.toList());
	}
}
