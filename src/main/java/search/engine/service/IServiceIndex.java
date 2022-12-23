package search.engine.service;

import search.engine.message.ResultResponse;

public interface IServiceIndex {
    ResultResponse runIndexing();

    ResultResponse runIndexing(String url);
}
