package vn.udn.dut.it2.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import vn.udn.dut.it2.model.entity.AcademicEvent;

import java.util.List;

@Repository
public interface AcademicEventRepository extends JpaRepository<AcademicEvent, Long> {

    @Query("SELECT e FROM AcademicEvent e WHERE " +
           "(:eventType IS NULL OR :eventType = 'ALL' OR e.eventType = :eventType) AND " +
           "(:faculty IS NULL OR e.facultyScope = 'ALL' OR e.facultyScope = :faculty) AND " +
           "(:className IS NULL OR e.classScope = 'ALL' OR e.classScope = :className) " +
           "ORDER BY e.eventDate ASC")
    List<AcademicEvent> findEventsWithFilter(@Param("eventType") String eventType,
                                             @Param("faculty") String faculty,
                                             @Param("className") String className);
}
