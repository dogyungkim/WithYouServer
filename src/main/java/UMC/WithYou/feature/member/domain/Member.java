package UMC.WithYou.feature.member.domain;

import UMC.WithYou.common.BaseEntity;
import UMC.WithYou.feature.travel.domain.Traveler;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.util.List;
import lombok.*;
import java.util.ArrayList;
import java.util.Objects;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseEntity {
    @Id
    @Getter
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;
    @Embedded
    private Email email;
    @Embedded
    private Identifier identifier;
    @Embedded
    private Name name;

    @Getter
    @Enumerated(EnumType.STRING)
    private MemberType memberType;

    @Getter
    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Traveler> travelers = new ArrayList<>();

    @Getter
    private String imageUrl;


    @Builder
    public Member(String email,String identifier,String name,MemberType memberType) {
        this.email=new Email(email);
        this.identifier=new Identifier(identifier);
        this.name = new Name(name);
        this.memberType=memberType;
        this.imageUrl="";
    }

    public String getEmail() {
        return this.email.getValue();
    }
    public String getIdentifier() {
        return this.identifier.getValue();
    }

    public String getName() {
        return this.name.getValue();
    }

    public void updateImage(String imageUrl){
        this.imageUrl = imageUrl;
    }

    public void updateName(String name){
        this.name = new Name(name);
    }

    public boolean isSameId(Long memberId) {
        return this.id.equals(memberId);
    }

    public void addTraveler(Traveler traveler) {
        this.travelers.add(traveler);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Member member = (Member) o;
        return  Objects.equals(id, member.id)
                && Objects.equals(identifier.getValue(), member.identifier.getValue());

    }

    @Override
    public int hashCode() {
        return Objects.hash(id, identifier.getValue());
    }
}
