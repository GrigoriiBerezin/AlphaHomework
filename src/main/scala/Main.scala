package com.github.grigoriiberezin

import services.{CandleService, TradeProducer}

import zio._

object Main extends ZIOAppDefault {
  private val mainApp = for {
    producer <- ZIO.service[TradeProducer]
    service <- ZIO.service[CandleService]
    bars <- service.barsFromTrades(producer.getTrades, 1.minute.toMillis).runCollect
  } yield bars

  override def run: ZIO[Any with ZIOAppArgs with Scope, Any, Any] = mainApp.provide(CandleService.live, TradeProducer.test)
}
