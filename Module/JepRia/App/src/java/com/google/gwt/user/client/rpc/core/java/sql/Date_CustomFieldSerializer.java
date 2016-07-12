package com.google.gwt.user.client.rpc.core.java.sql;

import java.sql.Date;

import com.google.gwt.user.client.rpc.SerializationException;
import com.google.gwt.user.client.rpc.SerializationStreamReader;
import com.google.gwt.user.client.rpc.SerializationStreamWriter;

/**
 * Custom field serializer for {@link java.sql.Date}.
 * 
 * Класс-сериализатор создан в целях устранения проблемы учёта часовых поясов и
 * предназначен для подмены в gwt-servlet.jar и в gwt-user.jar.<br/>
 * Проблема возникает, когда у пользователя и на application server установлен различный
 * часовой пояс. Пусть пользователь, находящийся в Самаре, выбрал в JepDateField дату 01.01.2015,
 * что передаётся к серверу как 01.01.2015 00:00:00.000. Но для сервера,
 * находящегося в Москве, это 31.12.2014 23:00:00.000, и именно это значение будет записано в базу.
 * При этом для пользователя в интерфейсе дата будет отображаться корректно,
 * т.к. при обратной пересылке вновь учитывается разница в часовых поясах.
 * Но хранящееся в базе значение при этом будет некорректным.<br/>
 * Чтобы избежать этой проблемы, при сериализации последовательно записываются год, месяц, число,
 * час, минута и секунда. Информация о часовом поясе, таким образом, отбрасывается.
 */
public final class Date_CustomFieldSerializer {
  public static void deserialize(SerializationStreamReader streamReader, Date instance) {
  }

  @SuppressWarnings("deprecation")
  public static Date instantiate(SerializationStreamReader streamReader)
      throws SerializationException {
    int year = streamReader.readInt();
    int month = streamReader.readInt();
    int date = streamReader.readInt();
    Date result = new Date(year, month, date);
    return result;
  }

  @SuppressWarnings("deprecation")
  public static void serialize(SerializationStreamWriter streamWriter, Date instance) throws SerializationException {
    streamWriter.writeInt(instance.getYear());
    streamWriter.writeInt(instance.getMonth());
    streamWriter.writeInt(instance.getDate());
  }
}