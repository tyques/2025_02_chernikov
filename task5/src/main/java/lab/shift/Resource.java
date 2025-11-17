package lab.shift;

public record Resource(int id) {

    private static int idCounter = 0;

    public static synchronized Resource create() {
        return new Resource(++idCounter);
    }
}
