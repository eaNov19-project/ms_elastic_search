package ea.sof.ms_elastic_search.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import ea.sof.ms_elastic_search.service.ElasticSearchService;
import ea.sof.ms_elastic_search.service.RedisQuestionService;
import ea.sof.shared.queue_models.QuestionQueueModel;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.Client;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/elastic-search")
public class ElasticSearchController {

    @Value("${maxhit}")
    public int maxhit;

    @Value("${redis.timeout}")
    public long timeout;

    @Autowired
    ElasticSearchService elasticSearchService;

    @Autowired
    RedisQuestionService redisQuestionService;

    @GetMapping("/search/{phrase}")
    public List<QuestionQueueModel> searchByName(@PathVariable(name="phrase") final String phrase) {

        List<QuestionQueueModel> questions = null;

        //check if data exists in redis
        Map<String, QuestionQueueModel> redisQuestions = redisQuestionService.findAll(phrase);
        if(redisQuestions != null && redisQuestions.size() > 0) {
            //get from redis
            questions = new ArrayList<QuestionQueueModel>(redisQuestions.values());
            return  questions;
        }

        //else, get from ElasticSearch engine and update Redis
        questions = elasticSearchService.search(phrase);
        redisQuestionService.saveAll(phrase, questions, timeout);

        return questions;
    }
}
