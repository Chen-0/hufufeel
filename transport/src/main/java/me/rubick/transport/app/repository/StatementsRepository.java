package me.rubick.transport.app.repository;

import me.rubick.transport.app.constants.StatementStatusEnum;
import me.rubick.transport.app.constants.StatementTypeEnum;
import me.rubick.transport.app.model.Statements;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StatementsRepository extends JpaRepository<Statements, Long> {

    Page<Statements> findByUserId(long userId, Pageable pageable);

    List<Statements> findByUserIdAndStatusAndTypeIn(long userId, StatementStatusEnum statementStatusEnum, List<StatementTypeEnum> statementTypeEnum);

    List<Statements> findByTargetAndTypeIn(String target, List<StatementTypeEnum> statementTypeEnum);
}
