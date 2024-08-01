package com.cmiethling.mplex.emulator.controller;

import com.cmiethling.mplex.emulator.config.Utils;
import com.cmiethling.mplex.emulator.model.Holiday;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

// 88, ex21 >> von Backend zu Frontend
@Controller
public class HolidaysController {

    @RequestMapping(value = Utils.HOLIDAYS, method = RequestMethod.GET) // get from Backend!
    public String displayHolidays(final Model model,
                                  // 93, ex22: queryparams from footer.html
                                  @RequestParam(required = false) final boolean festival,
                                  @RequestParam(required = false) final boolean federal) {
        // 93, ex22: queryparams: add to model >> send back to view holidays.html
        model.addAttribute("festival", festival);
        model.addAttribute("federal", federal);

        final var holidays = List.of(
                new Holiday("Jan 1", "New Years Day", Holiday.Type.FESTIVAL),
                new Holiday("Oct 31", "Halloween", Holiday.Type.FESTIVAL),
                new Holiday("Nov 24", "Thanksgiving Day", Holiday.Type.FESTIVAL),
                new Holiday("Dec 24", "Christmas", Holiday.Type.FESTIVAL),
                new Holiday("Jan 17", "Martin Luther King Jr. Day", Holiday.Type.FEDERAL),
                new Holiday("Jul 4", "Independence Day", Holiday.Type.FEDERAL),
                new Holiday("Sep 5", "Labor Day", Holiday.Type.FEDERAL),
                new Holiday("Nov 11", "Veterans Day", Holiday.Type.FEDERAL)
        );
        // Type enum zu String >> 2 (String) attribute: FESTIVAL und FEDERAL mit den jeweiligen holidays als liste
        final var types = Holiday.Type.values();
        for (final var type : types) {
            model.addAttribute(type.toString(),
                    holidays.stream().filter(holiday -> holiday.getType().equals(type)).toList());
        }
        // zu Thymeleaf: zeige holidays.html mit den neuen attributen im model (FEDERAL list, FESTIVAL list)
        // (man braucht (bzw. kann) nicht model zu übergeben, weil Thymeleaf sich darum automatisch kümmert
        return Utils.HOLIDAYS_HTML;
    }
}
