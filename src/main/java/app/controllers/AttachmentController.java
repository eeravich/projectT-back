package app.controllers;

import app.entities.enums.Roles;
import app.generated.jooq.tables.Role;
import app.generated.jooq.tables.pojos.Attachment;
import app.repository.AttachmentRepository;
import app.services.AttachmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/attachment")
@RequiredArgsConstructor
public class AttachmentController {
    private final AttachmentService service;

    @PostMapping("/upload")
    @Transactional
    @Secured({Roles.Fields.ROLE_ADMIN, Roles.Fields.ROLE_MANAGER})
    public void save(@RequestParam("file") MultipartFile file) {
        service.save(file);
    }

    @GetMapping("/{attachmentId}")
    public ResponseEntity<byte[]> getById(@PathVariable Long attachmentId) {
        Attachment attachment = service.getById(attachmentId);
        return ResponseEntity.ok().contentType(MediaType.valueOf(attachment.getMimeType())).body(attachment.getData());
    }

    @DeleteMapping("/delete/{attachmentId}")
    @Transactional
    @Secured({Roles.Fields.ROLE_ADMIN, Roles.Fields.ROLE_MANAGER})
    public void delete(@PathVariable Long attachmentId) {
        service.delete(attachmentId);
    }
}
