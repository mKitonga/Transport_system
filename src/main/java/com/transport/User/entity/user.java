package com.transport.User.entity;

import com.transport.liby.entity.BaseJpaEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "dtype", discriminatorType = DiscriminatorType.STRING, length = 31)
@DiscriminatorValue("User")
@Table(indexes = {
        @Index(name = "idx_user_type", columnList = "user_type")
})
public class User extends BaseJpaEntity {

    @Column
    private String name;

    @Column
    private String email;

    private boolean emailVerified;

    @Column
    private String phoneNumber;

    private boolean phoneNumberVerified;

    @Column
    private String password;

    @Enumerated(EnumType.STRING)
    @Column
    private UserType userType;

    @Enumerated(EnumType.STRING)
    @Column
    private UserStatus userStatus;

    @Column(length = 2048)
    private String publicKey;

    @Column
    private String recentAuthId;

    @Column
    private String notificationId;

    public String getUserTypeStr() {
        return userType == null ? "" : userType.name();
    }

    public String getUsername() {
        String email = getEmail() == null ? "" : getEmail();

        return String.format(
                "%s | %s %s %s",
                getName(),
                email,
                phoneNumber,
                getUserTypeStr()
        );
    }

    public boolean isActive(){
        return getUserStatus() == UserStatus.ACTIVE;
    }
}
