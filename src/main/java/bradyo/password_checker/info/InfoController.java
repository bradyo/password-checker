package bradyo.password_checker.info;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.ZoneOffset;

@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class InfoController {

    private final InfoRepository infoRepository;
    
    @GetMapping("/")
    public Info getInfo() {
        Info info = new Info();
        InfoEntity infoEntity = infoRepository.findOne(1L);
        if (infoEntity != null) {
            info.setImportStartedAt(infoEntity.getImportStartedAt().atOffset(ZoneOffset.UTC).toString());
            if (infoEntity.getImportCompletedAt() != null) {
                info.setImportCompletedAt(infoEntity.getImportCompletedAt().atOffset(ZoneOffset.UTC).toString());
            }
            info.setImportLine(infoEntity.getImportLine());
        } else {
            info.setImportLine(0);
        }
            
        return info;
    }

}
