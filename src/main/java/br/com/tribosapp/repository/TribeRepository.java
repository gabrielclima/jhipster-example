package br.com.tribosapp.repository;

import br.com.tribosapp.domain.Tribe;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Spring Data JPA repository for the Tribe entity.
 */
@SuppressWarnings("unused")
public interface TribeRepository extends JpaRepository<Tribe,Long> {

    @Query("select distinct tribe from Tribe tribe left join fetch tribe.posts left join fetch tribe.events")
    List<Tribe> findAllWithEagerRelationships();

    @Query("select tribe from Tribe tribe left join fetch tribe.posts left join fetch tribe.events where tribe.id =:id")
    Tribe findOneWithEagerRelationships(@Param("id") Long id);

}
