package quickdesk.quickdesk_backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import quickdesk.quickdesk_backend.model.Ticket;

import java.util.List;

public interface TicketRepository extends JpaRepository<Ticket, Long> {
    List<Ticket> findAllByCreatedByEmail(String email);
    List<Ticket> findAllByStatus(String status);
    List<Ticket> findAllByCategory(String category);
    List<Ticket> findAllByAssignedToEmail(String email);
}
