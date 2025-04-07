package UMC.WithYou.feature.rewind.service;

import UMC.WithYou.feature.rewind.domain.RewindQna;
import UMC.WithYou.feature.rewind.repository.RewindQnaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RewindQnaServiceImpl implements RewindQnaService{

    private final RewindQnaRepository rewindQnaRepository;
    @Override
    public boolean checkQnaIdExist(Long qnaId) {
        Optional<RewindQna> rewindQuestion = rewindQnaRepository.findById(qnaId);
        return rewindQuestion.isPresent();
    }
}
