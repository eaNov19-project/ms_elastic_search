package ea.sof.ms_elastic_search.service;

import ea.sof.shared.queue_models.QuestionQueueModel;

import java.util.List;
import java.util.Map;

public interface RedisQuestionService {
    void save(String keyword, QuestionQueueModel question, long timeout);
    void saveAll(String keyword, List<QuestionQueueModel> questions, long timeout);
    Map<String, QuestionQueueModel> findAll(String keyword);
}
