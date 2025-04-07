package UMC.WithYou.feature.rewind.service;

import UMC.WithYou.feature.rewind.domain.RewindQuestion;
import UMC.WithYou.feature.rewind.repository.RewindQuestionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RewindQuestionServiceImpl implements RewindQuestionService {

    private final RewindQuestionRepository rewindQuestionRepository;

    @Override
    public Boolean checkQuestionIdExist(Long questionId) {
        Optional<RewindQuestion> rewindQuestion = rewindQuestionRepository.findById(questionId);
        return rewindQuestion.isPresent();
    }
}
