package com.driver;


import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class OrderRepository {

    private Map<String, Order> orderDb;
    private Map<String, DeliveryPartner> partnerDb;

    private Map<String, String> orderPartnerPairDb;

    private Map<String, HashSet<String>> partnerOrderDb;

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


        if(orderDb.containsKey(orderId) && partnerDb.containsKey(partnerId)){

            HashSet<String> currentOrders = new HashSet<>();
            if(partnerOrderDb.containsKey(partnerId)){

                currentOrders = partnerOrderDb.get(partnerId);
            }
            currentOrders.add(orderId);
            partnerOrderDb.put(partnerId, currentOrders);

            DeliveryPartner partner = partnerDb.get(partnerId);
            partner.setNumberOfOrders(currentOrders.size());

            orderPartnerPairDb.put(orderId, partnerId);
        }

    }

    public Order getOrderById(String orderId){

        Order order = orderDb.get(orderId);
        return order;

    }

    public DeliveryPartner getPartnerById(String partnerId){

        DeliveryPartner deliveryPartner = null;

        if(partnerDb.containsKey(partnerId)){

            deliveryPartner = partnerDb.get(partnerId);
        }

        return deliveryPartner;

    }

    public int getOrderCountByPartnerId(String partnerId){

        int orderCount = 0;

        if(partnerOrderDb.containsKey(partnerId)){

            orderCount = partnerOrderDb.get(partnerId).size();

        }
        return orderCount;
    }

    public List<String> getOrdersByPartnerId(String partnerId){

        List<String> orders = new ArrayList<>();

        for(String orderId: partnerOrderDb.get(partnerId)){
            orders.add(orderId);
        }
        return orders;

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

        HashSet<String> set = new HashSet<>();
        if(partnerOrderDb.containsKey(partnerId)){

            set = partnerOrderDb.get(partnerId);

            for(String order: set){

                if(orderPartnerPairDb.containsKey(set)){

                    orderPartnerPairDb.remove(order);

                }
            }
            partnerOrderDb.remove(partnerId);
        }

        if(partnerDb.containsKey(partnerId)){

            partnerDb.remove(partnerId);

        }

    }

    public void deleteOrderById(String orderId){

        if(orderPartnerPairDb.containsKey(orderId)){

            String partnerId = orderPartnerPairDb.get(orderId);
            HashSet<String> set = partnerOrderDb.get(partnerId);
            set.remove(orderId);

            partnerOrderDb.put(partnerId, set);

            DeliveryPartner deliveryPartner = partnerDb.get(partnerId);
            deliveryPartner.setNumberOfOrders(set.size());
        }

        if(orderDb.containsKey(orderId)){

            orderDb.remove(orderId);
        }

    }
}
