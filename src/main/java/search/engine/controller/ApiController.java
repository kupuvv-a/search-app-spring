package search.engine.controller;

import com.google.gson.Gson;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import search.engine.message.ResultResponse;
import search.engine.model.statistics.StatisticsResponse;
import search.engine.service.IServiceIndex;
import search.engine.service.IStatisticsService;

@RestController
@RequestMapping("/api")
public class ApiController {

    private final IStatisticsService iStatisticsService;
    private final IServiceIndex iServiceIndex;
    private final Gson gson = new Gson();

    public ApiController(IStatisticsService iStatisticsService, IServiceIndex iServiceIndex) {
        this.iStatisticsService = iStatisticsService;
        this.iServiceIndex = iServiceIndex;
    }

    @GetMapping("/statistics")
    public ResponseEntity<StatisticsResponse> statistics() {
        return ResponseEntity.ok(iStatisticsService.getStatistics());
    }

    @GetMapping("/startIndexing")
    public ResponseEntity<String> startIndexing() {
        boolean indexing = iServiceIndex.runIndexing();
        final ResultResponse response;
        if (indexing)
            response = new ResultResponse("false", "Индексация уже запущенна");
        else
            response = new ResultResponse("true", null);
        return new ResponseEntity<>(gson.toJson(response), HttpStatus.OK);
    }

    @GetMapping("/stopIndexing")
    public ResponseEntity<String> stopIndexing() {
        //todo
        return null;
    }

    //todo escaping all unwanted char
    @PostMapping("/indexPage")
    public ResponseEntity<ResultResponse> startIndexing(String url) {
        return ResponseEntity.ok(iServiceIndex.runIndexing(url));
    }

}
