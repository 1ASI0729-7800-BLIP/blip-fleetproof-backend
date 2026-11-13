package com.blip.fleetproof.platform.userManagement.domain.model.aggregates;

import com.blip.fleetproof.platform.shared.domain.model.aggregates.AbstractDomainAggregateRoot;
import com.blip.fleetproof.platform.userManagement.domain.model.valueobjects.AccountType;

import java.util.Locale;

public class User extends AbstractDomainAggregateRoot<User> {
    private Long id;
    private String name;
    private String email;
    private String organization;
    private AccountType accountType;
    private String passwordHash;

    protected User() {
    }

    public User(String name, String email, String organization, AccountType accountType, String passwordHash) {
        this.name = required(name, "name");
        this.email = normalizeEmail(email);
        this.accountType = accountType == null ? AccountType.PERSONAL : accountType;
        this.organization = normalizeOrganization(organization, this.accountType);
        this.passwordHash = required(passwordHash, "passwordHash");
    }

    public User(Long id, String name, String email, String organization, AccountType accountType, String passwordHash) {
        this(name, email, organization, accountType, passwordHash);
        this.id = id;
    }

    public void updateProfile(String name, String organization) {
        this.name = required(name, "name");
        this.organization = normalizeOrganization(organization, accountType);
    }

    private static String required(String value, String field) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException(field + " must not be blank");
        }
        return value.trim();
    }

    private static String normalizeEmail(String value) {
        String email = required(value, "email").toLowerCase(Locale.ROOT);
        if (!email.matches("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$")) {
            throw new IllegalArgumentException("email must be valid");
        }
        return email;
    }

    private static String normalizeOrganization(String value, AccountType accountType) {
        String organization = value == null ? "" : value.trim();
        if (accountType == AccountType.COMPANY && organization.isEmpty()) {
            throw new IllegalArgumentException("organization is required for company accounts");
        }
        return organization;
    }

    public Long getId() { return id; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getOrganization() { return organization; }
    public AccountType getAccountType() { return accountType; }
    public String getPasswordHash() { return passwordHash; }
}
