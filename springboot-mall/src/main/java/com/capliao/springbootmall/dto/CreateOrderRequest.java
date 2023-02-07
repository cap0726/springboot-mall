package com.capliao.springbootmall.dto;

import javax.validation.constraints.NotEmpty;
import java.util.List;

public class CreateOrderRequest {

    @NotEmpty
    private List<BuyItems> buyItemList;

    public List<BuyItems> getBuyItemList() {
        return buyItemList;
    }

    public void setBuyItemList(List<BuyItems> buyItemList) {
        this.buyItemList = buyItemList;
    }
}
