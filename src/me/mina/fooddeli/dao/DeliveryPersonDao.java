package me.mina.fooddeli.dao;

import me.mina.fooddeli.model.person.DeliveryPerson;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DeliveryPersonDao implements Dao<DeliveryPerson> {

    //Simulam baza de date
    private static List<DeliveryPerson> deliveryPeople = null;


    public DeliveryPersonDao() {
        //TODO de inlocuit cu conexiunea la baza de date
        if (deliveryPeople == null) {
            deliveryPeople = new ArrayList<>();
        }
    }

    @Override
    public Optional<DeliveryPerson> get(int id) {
        //TODO De folosit baza de date
        return deliveryPeople.stream()
                .filter(obj -> obj.getId() == id)
                .findFirst();
    }

    @Override
    public List<DeliveryPerson> getAll() {
        //TODO De folosit baza de date
        return new ArrayList<>(deliveryPeople);
    }

    @Override
    public void create(DeliveryPerson obj) {
        //TODO De folosit baza de date
        deliveryPeople.add(obj);
    }

    @Override
    public void update(int id, DeliveryPerson obj) {
        Optional<DeliveryPerson> objOptional = get(id);
        //TODO De folosit baza de date
        if (objOptional.isPresent()) {
            DeliveryPerson objToUpdate = objOptional.get();
            objToUpdate.setName(obj.getName());
            objToUpdate.setCurrentOrder(obj.getCurrentOrder());
            objToUpdate.setReviews(obj.getReviews());
        }else{
            System.out.println("Update error: DeliveryPerson with id " + id + " not found");

        }
    }


    @Override
    public void delete(DeliveryPerson obj) {
        //TODO De folosit baza de date
        deliveryPeople.remove(obj);
    }
}
