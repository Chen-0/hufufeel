package me.rubick.transport.app.repository;

import me.rubick.transport.app.model.Document;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DocumentRepository extends JpaRepository<Document, Long> {

    Document findByPathName(String pathName);

    List<Document> findFirst5ByOrderByIdDesc();
}
