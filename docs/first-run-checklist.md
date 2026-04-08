# First Run Checklist — Shift Transport System

## Step 1 — Create the PostgreSQL Database

Open **DataGrip** (or psql in terminal) and run:

```sql
CREATE DATABASE shift_transport;
```

---

## Step 2 — Run the Schema

In DataGrip, connect to `shift_transport` and run the contents of:

```
src/main/resources/schema.sql
```

This creates all tables and enum types.

---

## Step 3 — Run the Seed Data

Still in DataGrip, run:

```
src/main/resources/data.sql
```

This inserts the 3 shift types and 5 test users.

---

## Step 4 — Open the Project in IntelliJ

1. Open IntelliJ IDEA
2. **File → Open** → select the `Transportation system project` folder
3. IntelliJ will detect it as a Maven project and import it automatically
4. Wait for Maven to download all dependencies (first time may take a minute or two)

---

## Step 5 — Verify application.yml

Open `src/main/resources/application.yml` and confirm these values match your local PostgreSQL setup:

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/shift_transport
    username: postgres
    password: postgres
```

Change `username` and `password` if your local PostgreSQL uses different credentials.

Also set:

```yaml
spring:
  jpa:
    hibernate:
      ddl-auto: validate   # validates schema but does not modify it
  sql:
    init:
      mode: never          # we already ran schema.sql manually
```

---

## Step 6 — Run the Application

In IntelliJ, open:

```
src/main/java/com/shimon/transport/ShiftTransportApplication.java
```

Click the green **Run** button next to `main()`.

You should see in the console:
```
Tomcat started on port(s): 8080
Started ShiftTransportApplication
```

---

## Step 7 — Open the App in Browser

Navigate to:

```
http://localhost:8080/auth/login
```

---

## Step 8 — Test Login with Seed Users

Use the phone numbers from `data.sql`:

| Phone       | Role                    | Goes to              |
|-------------|-------------------------|----------------------|
| 0501234567  | Employee                | /employee/dashboard  |
| 0527654321  | Employee + Driver       | /employee/dashboard  |
| 0539876543  | Volunteer Driver only   | /driver/dashboard    |
| 0541112233  | Shift Manager           | /manager/dashboard   |
| 0500000000  | System Admin            | /manager/dashboard   |

---

## Step 9 — Basic Smoke Test

**As Manager (0541112233):**
1. Log in → redirected to Manager Dashboard
2. Click **+ New Event** → create an event for a future date
3. Open the event → assign a driver

**As Employee (0501234567):**
1. Log in → see the event you just created
2. Click **Request** → choose PICKUP or BOTH → submit
3. See the request appear in "My Transport Requests"
4. Try cancelling it

**As Driver (0539876543):**
1. Log in → see the event assigned to you
2. Click **View List** → see the passenger with address and phone

---

## Common Issues

**`ddl-auto: validate` fails on startup**
→ The schema was not created correctly. Re-run `schema.sql` from scratch.

**`Password authentication failed for user "postgres"`**
→ Update `application.yml` with your actual PostgreSQL credentials.

**`relation "users" does not exist`**
→ You are connected to the wrong database. Make sure the URL says `shift_transport`.

**`UnsatisfiedDependencyException` on startup**
→ A bean failed to wire. Check the full stack trace — usually a missing `@Repository` or mismatched field name.

**Thymeleaf template not found**
→ Template file is in the wrong folder or has a typo in the return value of a controller method.
