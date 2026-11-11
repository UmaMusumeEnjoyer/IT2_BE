package vn.udn.dut.it2.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.udn.dut.it2.model.entity.ProcedureCriteria;

import java.util.List;

@Repository
public interface ProcedureCriteriaRepository extends JpaRepository<ProcedureCriteria, Long> {
    List<ProcedureCriteria> findByProcedureName(String procedureName);
}
