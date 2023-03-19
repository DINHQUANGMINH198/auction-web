import React, {useState, useEffect, useContext, createContext} from 'react'
import { AuctionContext } from './AuctionContextProvider'
import { toast } from "react-toastify";

export const SocketContext = createContext();

const SocketContextProvider = (props) => {
  const { updateBids, fetchOneAuctionItem } = useContext(AuctionContext)

  const [ws, setWs] = useState();
  const [isConnected, setIsConnected] = useState(false);

  useEffect(()=>{
    const connect = () => {
      const ws = new WebSocket('ws://localhost:9000/anbud-socket');
      setWs(ws)
      let dataWrapper;
      ws.onmessage = (e) => {
        try {
          dataWrapper = JSON.parse(e.data)
        } catch {
          console.warn('Could not parse:', e);
          return
        }

        switch(dataWrapper.action) {
          case 'bid':
            toast.success(dataWrapper.payload.bidder.name + ' bidded ' + dataWrapper.payload.bid + ' for aution ' + dataWrapper.payload.auction_item.item_name)
            updateBids([dataWrapper.payload])
            break;
          case 'user-status':
              console.log('New status change:', dataWrapper.payload);
              break;
          case 'buy':
            toast.success(dataWrapper.payload.bidder_name + ' buyed ' + dataWrapper.payload.auction_item_name)
            fetchOneAuctionItem(dataWrapper.payload.auction_id)
            break;
          case 'win':
            toast.success(dataWrapper.payload.bidder.name + ' winned aution ' + dataWrapper.payload.auction_item.item_name)
            fetchOneAuctionItem(dataWrapper.payload.auction_id)
            break;
          default:
              console.log('Could not read action:', dataWrapper.action);
        }
      }

      ws.onopen = (e) => {
        ws.send(JSON.stringify({
          action: 'connection',
          payload: 'user connected'
        }));
        setIsConnected(true);
      };

      ws.onclose = (e) => {
        console.log("Closing websocket...");
      };
      console.log("Connecting...");
    }
    connect();
  }, [])

  const disconnect = () => {
    if (ws != null) {
        ws.close();
    }
    setIsConnected(false);
    console.log("Disconnected");
  }

  const send = (data) => {
    ws.send(JSON.stringify(data));
  }

  const sendBidData = (newBid) => {
    send({
        action: 'bid',
        payload: newBid
    })
  }

  const sendBuyData = (buy) => {
    send({
      action: 'buy',
      payload: buy
    })
  }

  const values = {
    sendBidData,
    sendBuyData
  };

  return (
    <SocketContext.Provider value={values}>
      {props.children}
    </SocketContext.Provider>
  )
}

export default SocketContextProvider
