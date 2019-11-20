package ea.sof.ms_elastic_search.repository;

import ea.sof.shared.queue_models.QuestionQueueModel;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Repository
public class RedisQuestionRepositoryImpl implements RedisQuestionRepository {

    private RedisTemplate<String, QuestionQueueModel> redisTemplate;

    private HashOperations hashOperations;


    public RedisQuestionRepositoryImpl(RedisTemplate<String, QuestionQueueModel> redisTemplate) {
        this.redisTemplate = redisTemplate;

        hashOperations = redisTemplate.opsForHash();
    }

    @Override
    public void save(String keyword, QuestionQueueModel question, long timeout) {
        hashOperations.put(keyword, question.getId(), question);
        redisTemplate.expire(keyword, timeout, TimeUnit.SECONDS);
    }

    public void saveAll(String keyword, List<QuestionQueueModel> questions, long timeout){
        Map<String, QuestionQueueModel> map = questions.stream().collect(
                Collectors.toMap(q -> q.getId(), q-> q));
        hashOperations.putAll(keyword, map);
        redisTemplate.expire(keyword, timeout, TimeUnit.SECONDS);
    }
    @Override
    public Map<String, QuestionQueueModel> findAll(String keyword) {
        return hashOperations.entries(keyword);
    }

//    @Override
//    public QuestionQueueModel findById(String id) {
//        return (QuestionQueueModel)hashOperations.get("QuestionQueueModel", id);
//    }
//
//    @Override
//    public void update(QuestionQueueModel question) {
//        save(question);
//    }
//
//    @Override
//    public void delete(String id) {
//
//        hashOperations.delete("QuestionQueueModel", id);
//    }
}
