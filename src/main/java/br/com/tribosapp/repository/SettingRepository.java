package br.com.tribosapp.repository;

import br.com.tribosapp.domain.Setting;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Setting entity.
 */
@SuppressWarnings("unused")
public interface SettingRepository extends JpaRepository<Setting,Long> {

}
