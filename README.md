# Simple Metrics Poster

A simple application which will push metrics to a hawkular endpoint - just for running locally to push stuff on a timer..

Remember to change the token with oc whoami -t

```
    def token = 'Hm66kqh2JS1IgNR_AFLJMUHMWJp6SJDk9EAkrOJ58cY'
```

Also, change the camel route constants to suit your setup: in post-metrics.groovy

```
    from('timer://helloTimer?period=5000')
        .setProperty(Constants.AUTHORIZATION, constant('Bearer ' + token))
        .setProperty(Constants.HAWKULAR_TENANT, constant("origin-metrics"))
        .setProperty(Constants.HAWKULAR_METRICS_HOST, constant("https://hawkular-metrics-origin-metrics.rhel-cdk.10.1.2.2.xip.io"))
        .setProperty(Constants.HAWKULAR_METRICS_TYPE, constant("gauges"))
```                