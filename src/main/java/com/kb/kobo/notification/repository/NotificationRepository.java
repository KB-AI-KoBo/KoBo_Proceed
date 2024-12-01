package com.kb.kobo.notification.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.kb.kobo.notification.domain.Notification;


@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
}
