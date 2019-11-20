package ea.sof.ms_elastic_search.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import ea.sof.shared.queue_models.QuestionQueueModel;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.Client;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Service
public class ElasticSearchService {

    @Value("${questionIndex}")
    private String questionIndex;

    @Value("_doc")
    private String docType;

    @Value("${maxhit}")
    private int maxhit;

    @Autowired
    Client client;

    public List<QuestionQueueModel> search(String phrase) {

        Map<String,Object> map = null;
        SearchResponse response = client.prepareSearch(questionIndex)
                .setTypes(docType)
                .setSearchType(SearchType.QUERY_THEN_FETCH)
                .setQuery(QueryBuilders.multiMatchQuery(phrase, "id", "title", "body" ))
                .get()
                ;
        List<SearchHit> searchHits = Arrays.asList(response.getHits().getHits());
        List<QuestionQueueModel> questions = new ArrayList<>();

        int hits = maxhit < searchHits.size()? maxhit: searchHits.size();
        for (int i = 0; i < hits; i++){
            map = searchHits.get(i).getSourceAsMap();
            ObjectMapper mapper = new ObjectMapper();
            QuestionQueueModel question = mapper.convertValue(map, QuestionQueueModel.class);
            questions.add(question);
        }

        return questions;
    }
}
