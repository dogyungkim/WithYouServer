package UMC.WithYou.common.validation.validator;

import UMC.WithYou.common.apiPayload.code.status.ErrorStatus;
import UMC.WithYou.common.validation.annotation.ExistQuestionId;
import UMC.WithYou.feature.rewind.service.RewindQuestionService;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ExistQuestionIdValidator implements ConstraintValidator<ExistQuestionId, Long> {

    private final RewindQuestionService rewindQuestionService;

    @Override
    public void initialize(ExistQuestionId constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(Long value, ConstraintValidatorContext context) {
        boolean isValid = rewindQuestionService.checkQuestionIdExist(value);

        if (!isValid) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(ErrorStatus.QUESTION_NOT_FOUND.toString()).addConstraintViolation();
        }

        return isValid;
    }
}
