# Quickstart: Simplify Backend Logs

This feature simplifies the backend logging configuration. To see the feature in action, you can adjust the logging levels in the `logback-spring.xml` file.

## Configuration

The primary configuration for this feature is located in `backend/src/main/resources/logback-spring.xml`.

### Production (Default)

By default, the `production` profile is configured to only log `ERROR` messages.

```xml
<springProfile name="production">
    <root level="ERROR">
        <appender-ref ref="CONSOLE" />
    </root>
</springProfile>
```

### Development

To enable more detailed logging for development, you can activate the `development` Spring profile. This profile sets the root logging level to `INFO` and the level for the `com.contract.master` package to `DEBUG`.

```xml
<springProfile name="development">
    <root level="INFO">
        <appender-ref ref="CONSOLE" />
    </root>
    <logger name="com.contract.master" level="DEBUG" />
</springProfile>
```

To activate the `development` profile, you can set the following environment variable:

```bash
export SPRING_PROFILES_ACTIVE=development
```
