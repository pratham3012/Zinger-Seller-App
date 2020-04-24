package com.food.ordering.zinger.seller.ui.order

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.food.ordering.zinger.seller.data.local.Resource
import com.food.ordering.zinger.seller.data.model.OrderItemListModel
import com.food.ordering.zinger.seller.data.model.OrderModel
import com.food.ordering.zinger.seller.data.model.Response
import com.food.ordering.zinger.seller.data.model.TransactionModel
import com.food.ordering.zinger.seller.data.retrofit.OrderRepository
import kotlinx.coroutines.launch
import kotlin.Exception

class OrderViewModel(private val orderRepository: OrderRepository):ViewModel() {

    private val orderByIdRequest = MutableLiveData<Resource<Response<TransactionModel>>>()
    val orderByIdResponse : LiveData<Resource<Response<TransactionModel>>>
    get() = orderByIdRequest

    private val orderByShopId = MutableLiveData<Resource<Response<List<OrderItemListModel>>>>()
    val orderByShopIdResponse : LiveData<Resource<Response<List<OrderItemListModel>>>>
    get() = orderByShopId

    private val updateOrder = MutableLiveData<Resource<Response<String>>>()
    val updateOrderResponse: LiveData<Resource<Response<String>>>
    get() = updateOrder

    private val orderByPagination = MutableLiveData<Resource<Response<List<OrderItemListModel>>>>()
    val orderByPaginationResponse : LiveData<Resource<Response<List<OrderItemListModel>>>>
        get() = orderByPagination

    fun getOrderById(orderId: Int){
        viewModelScope.launch {
            try {
                orderByIdRequest.value = Resource.loading()
                val response = orderRepository.getOrderById(orderId)
                if(response.code==1)
                    orderByIdRequest.value=Resource.success(response)
                else{
                    orderByIdRequest.value=Resource.error(message = response.message)
                }

            }catch (e: Exception){
                println(e.printStackTrace())
            }
        }
    }

    fun updateOrder(orderModel: OrderModel){
        viewModelScope.launch {
            try{
                updateOrder.value = Resource.loading()
                val response = orderRepository.updateOrderStatus(orderModel)

                if(response.code==1) {
                    updateOrder.value = Resource.success(response)
                    getOrderByShopId(1)
                }
                else{
                    println("Something is wrong")
                    updateOrder.value = Resource.error(message=response.message)
                }
            }catch (e: Exception){
                println(e.printStackTrace())
            }
        }
    }



    fun getOrderByShopId(shopId: Int){
        viewModelScope.launch {
            try{
                orderByShopId.value = Resource.loading()
                val response = orderRepository.getOrderByShopId(shopId)

                if(response.code==1)
                    orderByShopId.value = Resource.success(response)
                else {
                    orderByShopId.value = Resource.error(message = response.message)
                }

            }catch (e: Exception){
                println(e.printStackTrace())
            }
        }
    }

    fun getOrderByPagination(shopId: Int,pageNum: Int,pageCnt: Int){
        viewModelScope.launch {
            try{
                orderByPagination.value = Resource.loading()

                val response = orderRepository.getOrderByPagination(shopId,pageNum,pageCnt)

                if(response.code==1)
                    orderByPagination.value = Resource.success(response)
                else{
                    orderByShopId.value = Resource.error(message = response.message)
                }

            }catch (e: Exception){
                //println(e.printStackTrace())
            }
        }
    }



}