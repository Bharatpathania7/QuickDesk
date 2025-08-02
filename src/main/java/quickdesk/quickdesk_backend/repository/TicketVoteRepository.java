package quickdesk.quickdesk_backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import quickdesk.quickdesk_backend.model.Ticket;
import quickdesk.quickdesk_backend.model.TicketVote;
import quickdesk.quickdesk_backend.model.User;

import java.util.Optional;

public interface TicketVoteRepository extends JpaRepository<TicketVote, Long> {
    Optional<TicketVote> findByUserIdAndTicketId(Long userId, Long ticketId);
    long countByTicketIdAndUpvoteTrue(Long ticketId);
    long countByTicketIdAndUpvoteFalse(Long ticketId);
    Optional<TicketVote> findByTicketAndUser(Ticket ticket, User user);

}
