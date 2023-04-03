package com.driver;


import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class OrderRepository {

    private Map<String, Order> orderDb;
    private Map<String, DeliveryPartner> partnerDb;

    private Map<String, String> orderPartnerPairDb;

    private Map<String, List<String>> partnerOrderDb;

    public OrderRepository() {
        this.orderDb = new HashMap<>();
        this.partnerDb = new HashMap<>();
        this.orderPartnerPairDb = new HashMap<>();
        this.partnerOrderDb = new HashMap<>();
    }

    public void addOrder(Order order){

        String orderId = order.getId();
        orderDb.put(orderId, order);

    }

    public void addPartner(String partnerId){

        DeliveryPartner deliveryPartner = new DeliveryPartner(partnerId);
        partnerDb.put(partnerId, deliveryPartner);

    }

    public void addOrderPartnerPair(String orderId, String partnerId){

        orderPartnerPairDb.put(orderId, partnerId);
        if(!partnerOrderDb.containsKey(partnerId)){

            partnerOrderDb.put(partnerId, new ArrayList<>());
            partnerOrderDb.get(partnerId).add(orderId);
        }else{

            partnerOrderDb.get(partnerId).add(orderId);
        }
        partnerDb.get(partnerId).setNumberOfOrders(partnerDb.get(partnerId).getNumberOfOrders() + 1);

    }

    public Order getOrderById(String orderId){

        return orderDb.get(orderId);

    }

    public DeliveryPartner getPartnerById(String partnerId){

        return partnerDb.get(partnerId);

    }

    public int getOrderCountByPartnerId(String partnerId){

        return partnerDb.get(partnerId).getNumberOfOrders();
    }

    public List<String> getOrdersByPartnerId(String partnerId){

        return partnerOrderDb.get(partnerId);

    }

    public List<String> getAllOrders(){

        List<String> orders = new ArrayList<>();

        for(String orderId: orderDb.keySet()){

            orders.add(orderId);
        }

        return orders;
    }


    public int getCountOfUnassignedOrders(){

        int count = 0;

        for(String orderId: orderDb.keySet()){

            if(!orderPartnerPairDb.containsKey(orderId)){

                count++;

            }
        }
        return count;
    }

    public void deletePartnerById(String partnerId){

        partnerDb.remove(partnerId);

        for(String order: orderPartnerPairDb.keySet()){

            if(orderPartnerPairDb.get(order).equals(partnerId)){

                orderPartnerPairDb.remove(order);

            }
        }
        partnerOrderDb.remove(partnerId);


    }

    public void deleteOrderById(String orderId){

        orderDb.remove(orderId);

        String partnerId = orderPartnerPairDb.get(orderId);
        partnerDb.get(partnerId).setNumberOfOrders(partnerDb.get(partnerId).getNumberOfOrders() - 1);

        orderPartnerPairDb.remove(orderId);

        List<String> orders = partnerOrderDb.get(partnerId);

        orders.remove(orderId);
        partnerOrderDb.put(partnerId, orders);


    }
}
