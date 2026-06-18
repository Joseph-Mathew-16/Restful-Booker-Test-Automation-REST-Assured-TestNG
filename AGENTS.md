# AGENTS.md - Restful Booker Test Automation Guide

## Project Overview
REST Assured + TestNG automation framework for testing the Restful Booker API (https://restful-booker.herokuapp.com/). Reports generated via Allure with parallel test execution support.

**Tech Stack**: Java 25, Maven, REST Assured 6.0, TestNG 7.12, Allure 2.29, AssertJ, Apache Commons CSV

---

## Architecture & Design Patterns

### 1. **Fluent Builder Pattern for Endpoint Classes**
Endpoints extend `RestfulBookerBase` and implement method chaining for readable test flows:
```java
// src/test/java/endpoints/BookingEndpoint.java
new BookingEndpoint()
    .setBookingId(bookingId)
    .setContentTypeForBookingEndpoint(ContentType.JSON)
    .setIfMatchHeader(etag)
    .setAuthorizationCookie(token)
    .put()  // Returns Response
```
**Pattern**: Each builder method returns `this`, methods delegate to parent `TestBase` using `setBasePath()`, `setHeader()`, `setRequestBody()`, etc. HTTP verbs (`get()`, `post()`, `put()`, `patch()`) execute the request and reset `requestSpecBuilder` ThreadLocal.

### 2. **ThreadLocal for Parallel Test Execution**
`TestBase` uses ThreadLocal to ensure thread-safe REST config across parallel tests:
```java
// Lines 17-36 in TestBase.java
public static ThreadLocal<RequestSpecBuilder> requestSpecBuilderThreadLocal = new ThreadLocal<>();
public ThreadLocal<Configuration> configurationThreadLocal = new ThreadLocal<>();
```
Constructor lazily initializes per-thread instances. **Critical**: HTTP verbs call `requestSpecBuilder = null; requestSpecBuilderThreadLocal.remove()` after execution to prevent state leakage.

### 3. **Dual Assertion Strategy**
`AssertionType` enum (SOFT/HARD) determines assertion behavior:
- **HARD**: Fails immediately via `Assert.assertEquals()` (testbase/TestBase.java:129)
- **SOFT**: Collects errors via `SoftAssert`, checked later with `assertSoftAssertions()` 

Use soft assertions for multi-step workflows; call `assertSoftAssertions()` at test end to verify all soft failures.

### 4. **Allure Integration with @Step Annotations**
All TestBase methods annotated with `@Step` auto-report to Allure. Custom steps via `step(String)` method (logs to both Allure and TestNG Reporter).
```java
// AllureRestAssured filter added at TestBase constructor (line 32)
new AllureRestAssured()  // Captures request/response details automatically
```

---

## Critical Workflows

### Run Tests
```bash
# Full regression suite (parallel, defined in src/test/resources/regression-test-suite.xml)
mvn clean test

# Generate Allure report
mvn allure:report  # Opens allure-report/index.html
```

### Test Execution Flow
1. **TestNG Suite XML** (`regression-test-suite.xml`) defines parallel execution and listeners
2. **AnnotationTransformer listener** applies retry logic (IRetryAnalyzer) based on config
3. **Tests inherit from RestfulBookerBase** → RestfulBookerBase extends TestBase
4. **Configuration auto-loads** from `config.properties` via ThreadLocal in TestBase constructor

### Test Data
CSV-based parameterized tests via `@DataProvider`:
- Located: `src/test/resources/testdata/` 
- Read by: `CSVFileReader` (utilities package)
- Usage: `@DataProvider(name="csvDataProvider")` + `CSVFileReader.getTestData(csvFilePath)`

### Parallel Execution
Configured in `regression-test-suite.xml` with `parallel="true"`. ThreadLocal prevents state conflicts. Clean up resources post-test by resetting ThreadLocal in HTTP verbs.

---

## Key Conventions

### 1. **Test Class Structure**
- Extend `RestfulBookerBase` (not `TestBase` directly)
- Create endpoint instance in test method (new BookingEndpoint())
- Use builder chain → HTTP verb → assertion chain
- Assertion types explicit: `assertStringsEquals(AssertionType.HARD, actual, expected)`

### 2. **Endpoint Naming & Organization**
- Package: `endpoints`
- Class per API resource: `BookingEndpoint`, `AuthEndpoint`
- Static `endpoint` field: `public static String endpoint = "/booking"`
- Specialized builder methods for domain logic (e.g., `setBookingDetailsInBody()` constructs JSON)

### 3. **Configuration Management**
- `Configuration` class reads `config.properties` (location via system property or classpath)
- Accessible via `configuration` field in TestBase
- ThreadLocal prevents cross-test pollution in parallel execution

### 4. **Retry Logic**
- Managed by `AnnotationTransformer` listener (testng XML)
- Configured via config property (e.g., max retry count)
- Applied at test method level via `IRetryAnalyzer`

### 5. **Soft Assert Cleanup**
Always call `assertSoftAssertions()` at test end if soft assertions used, else soft failures silently pass. Example:
```java
public void testBookingUpdate() {
    assertStringsEquals(AssertionType.SOFT, actual1, expected1);
    assertBooleansEquals(AssertionType.SOFT, actual2, expected2);
    assertSoftAssertions();  // Fail test if any soft assertion failed
}
```

---

## File Organization

```
src/test/java/
├── testbase/
│   ├── TestBase.java          # REST Assured wrappers, assertions, @Step methods
│   └── RestfulBookerBase.java # Extends TestBase, CSV integration
├── endpoints/
│   ├── BookingEndpoint.java   # Builder pattern for /booking operations
│   └── AuthEndpoint.java      # Builder pattern for auth
├── testcases/
│   ├── CreateBookingTest.java # @DataProvider + parameterized tests
│   └── UpdateBookingTest.java # Multi-step workflows with Allure steps
├── dataproviders/
│   └── CSVFileReader.java     # Parses CSV to Object arrays
└── utilities/
    ├── Configuration.java      # Properties loader, ThreadLocal config
    └── AnnotationTransformer.java  # TestNG listener for retry logic

src/test/resources/
├── regression-test-suite.xml   # Parallel execution, listener registration
├── config.properties           # Base URL, timeouts, retry count
└── testdata/                   # CSV test data files
```

---

## Common Tasks & Patterns

### Adding a New Test
1. Create test class extending `RestfulBookerBase` in `testcases` package
2. Use `@Test` annotation; `@DataProvider` if parameterized
3. Instantiate endpoint: `new BookingEndpoint().setBaseURI(...)`
4. Chain builder methods → HTTP verb → `assertXXX()`
5. Register class in `regression-test-suite.xml`

### Adding a New Endpoint
1. Create class in `endpoints` package extending `RestfulBookerBase`
2. Define `static String endpoint = "/path"`
3. Constructor calls `setBaseURI()` and `setBasePath()`
4. Builder methods return `this`, call parent methods (`setHeader()`, etc.)
5. Example: `BookingEndpoint.setIfMatchHeader()` shows custom header pattern

### Debugging Test Failures
1. Check Allure report: step-by-step request/response via `AllureRestAssured` filter
2. ThreadLocal issues: ensure HTTP verbs reset state (check lines 84-95 in TestBase)
3. Retry logic: configured via `AnnotationTransformer` + config property
4. Soft assertions: verify `assertSoftAssertions()` called at test end

### Adding Test Data
1. Create CSV in `src/test/resources/testdata/`
2. Use `CSVFileReader.getTestData(filePath)` in `@DataProvider`
3. Method returns `Object[][]` compatible with TestNG data provider

---

## Design Rationale

**Why ThreadLocal?** Parallel test execution requires thread-safe RequestSpecBuilder state. ThreadLocal isolates per-thread configs, preventing cross-test interference.

**Why Endpoint Builders?** Fluent chaining improves test readability. Centralizes API request construction logic, reducing duplication across tests.

**Why Separate Assertion Types?** Soft assertions allow multi-point validation in complex workflows (e.g., verifying multiple response fields before failing). Hard assertions fail fast for critical checks.

**Why Allure @Step?** Auto-reporting captures request/response without manual logging. Enhances report clarity for stakeholders.

---

## Important Dependencies & Versions
- **REST Assured 6.0.0** – HTTP client wrapper
- **TestNG 7.12.0** – Test framework, parallel execution
- **Allure 2.29** – Reporting, @Step annotations
- **AssertJ 4.0.0-M1** – Fluent assertions (available if needed)
- **AspectJ 1.9.25.1** – AOP for Allure integration with agent weaving

Maven Surefire runs tests with AspectJ agent to enable Allure annotation interception.

---

## Quick Start for AI Agents

1. **Understand test flow**: Test → Endpoint Builder → HTTP Verb → Assertion
2. **ThreadLocal pattern**: All state lives in ThreadLocal; HTTP verbs reset it
3. **Builder returns `this`**: Method chaining works via delegation to TestBase methods
4. **Assertions are explicit**: Use `AssertionType.SOFT` or `AssertionType.HARD`
5. **Allure auto-captures**: Request/response logged via `AllureRestAssured` filter; add `@Step` for custom steps
6. **Run with**: `mvn clean test` + `mvn allure:report`

