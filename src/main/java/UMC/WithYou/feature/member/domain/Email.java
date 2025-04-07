package UMC.WithYou.feature.member.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Generated;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Getter
@Embeddable
@NoArgsConstructor
public class Email {
    private static final int MIN_LENGTH = 1;
    private static final int MAX_LENGTH = 255;

    @Column(name = "email", nullable = false)
    private String value;

    public Email(String value) {
        validateBlank(value);
        validateLength(value);
        this.value = value;
    }

    private void validateBlank(String value) {
        if (value.isBlank()) {
            //throw new MemberException(MemberExceptionType.INVALID_VALUE);
        }
    }

    private void validateLength(String value) {
        int length = value.length();
        if (length < MIN_LENGTH || length > MAX_LENGTH) {
            //throw new MemberException(MemberExceptionType.INVALID_VALUE);
        }
    }

    @Override
    @Generated
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Email email = (Email) o;
        return Objects.equals(value, email.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
