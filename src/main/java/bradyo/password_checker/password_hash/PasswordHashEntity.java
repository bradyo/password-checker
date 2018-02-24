package bradyo.password_checker.password_hash;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "password_hash")
public class PasswordHashEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "sha1_hash")
    private String sha1Hash;
    
    private Integer count;
    
}
