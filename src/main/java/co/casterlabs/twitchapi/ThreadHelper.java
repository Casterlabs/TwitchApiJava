package co.casterlabs.twitchapi;

import lombok.SneakyThrows;

public class ThreadHelper {

    public static void executeAsync(Runnable run) {
        new Thread(run).start();
    }

    public static void executeAsyncLater(Runnable run, long millis) {
        (new Thread() {
            @SneakyThrows
            @Override
            public void run() {
                Thread.sleep(millis);
                run.run();
            }
        }).start();
    }

}
