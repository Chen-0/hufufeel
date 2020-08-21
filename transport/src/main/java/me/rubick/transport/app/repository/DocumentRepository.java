package me.rubick.transport.app.repository;

import me.rubick.transport.app.model.Document;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DocumentRepository extends JpaRepository<Document, Long> {

    Document findByName(String name);
}
