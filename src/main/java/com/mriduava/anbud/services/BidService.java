package com.mriduava.anbud.services;

import com.mriduava.anbud.dtos.SocketDTO;
import com.mriduava.anbud.entities.AuctionItem;
import com.mriduava.anbud.entities.Bid;
import com.mriduava.anbud.repositories.AuctionRepo;
import com.mriduava.anbud.repositories.BidRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BidService {
    @Autowired
    BidRepo bidRepo;

    @Autowired
    AuctionRepo auctionRepo;

    @Autowired
    SocketService socketService;

    public List<Bid> getAllBids() {
        return bidRepo.findAll();
    }

    public Optional<Bid> getBidById(Long id) {
        return bidRepo.findById(id);
    }

    public List<Bid> findBidsByAuctionId(long auctionID) {
        return bidRepo.findBidsByAuctionId(auctionID);
    }

    public Bid postNewBid(Bid bid) {
        Bid savedBid = bidRepo.save(bid);
        SocketDTO socketData = new SocketDTO("bid", savedBid);
        Thread counter = new Thread(() -> {
            for (int i = 0; i < 3; i++) {
                try {
                    int newestBid = bidRepo.findMaxBidByAuctionId(savedBid.getAuction_id());
                    if(savedBid.getBid() < newestBid) return;
                    if (savedBid.getBid() == newestBid && i == 2) {
                        AuctionItem auctionItem = auctionRepo.findById((long) savedBid.getAuction_id()).get();
                        auctionItem.setWinner(savedBid.getBidder().getName());
                        auctionItem.setOwner_id(savedBid.getBidder_id());
                        auctionRepo.save(auctionItem);
                        SocketDTO winSocketData = new SocketDTO("win", savedBid);
                        socketService.sendToAllClient(winSocketData);
                    } else {
                        socketService.sendToAllClient(socketData);
                    }
                    Thread.sleep(30000);
                } catch (InterruptedException e) {
                    System.out.println(e);
                }
            }
        });
        counter.start();
        return savedBid;
    }

}

