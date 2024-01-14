package quantenpfad.todoapp.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Concurrency service, handling all tasks, required multiple threads to complete.
 */
@Service
@Slf4j
public class InternalThreadPool {

    private final ExecutorService executorService = Executors.newFixedThreadPool(4);

    public void runAsyncTask(Runnable runnable) {
        executorService.execute(runnable);
    }
}
