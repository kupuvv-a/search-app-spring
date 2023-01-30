package search.engine.service;

import search.engine.message.ResultResponse;

public interface IServiceIndex {

    boolean stopIndexing();

    boolean runIndexing();

    ResultResponse runIndexing(String url);
}
