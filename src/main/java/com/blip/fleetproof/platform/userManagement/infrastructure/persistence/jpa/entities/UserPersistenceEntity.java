package com.blip.fleetproof.platform.userManagement.infrastructure.persistence.jpa.entities;

import com.blip.fleetproof.platform.shared.infrastructure.persistence.jpa.entities.AuditableAbstractPersistenceEntity;
import com.blip.fleetproof.platform.userManagement.domain.model.aggregates.User;
import com.blip.fleetproof.platform.userManagement.domain.model.valueobjects.AccountType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;

@Entity
@Table(name = "users")
public class UserPersistenceEntity extends AuditableAbstractPersistenceEntity {
    @Column(nullable = false, length = 120)
    private String name;
    @Column(nullable = false, unique = true, length = 180)
    private String email;
    @Column(nullable = false, length = 180)
    private String organization;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private AccountType accountType;
    @Column(nullable = false, length = 128)
    private String passwordHash;

    protected UserPersistenceEntity() {
    }

    public UserPersistenceEntity(User user) {
        setId(user.getId());
        this.name = user.getName();
        this.email = user.getEmail();
        this.organization = user.getOrganization();
        this.accountType = user.getAccountType();
        this.passwordHash = user.getPasswordHash();
    }

    public User toDomain() {
        return new User(getId(), name, email, organization, accountType, passwordHash);
    }

    public void updateFrom(User user) {
        name = user.getName();
        organization = user.getOrganization();
    }
}
