package com.technology.jep.jepria.auto.application.entrance;

public enum EntranceStatus {

  INSTANCE;
  
  /**
   * Singleton
   */
  private EntranceStatus() {}
  
  public static EntranceStatus getInstance() {
    return INSTANCE;
  }
  
  
  /*
   * Константы для оптимизации ожидания реакции на login/logout
   * Чтобы ожидание появления приложения/страницы логин можно было отложить 
   */
  public static final int LAST_ENTRANCE_OPERATION_LOGIN = 1;
  public static final int LAST_ENTRANCE_OPERATION_LOGOUT = 2;
  
  private int lastEntranceOperation;
  
  /**
   * @return the lastEntranceOperation
   */
  public int getLastEntranceOperation() {
    return lastEntranceOperation;
  }
  
  /**
   * @param lastEntranceOperation the lastEntranceOperation to set
   */
  public void setLastEntranceOperation(int lastEntranceOperation) {
    this.lastEntranceOperation = lastEntranceOperation;
  }
}
