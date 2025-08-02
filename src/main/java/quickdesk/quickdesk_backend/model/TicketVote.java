package quickdesk.quickdesk_backend.model;

import jakarta.persistence.*;

@Entity
@Table(name = "ticket_votes", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"user_id", "ticket_id"})
})
public class TicketVote {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private boolean upvote;

    @ManyToOne
    @JoinColumn(name = "ticket_id", nullable = false)
    private Ticket ticket;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // Getters and setters
    public Long getId() { return id; }

    public boolean isUpvote() { return upvote; }

    public void setUpvote(boolean upvote) { this.upvote = upvote; }

    public Ticket getTicket() { return ticket; }

    public void setTicket(Ticket ticket) { this.ticket = ticket; }

    public User getUser() { return user; }

    public void setUser(User user) { this.user = user; }
}
