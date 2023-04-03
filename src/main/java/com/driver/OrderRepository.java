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
    public Integer getOrdersLeftAfterGivenTimeByPartnerId(String time,String partnerId){

        int countOfOrders = 0;
        int hours = Integer.valueOf(time.substring(0,2));
        int minutes = Integer.valueOf(time.substring(3));
        int total = hours*60 + minutes;

        if(partnerOrderDb.containsKey(partnerId))
        {
            HashSet<String> set = partnerOrderDb.get(partnerId);

            for(String st : set)
            {
                if(orderDb.containsKey(st))
                {
                    Order order = orderDb.get(st);

                    if(total < order.getDeliveryTime())
                        countOfOrders++;
                }
            }
        }

        return countOfOrders;
    }

    public String getLastDeliveryTimeByPartnerId(String partnerId) {
        String time = null;
        int delivery_time = 0;

        if(partnerDb.containsKey(partnerId))
        {
            HashSet<String> list = partnerOrderDb.get(partnerId);

            for(String st : list)
            {
                if(orderDb.containsKey(st))
                {
                    Order order = orderDb.get(st);

                    if(delivery_time < order.getDeliveryTime())
                        delivery_time = order.getDeliveryTime();
                }
            }
        }
        StringBuilder str = new StringBuilder();

        int hr = delivery_time / 60;                 // calculate hour
        if(hr < 10)
            str.append(0).append(hr);
        else
            str.append(hr);

        str.append(":");

        int min = delivery_time - (hr*60);          // calculate minutes
        if(min < 10)
            str.append(0).append(min);
        else
            str.append(min);

//        str.append(min);

        return str.toString();
    }

    public void deletePartnerById(String partnerId) {

        HashSet<String> list = new HashSet<>();

        if(partnerOrderDb.containsKey(partnerId))
        {
            list = partnerOrderDb.get(partnerId);

            for (String st : list) {


                if (orderPartnerPairDb.containsKey(st))
                    orderPartnerPairDb.remove(st);
            }

            partnerOrderDb.remove(partnerId);
        }

        if(partnerDb.containsKey(partnerId)) {
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
