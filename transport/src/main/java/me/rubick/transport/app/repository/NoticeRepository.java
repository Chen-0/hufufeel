package me.rubick.transport.app.repository;

import me.rubick.transport.app.model.Notice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NoticeRepository extends JpaRepository<Notice, Long> {
}
