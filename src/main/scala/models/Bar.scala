package com.github.grigoriiberezin
package models

case class Bar(start: Double, end: Double, low: Double, high: Double, time: Long)

object Bar {

  def byOneTrade(trade: Trade, interval: Long): Bar = {
    val Trade(price, time) = trade
    Bar(price, price, price, price, (time / interval) * interval)
  }

}
