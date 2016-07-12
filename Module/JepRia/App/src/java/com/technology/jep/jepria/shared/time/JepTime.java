package com.technology.jep.jepria.shared.time;

import java.util.Date;

import com.technology.jep.jepria.shared.dto.JepDto;

/**
 * Представление времени суток.<br/>
 * Класс предназначен главным образом для совместного использования с java.util.Date,<br/>
 * когда требуется раздельная работа с датой и временем суток.<br/>
 * <br/>
 * TODO Разобраться с сериализацией (избавиться от необходимости наследования от JepDto).
 */
public class JepTime extends JepDto {
  private static final long serialVersionUID = 1L;
  
  private Integer hours;
  private Integer minutes;
  private Integer seconds;
  
  @Override
  public int hashCode() {
    final int prime = 31;
    int result = super.hashCode();
    result = prime * result + ((hours == null) ? 0 : hours.hashCode());
    result = prime * result + ((minutes == null) ? 0 : minutes.hashCode());
    result = prime * result + ((seconds == null) ? 0 : seconds.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (!(obj instanceof JepTime))
      return false;
    JepTime other = (JepTime) obj;
    if (hours == null) {
      if (other.hours != null)
        return false;
    } else if (!hours.equals(other.hours))
      return false;
    if (minutes == null) {
      if (other.minutes != null)
        return false;
    } else if (!minutes.equals(other.minutes))
      return false;
    if (seconds == null) {
      if (other.seconds != null)
        return false;
    } else if (!seconds.equals(other.seconds))
      return false;
    return true;
  }

  public JepTime() {
    this(0, 0, 0);
  }

  public JepTime(Integer hours, Integer minutes) {
    this(hours, minutes, null);
  }

  public JepTime(Integer hours, Integer minutes, Integer seconds) {
    this.hours = hours;
    this.minutes = minutes;
    this.seconds = seconds;
  }
  
  @SuppressWarnings("deprecation")
  public JepTime(Date date) {
    Date javaUtilDate = new Date(date.getTime());
    this.hours = javaUtilDate.getHours();
    this.minutes = javaUtilDate.getMinutes();
    this.seconds = javaUtilDate.getSeconds();
  }

  /**
   * Конструктор, создающий время по строковому представлению.<br/>
   * <br/> 
   * TODO Разобраться по работе с history. Получается "12:34" -> JepTime -> "15:34".
   * 
   * @param token строковое представление числа миллисекунд
   */
  public JepTime(String token) {
    this(new Date(Long.parseLong(token)));
  }

  public Integer getHours() {
    return hours;
  }

  public Integer getMinutes() {
    return minutes;
  }

  public Integer getSeconds() {
    return seconds;
  }

  /**
   * Получение времени в миллисекундах.<br/>
   * <br/>
   * Метод полезен при работе с java.util.Date.
   *
   * @return время в миллисекундах
   */
  public long getMilliseconds() {
    return ((seconds != null ? seconds : 0) + minutes*60 + hours*3600)*1000;
  }
  
  /**
   * Возвращает дату, у которой дата равна заданной, а время суток устанавливается по
   * текущему объекту, независимо от времени суток заданной даты.
   * 
   * @param date дата, для которой устанавливается время суток
   * @return дата с установленным временем суток
   */
  @SuppressWarnings("deprecation")
  public Date addDate(Date date) {
    date.setHours(hours);
    date.setMinutes(minutes);
    date.setSeconds(seconds != null ? seconds : 0);
    return date;
  }
  
  public String toString() {
    StringBuilder strTime = new StringBuilder();
    strTime.append(asTwoDigit(hours));
    strTime.append(":");
    strTime.append(asTwoDigit(minutes));
    if(seconds != null) {
      strTime.append(":");
      strTime.append(asTwoDigit(seconds));
    }
    
    return strTime.toString();
  }
  
  public String toHistoryToken() {
    return this.getMilliseconds() + "";
  }

  public static String asTwoDigit(Integer value) {
    if(value == null) {
      return "null";
    }
    return value < 10 ? ("0" + value) : (value + "");
  }

}
