package utils;

import java.util.concurrent.*;
import java.util.logging.Logger;

/**
 * 性能优化工具类
 * 
 * 优化点：
 * 1. 线程池管理
 * 2. 缓存机制
 * 3. 资源复用
 * 4. 性能监控
 */
public class PerformanceOptimizer {

    private static final Logger LOGGER = Logger.getLogger(PerformanceOptimizer.class.getName());
    
    // ============ 线程池 ============
    private static final ExecutorService executorService;
    private static final ForkJoinPool forkJoinPool;
    
    static {
        // 根据CPU核心数计算线程池大小
        int cpuCores = Runtime.getRuntime().availableProcessors();
        int threadPoolSize = Math.max(4, cpuCores * 2);
        
        executorService = new ThreadPoolExecutor(
            cpuCores,                          // 核心线程数
            threadPoolSize,                    // 最大线程数
            60L,                               // 空闲线程存活时间
            TimeUnit.SECONDS,
            new LinkedBlockingQueue<>(100),   // 任务队列
            new ThreadFactory() {
                private int count = 0;
                @Override
                public Thread newThread(Runnable r) {
                    Thread t = new Thread(r, "MemShellWorker-" + (++count));
                    t.setDaemon(true);
                    return t;
                }
            },
            new ThreadPoolExecutor.CallerRunsPolicy() // 拒绝策略
        );
        
        forkJoinPool = new ForkJoinPool(cpuCores);
        
        LOGGER.info("Thread pool initialized with " + threadPoolSize + " threads");
    }

    // ============ 缓存 ============
    private static final ConcurrentHashMap<String, Object> cache = new ConcurrentHashMap<>();
    private static final ScheduledExecutorService cacheCleaner = 
        Executors.newSingleThreadScheduledExecutor();
    
    static {
        // 定期清理过期缓存
        cacheCleaner.scheduleAtFixedRate(() -> {
            // 清理逻辑
            LOGGER.fine("Cache cleanup executed");
        }, 1, 1, TimeUnit.HOURS);
    }

    /**
     * 异步执行任务
     */
    public static Future<?> submitTask(Runnable task) {
        return executorService.submit(task);
    }

    /**
     * 异步执行任务并返回结果
     */
    public static <T> Future<T> submitTask(Callable<T> task) {
        return executorService.submit(task);
    }

    /**
     * 并行处理集合
     */
    public static <T> void parallelProcess(java.util.List<T> items, java.util.function.Consumer<T> processor) {
        forkJoinPool.submit(() ->
            items.parallelStream().forEach(processor)
        ).join();
    }

    /**
     * 添加缓存
     */
    public static void putCache(String key, Object value) {
        cache.put(key, value);
    }

    /**
     * 获取缓存
     */
    @SuppressWarnings("unchecked")
    public static <T> T getCache(String key) {
        return (T) cache.get(key);
    }

    /**
     * 获取缓存，如果不存在则计算
     */
    @SuppressWarnings("unchecked")
    public static <T> T getCacheOrCompute(String key, java.util.function.Supplier<T> supplier) {
        return (T) cache.computeIfAbsent(key, k -> supplier.get());
    }

    /**
     * 清除缓存
     */
    public static void clearCache() {
        cache.clear();
        LOGGER.info("Cache cleared");
    }

    /**
     * 获取缓存大小
     */
    public static int getCacheSize() {
        return cache.size();
    }

    /**
     * 关闭线程池（应用退出时调用）
     */
    public static void shutdown() {
        LOGGER.info("Shutting down thread pools...");
        
        executorService.shutdown();
        forkJoinPool.shutdown();
        cacheCleaner.shutdown();
        
        try {
            if (!executorService.awaitTermination(5, TimeUnit.SECONDS)) {
                executorService.shutdownNow();
            }
            if (!forkJoinPool.awaitTermination(5, TimeUnit.SECONDS)) {
                forkJoinPool.shutdownNow();
            }
        } catch (InterruptedException e) {
            executorService.shutdownNow();
            forkJoinPool.shutdownNow();
            Thread.currentThread().interrupt();
        }
        
        LOGGER.info("Thread pools shut down successfully");
    }

    /**
     * 获取系统性能信息
     */
    public static String getPerformanceInfo() {
        Runtime runtime = Runtime.getRuntime();
        long totalMemory = runtime.totalMemory() / 1024 / 1024;
        long freeMemory = runtime.freeMemory() / 1024 / 1024;
        long maxMemory = runtime.maxMemory() / 1024 / 1024;
        long usedMemory = totalMemory - freeMemory;
        
        StringBuilder sb = new StringBuilder();
        sb.append("=== Performance Info ===\n");
        sb.append("Active Threads: ").append(Thread.activeCount()).append("\n");
        sb.append("Used Memory: ").append(usedMemory).append(" MB\n");
        sb.append("Free Memory: ").append(freeMemory).append(" MB\n");
        sb.append("Total Memory: ").append(totalMemory).append(" MB\n");
        sb.append("Max Memory: ").append(maxMemory).append(" MB\n");
        sb.append("Cache Size: ").append(cache.size()).append("\n");
        sb.append("CPU Cores: ").append(runtime.availableProcessors());
        
        return sb.toString();
    }
}
