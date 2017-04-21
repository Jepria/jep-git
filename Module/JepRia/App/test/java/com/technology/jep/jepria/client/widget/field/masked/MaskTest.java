package com.technology.jep.jepria.client.widget.field.masked;

import static org.junit.Assert.*;

import org.junit.Test;

import com.technology.jep.jepria.client.widget.field.masked.Mask;

/*
 * TODO: Тесты в данном классе написаны не по правилам. Требуется рефакторинг.
 */
public class MaskTest {

  @Test
  public void maskCreationTest() {
    int caught = 0;
    try {
      // корректная маска
      new Mask("ACL0AAAAC:A_>aaa<aaa<>aaa");      
    }
    catch (IllegalArgumentException exc) {
      caught += 1;
    }
    try {
      // ещё одна корректная маска
      new Mask("aaa>aaaa<>aaa<aaa");      
    }
    catch (IllegalArgumentException exc) {
      caught += 1;
    }
    try {
      // ещё одна корректная маска
      new Mask("ZZZQQQFSDF");      
    }
    catch (IllegalArgumentException exc) {
      caught += 1;
    }
    
    assertTrue(caught == 0);

  }
/*
 * TODO: Чтобы заставить данный тест работать, необходим mock GWT-зависимостей.
 * Кроме того, он также нуждается в рефакторинге.  
 */
//  @Test
//  public void maskCreationErrorTest() {
//    int caught = 0;
//    try {
//      // некорректная маска: разные типы необязательных символов
//      new Mask("ACL0aaa9#ll");
//    }
//    catch (IllegalArgumentException exc) {
//      caught += 1;
//    }
//    try {
//      // некорректная маска: необязательные символы посреди маски
//      new Mask("ACL0aaaA<>LL");
//    }
//    catch (IllegalArgumentException exc) {
//      caught += 1;
//    }
//    assertTrue(caught == 2);
//  }
  
  @Test
  public void matchTest() {
    assertTrue(Mask.match("hello", "he\\l\\lo"));
    assertTrue(Mask.match("he11o0", "LL00aa"));
    assertTrue(Mask.match("22A_333В№(915)kksaa", "00L\\_0A0CC(000)cccccc"));
    assertFalse(Mask.match("22A_333В№(915Zkksaa", "00L\\_0A0CC(000)cccccc")); // неверный символ
    assertFalse(Mask.match("22A_333В№(915)kksaa", "00L\\_0A0CC(000)ccc")); // слишком длинная строка
    assertTrue(Mask.match("", "aaa"));
    assertTrue(Mask.match("А1вгд", "LALll"));
    assertFalse(Mask.match("222", "LLL"));
  }
  
  @Test
  public void matchWithEmptyCharsTest() {
    Mask mask = new Mask("AA()LL00aa");
    assertTrue(mask.match(new char[] {'\0','\0','\0','\0','\0','\0','\0','\0','\0','\0'}, true));
    assertTrue(mask.match(new char[] {'1','2','(','\0','\0','\0','\0','\0','\0','\0'}, true));
    assertFalse(mask.match(new char[] {'1','2','z','\0','\0','\0','\0','\0','\0','\0'}, true));
    assertTrue(mask.match(new char[] {'1','2','(','\0','\0','\0','\0','\0','\0','\0'}, true));
    assertTrue(mask.match(new char[] {'\0','1','\0',')','z','\0','2','3','a','\0'}, true));
    
  }
  
  private static String charArrayToStr(char[] chars) {
    String str = "";
    for (char ch : chars) {
      str += ch != '\0' ? ch : "*";
    }
    return str;
  }
  
  private static char[] toCharArray(String str) {
    return toCharArray(str, str.length());
  }
  
  private static char[] toCharArray(String str, int size) {
    if (str.length() > size) {
      throw new IllegalArgumentException();
    }
    char[] chars = new char[size];
    str.getChars(0, str.length(), chars, 0);
    return chars;
  }
  
  @Test
  public void insertCharTest() {
    assertTrue(
      charArrayToStr(new Mask("00AAccc").insertChar(toCharArray("11ZZ#", 7), 0, 'A'))
        .equals("11ZZ#**"));
    assertTrue(
      charArrayToStr(new Mask("00LLccc").insertChar(toCharArray("11ZZ#", 7), 3, '4'))
        .equals("11ZZ#**"));
    assertTrue(
      charArrayToStr(new Mask("00(LL)ccc").insertChar(toCharArray("11(ZZ)#", 9), 7, '_'))
        .equals("11(ZZ)#_*"));
    assertTrue(
      charArrayToStr(new Mask("00(LL)ccc").insertChar(toCharArray("11(ZZ)#", 9), 2, '_'))
        .equals("11(ZZ)#**"));
    assertTrue(
      charArrayToStr(new Mask("00(LL)ccc").insertChar(toCharArray("11(ZZ)#", 9), 6, '_'))
        .equals("11(ZZ)_#*"));
    assertTrue(
      charArrayToStr(new Mask("00(LL)999").insertChar(toCharArray("11(ZZ)2", 9), 6, '_'))
        .equals("11(ZZ)2**"));
    assertTrue(
      charArrayToStr(new Mask("00(LL)999").insertChar(toCharArray("11(ZZ)2", 9), 8, '2'))
        .equals("11(ZZ)22*"));
    assertTrue(
      charArrayToStr(new Mask("00(LL)999").insertChar(toCharArray("11(ZZ)2", 9), 9, '2'))
        .equals("11(ZZ)22*"));
    assertTrue(
      charArrayToStr(new Mask("00(LL)999").insertChar(toCharArray("11(ZZ)222", 9), 9, '2'))
        .equals("11(ZZ)222"));
    assertTrue(
      charArrayToStr(new Mask("000LLL").insertChar(toCharArray("2\0\0ABC"), 0, '1'))
        .equals("12*ABC"));
    assertTrue(
      charArrayToStr(new Mask("000LLL").insertChar(toCharArray("\0" + "2\0ABC"), 0, '1'))
        .equals("12*ABC"));
    assertTrue(
      charArrayToStr(new Mask("000LLL").insertChar(toCharArray("\0" + "2\0ABC"), 1, '1'))
        .equals("*12ABC"));    
  }
  
  @Test
  public void canInsertTest() {
    /*
     * \0000 - это символ \0, такое обозначение - потому что иначе читает
     * как \01 и получается фигня. :)
     */
    assertTrue(new Mask("00((LL)999").canInsert(toCharArray("\00001((ZZ)22", 10), 0, '2'));
    assertTrue(new Mask("00((LL)999").canInsert(toCharArray("1\0((ZZ)22", 10), 0, '2'));
    /*
     * Эта позиция уже занята и двигать некуда.
     */
    assertFalse(new Mask("00((LL)999").canInsert(toCharArray("11((ZZ)22", 10), 1, '3'));
    /*
     * В данной позиции литерал, а переданный символ с ним не совпадает.
     */
    assertFalse(new Mask("00((LL)999").canInsert(toCharArray("11((ZZ)22", 10), 2, '3'));
    assertTrue(new Mask("00((LL)999").canInsert(toCharArray("11((ZZ)22", 10), 2, '('));
    assertTrue(new Mask("000AAALLL").canInsert(toCharArray("112z2qAB\0"), 3, '3'));
    /*
     * Нельзя осуществить вставку из-за несовпадения типов символов.
     */
    assertFalse(new Mask("000AAALLL").canInsert(toCharArray("112z22AB\0"), 3, '3'));
    /*
     * В данной позиции литерал, а переданный символ с ним не совпадает.
     */
    assertTrue(new Mask("CCCC(C").canInsert(toCharArray("123((\0"), 2, '2'));
    assertFalse(new Mask("00((LL)999").canInsert(toCharArray("11((ZZ)22", 10), 3, '4'));
    assertTrue(new Mask("00((LL)999").canInsert(toCharArray("11((Z", 10), 5, 'q'));
    assertTrue(new Mask("00((LL)999").canInsert(toCharArray("11((ZZ)22", 10), 7, '3'));
    assertTrue(new Mask("00((LL)999").canInsert(toCharArray("11((ZZ)22", 10), 9, '3'));
    assertTrue(new Mask("00((LL)999").canInsert(toCharArray("11((ZZ)22", 10), 10, '3'));
    assertFalse(new Mask("00((LL)999").canInsert(toCharArray("11((ZZ)222", 10), 10, '3'));
    
  }
  
  @Test
  public void positionOnInsertTest() {
    assertTrue(new Mask("00AAccc").getCursorPositionOnInsert(toCharArray("11ZZ#", 7), 0, 'A') == 0);
    assertTrue(new Mask("00AAccc").getCursorPositionOnInsert(toCharArray("11ZZ#", 7), 3, '4') == 4);
    assertTrue(new Mask("00ALccc").getCursorPositionOnInsert(toCharArray("11ZZ#", 7), 3, '4') == 3);
    assertTrue(new Mask("00(LL)ccc").getCursorPositionOnInsert(toCharArray("11(ZZ)#", 9), 7, '_') == 8);
    assertTrue(new Mask("00(LL)999").getCursorPositionOnInsert(toCharArray("11(ZZ)2", 9), 6, '_') == 6);
    assertTrue(new Mask("00(LL)999").getCursorPositionOnInsert(toCharArray("11(ZZ)22", 9), 8, '2') == 9);
    assertTrue(new Mask("00((LL)999").getCursorPositionOnInsert(toCharArray("11((ZZ)22", 10), 2, '2') == 4);
    assertTrue(new Mask("00((LL)999").getCursorPositionOnInsert(toCharArray("1", 10), 1, '2') == 4);
    assertTrue(new Mask("00((LL)999").getCursorPositionOnInsert(toCharArray("11((a1)2", 10), 10, '2') == 9);
    assertTrue(new Mask("zzzzzzz").getCursorPositionOnInsert(toCharArray("zz", 7), 1, 'z') == 7);
  }
  
  @Test
  public void positionOnLeftTest() {
    assertTrue(new Mask("00((LL)999").getCursorPositionOnLeft(4) == 2);
    assertTrue(new Mask("00((LL)999").getCursorPositionOnLeft(5) == 4);
    assertTrue(new Mask("00((LL)999").getCursorPositionOnLeft(0) == 0);
    assertTrue(new Mask("00((LL)999").getCursorPositionOnLeft(10) == 9);
    assertTrue(new Mask("zzzzzzzzzzzzzzzzz").getCursorPositionOnLeft(7) == 0);
  }
  
  public void positionOnRightTest() {
    assertTrue(new Mask("00((LL)999").getCursorPositionOnRight(0) == 1);
    assertTrue(new Mask("00((LL)999").getCursorPositionOnRight(2) == 4);
    assertTrue(new Mask("00((LL)999").getCursorPositionOnRight(9) == 10);
    assertTrue(new Mask("00((LL)999").getCursorPositionOnRight(10) == 10);
    assertTrue(new Mask("zzzzzzzzzzzzzzzzz").getCursorPositionOnRight(7) == 17);
  }
  
  @Test
  public void removeCharTest() {
    assertTrue(
        charArrayToStr(new Mask("00AAccc").removeChar(toCharArray("11ZZ#", 7), 0))
          .equals("*1ZZ#**"));
    assertTrue(
        charArrayToStr(new Mask("00AAccc").removeChar(toCharArray("11ZZ#az", 7), 6))
          .equals("11ZZ#a*"));
    assertTrue(
        charArrayToStr(new Mask("00(AA)ccc").removeChar(toCharArray("11(ZZ)#az", 9), 2))
          .equals("11(ZZ)#az"));
    assertTrue(
        charArrayToStr(new Mask("00(AA)ccc").removeChar(toCharArray("11(ZZ)#az", 9), 4))
          .equals("11(Z*)#az"));
    assertTrue(
        charArrayToStr(new Mask("00(AA)ccc").removeChar(toCharArray("11(ZZ)#az", 9), 8))
          .equals("11(ZZ)#a*"));
    assertTrue(
        charArrayToStr(new Mask("AC>L0(((00))ll<>ll").removeChar(toCharArray("11Z1(((11))ABcd", 15), 14))
          .equals("11Z1(((11))ABc*"));
    
  }
  
  @Test
  public void pasteTest() {
    assertTrue(
        charArrayToStr(new Mask("00((LL)999").paste(toCharArray("11((al)2", 10), 8, "23"))
          .equals("11((al)223"));
    assertTrue(
        charArrayToStr(new Mask("00((LL)999").paste(toCharArray("\0\0((a\0)2", 10), 0, "23"))
          .equals("23((a*)2**"));
    assertTrue(
      charArrayToStr(new Mask("00((LL)999").paste(toCharArray("1\0((\0\0)2", 10), 1, "3((al)"))
        .equals("13((al)2**"));
    assertTrue(
      charArrayToStr(new Mask("00((LL)999").paste(toCharArray("1\0((\0\0)2", 10), 1, "3((al)31"))
        .equals("13((al)312"));
    assertTrue(
      charArrayToStr(new Mask("00((LL)999").paste(toCharArray("11((al)2", 10), 2, "3((al)31"))
        .equals("11((al)2**"));
    assertTrue(
      charArrayToStr(new Mask("00((LL)999").paste(toCharArray("11((al)212", 10), 10, "a"))
        .equals("11((al)212"));
    assertTrue(
      charArrayToStr(new Mask("00((LL)999").paste(toCharArray("11((al)2", 10), 1, "3((al)312"))
        .equals("11((al)2**"));
    assertTrue(
        charArrayToStr(new Mask("00((LL)999").paste(toCharArray("11((al)2", 10), 1, "3((al)3a"))
          .equals("11((al)2**"));
    assertTrue(
        charArrayToStr(new Mask("00((LL)999").paste(toCharArray("11((al)2", 10), 10, "33"))
          .equals("11((al)233"));
  }
  
  @Test
  public void canPasteTest() {
    assertTrue(
      new Mask("00((LL)999").canPaste(toCharArray("\0\0((al)2", 10), 0, "23"));
    assertTrue(
      new Mask("00((LL)999").canPaste(toCharArray("1\0((\0\0)2", 10), 1, "3((al)"));
    assertTrue(
      new Mask("00((LL)999").canPaste(toCharArray("1\0((\0\0)2", 10), 1, "3((al)31"));
    assertFalse(
      new Mask("00((LL)999").canPaste(toCharArray("11((al)2", 10), 2, "3((al)31"));
    assertFalse(
      new Mask("00((LL)999").canPaste(toCharArray("11((al)212", 10), 10, "a"));
    assertFalse(
      new Mask("00((LL)999").canPaste(toCharArray("11((al)2", 10), 1, "3((al)312"));
    assertFalse(
      new Mask("00((LL)999").canPaste(toCharArray("11((al)2", 10), 1, "3((al)3a"));
    assertTrue(
      new Mask("00((LL)999").canPaste(toCharArray("11((al)2", 10), 10, "34"));
  }
  
  @Test
  public void cursonPositionOnPasteTest() {
    assertTrue((
      new Mask("lllllll")).getCursorPositionOnPaste(toCharArray("", 7), 7, "abc") == 3);
  }
}
