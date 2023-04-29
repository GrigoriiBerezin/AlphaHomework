package com.github.grigoriiberezin
package services

import models.Trade

import zio._
import zio.stream._

import java.util.concurrent.TimeUnit

sealed trait TradeProducer {
  def getTrades: UStream[Trade]
}

object TradeProducer {

  private final case class InfiniteTradeProducer() extends TradeProducer {

    private def generateNextTrade: UIO[Trade] = for {
      price <- Random.nextDouble
      time <- Clock.currentTime(TimeUnit.MILLISECONDS)
    } yield Trade(Math.floor(price * 10_000), time)

    override def getTrades: UStream[Trade] = ZStream
      .repeatZIOWithSchedule(generateNextTrade, Schedule.fixed(1.second)).debug

  }

  lazy val test: ULayer[TradeProducer] = ZLayer.fromFunction(InfiniteTradeProducer.apply _)
}
