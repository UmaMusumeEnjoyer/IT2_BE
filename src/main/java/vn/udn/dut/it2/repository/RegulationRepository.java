package vn.udn.dut.it2.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import vn.udn.dut.it2.model.entity.Regulation;

import java.util.List;

@Repository
public interface RegulationRepository extends JpaRepository<Regulation, Long> {

    List<Regulation> findByCategory(String category);

    @Query("SELECT r FROM Regulation r WHERE " +
           "(:category IS NULL OR :category = 'ALL' OR r.category = :category) AND " +
           "(:keyword IS NULL OR LOWER(r.title) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(r.documentNumber) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(r.summary) LIKE LOWER(CONCAT('%', :keyword, '%')))")
    List<Regulation> searchRegulations(@Param("category") String category, @Param("keyword") String keyword);
}
