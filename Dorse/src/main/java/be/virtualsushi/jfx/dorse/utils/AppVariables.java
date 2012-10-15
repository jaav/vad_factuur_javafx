package be.virtualsushi.jfx.dorse.utils;

import org.springframework.stereotype.Service;

import java.util.HashMap;

/**
 * Created on IntelliJ
 * User: Jef Waumans for Virtual Sushi
 * Date: 15/10/12
 * Time: 19:57
 */
@Service("appVariables")
public class AppVariables extends HashMap{
  public AppVariables() {
    System.out.println("AppVariables created");
  }
}
