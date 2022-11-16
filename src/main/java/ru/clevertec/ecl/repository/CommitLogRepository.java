package ru.clevertec.ecl.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.clevertec.ecl.model.CommitLog;

public interface CommitLogRepository extends JpaRepository<CommitLog, Long> {

}
