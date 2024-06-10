package com.java.recruitment.service.model.mail;

import lombok.Getter;

@Getter
public enum SmtpServer {

    GMAIL("smtp.gmail.com"),
    YAHOO("smtp.mail.yahoo.com"),
    OUTLOOK("smtp-mail.outlook.com"),
    HOTMAIL("smtp.live.com"),
    YANDEX("smtp.yandex.com");

    private final String serverAddress;

    SmtpServer(String serverAddress) {
        this.serverAddress = serverAddress;
    }

}
