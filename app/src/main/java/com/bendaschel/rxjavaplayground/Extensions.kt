package com.bendaschel.rxjavaplayground

import org.apache.commons.codec.binary.Hex
import kotlin.math.min

val Any.logTag: String
  get() {
      val className = this.javaClass.simpleName
      val endIndex = min(className.length, 23)
      return className.substring(0, endIndex)
  }


fun ByteArray.bytesToHex(): String {
    return Hex.encodeHexString(this)
}