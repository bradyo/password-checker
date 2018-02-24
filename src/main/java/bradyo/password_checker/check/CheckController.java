package bradyo.password_checker.check;

import com.google.common.base.Charsets;
import com.google.common.hash.Hashing;
import bradyo.password_checker.password_hash.PasswordHashEntity;
import bradyo.password_checker.password_hash.PasswordHashRespository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CheckController {
    
    private final PasswordHashRespository passwordHashRespository;
    
    @PostMapping("/passwordChecks")
    public CheckResult createCheck(@Valid @RequestBody CheckRequest checkRequest) {
        String password = checkRequest.getPassword();
        String passwordSha1Hash = Hashing.sha1().hashString( password, Charsets.UTF_8 ).toString()
            .toUpperCase();
        
        boolean isCompromised = false;
        PasswordHashEntity passwordHashEntity = passwordHashRespository.findOneBySha1Hash(passwordSha1Hash);
        if (passwordHashEntity != null) {
            isCompromised = true;
        }
        
        CheckResult checkResult = new CheckResult();
        checkResult.setCompromised(isCompromised);
        
        return checkResult;
    }
}
