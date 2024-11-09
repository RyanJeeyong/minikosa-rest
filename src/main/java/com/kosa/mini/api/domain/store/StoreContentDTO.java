package com.kosa.mini.api.domain.store;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class StoreContentDTO {
   private int storeId;
   private String storeName;
   private String postcode;
   private String roadAddress;
   private String detailAddress;
   private String extraAddress;
   private String storeDescription;
   private String webSiteInfo;
   private Date openingTime;
   private Date closingTime;
   private String contactNumber;
   private String storePhoto;
   private int ownerId;
   private double ratingAvg;
   private int countReview;
   private List<MenuDTO> menuDTO;
}
