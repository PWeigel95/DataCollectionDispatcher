import service.DataCollectionDispatcherService;

public class Main {

    private final static String BROKER_URL = "tcp://localhost:61616";

    public static void main(String[] args) {
        DataCollectionDispatcherService dataCollectionDispatcherService = new DataCollectionDispatcherService("INVOICE REQUEST", "DATA COLLECTION", BROKER_URL);
        dataCollectionDispatcherService.run();
    }
}
