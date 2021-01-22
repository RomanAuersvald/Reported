#### Stage 1: Build the application
FROM openjdk:11-jdk as build

# Set the current working directory inside the image
WORKDIR /idea

# Copy maven executable to the image
COPY mvnw .
COPY .mvn .mvn

# Copy the pom.xml file
COPY ./pom.xml .

# Build all the dependencies in preparation to go offline.
# This is a separate step so the dependencies will be cached unless
# the pom.xml file has changed.
RUN ./mvnw dependency:go-offline -B

# Copy the project source
COPY src src

# Package the application
RUN ./mvnw package -DskipTests
RUN mkdir -p target/dependency && (cd target/dependency; jar -xf ../*.jar)

#### Stage 2: A minimal docker image with command to run the app
FROM openjdk:11-jdk

ARG DEPENDENCY=/idea/target/dependency

# Copy project dependencies from the build stage
COPY --from=build ${DEPENDENCY}/BOOT-INF/lib /idea/lib
COPY --from=build ${DEPENDENCY}/META-INF /idea/META-INF
COPY --from=build ${DEPENDENCY}/BOOT-INF/classes /idea

ENTRYPOINT ["java","-cp","idea:idea/lib/*","com.example.securingweb.ReportedApp"]