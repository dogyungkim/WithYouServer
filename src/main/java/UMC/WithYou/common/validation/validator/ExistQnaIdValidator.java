package UMC.WithYou.common.validation.validator;

import UMC.WithYou.common.apiPayload.code.status.ErrorStatus;
import UMC.WithYou.common.validation.annotation.ExistQnaId;
import UMC.WithYou.feature.rewind.service.RewindQnaService;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ExistQnaIdValidator implements ConstraintValidator<ExistQnaId, Long> {

    private final RewindQnaService rewindQnaService;

    @Override
    public void initialize(ExistQnaId constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(Long value, ConstraintValidatorContext context) {
        boolean isValid = rewindQnaService.checkQnaIdExist(value);

        if (!isValid) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(ErrorStatus.QNA_NOT_FOUND.toString()).addConstraintViolation();
        }

        return isValid;
    }
}