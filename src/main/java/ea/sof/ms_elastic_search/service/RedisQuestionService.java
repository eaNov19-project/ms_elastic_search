package ea.sof.ms_elastic_search.service;

import ea.sof.ms_elastic_search.model.ScoredQuestionQueueModel;
import ea.sof.shared.queue_models.QuestionQueueModel;

import java.util.List;
import java.util.Map;

public interface RedisQuestionService {
    void save(String keyword, ScoredQuestionQueueModel question, long timeout);
    void saveAll(String keyword, List<ScoredQuestionQueueModel> questions, long timeout);
    Map<String, ScoredQuestionQueueModel> findAll(String keyword);
}
