package bradyo.password_checker;

import bradyo.password_checker.info.InfoRepository;
import bradyo.password_checker.info.InfoEntity;
import bradyo.password_checker.password_hash.PasswordHashEntity;
import bradyo.password_checker.password_hash.PasswordHashRespository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.env.Environment;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Slf4j
public class ApplicationStartup implements ApplicationListener<ApplicationReadyEvent> {

    private final Environment environment;
    private final InfoRepository infoRepository;
    private final PasswordHashRespository passwordHashRespository;
    
    @Override
    public void onApplicationEvent(final ApplicationReadyEvent event) {
        log.info("Start up complete, checking for password hash import");
        
        InfoEntity infoEntity = infoRepository.findOne(1L);
        if (infoEntity == null) {
            infoEntity = new InfoEntity();
            infoEntity.setImportLine(0);
            infoEntity.setImportStartedAt(LocalDateTime.now());
            infoRepository.save(infoEntity);
        }
        
        if (infoEntity.getImportCompletedAt() != null) {
            log.info("Password hash already imported");
        } else {
            if (infoEntity.getImportLine() == 0) {
                log.info("Password hash import starting");
            } else {
                log.info("Password hash import continuing from line " + infoEntity.getImportLine());
            }
            startImport(infoEntity);
        } 
    }
    
    private void startImport(InfoEntity infoEntity) {
        File file = new File(environment.getProperty("passwordsFile"));
        LineIterator it = null;
        try {
            it = FileUtils.lineIterator(file, "UTF-8");
        } catch (IOException e) {
            throw new RuntimeException("Failed to read passwords file", e);
        }
        try {
            int lineNumber = 0;
            while (it.hasNext()) {
                String line = it.nextLine().trim();
                
                lineNumber++;
                if (lineNumber < infoEntity.getImportLine()) {
                    continue;
                }
                
                log.debug("Importing line " + lineNumber + ": " + line);

                String[] values = line.split(":");
                if (values.length == 2) {
                    String sha1Hash = values[0];
                    Integer count = Integer.parseInt(values[1]);
                    try {
                        PasswordHashEntity passwordHashEntity = new PasswordHashEntity();
                        passwordHashEntity.setSha1Hash(sha1Hash);
                        passwordHashEntity.setCount(count);
                        passwordHashRespository.save(passwordHashEntity);

                        infoEntity.setImportLine(lineNumber);
                        infoRepository.save(infoEntity);
                    } catch (DataIntegrityViolationException e) {
                        log.debug("Skipping duplicate password hash");
                    }
                } else {
                    log.debug("Invalid line skipped");
                }
            }
        } finally {
            LineIterator.closeQuietly(it);
        }
        
        infoEntity.setImportCompletedAt(LocalDateTime.now());
        infoRepository.save(infoEntity);
        
        log.info("Completed password hash import");
    }

}
