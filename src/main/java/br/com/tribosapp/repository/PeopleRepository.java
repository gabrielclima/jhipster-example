package br.com.tribosapp.repository;

import br.com.tribosapp.domain.People;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Spring Data JPA repository for the People entity.
 */
@SuppressWarnings("unused")
public interface PeopleRepository extends JpaRepository<People,Long> {

    @Query("select distinct people from People people left join fetch people.socialNetworks left join fetch people.events left join fetch people.likedPosts left join fetch people.tribes")
    List<People> findAllWithEagerRelationships();

    @Query("select people from People people left join fetch people.socialNetworks left join fetch people.events left join fetch people.likedPosts left join fetch people.tribes where people.id =:id")
    People findOneWithEagerRelationships(@Param("id") Long id);

}
