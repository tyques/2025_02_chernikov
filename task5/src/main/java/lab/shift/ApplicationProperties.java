package lab.shift;

public class ApplicationProperties {
    private final int producerCount;
    private final int consumerCount;
    private final int producerTime;
    private final int consumerTime;
    private final int storageSize;

    public ApplicationProperties(int producerCount, int consumerCount, int producerTime, int consumerTime, int storageSize) {
        this.producerCount = producerCount;
        this.consumerCount = consumerCount;
        this.producerTime = producerTime;
        this.consumerTime = consumerTime;
        this.storageSize = storageSize;
    }

    public int getProducerCount() {
        return producerCount;
    }

    public int getConsumerCount() {
        return consumerCount;
    }

    public int getProducerTime() {
        return producerTime;
    }

    public int getConsumerTime() {
        return consumerTime;
    }

    public int getStorageSize() {
        return storageSize;
    }
}