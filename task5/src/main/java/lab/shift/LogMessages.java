package lab.shift;

public final class LogMessages {
    private LogMessages(){}

    // Storage
    public static final String THREAD_WAITING_STORAGE_FULL = "Поток {} переходит в режим ожидания, склад переполнен";
    public static final String THREAD_WAITING_STORAGE_EMPTY = "Поток {} переходит в режим ожидания, склад пуст";
    public static final String THREAD_RESUMED = "Поток {} возобновил работу";
    public static final String RESOURCE_PRODUCED = "Поток {} произвел ресурс. Id ресурса: {}. \n Количество ресурсов на складе: {}";
    public static final String RESOURCE_CONSUMED = "Поток {} потребил ресурс. Id ресурса: {}. \n Количество ресурсов на складе: {}";
    public static final String LOAD_CONFIG_EXCEPTION = "Не удалось загрузить файл конфигурации {}";
    public static final String CONFIG_NOT_FOUND = "Ресурс '{}' не найден в classpath";

    //Main
    public static final String APP_START_WITH_PARAMS = "Приложение запущено с параметрами:";
    public static final String PRODUCERS_COUNT = " - Количество производителей: {}";
    public static final String CONSUMER_COUNT = " - Количество потребителей: {}";
    public static final String PRODUCER_TIME = " - Время производства: {} мс";
    public static final String CONSUMER_TIME = " - Время потребления: {} мс";
    public static final String STORAGE_SIZE = " - Размер склада: {}";
    public static final String APP_SHUTTING_DOWN = "Приложение работает... Завершение через 20 секунд.";
    public static final String INITIATING_SHUTDOWN = "Инициируется остановка потоков...";
}