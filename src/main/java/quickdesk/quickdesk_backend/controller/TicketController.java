package quickdesk.quickdesk_backend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import quickdesk.quickdesk_backend.dto.*;
import quickdesk.quickdesk_backend.service.TicketService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/tickets")
public class TicketController {

    private final TicketService ticketService;

    public TicketController(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    // Create a new ticket
    @PostMapping
    public ResponseEntity<TicketResponse> createTicket(@RequestBody CreateTicketRequest request,
                                                       Authentication authentication) {
        String userEmail = authentication.getName();
        return ResponseEntity.ok(ticketService.createTicket(request, userEmail));
    }

    // Get tickets created by logged-in user
    @GetMapping("/my")
    public ResponseEntity<List<TicketResponse>> getMyTickets(Authentication authentication) {
        String userEmail = authentication.getName();
        return ResponseEntity.ok(ticketService.getUserTickets(userEmail));
    }

    // Get all tickets (Agent access)
    @GetMapping("/agent/all")
    public ResponseEntity<List<TicketResponse>> getAllTicketsForAgent(Authentication authentication) {
        String agentEmail = authentication.getName(); // Optional: validate role
        return ResponseEntity.ok(ticketService.getAllTicketsForAgent(agentEmail));
    }

    // Filter tickets
    @GetMapping("/filter")
    public ResponseEntity<List<TicketResponse>> filterTickets(
            @RequestParam Optional<String> status,
            @RequestParam Optional<String> category,
            @RequestParam Optional<String> assignedTo) {
        return ResponseEntity.ok(ticketService.filterTickets(status, category, assignedTo));
    }

    // Update ticket status
    @PutMapping("/status")
    public ResponseEntity<TicketResponse> updateStatus(@RequestBody UpdateTicketStatusRequest request) {
        return ResponseEntity.ok(ticketService.updateStatus(request));
    }

    // Assign ticket to agent
    @PutMapping("/{ticketId}/assign")
    public ResponseEntity<TicketResponse> assignTicket(@PathVariable Long ticketId,
                                                       @RequestParam String agentEmail) {
        return ResponseEntity.ok(ticketService.assignTicket(ticketId, agentEmail));
    }

    // Add comment to ticket
    @PostMapping("/comment")
    public ResponseEntity<String> addComment(@RequestBody TicketCommentRequest request,
                                             Authentication authentication) {
        String userEmail = authentication.getName();
        ticketService.addComment(request, userEmail);
        return ResponseEntity.ok("Comment added successfully.");
    }
}
