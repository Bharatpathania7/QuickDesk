package quickdesk.quickdesk_backend.dto;

public class TicketCommentRequest {
    private Long ticketId;
    private String comment;

    public Long getTicketId() {
        return ticketId;
    }

    public void setTicketId(Long ticketId) {
        this.ticketId = ticketId;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
// Getters and Setters
}
