package search.engine.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import search.engine.model.statistics.StatisticsResponse;
import search.engine.service.IStatisticsService;


@RestController
@RequestMapping("/api")
public class ApiController {

    private final IStatisticsService IStatisticsService;

    public ApiController(IStatisticsService IStatisticsService) {
        this.IStatisticsService = IStatisticsService;
    }

    @GetMapping("/statistics")
    public ResponseEntity<StatisticsResponse> statistics() {
        return ResponseEntity.ok(IStatisticsService.getStatistics());
    }
}
