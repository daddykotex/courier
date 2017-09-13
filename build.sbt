lazy val commonSettings = Seq(
  organization := "ch.lightshed",
  version := "0.2.0",
  description := "deliver electronic mail with scala",
  licenses := Seq(("MIT", url(s"https://github.com/softprops/${name.value}/blob/${version.value}/LICENSE"))),
  homepage := Some(url(s"https://github.com/softprops/${name.value}/#readme")),
  crossScalaVersions := Seq("2.10.6", "2.11.11", "2.12.3"),
  scalaVersion := crossScalaVersions.value.last,
  scalafmtOnCompile := true,
  pomExtra := (
    <scm>
      <url>git@github.com:softprops/courier.git</url>
      <connection>scm:git:git@github.com:softprops/courier.git</connection>
    </scm>
    <developers>
      <developer>
        <id>softprops</id>
        <name>Doug Tangren</name>
        <url>https://github.com/softprops</url>
      </developer>
    </developers>
  )
)
lazy val cFlags = Seq(
  scalacOptions ++= (CrossVersion.partialVersion(scalaVersion.value) match {
    case Some((2, n)) if n <= 10 =>
      Seq(
        "-language:existentials",
        "-language:higherKinds",
        "-language:implicitConversions"
      )
    case Some((2, n)) if n == 11 =>
      Seq(
        "-language:existentials",
        "-language:higherKinds",
        "-language:implicitConversions",
        "-Ypartial-unification"
      )
    case _ =>
      ScalacOptions.All
  })
)

lazy val bintraySettings = Seq(
  bintrayOrganization := Some("lightshed"),
  bintrayReleaseOnPublish in ThisBuild := false,
  bintrayPackageLabels := Seq("email", "mail", "javamail")
)

lazy val root = (project in file("."))
  .settings(inThisBuild(commonSettings ++ bintraySettings))
  .settings(
    inThisBuild(
      Seq(
        publish := {}, //do not publish the root
        name := "courier-core"
      )))
  .aggregate(core, cats)

lazy val core = (project in file("core"))
  .settings(commonSettings ++ bintraySettings ++ cFlags)
  .settings(
    name := "courier-core",
    libraryDependencies ++= Seq(
      "com.sun.mail" % "javax.mail" % "1.6.0"
    )
  )

lazy val cats = (project in file("cats"))
  .settings(commonSettings ++ bintraySettings ++ cFlags)
  .settings(
    name := "courier-for-cats",
    libraryDependencies ++= Seq(
      "org.typelevel" %% "cats-core"   % "1.0.0-MF",
      "org.typelevel" %% "cats-effect" % "0.4"
    )
  )
  .dependsOn(core)
