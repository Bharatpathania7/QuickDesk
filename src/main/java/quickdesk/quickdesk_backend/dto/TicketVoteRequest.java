package quickdesk.quickdesk_backend.dto;

public class TicketVoteRequest {
    private Long ticketId;
    private boolean upvote;

    // Getters and setters
    public Long getTicketId() { return ticketId; }

    public void setTicketId(Long ticketId) { this.ticketId = ticketId; }

    public boolean isUpvote() { return upvote; }

    public void setUpvote(boolean upvote) { this.upvote = upvote; }
}
