package com.securepass.analyzer.repository;

import com.securepass.analyzer.model.PasswordHistory;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PasswordHistoryRepository extends JpaRepository<PasswordHistory, Long> {

    List<PasswordHistory> findTop10ByOrderByAnalyzedAtDesc();
}
