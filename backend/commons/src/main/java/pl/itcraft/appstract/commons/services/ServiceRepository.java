package pl.itcraft.appstract.commons.services;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import pl.itcraft.appstract.core.entities.ServiceType;

@Repository
public interface ServiceRepository extends JpaRepository<ServiceType, Long> {

}
