package com.cmiethling.mplex.emulator.model;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

// 90, ex21 Lombok
@Data
// 86, ex21 >> von UI zu backend
public class Contact {// pojo object zwischen frontend und backend

    // fields müssen wie in html (contact.html oder wo immer diese Daten erstellt werden)

    // 98, ex24: Bean validation
    @SuppressWarnings("GrazieInspection")
    @NotBlank(message = "Name must not be blank.") // " " ist bei @NotEmpty erlaubt, bei @NotBlank nicht
    private String name;
    // 98, ex24: Bean validation
    // @Pattern(regexp = "(^$|[0-9]{10})", message = "Mobile number must be 10 digits")
    @NotBlank(message = "Mobile number must not be blank.")
    private String mobileNum;
    // @Email // 98, ex24: Bean validation
    @NotBlank(message = "Email must not be blank.")
    private String email;
    private String subject;
    private String message;

    // 90, ex21 Lombok
    // public String getName() {
    //     return this.name;
    // }
    //
    // public void setName(final String name) {
    //     this.name = name;
    // }
    //
    // public String getMobileNum() {
    //     return this.mobileNum;
    // }
    //
    // public void setMobileNum(final String mobileNum) {
    //     this.mobileNum = mobileNum;
    // }
    //
    // public String getEmail() {
    //     return this.email;
    // }
    //
    // public void setEmail(final String email) {
    //     this.email = email;
    // }
    //
    // public String getSubject() {
    //     return this.subject;
    // }
    //
    // public void setSubject(final String subject) {
    //     this.subject = subject;
    // }
    //
    // public String getMessage() {
    //     return this.message;
    // }
    //
    // public void setMessage(final String message) {
    //     this.message = message;
    // }
    //
    // @Override
    // public String toString() {
    //     return "Contact{" +
    //             "name='" + this.name + '\'' +
    //             ", mobileNum='" + this.mobileNum + '\'' +
    //             ", email='" + this.email + '\'' +
    //             ", subject='" + this.subject + '\'' +
    //             ", message='" + this.message + '\'' +
    //             '}';
    // }
}
