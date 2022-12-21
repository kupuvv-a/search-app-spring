package search.engine.crawler;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ForkJoinPool;

@Slf4j
public class Main {

    public static void main(String[] args) {

        String startPage = "https://skillbox.ru/";
        log.info("info:{}", startPage);

        ForkJoinPool forkJoinPool = new ForkJoinPool(4);
        forkJoinPool.invoke(new CrawlerTask(startPage));

    }
}
