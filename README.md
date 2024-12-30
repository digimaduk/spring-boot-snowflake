# spring-boot-snowflake
Integrating Snowflake with Spring Boot
Connection Pooling by HikariCP - Connection pooling is a way to store and reuse a connection to be reused again later, avoiding the expensive cost of creating a connection for each use.
# minimumIdle = minimum number of inactive connections that the connection pool can maintain
# maximumPoolSize = maximum size that the pool is allowed to reach, including both idle and in-use connections
# connectionTimeout = defines the maximum time application is willing to wait for a connection from the pool = 30000 or 30 seconds
# idleTimeout = how long a connection can be idle in the pool before it's retired = 600,000 milliseconds or 10 minutes
# maxLifetime = maximum possible lifetime of a connection in the pool = 1,800,000 milliseconds or 30 minutes
