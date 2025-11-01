package lab.shift;

public final class LogMessages {
    private LogMessages(){}

    public static final String THREAD_WAITING_STORAGE_FULL = "{}, склад переполнен, поток переходитв режим ожидания";
    public static final String THREAD_WAITING_STORAGE_EMPTY = "{} склад пуст, поток переходит в режим ожидания";
    public static final String THREAD_RESUMED = "{}, возобновил работу";
    public static final String RESOURCE_PRODUCED = "{} ресурс произведен. Id ресурса: {}. \n Количество ресурсов на складе: {}";
    public static final String RESOURCE_CONSUMED = "{} ресурс потреблён. Id ресурса: {}. \n Количество ресурсов на складе: {}";
    public static final String LOAD_CONFIG_EXCEPTION = "Не удалось загрузить файл конфигурации {}";
    public static final String CONFIG_NOT_FOUND = "Ресурс '{}' не найден в classpath";
}
