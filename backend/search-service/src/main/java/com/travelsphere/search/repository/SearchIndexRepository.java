package com.travelsphere.search.repository;

import com.travelsphere.search.model.SearchIndex;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface SearchIndexRepository extends JpaRepository<SearchIndex, UUID> {

    @Query("SELECT s FROM SearchIndex s WHERE " +
           "LOWER(s.title) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
           "LOWER(s.description) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
           "LOWER(s.city) LIKE LOWER(CONCAT('%', :query, '%'))")
    List<SearchIndex> fullTextSearch(@Param("query") String query);

    @Query("SELECT s FROM SearchIndex s WHERE " +
           "(:query IS NULL OR LOWER(s.title) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
           "LOWER(s.description) LIKE LOWER(CONCAT('%', :query, '%'))) AND " +
           "(:entityType IS NULL OR s.entityType = :entityType) AND " +
           "(:city IS NULL OR LOWER(s.city) = LOWER(:city)) AND " +
           "(:category IS NULL OR LOWER(s.category) = LOWER(:category)) AND " +
           "s.isActive = true")
    List<SearchIndex> searchWithFilters(@Param("query") String query,
                                         @Param("entityType") String entityType,
                                         @Param("city") String city,
                                         @Param("category") String category);
}
