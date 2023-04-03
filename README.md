# Timer Ninja
Timer Ninja is a brainless Java library to evaluate your Java code method execution time.
Timer Ninja follows the Aspect Oriented Programming (AOP) concept, backed by AspectJ, to make it super simple
to declare tracker on method.

## Problem Space
Measuring the method execution time is a super common thing in the programmer world.

### Traditional approach
The traditional approach is to get the different between two timestamp points before and after the method is executed.
This approach is obviously fast and easy, but will soon become messy because you will need to manually declare 2 timestamp points
around every method that need to be evaluated. This introduces a lot of boilerplate code in the code base.

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
public void requestMoneyTransfer(int sourceUserId, int targetUserId, int amount) - 1747 ms
   |-- public User findUser(int userId) - 105000 µs
   |-- public void processPayment(User user, int amount) - 770 ms
     |-- public boolean changeAmount(User user, int amount) - 306 ms
     |-- public void notify(User user) - 258 ms
       |-- private void notifyViaSMS(User user) - 53 ms
       |-- private void notifyViaEmail(User user) - 205 ms
{====== End of trace context id: 851ac23b-2669-4883-8c97-032b8fd2d45c ======}
```


## Installation
(WIP)



I'm working very hard to ship this library to the Maven central repository



## Usage

