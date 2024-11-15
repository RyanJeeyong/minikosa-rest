package com.kosa.mini.api.dto.search;


import com.kosa.mini.api.dto.review.StoreReviewDTO;
import lombok.Data;

import java.util.List;

@Data
public class SearchResultDTO {
    private String query;
    private List<SearchStoreDTO> storeResults;
    private List<StoreReviewDTO> reviewResults;
    private int storeCount;
    private int reviewCount;
}
