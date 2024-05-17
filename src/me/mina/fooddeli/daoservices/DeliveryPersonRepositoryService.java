package me.mina.fooddeli.daoservices;

import me.mina.fooddeli.FoodDeliveryService;
import me.mina.fooddeli.dao.DeliveryPersonDao;
import me.mina.fooddeli.dao.ReviewDao;
import me.mina.fooddeli.model.review.Review;
import me.mina.fooddeli.model.order.Order;
import me.mina.fooddeli.model.person.DeliveryPerson;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class DeliveryPersonRepositoryService {

    private static DeliveryPersonDao deliveryPersonDao;

    private static List<DeliveryPerson> deliveryPersons;

    public DeliveryPersonRepositoryService() {
        if (deliveryPersonDao == null) {
            deliveryPersonDao = new DeliveryPersonDao();
        }

        if (deliveryPersons == null) {
            deliveryPersons = deliveryPersonDao.getAll();
        }
    }

    public Optional<DeliveryPerson> get(int id) {
        return Optional.ofNullable(deliveryPersons.stream()
                .filter(deliveryPerson -> deliveryPerson.getId() == id)
                .findFirst()
                .orElseGet(() -> deliveryPersonDao.get(id).orElse(null)));
    }

    public List<DeliveryPerson> getAll() {
        Collections.sort(deliveryPersons);
        return deliveryPersons;
    }

    public void create(DeliveryPerson deliveryPerson) {
        int id = deliveryPersonDao.create(deliveryPerson);
        deliveryPerson.setId(id);
        // If database fails, do not add to the list
        deliveryPersons.add(deliveryPerson);
    }

    public void update(int id, DeliveryPerson deliveryPerson) {
        Optional<DeliveryPerson> deliveryPersonToUpdateOptional = get(id);
        deliveryPersonToUpdateOptional.ifPresent(deliveryPersonToUpdate -> {
            deliveryPersonToUpdate.setName(deliveryPerson.getName());
            deliveryPersonToUpdate.setCurrentOrder(deliveryPerson.getCurrentOrder());
            deliveryPersonToUpdate.setReviews(deliveryPerson.getReviews());
            deliveryPersonDao.update(id, deliveryPerson);
        });
    }

    public void delete(DeliveryPerson deliveryPerson) {
        deliveryPersonDao.delete(deliveryPerson);
        deliveryPersons.remove(deliveryPerson);
        FoodDeliveryService.getOrderRepositoryService().delete(deliveryPerson.getCurrentOrder());
    }

    public void delete(int id) {
        Optional<DeliveryPerson> deliveryPersonOptional = get(id);
        deliveryPersonOptional.ifPresent(this::delete);
    }

    public void addReview(int deliveryPersonId, Review review) {
        Optional<DeliveryPerson> deliveryPersonOptional = get(deliveryPersonId);
        deliveryPersonOptional.ifPresent(deliveryPerson -> {
            int id = FoodDeliveryService.getReviewDao().create(review);
            review.setId(id);
            deliveryPerson.addReview(review);
            deliveryPersonDao.update(deliveryPersonId, deliveryPerson);
        });
    }

    public void deleteReview(int deliveryPersonId, Review review) {
        Optional<DeliveryPerson> deliveryPersonOptional = get(deliveryPersonId);
        deliveryPersonOptional.ifPresent(deliveryPerson -> {
            deliveryPerson.removeReview(review);
            FoodDeliveryService.getReviewDao().delete(review);
            deliveryPersonDao.update(deliveryPersonId, deliveryPerson);
        });

    }

    public void setCurrentOrder(int deliveryPersonId, int orderId) {
        Optional<DeliveryPerson> deliveryPersonOptional = get(deliveryPersonId);
        deliveryPersonOptional.ifPresent(deliveryPerson -> {
            deliveryPerson.setCurrentOrder(FoodDeliveryService
                    .getOrderRepositoryService().get(orderId).orElse(null));
            deliveryPersonDao.update(deliveryPersonId, deliveryPerson);
        });
    }

    public void deleteCurrentOrder(int deliveryPersonId) {
        Optional<DeliveryPerson> deliveryPersonOptional = get(deliveryPersonId);
        deliveryPersonOptional.ifPresent(deliveryPerson -> {
            FoodDeliveryService.getRestaurantRepositoryService()
                    .removeOrderFromAny(deliveryPerson.getCurrentOrder().getId());
            FoodDeliveryService.getOrderRepositoryService()
                    .delete(deliveryPerson.getCurrentOrder());
            deliveryPerson.setCurrentOrder(null);
            deliveryPersonDao.update(deliveryPersonId, deliveryPerson);
        });
    }

    public void deleteOrder(int orderId) {
        deliveryPersons.stream()
                .filter(deliveryPerson -> deliveryPerson.getCurrentOrder() != null
                        && deliveryPerson.getCurrentOrder().getId() == orderId)
                .findFirst()
                .ifPresent(deliveryPerson -> {
                    Order order = deliveryPerson.getCurrentOrder();
                    deliveryPerson.setCurrentOrder(null);
                    FoodDeliveryService.getRestaurantRepositoryService()
                            .removeOrderFromAny(order.getId());
                    FoodDeliveryService.getOrderRepositoryService().delete(order);

                    deliveryPersonDao.update(deliveryPerson.getId(), deliveryPerson);
                });
    }

    public Optional<DeliveryPerson> findByUsername(String username) {
        return deliveryPersons.stream()
                .filter(deliveryPerson -> deliveryPerson.getName().equals(username))
                .findFirst();
    }

    public Optional<DeliveryPerson> findFirstAvailable() {
        return getAll().stream()
                .filter(deliveryPerson -> deliveryPerson.getCurrentOrder() == null)
                .findFirst();
    }


}
