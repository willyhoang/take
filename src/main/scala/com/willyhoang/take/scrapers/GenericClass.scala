package com.willyhoang.take.scrapers

/**
 * A generic trait to represent all classes.
 */
trait GenericClass {
  val studioName : String
  def toNormalizedClass() : NormalizedClass
}
