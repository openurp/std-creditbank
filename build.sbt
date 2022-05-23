import org.openurp.parent.Settings._
import org.openurp.parent.Dependencies._

ThisBuild / organization := "org.openurp.std.creditbank"
ThisBuild / version := "0.0.7"

ThisBuild / scmInfo := Some(
  ScmInfo(
    url("https://github.com/openurp/std-creditbank"),
    "scm:git@github.com:openurp/std-creditbank.git"
  )
)

ThisBuild / developers := List(
  Developer(
    id    = "chaostone",
    name  = "Tihua Duan",
    email = "duantihua@gmail.com",
    url   = url("http://github.com/duantihua")
  )
)

ThisBuild / description := "OpenURP Std CreditBank"
ThisBuild / homepage := Some(url("http://openurp.github.io/std-creditbank/index.html"))

val apiVer = "0.25.1"
val starterVer = "0.0.20"
val baseVer = "0.1.29"
val openurp_edu_api = "org.openurp.edu" % "openurp-edu-api" % apiVer
val openurp_std_api = "org.openurp.std" % "openurp-std-api" % apiVer
val openurp_stater_web = "org.openurp.starter" % "openurp-starter-web" % starterVer
val openurp_base_tag = "org.openurp.base" % "openurp-base-tag" % baseVer

lazy val root = (project in file("."))
  .enablePlugins(WarPlugin,UndertowPlugin)
  .settings(
    name := "openurp-std-creditbank-webapp",
    common,
    libraryDependencies ++= Seq(openurp_stater_web,openurp_base_tag),
    libraryDependencies ++= Seq(openurp_edu_api,openurp_std_api,beangle_ems_app)
  )
