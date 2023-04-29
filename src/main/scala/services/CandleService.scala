package com.github.grigoriiberezin
package services

import models.Bar
import models.Trade
import zio._
import zio.stream._

sealed trait CandleService {

  def barsFromTrades(
      trades: ZStream[Any, Nothing, Trade],
      interval: Long
  ): ZStream[Any, Nothing, Bar]

}

object CandleService {

  private final case class StockCandleService() extends CandleService {

    override def barsFromTrades(trades: UStream[Trade], interval: Long): UStream[Bar] = trades
      .scan[Option[Bar]](None) { case (acc, trade) =>
        val curBar = acc.getOrElse(Bar.byOneTrade(trade, interval))
        if (trade.time < curBar.time + interval) Some(curBar.copy(
          end = trade.price,
          low = Math.min(curBar.low, trade.price),
          high = Math.max(curBar.high, trade.price)
        ))
        else Some(Bar.byOneTrade(trade, interval))
      }.collect { case Some(bar) => bar }
      .debug

  }

  lazy val live: ULayer[CandleService] = ZLayer.fromFunction(StockCandleService.apply _)
}
