import groovy.json.JsonOutput
import org.apache.camel.Exchange
import org.apache.camel.Processor
import org.apache.camel.builder.RouteBuilder
import org.apache.camel.impl.DefaultCamelContext
import org.apache.camel.impl.SimpleRegistry
import org.swinchester.metrics.Constants
import org.swinchester.metrics.HawkularRequestProcessor

def static runCamel() {

    def token = 'Hm66kqh2JS1IgNR_AFLJMUHMWJp6SJDk9EAkrOJ58cY'


    def camelCtx = new DefaultCamelContext(new SimpleRegistry())
    camelCtx.addRoutes(new RouteBuilder() {
        def void configure() {
            from('timer://helloTimer?period=5000')
                .setProperty(Constants.AUTHORIZATION, constant('Bearer ' + token))
                .setProperty(Constants.HAWKULAR_TENANT, constant("origin-metrics"))
                .setProperty(Constants.HAWKULAR_METRICS_HOST, constant("https://hawkular-metrics-origin-metrics.rhel-cdk.10.1.2.2.xip.io"))
                .setProperty(Constants.HAWKULAR_METRICS_TYPE, constant("gauges"))
                .process(new Processor() {
                @Override
                void process(Exchange exchange) throws Exception {

                    //Random number between 1 and 100
                    int randomNumber = new Random().nextInt(100 - 1) + 1;
                    //create a metric...

                    exchange.in.body = JsonOutput.toJson(
                            [
                                    [
                                            id: "1.my-metric.count",
                                            data:[[
                                                    timestamp: System.currentTimeMillis(),
                                                    value: randomNumber
                                            ]]
                                    ]
                            ]
                    )
                }
            }).process(new HawkularRequestProcessor())
        }
    })
    camelCtx.start()
    // Stop Camel when the JVM is shut down
    Runtime.runtime.addShutdownHook({ ->
        camelCtx.stop()
    })
    synchronized(this){ this.wait() }
}

println 'about to run camel....!'
runCamel()

