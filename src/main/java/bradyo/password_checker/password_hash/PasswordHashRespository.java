package bradyo.password_checker.password_hash;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PasswordHashRespository extends JpaRepository<PasswordHashEntity, Long> {
    
    PasswordHashEntity findOneBySha1Hash(String sha1Hash);

}
