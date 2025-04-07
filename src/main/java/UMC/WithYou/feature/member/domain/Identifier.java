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
public class Identifier {
    @Column(name = "identifier", nullable = false)
    private String value;

    public Identifier(String value) {
        validateBlank(value);
        this.value = value;
    }

    private void validateBlank(String value) {
        if (value.isBlank()) {
            //throw new MemberException(MemberExceptionType.INVALID_VALUE);
        }
    }

    @Override
    @Generated
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        Identifier identifier = (Identifier)o;
        return this.value.equals(identifier.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
