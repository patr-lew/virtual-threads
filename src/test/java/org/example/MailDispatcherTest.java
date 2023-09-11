package org.example;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MailDispatcherTest {

    public static final int REQUESTS_COUNT = 100_000;

    @Test
    void given_100_000_mails__send_them_most_effectively() {
        // given
        List<String> mails = generateMailList();
        ExecutorService executor = Executors.newSingleThreadExecutor();

        // when
        List<String> responses;
        try (executor) {
            responses = new MailDispatcher(executor).dispatchEmails(mails, "Hello there!");
        }

        // then
        assertEquals(responses.size(), REQUESTS_COUNT);
    }

    @Test
    void given_100_000_mails__and_platform_threads__send_mails_slowly() {
        // given
        var mails = generateMailList();
        var executor = Executors.newFixedThreadPool(12);

        // when
        List<String> responses;
        try (executor) {
            responses = new MailDispatcher(executor).dispatchEmails(mails, "Hello There!");
        }

        // then
        assertEquals(responses.size(), REQUESTS_COUNT);
    }

    @Test
    void given_100_000_mails__and_platform_thread_pool__hang_up() {
        // given
        var mails = generateMailList();
        var executor = Executors.newCachedThreadPool();

        // when
        List<String> responses;
        try (executor) {
            responses = new MailDispatcher(executor).dispatchEmails(mails, "Hello There!");
        }

        // then
        assertEquals(responses.size(), REQUESTS_COUNT);
    }

    @Test
    void given_100_000_mails__and_virtual_threads__send_them_fast() {
        // given
        var mails = generateMailList();
        var executor = Executors.newVirtualThreadPerTaskExecutor();

        // when
        List<String> responses;
        try (executor) {
            responses = new MailDispatcher(executor).dispatchEmails(mails, "Hello There!");
        }

        // then
        assertEquals(responses.size(), REQUESTS_COUNT);
    }

    private static List<String> generateMailList() {
        return IntStream.range(0, 100_000).mapToObj("mail%d@example.com"::formatted).toList();
    }
}