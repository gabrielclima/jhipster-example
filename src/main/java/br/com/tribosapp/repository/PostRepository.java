package br.com.tribosapp.repository;

import br.com.tribosapp.domain.Post;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Post entity.
 */
@SuppressWarnings("unused")
public interface PostRepository extends JpaRepository<Post,Long> {

}
