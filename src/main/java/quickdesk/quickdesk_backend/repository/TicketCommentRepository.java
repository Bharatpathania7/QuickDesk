package quickdesk.quickdesk_backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import quickdesk.quickdesk_backend.model.TicketComment;

import java.util.List;

public interface TicketCommentRepository extends JpaRepository<TicketComment, Long> {
    List<TicketComment> findByTicketId(Long ticketId);
}
