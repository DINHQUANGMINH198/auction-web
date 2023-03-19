package com.mriduava.anbud.services;

import com.mriduava.anbud.dtos.BuyRequest;
import com.mriduava.anbud.dtos.SocketDTO;
import com.mriduava.anbud.entities.AuctionItem;
import com.mriduava.anbud.entities.User;
import com.mriduava.anbud.repositories.AuctionRepo;
import com.mriduava.anbud.repositories.BidRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AuctionService {
    @Autowired
    AuctionRepo auctionRepo;

    @Autowired
    SocketService socketService;

    @Autowired
    BidRepo bidRepo;

    public List<AuctionItem> getAllItems() {
        return auctionRepo.findAllOrderByCreateDate();
    }

    public Optional<AuctionItem> getOneItem(Long id) {
        return auctionRepo.findById(id);
    }

    public boolean postNewAuction(AuctionItem auctionItem) {
        auctionItem.setIs_public(1);
        AuctionItem savedAuction = auctionRepo.save(auctionItem);
        SocketDTO socketData = new SocketDTO("auction", savedAuction);
        socketService.sendToAllClient(socketData);
        return savedAuction.getId() > 0;
    }

    public boolean buyAuction(BuyRequest buyRequest) {
        AuctionItem auctionItem = auctionRepo.findAuctionItemById(buyRequest.getAuction_id());
        auctionItem.setOwner_id(buyRequest.getBidder_id());
        auctionItem.setIs_public(0);
        auctionItem.setBuyer(buyRequest.getBidder_name());
        auctionRepo.save(auctionItem);
        SocketDTO socketData = new SocketDTO("buy", buyRequest);
        socketService.sendToAllClient(socketData);
        return true;
    }

    public List<AuctionItem> getByItemName(String item_name) {
        return auctionRepo.findAuctionByName(item_name);
    }

    public List<AuctionItem> getAuctionByOwner(int owner_id) {
        return auctionRepo.findAuctionByOwner(owner_id);
    }

}
