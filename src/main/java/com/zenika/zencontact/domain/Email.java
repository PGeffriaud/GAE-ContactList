package com.zenika.zencontact.domain;

/**
 * Created by PierreG on 01/03/17.
 * Email DTO
 */
public class Email {

    public String subject;
    public String body;
    public String to;
    public String toName;

    @Override
    public String toString() {
        return "Email{" +
                "subject='" + subject + '\'' +
                ", body='" + body + '\'' +
                ", to='" + to + '\'' +
                ", toName='" + toName + '\'' +
                '}';
    }
}
