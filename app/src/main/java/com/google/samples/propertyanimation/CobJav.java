package com.google.samples.propertyanimation;

public class CobJav {
  public static int a = 10;
}

class CobJav2 extends CobJav {

}

class C {

  public static void main(String[] args) {
    //CobJav2.a;
    A.Companion.getI();
    B.Companion.getI();
    //A.getI();
    //B.getI();
  }
}