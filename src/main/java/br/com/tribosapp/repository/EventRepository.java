package br.com.tribosapp.repository;

import br.com.tribosapp.domain.Event;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Spring Data JPA repository for the Event entity.
 */
@SuppressWarnings("unused")
public interface EventRepository extends JpaRepository<Event,Long> {

    @Query("select distinct event from Event event left join fetch event.posts left join fetch event.socialNetworks")
    List<Event> findAllWithEagerRelationships();

    @Query("select event from Event event left join fetch event.posts left join fetch event.socialNetworks where event.id =:id")
    Event findOneWithEagerRelationships(@Param("id") Long id);

}
