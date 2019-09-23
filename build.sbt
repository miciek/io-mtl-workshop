lazy val root = (project in file("."))
  .settings(
    name := "io-mtl-workshop",
    organization := "miciek",
    scalaVersion := "2.12.9",
    scalacOptions ++= List(
        "-unchecked",
        "-deprecation",
        "-Ypartial-unification",
        "-language:higherKinds",
        "-language:implicitConversions"
      ),
    libraryDependencies ++= Seq(
        "org.typelevel"         %% "cats-core"        % "2.0.0",
        "org.typelevel"         %% "cats-effect"      % "2.0.0",
        "org.typelevel"         %% "cats-mtl-core"    % "0.7.0",
        "com.github.mpilquist"  %% "simulacrum"       % "0.19.0",
        "com.olegpy"            %% "meow-mtl-core"    % "0.4.0",
        "com.olegpy"            %% "meow-mtl-effects" % "0.4.0",
        "io.circe"              %% "circe-generic"    % "0.12.1",
        "com.github.pureconfig" %% "pureconfig"       % "0.12.0",
        "org.scalatest"         %% "scalatest"        % "3.0.8" % Test
      ),
    addCompilerPlugin("org.scalamacros" % "paradise" % "2.1.0" cross CrossVersion.full)
  )
