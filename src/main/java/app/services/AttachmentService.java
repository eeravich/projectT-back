package app.services;

import app.generated.jooq.tables.pojos.Attachment;
import app.repository.AttachmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class AttachmentService {
    private final AttachmentRepository repository;

    public void save(MultipartFile file) {
        Attachment attachment = new Attachment();
        attachment.setName(file.getOriginalFilename());
        attachment.setMimeType(file.getContentType());
        attachment.setSize(file.getSize());
        try {
            attachment.setData(file.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }

        repository.save(attachment);
    }

    public void delete(Long id) {
        repository.delete(id);
    }

    public Attachment getById(Long id) {
        return repository.getById(id);
    }
}
