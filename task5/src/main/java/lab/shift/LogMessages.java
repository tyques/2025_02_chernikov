package lab.shift;

public final class LogMessages {
    private LogMessages(){}

    // Storage
    public static final String THREAD_WAITING_STORAGE_FULL = "склад переполнен, поток переходитв режим ожидания";
    public static final String THREAD_WAITING_STORAGE_EMPTY = "склад пуст, поток переходит в режим ожидания";
    public static final String THREAD_RESUMED = "возобновил работу";
    public static final String RESOURCE_PRODUCED = "ресурс произведен. Id ресурса: {}. \n Количество ресурсов на складе: {}";
    public static final String RESOURCE_CONSUMED = "ресурс потреблён. Id ресурса: {}. \n Количество ресурсов на складе: {}";
    public static final String LOAD_CONFIG_EXCEPTION = "Не удалось загрузить файл конфигурации {}";
    public static final String CONFIG_NOT_FOUND = "Ресурс '{}' не найден в classpath";

    //Main
    public static final String APP_START_WITH_PARAMS = "Приложение запущено с параметрами:";
    public static final String PRODUCERS_COUNT = " - Количество производителей: {}";
    public static final String CONSUMER_COUNT = " - Количество потребителей: {}";
    public static final String PRODUCER_TIME = " - Время производства: {} мс";
    public static final String CONSUMER_TIME = " - Время потребления: {} мс";
    public static final String STORAGE_SIZE = " - Размер склада: {}";
}
