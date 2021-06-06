package com.iop.repository;

import com.iop.domain.Docking;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Docking entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DockingRepository extends JpaRepository<Docking, Long> {}
