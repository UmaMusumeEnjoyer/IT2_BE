package vn.udn.dut.it2.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.udn.dut.it2.model.entity.ProcedureTicket;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProcedureTicketRepository extends JpaRepository<ProcedureTicket, Long> {
    List<ProcedureTicket> findByStudentCode(String studentCode);
    Optional<ProcedureTicket> findByTicketCode(String ticketCode);
}
