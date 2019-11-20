package ea.sof.ms_elastic_search.service;

import ea.sof.ms_elastic_search.repository.RedisQuestionRepository;
import ea.sof.shared.queue_models.QuestionQueueModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class RedisQuestionServiceImpl implements RedisQuestionService {

    @Autowired
    RedisQuestionRepository redisQuestionRepository;

    public void save(String keyword, QuestionQueueModel question, long timeout){
        redisQuestionRepository.save(keyword, question, timeout);
    }

    public Map<String, QuestionQueueModel> findAll(String keyword){
        return redisQuestionRepository.findAll(keyword);
    }

    public void saveAll(String keyword, List<QuestionQueueModel> questions, long timeout){
        redisQuestionRepository.saveAll(keyword, questions, timeout);
    }
}
