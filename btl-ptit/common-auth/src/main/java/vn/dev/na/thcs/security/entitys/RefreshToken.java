package vn.dev.na.thcs.security.entitys;


import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity(name = "refresh_token")
public class RefreshToken  implements Serializable {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO,generator="native")
    @GenericGenerator(name = "native",strategy = "native")
    @Column(name = "id")
    private Long id;
    @Column(name = "user_id")
    private Long userId;
    @Column(name = "refresh_token")
    private String refreshToken;
    @Column(name = "token")
    private String token;
    @Column(name = "ip_address")
    private String ipAddress;
    @Column(name = "user_agent")
    private String userAgent;
    @Column(name = "expiry_date")
    private LocalDateTime expiryDate;
    @Column(name = "create_date")
    private LocalDateTime createDate;

    @PrePersist
    public void pre() {
        createDate = LocalDateTime.now();
        refreshToken = UUID.randomUUID().toString();
        expiryDate = LocalDateTime.now().plusDays(30);
    }
}
