package com.google.samples.propertyanimation

import com.google.samples.propertyanimation.A.Companion.i

open class A {
  companion object {
    //@JvmStatic
    val i = 70
  }
}

class B: A() {

}

private fun a() {
  i
}