package com.kosa.mini.api.dto.store;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class StoreSearchDTO {
    private String storeName;
    private String roadAddress;
    private String contactNumber;
}
