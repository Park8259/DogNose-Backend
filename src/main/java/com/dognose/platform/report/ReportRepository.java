package com.dognose.platform.report;

import com.dognose.platform.user.User;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReportRepository extends JpaRepository<Report, Long> {

    List<Report> findAllByReporterOrderByCreatedAtDesc(User reporter);

    List<Report> findAllByOrderByCreatedAtDesc();

    List<Report> findAllByStatusOrderByCreatedAtDesc(ReportStatus status);
}
