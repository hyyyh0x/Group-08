package dbdr.domain.guardian.entity;

import dbdr.domain.core.base.entity.BaseEntity;
import dbdr.domain.recipient.entity.Recipient;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Pattern;
import java.time.LocalTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

@Entity
@Getter
@Table(name = "guardians")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLDelete(sql = "UPDATE guardians SET is_active = false WHERE id = ?")
@SQLRestriction("is_active = true")
public class Guardian extends BaseEntity {
    @Column(unique = true)
    private String loginId;

    private String loginPassword;

    @Column(nullable = false, unique = true)
    @Pattern(regexp = "010\\d{8}")
    private String phone;

    @Column(nullable = false, length = 50)
    private String name;

    @Column(nullable = true)
    private String lineUserId;

    @Column(nullable = true)
    private LocalTime alertTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recipient_id")
    private Recipient recipient;

    @Builder
    public Guardian(String phone, String name) {
        this.phone = phone;
        this.name = name;
        this.alertTime = LocalTime.of(9, 0); // 오전 9시로 초기화
    }

    public void updateGuardian(String phone, String name) {
        this.phone = phone;
        this.name = name;
    }

    public void updateLineUserId(String lineUserId) {
        this.lineUserId = lineUserId;
    }

    public void updateAlertTime(LocalTime alertTime) {
        this.alertTime = alertTime;
    }
}
