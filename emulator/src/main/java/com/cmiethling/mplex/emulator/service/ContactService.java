package com.cmiethling.mplex.emulator.service;

import com.cmiethling.mplex.emulator.model.Contact;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.ApplicationScope;

@Slf4j // 91, ex21: Lombok
// 86, ex21 >> pojo object zwischen ui und backend (Service layer)
@Service // stereotype

// @RequestScope // 103, ex 25: jedes mal wenn ich eine contact form submitte gibt es eine neue bean
// @SessionScope // 104, ex 25 >> jeder user bekommt eine eigene Bean
@ApplicationScope // 105, ex 25 >> jeder user bekommt die selbe bean
@Data
public class ContactService {
    // 91, ex21: Lombok
    // private static final Logger log = LoggerFactory.getLogger(ContactService.class);

    // 103, ex 25: Request scoped bean
    private int counter = 0; // wird immer wieder 0 sein, weil für jeden request eine neue bean erstellt wird

    public ContactService() {
        System.out.println("ContactService bean initialized");
    }

    public boolean saveMessageDraft(final Contact contact) {
        final var isSaved = true;
        // TODO need to persist into DB table
        log.info(contact.toString());
        return isSaved;
    }
}
