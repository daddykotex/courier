addCommandAlias("ci", ";clean ;+test ;project docs ;tut")
addCommandAlias("release", ";project root ;+publishSigned ;sonatypeReleaseAll")

lazy val gpgSettings = Seq(
  useGpg := false,
  usePgpKeyHex("E70E9111FD34D631"),
  pgpPublicRing := file(".") / "project" / ".gnupg" / "pubring.gpg",
  pgpSecretRing := file(".") / "project" / ".gnupg" / "secring.gpg",
  pgpPassphrase := sys.env.get("PGP_PASS").map(_.toArray)
)

lazy val publisherSettings = Seq(
  sonatypeProfileName := organization.value,
  credentials += Credentials(
    "Sonatype Nexus Repository Manager",
    "oss.sonatype.org",
    sys.env.getOrElse("SONATYPE_USER", ""),
    sys.env.getOrElse("SONATYPE_PASS", "")
  ),
  publishMavenStyle := true,
  isSnapshot := version.value endsWith "SNAPSHOT",
  publishTo := Some(
    if (isSnapshot.value)
      Opts.resolver.sonatypeSnapshots
    else
      Opts.resolver.sonatypeStaging
  )
)

lazy val releaseSettings = gpgSettings ++ publisherSettings

lazy val commonSettings = releaseSettings ++ Seq(
  organization := "com.github.daddykotex",
  description := "deliver electronic mail with scala",
  licenses := Seq(("MIT", url(s"https://opensource.org/licenses/MIT"))),
  homepage := Some(url(s"https://github.com/daddykotex/${name.value}/#readme")),
  crossScalaVersions := Seq("2.10.6", "2.11.11", "2.12.3"),
  scalaVersion := crossScalaVersions.value.last,
  scalafmtOnCompile := true,
  scmInfo := Some(
    ScmInfo(
      url(s"https://github.com/daddykotex/${name.value}/#readme"),
      s"scm:git@github.com:daddykotex/${name.value}.git"
    )
  ),
  developers := List(
    Developer(
      id = "daddykotex",
      name = "David Francoeur",
      email = "noreply@davidfrancoeur.com",
      url = url("https://davidfrancoeur.com")
    )
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

lazy val doNotPublishArtifact = Seq(
  publish := {},
  publishArtifact := false,
  publishArtifact in (Compile, packageDoc) := false,
  publishArtifact in (Compile, packageSrc) := false,
  publishArtifact in (Compile, packageBin) := false
)

lazy val root = (project in file("."))
  .aggregate(core, cats, docs)
  .settings(inThisBuild(commonSettings))
  .settings(doNotPublishArtifact)
  .settings(
    inThisBuild(
      Seq(
        name := "courier-core"
      )))

lazy val core = (project in file("core"))
  .settings(commonSettings ++ cFlags)
  .settings(
    name := "courier-core",
    libraryDependencies ++= Seq(
      "com.sun.mail" % "javax.mail" % "1.6.0"
    )
  )

lazy val cats = (project in file("cats"))
  .settings(commonSettings ++ cFlags)
  .settings(
    name := "courier-for-cats",
    libraryDependencies ++= Seq(
      "org.typelevel" %% "cats-core"   % "1.0.0-MF",
      "org.typelevel" %% "cats-effect" % "0.4"
    )
  )
  .dependsOn(core)

lazy val docs = (project in file("docs"))
  .enablePlugins(TutPlugin)
  .settings(inThisBuild(commonSettings))
  .settings(doNotPublishArtifact)
  .settings(
    name := "courier-docs",
    tutTargetDirectory := file("docs")
  )
  .dependsOn(core)
