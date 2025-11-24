package ru.shift.common;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Транспортный объект, представляющий сообщение в системе обмена данными.
 * Содержит метаданные (тип, отправитель, время) и полезную нагрузку.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Message {

    /**
     * Тип сообщения.
     * Определяет назначение пакета и способ обработки поля content.
     */
    private MessageType type;

    /**
     * Имя отправителя.
     * Может содержать имя пользователя (для чата) или системный идентификатор (для служебных сообщений).
     */
    private String sender;

    /**
     * Содержимое сообщения.
     * В зависимости от типа может содержать обычный текст или структурированные данные (payload).
     */
    private String content;

    /**
     * Дата и время создания или отправки сообщения.
     */
    private LocalDateTime timestamp;

    /**
     * Вспомогательное поле для индексации.
     * Используется преимущественно при запросах истории сообщений (например, для пагинации или указания смещения).
     */
    private Integer index;
}