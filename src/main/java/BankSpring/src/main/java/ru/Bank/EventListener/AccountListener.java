package ru.Bank.EventListener;

import org.hibernate.event.service.spi.EventListenerRegistry;
import org.hibernate.event.spi.EventType;
import org.hibernate.event.spi.PreInsertEvent;
import org.hibernate.event.spi.PreInsertEventListener;
import org.hibernate.internal.SessionFactoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import ru.Bank.Entity.Account;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PrePersist;
import javax.persistence.PreRemove;
import javax.persistence.PreUpdate;
import java.time.Instant;

@Component
public class AccountListener {
    @PrePersist
    @PreUpdate
    private void beforeOperation(Object object) {
        if (object instanceof Account) {
            ((Account) object).setUpdatedAt(Instant.now().getEpochSecond());
        }
    }

    @PreRemove
    private void beforeRemove(Object object) {
        if (object instanceof Account) {
            ((Account) object).setDeletedAt(Instant.now().getEpochSecond());
        }
    }
}