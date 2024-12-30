package services;

import constants.RateLimitterConstant;
import java.util.HashMap;
import java.util.Map;

public class RateLimitterService {
    Map<Integer, Pairs<Integer, Long>> CustomerHitMap = new HashMap<>();

    public boolean rateLimitter(int customerId) {
        long currentTime = System.currentTimeMillis();

        if (!CustomerHitMap.containsKey(customerId)) {
            // First request for the customer, initialize bucket and time window
            Pairs<Integer, Long> pair = new Pairs<>(RateLimitterConstant.BUCKET_SIZE - 1, currentTime + RateLimitterConstant.TIME_WINDOW);
            CustomerHitMap.put(customerId, pair);
            System.out.println("New customer: Allowed. Remaining hits: " + (RateLimitterConstant.BUCKET_SIZE - 1));
            return true;
        } else {
            Pairs<Integer, Long> customerData = CustomerHitMap.get(customerId);
            int remainingHits = customerData.getKey();
            long windowExpiry = customerData.getValue();

            System.out.println("Customer " + customerId + ": Remaining hits: " + remainingHits + ", Window expiry: " + windowExpiry + ", Current time: " + currentTime);

            if (currentTime <= windowExpiry) {
                // Within the time window
                if (remainingHits > 0) {
                    // Allow request and decrement bucket
                    CustomerHitMap.put(customerId, new Pairs<>(remainingHits - 1, windowExpiry));
                    System.out.println("Request allowed. Remaining hits: " + (remainingHits - 1));
                    return true;
                } else {
                    // Block the request if no remaining hits
                    System.out.println("Request blocked. No remaining hits.");
                    return false;
                }
            } else {
                // Time window expired, reset bucket and time window
                Pairs<Integer, Long> pair = new Pairs<>(RateLimitterConstant.BUCKET_SIZE - 1, currentTime + RateLimitterConstant.TIME_WINDOW);
                CustomerHitMap.put(customerId, pair);
                System.out.println("Time window reset. Request allowed. Remaining hits: " + (RateLimitterConstant.BUCKET_SIZE - 1));
                return true;
            }
        }
    }
}
