package org.example;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.IntStream;

class MailDispatcherTest {

    @Test
    void given_100_000_mails__send_them_most_effectively() {
        // given
        List<String> mails = generateMailList();
        ExecutorService executor = null;

        // when
        new MailDispatcher(executor).dispatchEmails(mails, "Hello there!");
    }

    @Test
    void given_100_000_mails__and_platform_threads__send_mails_slowly() {
        // given
        var mails = generateMailList();
        var executor = Executors.newFixedThreadPool(12);

        // when
        try (executor) {
            new MailDispatcher(executor).dispatchEmails(mails, "Hello There!");
        }
    }

    @Test
    void given_100_000_mails__and_platform_thread_pool__hang_up() {
        // given
        var mails = generateMailList();
        var executor = Executors.newCachedThreadPool();

        // when
        try (executor) {
            new MailDispatcher(executor).dispatchEmails(mails, "Hello There!");
        }
    }

    @Test
    void given_10_000_mails__and_virtual_threads__send_them_fast() {
        // given
        var mails = generateMailList();
        var executor = Executors.newVirtualThreadPerTaskExecutor();

        // when
        try (executor) {
            new MailDispatcher(executor).dispatchEmails(mails, "Hello There!");
        }
    }

    private static List<String> generateMailList() {
        return IntStream.range(0, 100_000).mapToObj("mail%d@example.com"::formatted).toList();
    }
}