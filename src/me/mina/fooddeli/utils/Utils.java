package me.mina.fooddeli.utils;

import me.mina.fooddeli.model.review.Review;

import java.util.List;
import java.util.Queue;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Utils {

    public static double reviewsToRating(List<Review> reviews){
        return reviews.stream()
                .mapToInt(Review::getRating)
                .average()
                .orElse(0D);
    }

    public static String objectListToString(List<?> list){
        return streamToString(list.stream());
    }

    public static String queueToString(Queue<?> queue){
        return streamToString(queue.stream());
    }

    private static String streamToString(Stream<?> stream){
        return "[\n"+stream
                .map(Object::toString)
                .collect(Collectors.joining(", \n"))+"\n]";
    }

}
