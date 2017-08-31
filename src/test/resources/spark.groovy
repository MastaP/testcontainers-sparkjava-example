@Grab(group = 'org.slf4j', module = 'slf4j-simple', version = '1.7.21')
@Grab(group = 'com.sparkjava', module = 'spark-core', version = '2.6.0')
import static spark.Spark.*

println "starting spark"

port(4567)

get("/hello") { req, res ->
    return "hello there!"
}

println "awaiting spark initialization"
awaitInitialization()
println "spark app started on port " + port()

