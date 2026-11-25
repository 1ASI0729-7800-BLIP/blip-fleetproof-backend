# FleetProof Backend

Spring Boot REST API for the FleetProof platform. The application uses Java 21, Gradle, Spring Data JPA, MySQL/H2 and Springdoc OpenAPI.

## Local development

Local execution uses the in-memory H2 database by default:

```powershell
.\gradlew.bat bootRun
```

Swagger UI:

```text
http://localhost:8080/swagger-ui/index.html
```

OpenAPI document:

```text
http://localhost:8080/v3/api-docs
```

## Tests

```powershell
.\gradlew.bat test
```

## Railway production configuration

The Railway service must define:

```text
SPRING_PROFILES_ACTIVE=prod
DB_URL=jdbc:mysql://<host>:<port>/<database>?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC
DB_USERNAME=<mysql-user>
DB_PASSWORD=<mysql-password>
DB_DRIVER=com.mysql.cj.jdbc.Driver
JPA_DDL_AUTO=update
APP_CORS_ALLOWED_ORIGINS=https://blip-fleetproof.netlify.app
```

When Railway provides MySQL variables, use references instead of copying secrets where possible. `application-prod.properties` intentionally has no H2 fallback, so a production deployment fails clearly when MySQL is not configured instead of silently losing data in memory.

The service domain must target port `8080`. Railway can still override it through `PORT`.

## Production verification

After deployment, verify:

```text
https://<railway-domain>/v3/api-docs
https://<railway-domain>/swagger-ui/index.html
https://<railway-domain>/api/v1/users
```

User responses never expose password hashes. Authentication uses:

```http
POST /api/v1/users/sign-in
```

Report source assessment uses:

```http
GET /api/v1/reports/source-checks?plate=ABC-123&version=1
```

Official SUNARP, SAT, CITV and SOAT credentials are not configured in this repository. The endpoint therefore reports those providers as unavailable and does not fabricate findings.
