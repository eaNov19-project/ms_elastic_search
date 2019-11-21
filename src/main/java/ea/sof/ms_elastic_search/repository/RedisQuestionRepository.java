package ea.sof.ms_elastic_search.repository;

import ea.sof.ms_elastic_search.model.ScoredQuestionQueueModel;
import ea.sof.shared.queue_models.QuestionQueueModel;

import java.util.List;
import java.util.Map;

public interface RedisQuestionRepository {
    void save(String keyword, ScoredQuestionQueueModel question, long timeout);
    void saveAll(String keyword, List<ScoredQuestionQueueModel> questions, long timeout);
    Map<String, ScoredQuestionQueueModel> findAll(String keyword);
//    QuestionQueueModel findById(String id);
//    void update(QuestionQueueModel question);
//    void delete(String id);
}
