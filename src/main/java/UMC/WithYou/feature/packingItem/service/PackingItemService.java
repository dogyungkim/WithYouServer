package UMC.WithYou.feature.packingItem.service;

import UMC.WithYou.common.apiPayload.code.status.ErrorStatus;
import UMC.WithYou.common.apiPayload.exception.handler.CommonErrorHandler;
import UMC.WithYou.feature.member.domain.Member;
import UMC.WithYou.feature.member.repository.MemberRepository;
import UMC.WithYou.feature.packingItem.domain.PackingItem;
import UMC.WithYou.feature.packingItem.repository.PackingItemRepository;
import UMC.WithYou.feature.travel.domain.Travel;
import UMC.WithYou.feature.travel.repository.TravelRepository;
import jakarta.transaction.Transactional;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@Transactional
@AllArgsConstructor
public class PackingItemService {
    private final PackingItemRepository packingItemRepository;
    private final TravelRepository travelRepository;
    private final MemberRepository memberRepository;


    public PackingItem addPackingItem(Long travelId, String itemName) {
        Travel travel = travelRepository.findById(travelId).orElseThrow(
                ()->new CommonErrorHandler(ErrorStatus.TRAVEL_LOG_NOT_FOUND)
        );

        PackingItem packingItem =  PackingItem.createPackingItem(travel, itemName);

        return packingItemRepository.save(packingItem);
    }

    public List<PackingItem> findPackingItems(Long travelId){
        Travel travel = travelRepository.findById(travelId).orElseThrow(
                ()->new CommonErrorHandler(ErrorStatus.TRAVEL_LOG_NOT_FOUND)
        );
        return packingItemRepository.findByTravelId(travelId);
    }

    public void deletePackingItem(Long packingItemId){
        PackingItem packingItem = this.findPeckingItemById(packingItemId);
        packingItemRepository.delete(packingItem);
    }

    public Boolean setPacker(Long packingItemId, Long packerId){
        PackingItem packingItem = this.findPeckingItemById(packingItemId);

        Member packer = memberRepository.findById(packerId).orElseThrow(
                ()->new CommonErrorHandler(ErrorStatus.MEMBER_NOT_FOUND)
        );

        if (packingItem.getPacker() != null && packingItem.getPacker().isSameId(packerId)){

            if (packingItem.getIsChecked()) {
                packingItem.toggleCheckbox();
            }
            packer = null;
        }
        packingItem.setPacker(packer);
        return packingItem.getIsChecked();
    }

    public Boolean toggleCheckbox(Long packingItemId) {
        PackingItem packingItem = this.findPeckingItemById(packingItemId);

        if (packingItem.isPackerChosen()){
            return packingItem.getIsChecked();
        }
        return packingItem.toggleCheckbox();
    }



    private PackingItem findPeckingItemById(Long packingItemId){
        return packingItemRepository.findById(packingItemId).orElseThrow(
                ()->new CommonErrorHandler(ErrorStatus.PACKING_ITEM_NOT_FOUND)
        );
    }
}
