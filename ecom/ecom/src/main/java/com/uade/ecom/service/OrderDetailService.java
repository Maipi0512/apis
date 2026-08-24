package com.uade.ecom.service;

import java.util.List;

import com.uade.ecom.dto.OrderDetailRequestDTO;
import com.uade.ecom.model.OrderDetail;

public interface OrderDetailService {

    List<OrderDetail> getAllOrderDetails();

    OrderDetail getOrderDetailById(Long id);

    OrderDetail createOrderDetail(OrderDetailRequestDTO orderDetailRequestDTO);
}
