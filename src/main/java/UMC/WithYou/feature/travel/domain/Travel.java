package UMC.WithYou.feature.travel.domain;

import UMC.WithYou.common.BaseEntity;
import UMC.WithYou.feature.member.domain.Member;
import UMC.WithYou.feature.rewind.domain.Rewind;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class Travel extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Member member;

    private String title;
    private LocalDate startDate;
    private LocalDate endDate;
    @Setter
    private String imageUrl;

    @Enumerated(EnumType.STRING)
    private TravelStatus status;

    private String invitationCode;



    @OneToMany(mappedBy = "travel", cascade = CascadeType.ALL)
    private List<Traveler> travelers = new ArrayList<>();

    @OneToMany(mappedBy = "travel", cascade = CascadeType.ALL)
    private List<Rewind> rewinds;

    public Travel(Member member, String title, LocalDate startDate, LocalDate endDate) {
        this.member = member;
        this.title = title;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public void edit(String title, LocalDate startDate, LocalDate endDate){
        this.title = title;
        this.startDate =startDate;
        this.endDate = endDate;
    }


    public void addTravelMember(Traveler traveler){
        this.travelers.add(traveler);
    }
    public List<Member> getTravelMembers(){
        List<Member> travelMembers = new ArrayList<>();
        for (Traveler traveler : getTravelers()){
            travelMembers.add(traveler.getMember());
        }
        return travelMembers;
    }


    public boolean isTraveler(Member member){
        for (Traveler traveler: getTravelers()){
            if (traveler.isMember(member)) {
                return true;
            }
        }
        return false;

    }

    public boolean validateOwnership(Member member) {
        return this.member.isSameId(member.getId());
    }

    public boolean hasInvitationCode() {
        return this.invitationCode != null;
    }

    public void setInvitationCode(String invitationCode) {
        this.invitationCode = invitationCode;
    }

    public void setTravelStatus(LocalDate localDate){
        if(endDate.isBefore(localDate)){
            this.status = TravelStatus.BYGONE;
        }
        else if (!startDate.isAfter(localDate)){
            this.status = TravelStatus.ONGOING;
        }
        else{
            this.status = TravelStatus.UPCOMING;
        }
    }

    public void leave(Member travelMember) {

        for (Traveler traveler : getTravelers()){
            if (traveler.isMember(travelMember)){
                travelers.remove(traveler);
                break;
            }
        }

    }
}
