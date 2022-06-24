package service;

import communication.Consumer;
import communication.Producer;

public abstract class BaseService implements Runnable {

    private final String inDestination;
    private final String outDestination;
    private final String brokerUrl;

    public BaseService(String inDestination, String outDestination, String brokerUrl) {
        this.inDestination = inDestination;
        this.outDestination = outDestination;
        this.brokerUrl = brokerUrl;
    }

    @Override
    public void run() {
        while (true) {
            try {
                execute(inDestination, outDestination, brokerUrl);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void execute(String inDestination, String outDestination, String brokerUrl) throws Exception {
        String input = Consumer.receive(inDestination, 10000, brokerUrl);

        if (null == input) {
            return;
        }

        String output = executeInternal(input);
        Producer.send(output, outDestination, brokerUrl);
    }

    protected abstract String executeInternal(String input) throws Exception;
}
