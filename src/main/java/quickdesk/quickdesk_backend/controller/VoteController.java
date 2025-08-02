package quickdesk.quickdesk_backend.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import quickdesk.quickdesk_backend.service.TicketService;

@RestController
@RequestMapping("/api/votes")
@RequiredArgsConstructor
public class VoteController {

    private  TicketService ticketService;

    // ✅ Upvote a ticket
    @PostMapping("/upvote/{ticketId}")
    public ResponseEntity<String> upvoteTicket(
            @PathVariable Long ticketId,
            @RequestParam String userEmail
    ) {
        ticketService.upvoteTicket(ticketId, userEmail);
        return ResponseEntity.ok("Ticket upvoted successfully");
    }

    // ✅ Downvote a ticket
    @PostMapping("/downvote/{ticketId}")
    public ResponseEntity<String> downvoteTicket(
            @PathVariable Long ticketId,
            @RequestParam String userEmail
    ) {
        ticketService.downvoteTicket(ticketId, userEmail);
        return ResponseEntity.ok("Ticket downvoted successfully");
    }
}
