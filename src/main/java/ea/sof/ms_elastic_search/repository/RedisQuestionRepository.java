package ea.sof.ms_elastic_search.repository;

import ea.sof.shared.queue_models.QuestionQueueModel;

import java.util.List;
import java.util.Map;

public interface RedisQuestionRepository {
    void save(String keyword, QuestionQueueModel question, long timeout);
    void saveAll(String keyword, List<QuestionQueueModel> questions, long timeout);
    Map<String, QuestionQueueModel> findAll(String keyword);
//    QuestionQueueModel findById(String id);
//    void update(QuestionQueueModel question);
//    void delete(String id);
}
