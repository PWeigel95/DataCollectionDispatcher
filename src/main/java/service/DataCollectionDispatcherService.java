package service;

import communication.Producer;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class DataCollectionDispatcherService extends BaseService {

    private final String id;

    private static final String DB_CONNECTION = "jdbc:postgresql://localhost:5432/postgres?user=admin&password=password";

    private final static String BROKER_URL = "tcp://localhost:61616";

    public DataCollectionDispatcherService(String inDestination, String outDestination, String brokerUrl) {
        super(inDestination, outDestination, brokerUrl);

        this.id = UUID.randomUUID().toString();

        System.out.println("Invoice Worker (" + this.id + ") started...");
    }

    @Override
    protected String executeInternal(String input) throws Exception {
        /*
        int amountOfStations = getIdOfStations();

        //Message an StationData Collector
        for(int i = 1; i <= amountOfStations; i++){
            //Producer.send(customer_id:station_id, station_id, BROKER_URL);
            Producer.send(input+":"+i+":"+amountOfStations, "DATA COLLECTION", BROKER_URL);

        }*/



        for(Integer station_id: getListOfStationsId())
        {
            Producer.send(input+":"+station_id+":"+getListOfStationsId().size(), "DATA COLLECTION", BROKER_URL);
        }

        //Message an DataCollectionDispatcher
        Producer.send(input, "INVOICE REQUEST STARTED", BROKER_URL);

        System.out.println("Finished " + input + " job (" + id + ")");

        return "ok";
    }

    private Connection connect() throws SQLException {
        return DriverManager.getConnection(DB_CONNECTION);
    }

    public List<Integer> getListOfStationsId() throws Exception {
        List<Integer> stations = new ArrayList<>();

        try (Connection conn = connect()) {
            String sql = "SELECT station_id FROM stations WHERE available = 'true'";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);

            ResultSet resultSet = preparedStatement.executeQuery();

            while(resultSet.next()){
                stations.add(resultSet.getInt(1));
            }


        } catch (SQLException e) {
            throw new Exception(e.getNextException());
        }
        return stations;
    }
}
