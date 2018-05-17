package me.rubick.hufu.logistics.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import me.rubick.hufu.logistics.app.model.Message;

public interface MessageRepository extends JpaRepository<Message, Integer> {


}
