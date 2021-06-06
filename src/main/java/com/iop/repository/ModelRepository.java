package com.iop.repository;

import com.iop.domain.Model;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Model entity.
 */
@Repository
public interface ModelRepository extends JpaRepository<Model, Long> {
    @Query(
        value = "select distinct model from Model model left join fetch model.scenes",
        countQuery = "select count(distinct model) from Model model"
    )
    Page<Model> findAllWithEagerRelationships(Pageable pageable);

    @Query("select distinct model from Model model left join fetch model.scenes")
    List<Model> findAllWithEagerRelationships();

    @Query("select model from Model model left join fetch model.scenes where model.id =:id")
    Optional<Model> findOneWithEagerRelationships(@Param("id") Long id);
}
