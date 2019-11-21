package ea.sof.ms_elastic_search.controller;

import ea.sof.ms_elastic_search.repository.RedisQuestionRepositoryImpl;
import ea.sof.ms_elastic_search.service.ElasticSearchService;
import ea.sof.ms_elastic_search.service.RedisQuestionService;
import ea.sof.shared.queue_models.QuestionQueueModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@RestController
@CrossOrigin
@RequestMapping("/elastic-search")
public class ElasticSearchController {
	private static final Logger LOGGER = LoggerFactory.getLogger(RedisQuestionRepositoryImpl.class);

	@Value("${maxhit}")
	public int maxhit;

	@Value("${redis.timeout}")
	public long timeout;

	@Autowired
	ElasticSearchService elasticSearchService;

	@Autowired
	RedisQuestionService redisQuestionService;

    @Value("${app.version}")
    private String appVersion;

    @GetMapping("/health")
    public ResponseEntity<?> index() {
        String host = "Unknown host";
        try {
            host = InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

        return new ResponseEntity<>("Elastic search service (" + appVersion + "). Host: " + host, HttpStatus.OK);
    }

	@GetMapping("/search/{phrase}")
	public List<QuestionQueueModel> searchByName(@PathVariable(name = "phrase") final String phrase) {
		LOGGER.info("New search: " + phrase);

		List<QuestionQueueModel> questions = null;

		//check if data exists in redis
		Map<String, QuestionQueueModel> redisQuestions = redisQuestionService.findAll(phrase);
		if (redisQuestions != null && redisQuestions.size() > 0) {
			questions = new ArrayList<QuestionQueueModel>(redisQuestions.values());
			LOGGER.info("Found " + questions.size() + " records from Redis");
			return questions;
		}

		//else, get from ElasticSearch engine and update Redis
		questions = elasticSearchService.search(phrase);
		LOGGER.info("Found " + questions.size() + " records from elastic search engine");
		redisQuestionService.saveAll(phrase, questions, timeout);

		return questions;
	}
}
