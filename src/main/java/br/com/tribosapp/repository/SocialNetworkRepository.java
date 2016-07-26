package br.com.tribosapp.repository;

import br.com.tribosapp.domain.SocialNetwork;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the SocialNetwork entity.
 */
@SuppressWarnings("unused")
public interface SocialNetworkRepository extends JpaRepository<SocialNetwork,Long> {

}
