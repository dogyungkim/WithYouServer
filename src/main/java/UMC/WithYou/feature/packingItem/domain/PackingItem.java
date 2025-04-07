package UMC.WithYou.feature.packingItem.domain;


import UMC.WithYou.common.BaseEntity;
import UMC.WithYou.feature.member.domain.Member;
import UMC.WithYou.feature.travel.domain.Travel;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "packing_item")
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class PackingItem extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "packer_id")
    private Member packer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "travel_id")
    private Travel travel;

    private Boolean isChecked;
    private String itemName;

    public static PackingItem createPackingItem(Travel travelId, String itemName){
        return PackingItem.builder()
                .travel(travelId)
                .itemName(itemName)
                .isChecked(false)
                .build();
    }

    public Boolean toggleCheckbox(){
        isChecked = !isChecked;
        return isChecked;
    }

    public boolean isPackerChosen(){
        return packer == null;
    }

    public void setPacker(Member packer){
        this.packer = packer;
    }

}
