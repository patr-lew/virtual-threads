package org.example;

import java.util.List;
import java.util.concurrent.ExecutorService;

public class MailDispatcher {

    private final ExecutorService executor;

    public MailDispatcher(ExecutorService executor) {
        this.executor = executor;    }

    public void dispatchEmails(List<String> mails, String message) {
        mails.forEach(mail -> executor.submit(sendEmailTask(mail, message)));
    }

    private Runnable sendEmailTask(String mail, String message) {
        return () -> {
            try {
                Thread.sleep(1000); // sending the email for 1s
                System.out.printf("SENT to %s from %s%n", mail, Thread.currentThread());
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        };
    }
}
