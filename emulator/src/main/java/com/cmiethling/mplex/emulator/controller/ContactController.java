package com.cmiethling.mplex.emulator.controller;

import com.cmiethling.mplex.emulator.config.Utils;
import com.cmiethling.mplex.emulator.model.Contact;
import com.cmiethling.mplex.emulator.service.ContactService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

@Slf4j // 91, ex21: Lombok
@Controller // stereotype
public class ContactController {
    public static final String SAVE_MSG = "/saveMsg";

    // 91, ex21: Lombok
    // private static final Logger log = LoggerFactory.getLogger(ContactController.class);

    // 86, ex21
    private final ContactService contactService;

    @Autowired
    public ContactController(final ContactService contactService) {
        this.contactService = contactService;
    }

    // @RequestMapping(Utils.CONTACT) // 84, ex21
    // public String displayContactPage() {
    //     return "contact.html";
    // }

    // 95, ex23: @PathVariable (anderes Beispiel als Kurs (der hatte Holidays verändert))
    @GetMapping(value = {Utils.CONTACT + "/{name}/{email}", Utils.CONTACT})
    public String displayContactPage2(@PathVariable(required = false) final String name,
                                      @PathVariable(required = false) final String email,
                                      final Model model) {
        // 95, ex23: @PathVariable (anderes Beispiel als Kurs (der hatte Holidays verändert))
        // egal ob null
        model.addAttribute("name2", name); // 95, ex23: geht nicht mehr wegen contact attribut

        final var contact = new Contact();
        contact.setEmail(email); // 95, ex23: veränderung für @PathVariable
        contact.setName(name);
        // 1) 99, ex24: Bean validation (establish link from backend bean (pojo Contact) to frontend form
        // >> indem man eine blanke Contact bean der frontend form übergibt
        model.addAttribute("contact", contact);

        return Utils.CONTACT_HTML;
    }

    // // 85, ex21
    // @RequestMapping(value = "/saveMsg", method = RequestMethod.POST)
    // // @PostMapping("/saveMsg") // das gleiche
    // public ModelAndView saveMessage(@RequestParam("name") final String bla, @RequestParam String mobileNum,
    //                                 @RequestParam String email, @RequestParam String subject,
    //                                 @RequestParam String message) {
    //     this.log.info("name: " + bla);
    //     this.log.info("mobileNum: " + mobileNum);
    //     this.log.info("email: " + email);
    //     this.log.info("subject: " + subject);
    //     this.log.info("message: " + message);
    //     // redirect to map "/contact" >> to method displayContactPage()
    //     return new ModelAndView("redirect:" + Utils.CONTACT);
    // }

    // 86, ex21
    @RequestMapping(value = SAVE_MSG, method = RequestMethod.POST)
    // 99, ex24: Bean validation: @Valid >> schau nach validation annotations in pojo Contact
    // 99, ex24: falls validation error wirft ist es in errors und ich muss mich drum kümmern
    // 99, ex24: @ModelAttribute("contactXXX") holt sich contactXXX aus der form in contact.html:
    // <form th:action="@{/saveMsg}"... th:object="${contactXXX}">
    public String saveMessage(@Valid @ModelAttribute("contact") final Contact contact, final Errors errors) {
        if (errors.hasErrors()) {
            log.error("Contact form validation failed due to: " + errors);
            return Utils.CONTACT_HTML;
        }
        // in controller layer nur validations, andere business logic im service layer
        this.contactService.saveMessageDraft(contact);
        // 103, ex 25: Request scoped bean
        this.contactService.setCounter(this.contactService.getCounter() + 1);
        System.out.println("Number of times the contact form is submitted: " + this.contactService.getCounter());
        return "redirect:" + Utils.CONTACT;
    }
}
