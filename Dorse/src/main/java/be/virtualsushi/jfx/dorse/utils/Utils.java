package be.virtualsushi.jfx.dorse.utils;

/**
 * Created on IntelliJ
 * User: Jef Waumans for Virtual Sushi
 * Date: 18/10/12
 * Time: 10:53
 */
public class Utils {

  public static String shorten(String original, int newLenght){
    if(original.length()<=newLenght) return original;
    else return original.substring(0, newLenght-3)+"...";
  }
}
