package br.com.tribosapp.repository;

import br.com.tribosapp.domain.Picture;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Picture entity.
 */
@SuppressWarnings("unused")
public interface PictureRepository extends JpaRepository<Picture,Long> {

}
