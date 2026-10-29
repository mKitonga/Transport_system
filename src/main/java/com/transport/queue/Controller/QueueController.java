package com.transport.queue.Controller;

import com.transport.queue.Form.CreateArrivalForm;
import com.transport.queue.Form.DispatchQueueEntryForm;
import com.transport.queue.Service.QueueEntryEditService;
import com.transport.queue.Service.QueueEntryReadService;
import com.transport.queue.View.QueueEntryView;
import com.transport.liby.service.Message;
import com.transport.liby.form.SessionUserIdForm;
import com.transport.liby.view.EntityApiResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Locale;

import static com.transport.liby.service.SystemConfig.STS_USER_BASE_URL;

@RestController
@RequestMapping(STS_USER_BASE_URL + "/queue")
public class QueueController {

    private QueueEntryEditService queueEntryEditService;
    private QueueEntryReadService queueEntryReadService;

    @PreAuthorize("hasAuthority('NOTIFY_ARRIVAL')")
    @PostMapping("/sacco-driver/arrivals")
    public ResponseEntity<EntityApiResponse<QueueEntryView>> createArrival(
            @RequestBody @Valid CreateArrivalForm form,
            @RequestHeader("Idempotency-Key") @NotBlank @Size(max = 128) String idempotencyKey,
            Authentication auth,
            Locale locale) {
        form.setSessionUserId(auth.getName());
        var entry = queueEntryEditService.createArrival(form, idempotencyKey.trim());

        return ResponseEntity.status(HttpStatus.CREATED).body(new EntityApiResponse<>(
                true,
                HttpStatus.CREATED.value(),
                Message.get("queue.arrival.success", locale),
                new QueueEntryView(entry)
        ));
    }

    @PreAuthorize("hasAuthority('VIEW_CURRENT_QUEUE_ENTRY')")
    @GetMapping("/sacco-driver/arrivals/current")
    public ResponseEntity<QueueEntryView> getCurrentArrival(Authentication auth) {
        return queueEntryReadService.findCurrentEntry(auth.getName())
                .map(entry -> ResponseEntity.ok(new QueueEntryView(entry)))
                .orElseGet(() -> ResponseEntity.noContent().build());
    }

    @PreAuthorize("hasAuthority('VIEW_QUEUE_ENTRY')")
    @GetMapping("/sacco-admin/view-entry")
    public List<QueueEntryView> getAdminQueue(Authentication auth) {
        return queueEntryReadService.listActiveEntriesForAdmin(auth.getName())
                .stream()
                .map(QueueEntryView::new)
                .toList();
    }

    @PreAuthorize("hasAuthority('UPDATE_QUEUE_STATUS_TO_DISPATCHED')")
    @PutMapping("/sacco-admin/{entryId}/dispatch")
    public EntityApiResponse<QueueEntryView> dispatch(
            @PathVariable String entryId,
            @RequestBody @Valid DispatchQueueEntryForm form,
            Authentication auth,
            Locale locale) {
        form.setSessionUserId(auth.getName());
        var entry = queueEntryEditService.dispatch(entryId, form);
        return new EntityApiResponse<>(
                Message.get("queue.dispatch.success", locale),
                new QueueEntryView(entry)
        );
    }

    @PreAuthorize("hasAuthority('UPDATE_QUEUE_STATUS_TO_LEFT')")
    @PutMapping("/sacco-admin/{entryId}/left")
    public EntityApiResponse<QueueEntryView> markLeft(
            @PathVariable String entryId,
            Authentication auth,
            Locale locale) {
        var form = new SessionUserIdForm();
        form.setSessionUserId(auth.getName());
        var entry = queueEntryEditService.markLeft(entryId, form);
        return new EntityApiResponse<>(
                Message.get("queue.left.success", locale),
                new QueueEntryView(entry)
        );
    }

    @Autowired
    public void setQueueEntryEditService(QueueEntryEditService queueEntryEditService) {
        this.queueEntryEditService = queueEntryEditService;
    }

    @Autowired
    public void setQueueEntryReadService(QueueEntryReadService queueEntryReadService) {
        this.queueEntryReadService = queueEntryReadService;
    }
}
