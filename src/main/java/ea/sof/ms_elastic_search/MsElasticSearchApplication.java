package ea.sof.ms_elastic_search;

import ea.sof.shared.queue_models.QuestionQueueModel;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

@SpringBootApplication
public class MsElasticSearchApplication {

    public static void main(String[] args) {
        SpringApplication.run(MsElasticSearchApplication.class, args);
    }

}
