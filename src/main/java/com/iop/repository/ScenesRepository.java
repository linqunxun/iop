package com.iop.repository;

import com.iop.domain.Scenes;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Scenes entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ScenesRepository extends JpaRepository<Scenes, Long> {}
