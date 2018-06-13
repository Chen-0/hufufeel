package me.rubick.transport.app.repository;

import me.rubick.transport.app.model.CostSubject;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CostSubjectRepository extends JpaRepository<CostSubject, Long> {

    CostSubject findTopByUserId(long userId);
}
