package UMC.WithYou.common.validation.validator;

import UMC.WithYou.common.apiPayload.code.status.ErrorStatus;
import UMC.WithYou.common.validation.annotation.ExistClouds;
import UMC.WithYou.feature.cloud.repository.CloudRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CloudsExistValidator implements ConstraintValidator<ExistClouds,Long> {

    private final CloudRepository cloudRepository;

    @Override
    public void initialize(ExistClouds constraintAnnotation){
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(Long value, ConstraintValidatorContext context){
        boolean isValid=cloudRepository.existsById(value);

        if (!isValid) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(ErrorStatus._CLOUD_NOT_FOUNT.toString()).addConstraintViolation();
        }

        return isValid;
    }
}
