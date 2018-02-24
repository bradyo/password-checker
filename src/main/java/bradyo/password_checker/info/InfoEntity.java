package bradyo.password_checker.info;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "info")
public class InfoEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer importLine;
    private LocalDateTime importStartedAt;
    private LocalDateTime importCompletedAt;
    
}
