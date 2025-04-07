package UMC.WithYou.common.validation.validator;

import UMC.WithYou.common.apiPayload.code.status.ErrorStatus;
import UMC.WithYou.common.validation.annotation.ExistNotices;
import UMC.WithYou.feature.notice.repository.NoticeRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class NoticesExistValidator implements ConstraintValidator<ExistNotices, Long> {

    private final NoticeRepository noticeRepository;

    @Override
    public void initialize(ExistNotices constraintAnnotation){
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(Long value, ConstraintValidatorContext context){
        boolean isValid=noticeRepository.existsById(value);

        if (!isValid) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(ErrorStatus._NOTICE_NOT_FOUND.toString()).addConstraintViolation();
        }

        return isValid;
    }
}
