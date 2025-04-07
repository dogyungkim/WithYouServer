package UMC.WithYou.common.validation.validator;

import UMC.WithYou.common.apiPayload.code.status.ErrorStatus;
import UMC.WithYou.common.validation.annotation.ExistRewindId;
import UMC.WithYou.feature.rewind.service.RewindQueryService;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ExistRewindIdValidator implements ConstraintValidator<ExistRewindId, Long> {

    private final RewindQueryService rewindQueryService;

    @Override
    public void initialize(ExistRewindId constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(Long value, ConstraintValidatorContext context) {
        boolean isValid = rewindQueryService.checkRewindIdExist(value);

        if (!isValid) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(ErrorStatus.REWIND_NOT_FOUND.toString()).addConstraintViolation();
        }

        return isValid;
    }
}
