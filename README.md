# Task Manager

## Introduction
This is a test project to create the Task Manager.


## Authors

- [@OstapMelnychuk](https://github.com/OstapMelnychuk)


## Overall Changes

What was done:
- Added CRUD operations for the task (OpenAPI used)
- Added reserve Postgres database
- Added tests. The project has a Jacoco plugin so you can run ```mvn clean install -P covarage``` to get coverage (should be above 75% if not considering telegram bot-related classes)
- Added data validation
- Added tests that demonstrate all types of calls
- Added business logic rules for task creation. Those are for creating max quantity of tasks and creating tasks with the same name and status
- Added telegram bot for tracking task creation/updating
- Added JavaDoc and logging 
## How to run
Note: you should have docker installed. There is a test container for Postgres that requires docker.
Just pull the project (feature/taskManager branch) and run ```mvn clean install``` to generate OpenAPI classes.
To test the telegram bot run the application and try to create/update some tasks.

To test endpoints you can use swagger via this [link](http://localhost:8080/swagger-ui.html#/)

Telegram bot [link](https://t.me/task_manager_test_pb_bot)
## P.S.
I have created a PR with all the main changes. I will be grateful for a review.

If you have any suggestions you can contact me via [LinkedIn](https://www.linkedin.com/in/ostap-melnychuk-b368201a2/)
