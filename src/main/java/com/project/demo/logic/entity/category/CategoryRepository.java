package com.project.demo.logic.entity.category;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category,Long> {

    @Query("SELECT u FROM Category u WHERE LOWER(u.name) LIKE %?1%")
    List<Category> findCategoryWithCharacterInName(String character);

    @Query("SELECT u FROM Category u WHERE u.name = ?1")
    Optional<Category> findByName(String name);

}
