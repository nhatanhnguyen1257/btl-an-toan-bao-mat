package vn.dev.na.auth.entitys;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "file_storage")
public class FileStorage  implements Serializable {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO,generator="native")
    @GenericGenerator(name = "native",strategy = "native")
    @Column(name = "id")
    private Long id;

    @Column(name = "account_id")
    private Integer accountId;

    @Column(name = "file_name")
    private String fileName;

    @Column(name = "file_size")
    private Long fileSize;
    @Column(name = "file_hash")
    private Long fileHash;
    @Column(name = "file_type")
    private Long fileType;
    @Column(name = "file_extension")
    private Long fileExtension;
    @Column(name = "file_code")
    private Long fileCode;
    @Column(name = "file_url")
    private Long fileUrl;
    @Column(name = "create_date")
    private LocalDateTime createDate;
    @Column(name = "is_delete")
    private int isDelete;

    @PrePersist
    public void pre() {
        createDate = LocalDateTime.now();
    }
}
