Fullstack developer tests
These tests are made to assess your skills using the stack of our AI customer service app which:

Integrates with Gmail API, Shopify API, Airtable API, Webshipper API, OpenAI API
Generates automatic answers and actions to customer service emails, with human in the loop
Tech stack:

Spring Boot & Kotlin
Postgres
Flyway for database migrations
HTMX & Thymeleaf server side rendering
Shoelace for UI components (web components)
Gradle, Kotlin DSL (build.gradle.kts)
Other details:

JdbcClient (new in Spring) for database access
@Scheduled for scheduled jobs
Tests introduction

Tooling requirements
Use Intellij ultimate. You can use it for free through: EAP program
Use the built in DB client in Intellij to connect to the Postgres database (no external tools)
Use the built in git client in Intellij to commit code
Use the built in HTTP client for testing HTTP requests. It is better than Postman etc. It makes it easy to share version controlled requests in the repo
Use the latest version of all libraries and tooling. AI might generate projects with older version, so make sure you fix this manually. This means the latest version version of Gradle, JVM etc. Many developers don’t know that you can run your Java/Kotlin code targeting an earlier JVM, but still run that bytecode on a newer (and more efficient) JVM. Show your project settings and show that you run on the latest JVM.
Make sure you use all these tools during the looms so I see you master them / know about them

Test - simple frontend + Backend integration
Make a loom showing and explaining all the code and the working web app in action. Use the same tech stack described above.

Make a spring boot app that has a page that:

Displays a button to load products
When the button is clicked, it fetches products from a Postgres database and displays them in a table.
Lets you add a new product via a form, which is then saved to the database and updates the table without a page reload.
The DB has been populated with a scheduled job that fetches products details from https://famme.no/products.json. Limit number of products you save to 50.

Note that one product can have multiple variants, so the json response from that endpoint does not necessarily translate cleanly to one database table. Solution: JSONB or multiple tables with foreign keys.

You also don’t need to save all the fields from the json response, only the ones you think is most relevant. Choose a few 3-5 fields at most to make your life easy.

Tips:

Use flyway for database migrations (initial table setup).
Use initialDelay=0, to make the scheduled job run immediately at startup.
Important:

Make the table nice
Use Shoelace for components like buttons
Use HTMX to update the table without a full page reload
So the website would be something like:

header
button to load products into UI
table with products
Form to add new products (can maybe show up after table is loaded). Again, using HTMX to update the table without a full page reload.