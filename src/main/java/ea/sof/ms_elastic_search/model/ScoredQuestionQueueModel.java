package ea.sof.ms_elastic_search.model;

import ea.sof.shared.queue_models.QuestionQueueModel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ScoredQuestionQueueModel implements Serializable {
    private float score;
    private QuestionQueueModel question;
}
