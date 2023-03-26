# Timer Ninja
It's just a very blank read me at the beginning...

Author: Thang Le Quoc





Future plan look like
```shell
Timer Ninja time track trace for uuid: 123-abc-def
Trace timestamp: 2023-01-01 01:00:00:000.00Z
-------------------------------------------------------
Food orderFood() - 12000ms
  |-- int subExecutionTime() - 1560ms
    |-- RecordType insertRecord(int x, int y) - 1100ms
    |-- Bank findBank(User u) - 500ms
-------------------------------------------------------
```


Reference document:
https://www.eclipse.org/aspectj/doc/released/runtime-api/org/aspectj/lang/Signature.html
https://docs.freefair.io/gradle-plugins/current/reference/

AspectJ build.gradle
https://github.com/freefair/gradle-plugins/issues/53#issuecomment-519550317
Different type of AspectJ weaving: https://dzone.com/articles/different-types-of-aspectj-weaving


LOGO
```
A logo for an application library called TimerNinja. There is a clock look human like (with arms, legs, eyes), his eyes are cover with Ninja cloth
```


Artifact Publishing
https://www.albertgao.xyz/2018/01/18/how-to-publish-artifact-to-maven-central-via-gradle/

**Tasks**
- **DONE** -  Include the method signature in the output  
- **DONE** Support different timeunit (s, ms<-default, ns)  
- **DONE** Generate unique time track trace id and trace timestamp  
- **DONE** (chore) Dynamically print the ---- separator base on the length?  
- **DONE** Integrate with SLF4J logging api, get rid of System.out (or maybe have a single tracker configuration instance that allow end user to toggle System out logging)
- Add more test
- Plan to migrate the code to `aspectj` folder to follow the convention?  
- Performance tuning to it max?  
- Benchmark testing plan?  
- Code refactoring  
**Test plan**
- Add test on single tracker  
- Nested tracker
- Tracker enabling
- Tracker wiht multithreaded execution (multiple threads, completablefuture API, etc...)  
**Publication plan**
- Publish to maven central repository
- Create another project to test the integration (Spring Boot)
- Test integration with maven project


Problem with Spring Boot Worker Thread

(document this more carefully)
No, when a thread is returned to the ThreadPool in Tomcat NIO, the ThreadLocal of that thread is not preserved. The ThreadLocal values are associated with the thread that created them, and when that thread is returned to the thread pool, its associated ThreadLocal values are not transferred to other threads.
Therefore, if you are using ThreadLocal in a multi-threaded environment, you need to be aware that the values stored in the ThreadLocal may not be available in subsequent requests. One approach to dealing with this is to set up a request filter or interceptor to initialize and clean up the ThreadLocal values for each request.

