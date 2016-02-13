name := """amazonReceiptCollector"""

version := "1.0"

organization := "jp.sharelives"

crossPaths := false

autoScalaLibrary := false

javacOptions ++= Seq("-source", "1.8", "-target", "1.8")

libraryDependencies ++= Seq(
	"org.seleniumhq.selenium" % "selenium-java" % "2.51.0",
	"junit" % "junit" % "4.12"
)

