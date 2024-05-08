package vn.dev.na.thcs.security.entitys;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import vn.dev.na.thcs.security.helper.Common;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "account")
@Entity(name = "account")
public class Account implements Serializable {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO,generator="native")
    @GenericGenerator(name = "native",strategy = "native")
    @Column(name = "id")
    private Long id;
    @Column(name = "user_name")
    private String username;
    @Column(name = "password")
    private String password;
    @Column(name = "email")
    private String email;
    @Column(name = "full_name")
    private String fullName;
    @Column(name = "create_Date")
    private LocalDateTime createDate;
    @Column(name = "status")
    private Integer status;
    @Column(name = "code_cdn")
    private Integer codeCdn;

    @PrePersist
    public void pre() {
        createDate = LocalDateTime.now();
        status = Common.Account.ACCOUNT_STATUS.OK.ordinal();
    }
}
