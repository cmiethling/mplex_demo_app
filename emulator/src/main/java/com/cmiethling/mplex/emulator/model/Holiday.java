package com.cmiethling.mplex.emulator.model;

import lombok.Data;

// 90, ex21 Lombok
@Data
// 88, ex21 >> von Backend zu Frontend
public class Holiday {// pojo object zwischen frontend und backend

    // namen müssen wie in html (holiday.html oder wo immer diese Daten erstellt werden)
    private final String day;
    private final String reason;
    private final Type type;
    public enum Type {
        FESTIVAL, FEDERAL
    }
}
