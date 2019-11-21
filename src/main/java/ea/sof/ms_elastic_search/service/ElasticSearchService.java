package ea.sof.ms_elastic_search.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import ea.sof.ms_elastic_search.model.ScoredQuestionQueueModel;
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

import java.util.*;
import java.util.stream.Collectors;

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

    public List<ScoredQuestionQueueModel> search(String phrase) {

        Map<String,Object> map = null;
        SearchResponse response = client.prepareSearch(questionIndex)
                .setTypes(docType)
                .setSearchType(SearchType.QUERY_THEN_FETCH)
                .setQuery(QueryBuilders.multiMatchQuery(phrase, "id", "title", "body" ))
                .get()
                ;
        List<SearchHit> searchHits = Arrays.asList(response.getHits().getHits());
        List<ScoredQuestionQueueModel> questions = new ArrayList<>();

        int hits = maxhit < searchHits.size()? maxhit: searchHits.size();
        for (int i = 0; i < hits; i++){
            map = searchHits.get(i).getSourceAsMap();
            ObjectMapper mapper = new ObjectMapper();
            QuestionQueueModel question = mapper.convertValue(map, QuestionQueueModel.class);
            questions.add(new ScoredQuestionQueueModel(searchHits.get(i).getScore(), question));
        }

        return questions;
    }

    public List<QuestionQueueModel> convertToOrderedList(List<ScoredQuestionQueueModel> scoredQuestions){
        if(scoredQuestions == null)
            return null;

        scoredQuestions.sort(new Comparator<ScoredQuestionQueueModel>() {
            @Override
            public int compare(ScoredQuestionQueueModel o1, ScoredQuestionQueueModel o2) {
                return Float.compare(o2.getScore(), o1.getScore());
            }
        });
        return scoredQuestions.stream().map(q -> q.getQuestion()).collect(Collectors.toList());
    }
}
