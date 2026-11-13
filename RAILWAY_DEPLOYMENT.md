# Railway deployment

The repository includes a production `Dockerfile` and `railway.json`. Railway builds the Spring Boot JAR with Java 21, starts it using the platform-provided `PORT`, and verifies `/v3/api-docs` as its health check.

## Services

Create these two services in the same Railway project and environment:

1. A MySQL database named `MySQL`.
2. A GitHub service connected to this backend repository.

If the database service has a different name, replace `MySQL` in every reference variable below with its exact Railway service name.

## Backend variables

Add these values in the backend service **Variables** tab:

```text
DB_URL=jdbc:mysql://${{MySQL.MYSQLHOST}}:${{MySQL.MYSQLPORT}}/${{MySQL.MYSQLDATABASE}}?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC
DB_USERNAME=${{MySQL.MYSQLUSER}}
DB_PASSWORD=${{MySQL.MYSQLPASSWORD}}
DB_DRIVER=com.mysql.cj.jdbc.Driver
JPA_DDL_AUTO=update
APP_CORS_ALLOWED_ORIGINS=https://blip-fleetproof.netlify.app
```

Do not define `PORT` manually. Railway supplies it automatically and Spring Boot reads it through `server.port=${PORT:8080}`.

## Public domain and verification

After the deployment becomes healthy:

1. Open the backend service **Settings** tab.
2. Under **Networking > Public Networking**, select **Generate Domain**.
3. Verify `https://<railway-domain>/v3/api-docs`.
4. Verify `https://<railway-domain>/swagger-ui/index.html`.
5. Use `https://<railway-domain>/api/v1` as the Angular production API base URL.

If the final frontend domain changes, update `APP_CORS_ALLOWED_ORIGINS` with the exact HTTPS origin and redeploy the backend.
