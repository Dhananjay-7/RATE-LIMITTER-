import services.RateLimitterService;

public class RateLimitterApplication {
    public static void main(String[] args) throws Exception {

        RateLimitterService rateLimiterService = new RateLimitterService();

        System.out.println("Testing customerId=1 with 10 rapid requests:");
        
        for (int i = 0; i < 12; i++) {
            boolean isAllowed = rateLimiterService.rateLimitter(1);
            System.out.println("Request " + (i + 1) + " for customerId=1: " + (isAllowed ? "Allowed" : "Blocked"));
            Thread.sleep(200); // Sleep 200ms between requests
        }

        System.out.println("\nTesting rate limit reset:");
        
        // Wait for the TIME_WINDOW to reset
        Thread.sleep(1000);
        
        for (int i = 0; i < 5; i++) {
            boolean isAllowed = rateLimiterService.rateLimitter(1);
            System.out.println("Request after reset " + (i + 1) + " for customerId=1: " + (isAllowed ? "Allowed" : "Blocked"));
        }

    }
}
