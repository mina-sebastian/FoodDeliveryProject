package me.mina.fooddeli.utils;

import me.mina.fooddeli.model.Review;

import java.util.List;
import java.util.stream.Collectors;

public class Utils {

    public static double reviewsToRating(List<Review> reviews){
        return reviews.stream()
                .mapToInt(Review::getRating)
                .average()
                .orElse(0D);
    }

    public static String objectListToString(List<?> list){
        return list.stream()
                .map(Object::toString)
                .collect(Collectors.joining("\n"));
    }

}
