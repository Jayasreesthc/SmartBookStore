package com.bookStore.service;

        import com.bookStore.entity.*;
        import com.bookStore.repository.OrderRepository;
        import com.bookStore.repository.UserOrderRepository;
        import com.bookStore.repository.UserRepository;
        import com.bookStore.repository.bookRepository;
        import org.springframework.beans.factory.annotation.Autowired;
        import org.springframework.stereotype.Service;

        import java.util.List;
        import java.util.Optional;

@Service
public class OrderService {
    @Autowired
    private OrderRepository orderRepo;

    @Autowired
    private bookRepository bRepo;

    @Autowired
    private UserOrderRepository UORepo;

    @Autowired
    private UserRepository URepo;

    public Order saveOrder(Order order) {
        return orderRepo.save(order);
    }


    public List<Order> getAllOrders() {
        return orderRepo.findAll();
    }


    public Order getOrderById(int id) {
        return orderRepo.findById(id).orElse(null);
    }

    public void updateStatus(int id, String status) {
        Order order = getOrderById(id);
        if (order != null) {
            order.setStatus(status);
            orderRepo.save(order);
        }

    }
    public List<Order> getOrdersByClient(Client client) {

        return orderRepo.findByClient(client);
    }

    public void deleteOrder(int id) {
        orderRepo.deleteById(id);
    }
}
