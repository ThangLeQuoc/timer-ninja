![TimerNinjaBanner](https://i.imgur.com/dOdfZmV.png)
# Timer Ninja

Timer Ninja is a lightweight Java library for measuring method execution time with ease. It provides annotation-based tracking to measure execution time, supports different time units, and visualizes the tracking context in stacktrace tree format.  
Built on Aspect-Oriented Programming (AOP) with AspectJ, this library seamlessly integrates into your application for precise performance troubleshooting.

## Problem Space
Measuring code execution time is a fundamental practice in software development. Whether optimizing performance, debugging slow processes, or ensuring system efficiency, developers frequently need insights into how long the methods take to execute.

### Traditional approach
A common way to measure method execution time is by capturing timestamps before and after the method runs and calculating the difference.  
This approach is straightforward and fast but quickly becomes cumbersome. You must manually declare two timestamp points around every method that needs evaluation, leading to excessive boilerplate code and reduced maintainability. As the codebase grows, keeping track of these measurements becomes inefficient and error-prone.

```java
long beforeExecution = System.currentTimeMillis();
doSomethingInteresting();
long afterExecution = System.currentTimeMillis();
System.out.println("Execution time (ms): " + (afterExecution - beforeExecution));
```

### Timer Ninja approach
Timer Ninja library take the AOP approach with AspectJ under the hood. All you need to do, is to simply
annotate the method you would want to measure the execution time with `@TimerNinjaTracker` annotation.

```java
@TimerNinjaTracker
public String doSomethingInteresting() {
     
}
```

Timer Ninja also keeps track of method execution context. If your annotated method is called from the parent method, which also has the 
`@TimerNinjaTracker` declared, then the execution order and relationship of the methods will be preserved in a tracking output.

**Example Timer Ninja trace output**  
```shell
Timer Ninja trace context id: 851ac23b-2669-4883-8c97-032b8fd2d45c
Trace timestamp: 2023-04-03T07:16:48.491Z
{===== Start of trace context id: 851ac23b-2669-4883-8c97-032b8fd2d45c =====}
public void requestMoneyTransfer(int sourceUserId, int targetUserId, int amount) - Args: [sourceUserId={1}, targetUserId={2}, amount={500}] - 1747 ms
   |-- public User findUser(int userId) - 105000 µs
   |-- public void processPayment(User user, int amount) - Args: [user={name='John Doe', email=johndoe@gmail.com}, amount={500}] - 770 ms
     |-- public boolean changeAmount(User user, int amount) - 306 ms
     |-- public void notify(User user) - 258 ms
       |-- private void notifyViaSMS(User user) - 53 ms
       |-- private void notifyViaEmail(User user) - 205 ms
{====== End of trace context id: 851ac23b-2669-4883-8c97-032b8fd2d45c ======}
```

## Installation
For this to work, you will need to do two thing: declare a dependency of `timer-ninja`, and a plugin to compile
the aspect defined in `timer-ninja` dependency

### Declare dependency on timer-ninja
**Gradle**  
```groovy
implementation group: 'io.github.thanglequoc', name: 'timer-ninja', version: '1.0.3'
```

**Maven**  
```xml
<dependency>
    <groupId>io.github.thanglequoc</groupId>
    <artifactId>timer-ninja</artifactId>
    <version>1.0.3</version>
    <scope>compile</scope>
</dependency>
```

### Declare plugin to compile the aspect
**Gradle**  
You can use the [FreeFair AspectJ Gradle plugin](https://github.com/freefair/gradle-plugins)

Example project's `build.gradle`:

```groovy
plugins {
    // ...
    id "io.freefair.aspectj.post-compile-weaving" version '6.6.3'
}

dependencies {
    // ...
    // Timer ninja dependency
    implementation group: 'io.github.thanglequoc', name: 'timer-ninja', version: '1.0.3'
    aspect 'io.github.thanglequoc:timer-ninja:1.0.3'
}
```

### Maven project
You can use the [Mojo's AspectJ Plugin](https://www.mojohaus.org/aspectj-maven-plugin/index.html)  
Example project's `pom.xml`

```xml
<dependencies>
    <dependency>
        <groupId>org.aspectj</groupId>
        <artifactId>aspectjrt</artifactId>
        <version>1.9.7</version>
    </dependency>
    <dependency>
        <groupId>io.github.thanglequoc</groupId>
        <artifactId>timer-ninja</artifactId>
        <version>1.0.3</version>
        <scope>compile</scope>
    </dependency>
</dependencies>

<build>
    <plugins>
        <!-- Codehaus Maven plugin -->
        <plugin>
            <groupId>org.codehaus.mojo</groupId>
            <artifactId>aspectj-maven-plugin</artifactId>
            <version>1.14.0</version>
            <configuration>
                <complianceLevel>11</complianceLevel>
                <source>11</source>
                <target>11</target>
                <!-- Specify timer-ninja as the aspect library -->
                <aspectLibraries>
                    <aspectLibrary>
                        <groupId>io.github.thanglequoc</groupId>
                        <artifactId>timer-ninja</artifactId>
                    </aspectLibrary>
                </aspectLibraries>
            </configuration>
            <executions>
                <execution>
                    <goals>
                        <goal>compile</goal>
                    </goals>
                </execution>
            </executions>
        </plugin>
    </plugins>
</build>
```

### Getting the time trace output
The library is logging the time trace with [SLF4J Logger](https://www.slf4j.org/). So if you've already had an Slf4j provider (e.g: Logback, Log4J) in your project, then
you should be able to see the time trace output after the method executed.  
Otherwise, you will need to add a log provider into the project, my personal recommendation is [Logback](https://logback.qos.ch/) for 
robustness and simplicity. You can refer to this [Logback tutorial from Baeldung](https://www.baeldung.com/logback)

See this [Slf4j manual](https://slf4j.org/manual.html) for how to configure your logging framework with Slf4j

**Note**: Spring Boot project uses Logback as it default log provider, so you don't need to do anything here.

The logger class is `io.github.thanglequoc.timerninja.TimerNinjaUtil`, with the default log level is `INFO`.

If logging framework is not your preference, and you just want to have a quick result. Then you can choose to fall back
to the good old `System.out.println` output by executing this code **once** (since this is a singleton configuration instance). This setting will instruct
Timer Ninja to also print the time trace output to `System.out`

```java
TimerNinjaConfiguration.getInstance().toggleSystemOutLog(true);
```

## Usage
Now that you're all set and ready to go. Just simply place the tracker by annotating `@TimerNinjaTracker` on any method/constructor
that you want to measure

```java
@TimerNinjaTracker
public void processPayment(User user, int amount) {
    // Method logic
}
```

### Tracker Options

The following options is available on the `@TimerNinjaTracker` annotation

```java
@TimerNinjaTracker(enabled = true, timeUnit = ChronoUnit.MILLIS, includeArgs = true)
public void processPayment(User user, int amount) {
    (...)
}
```
#### Toggle tracking
Determine if this tracker should be active. Set to `false` will disable this tracker from the overall tracking trace result. Default: `true`
> @TimerNinjaTracker(enabled = false)

#### Timing Unit
The tracker allows specifying the time unit for measurement. Supported units include:  
•   Seconds (`ChronoUnit.SECONDS`)  
•   Milliseconds (`ChronoUnit.MILLIS`)  
•   Microseconds (`ChronoUnit.MICROS`)  
By default, the time unit of the tracker is **millisecond (ms)**.
```java
import java.time.temporal.ChronoUnit;

@TimerNinjaTracker(timeUnit = ChronoUnit.MICROS)
public void processPayment(User user, int amount) {

}
```

#### Include argument information in the log trace context
The tracker can optionally log the arguments passed to the tracked method. This is particularly useful for gaining insights into the input data when analyzing performance. Default: `false`

**Note**: Ensure that the `toString()` method of the argument objects is properly implemented to display meaningful details in the logs.

```java
@TimerNinjaTracker(includeArgs = true)
public void processPayment(User user, int amount) {
    // Method logic
}
```

**Sample output:**
> public void processPayment(User user, int amount) - Args: [user={name='John Doe', email=johndoe@gmail.com}, amount={500}] - 770 ms

### Reading the time trace output
Once the method is executed, you should be able to find the result similar to this one in the output/log

```log
2023-04-06T21:27:50.878+07:00  INFO 14796 --- [nio-8080-exec-1] c.g.t.timerninja.TimerNinjaUtil          : Timer Ninja trace context id: c9ffeb39-3457-48d4-9b73-9ffe7d612165
2023-04-06T21:27:50.878+07:00  INFO 14796 --- [nio-8080-exec-1] c.g.t.timerninja.TimerNinjaUtil          : Trace timestamp: 2023-04-06T14:27:50.322Z
2023-04-06T21:27:50.878+07:00  INFO 14796 --- [nio-8080-exec-1] c.g.t.timerninja.TimerNinjaUtil          : {===== Start of trace context id: c9ffeb39-3457-48d4-9b73-9ffe7d612165 =====}
2023-04-06T21:27:50.878+07:00  INFO 14796 --- [nio-8080-exec-1] c.g.t.timerninja.TimerNinjaUtil          : public User getUserById(int userId) - 554 ms
2023-04-06T21:27:50.878+07:00  INFO 14796 --- [nio-8080-exec-1] c.g.t.timerninja.TimerNinjaUtil          :   |-- public User findUserById(int userId) - 251 ms
2023-04-06T21:27:50.878+07:00  INFO 14796 --- [nio-8080-exec-1] c.g.t.timerninja.TimerNinjaUtil          : {====== End of trace context id: c9ffeb39-3457-48d4-9b73-9ffe7d612165 ======}
```
In detail:  
`Timer Ninja trace context id`: The auto generated uuid of a trace context. A trace context is initiated for the very first method encountered with `@TimerNinjaTracker` annotation.
Any sequence execution of other annotated tracker methods inside the parent method will also be accounted for in the existing trace context.  
`Trace timestamp`: The timestamp when the trace context is initiated, in UTC timezone.  
`Begin-end of trace context`: The detailed execution time of each method. The `|--` sign indicate the call to this method originated from the above parent method, which help to visualize the execution stacktrace.

## Troubleshooting
If you need to troubleshoot, you can toggle the `DEBUG` log level on logger `io.github.thanglequoc.timerninja.TimerNinjaThreadContext`.

## Issue and contribution
Any contribution is warmly welcome. Please feel free to open an Issue if you have any problem setting up timer-ninja. Or open a Pull Request
if you have any improvement to this project.

## Example projects
Below are some example projects which has Timer Ninja integrated for your setup reference 

[Spring Boot ToDo List - Gradle build](https://github.com/ThangLeQuoc/timer-ninja-examples/tree/main/timerninja-todolist-gradle)  
[Spring Boot ToDo List - Maven build](https://github.com/ThangLeQuoc/timer-ninja-examples/tree/main/timerninja-todolist-maven)


----
###### 🦥 Project logo is randomly (and nicely) generated by [Dall-E](https://openai.com/product/dall-e-2)
