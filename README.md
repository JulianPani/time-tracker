# time-tracker
A test time-tracker web app to learn Kotlin and show off coding skills

**Note**: This is a toy project. The data is only persisted in memory during a single app execution.

## Building the app
The app uses gradle. In the root directory, run 
`./gradlew build`
to build the jar

## Running the app
The app uses Docker. Make sure have docker installed.
After building the JAR as explained above, run
```bash
docker run -m512M --cpus 2 -it -p 3000:3000 --rm time-tracker
```
to start the container.

Now you should be able to access the API on the host running the docker with port 3000.

## The API
You can report arrival or exit of a user, by doing:
```bash
curl --request POST 'http://0.0.0.0:3000/time-reporting/arrival' \
--header 'Content-Type: multipart/form-data' \
--form 'user_id=1' \
--form 'time=2018-04-10T04:33:33'
```
or 
```bash
curl --request POST 'http://0.0.0.0:3000/time-reporting/exit' \
--header 'Content-Type: multipart/form-data' \
--form 'user_id=1' \
--form 'time=2018-04-10T04:33:55'
```

You can retrieve the entries for a specific user_id with:
```
curl --request GET 'http://0.0.0.0:3000/time-reporting/get?user_id=1'
```
