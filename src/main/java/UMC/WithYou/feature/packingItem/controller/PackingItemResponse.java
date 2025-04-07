package UMC.WithYou.feature.packingItem.controller;

import UMC.WithYou.feature.member.domain.Member;
import UMC.WithYou.feature.packingItem.domain.PackingItem;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class PackingItemResponse {
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    public static class AdditionResponseDTO{
        private Long packingItemId;
    }




    @Getter
    public static class SearchResponseDTO{
        private Long itemId;
        private String itemName;
        private Long packerId;
        private boolean isChecked;
        public SearchResponseDTO(PackingItem packingItem){
            this.itemId = packingItem.getId();
            this.itemName = packingItem.getItemName();
            Member packer = packingItem.getPacker();
            if (packer != null){
                this.packerId = packer.getId();
            }
            this.isChecked = packingItem.getIsChecked();
        }
    }


    @Getter
    public static class DeletionResponseDTO{
        private Long packingItemId;
        public DeletionResponseDTO(Long packingItemId){
            this.packingItemId = packingItemId;
        }
    }



    @Getter
    @AllArgsConstructor
    public static class ToggleResponseDTO{
        private Long packingItemId;
        private Boolean checkboxState;
    }

    @Getter
    @AllArgsConstructor
    public static class PackerChoiceResponseDTO{
        private Long packingItemId;
        private Long packerId;
        private Boolean checkboxState;
    }
}
