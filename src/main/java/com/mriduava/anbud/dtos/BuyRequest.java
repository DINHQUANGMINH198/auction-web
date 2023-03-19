package com.mriduava.anbud.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BuyRequest {
    private long auction_id;
    private int bidder_id;
    private String bidder_name;
    private String auction_item_name;
}
