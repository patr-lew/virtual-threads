package org.example;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

public class MailDispatcher {

    private final ExecutorService executor;

    public MailDispatcher(ExecutorService executor) {
        this.executor = executor;
    }

    public List<String> dispatchEmails(List<String> mails, String message) {
        var callables = mails.stream().map(m -> wrapToCallable(m, message)).toList();
        return invokeAllAndGet(callables);
    }

    private List<String> invokeAllAndGet(List<Callable<String>> callables) {
        try {
            return executor.invokeAll(callables).stream()
                    .map(this::getFuture)
                    .toList();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private Callable<String> wrapToCallable(String mail, String message) {
        return () -> {
            try {
                Thread.sleep(1000); // sending the email for 1s
                System.out.printf("SENT %s to %s from %s%n", message, mail, Thread.currentThread());
                return "OK";
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        };
    }

    private String getFuture(Future<String> stringFuture) {
        try {
            return stringFuture.get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }
}
