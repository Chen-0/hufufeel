package me.rubick.transport.app.repository;

import me.rubick.transport.app.model.Message;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface MessageRepository extends JpaRepository<Message, Long> {

    int countByUserIdAndIsRead(long userId, boolean isRead);

    Page<Message> findByUserIdAndIsRead(long userId, boolean isRead, Pageable pageable);

    Page<Message> findByUserId(long userId, Pageable pageable);

    @Modifying
    @Query("update Message m set m.isRead = true where m.userId = ?1")
    void updateAllToRead(long userId);
}
