import sbt._

object Dependencies {
  object Versions {
    val zio = "2.0.10"
  }

  lazy val zio: List[ModuleID] = List("zio", "zio-streams").map("dev.zio" %% _ % Versions.zio)
}
