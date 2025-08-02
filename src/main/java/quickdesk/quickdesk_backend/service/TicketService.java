package quickdesk.quickdesk_backend.service;

import org.springframework.stereotype.Service;
import quickdesk.quickdesk_backend.dto.*;
import quickdesk.quickdesk_backend.model.*;
import quickdesk.quickdesk_backend.repository.*;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class TicketService {

    private final TicketRepository ticketRepository;
    private final UserRepository userRepository;
    private final TicketCommentRepository commentRepository;
    private final EmailService emailService;

    public TicketService(
            TicketRepository ticketRepository,
            UserRepository userRepository,
            TicketCommentRepository commentRepository,
            EmailService emailService
    ) {
        this.ticketRepository = ticketRepository;
        this.userRepository = userRepository;
        this.commentRepository = commentRepository;
        this.emailService = emailService;
    }

    // CREATE TICKET
    public TicketResponse createTicket(CreateTicketRequest request, String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Ticket ticket = new Ticket();
        ticket.setSubject(request.getSubject());
        ticket.setDescription(request.getDescription());
        ticket.setCategory(request.getCategory());
        ticket.setAttachmentUrl(request.getAttachmentUrl());
        ticket.setStatus("OPEN");
        ticket.setCreatedBy(user);
        ticket.setCreatedAt(LocalDateTime.now());
        ticket.setUpdatedAt(LocalDateTime.now());

        ticketRepository.save(ticket);
        emailService.sendEmail(
                user.getEmail(),
                "Ticket Created - QuickDesk",
                "Hi " + user.getName() + ",\n\nYour ticket \"" + ticket.getSubject() + "\" has been created successfully.\n\nStatus: OPEN\n\nThank you,\nQuickDesk Support"
        );


        return mapToResponse(ticket);
    }

    // USER: VIEW OWN TICKETS
    public List<TicketResponse> getUserTickets(String userEmail) {
        return ticketRepository.findAllByCreatedByEmail(userEmail)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // AGENT: VIEW ALL TICKETS
    public List<TicketResponse> getAllTicketsForAgent(String agentEmail) {
        return ticketRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // FILTER BY STATUS / CATEGORY / ASSIGNED TO
    public List<TicketResponse> filterTickets(Optional<String> status, Optional<String> category, Optional<String> assignedTo) {
        List<Ticket> tickets = ticketRepository.findAll();

        if (status.isPresent()) {
            tickets = tickets.stream()
                    .filter(t -> t.getStatus().equalsIgnoreCase(status.get()))
                    .collect(Collectors.toList());
        }

        if (category.isPresent()) {
            tickets = tickets.stream()
                    .filter(t -> t.getCategory().equalsIgnoreCase(category.get()))
                    .collect(Collectors.toList());
        }

        if (assignedTo.isPresent()) {
            tickets = tickets.stream()
                    .filter(t -> t.getAssignedTo() != null &&
                            t.getAssignedTo().getEmail().equalsIgnoreCase(assignedTo.get()))
                    .collect(Collectors.toList());
        }

        return tickets.stream().map(this::mapToResponse).collect(Collectors.toList());
    }

    // UPDATE STATUS
    public TicketResponse updateStatus(UpdateTicketStatusRequest request) {
        Ticket ticket = ticketRepository.findById(request.getTicketId())
                .orElseThrow(() -> new RuntimeException("Ticket not found"));

        ticket.setStatus(request.getStatus());
        ticket.setUpdatedAt(LocalDateTime.now());

        ticketRepository.save(ticket);

        emailService.sendEmail(
                ticket.getCreatedBy().getEmail(),
                "Ticket Status Updated - QuickDesk",
                "Hi " + ticket.getCreatedBy().getName() + ",\n\nThe status of your ticket \"" + ticket.getSubject() + "\" has been updated to: " + request.getStatus() + ".\n\nThank you,\nQuickDesk Support"
        );
        return mapToResponse(ticket);
    }

    // ASSIGN TICKET
    public TicketResponse assignTicket(Long ticketId, String agentEmail) {
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new RuntimeException("Ticket not found"));

        User agent = userRepository.findByEmail(agentEmail)
                .orElseThrow(() -> new RuntimeException("Agent not found"));

        ticket.setAssignedTo(agent);
        ticket.setUpdatedAt(LocalDateTime.now());

        return mapToResponse(ticketRepository.save(ticket));
    }

    // ADD COMMENT
    public void addComment(TicketCommentRequest request, String userEmail) {
        Ticket ticket = ticketRepository.findById(request.getTicketId())
                .orElseThrow(() -> new RuntimeException("Ticket not found"));

        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        TicketComment comment = new TicketComment();
        comment.setTicket(ticket);
        comment.setComment(request.getComment());
        comment.setCommentedBy(user);
        comment.setCreatedAt(LocalDateTime.now());

        commentRepository.save(comment);
    }

    // MAPPING TO DTO
    private TicketResponse mapToResponse(Ticket ticket) {
        TicketResponse response = new TicketResponse();
        response.setId(ticket.getId());
        response.setSubject(ticket.getSubject());
        response.setDescription(ticket.getDescription());
        response.setCategory(ticket.getCategory());
        response.setStatus(ticket.getStatus());
        response.setAttachmentUrl(ticket.getAttachmentUrl());
        response.setCreatedBy(ticket.getCreatedBy().getEmail());
        response.setAssignedTo(ticket.getAssignedTo() != null ? ticket.getAssignedTo().getEmail() : null);
        response.setCreatedAt(ticket.getCreatedAt());
        response.setUpdatedAt(ticket.getUpdatedAt());

        List<String> commentTexts = ticket.getComments()
                .stream()
                .map(TicketComment::getComment)
                .collect(Collectors.toList());

        response.setComments(commentTexts);

        return response;
    }
}
