package bradyo.password_checker.check;

import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class CheckRequest {
    @NotNull
    @Size(min = 1)
    private String password;
}
