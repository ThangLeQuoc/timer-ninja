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



ChatGPT suggestion, but look like only feasible for single thread
```aspectj
public aspect TrackedAspect {
    private static ThreadLocal<Stack<String>> invocationStack = new ThreadLocal<>();

    pointcut trackedMethods() : execution(@Tracked * *(..));

    Object around() : trackedMethods() {
        String methodName = thisJoinPointStaticPart.getSignature().getName();
        Stack<String> stack = invocationStack.get();

        if (stack == null) {
            stack = new Stack<>();
            invocationStack.set(stack);
        }

        try {
            stack.push(methodName);

            Object result = proceed();

            return result;
        } finally {
            stack.pop();
        }
    }

    public static String getInvocationStacktrace() {
        Stack<String> stack = invocationStack.get();

        if (stack == null) {
            return "";
        }

        StringBuilder sb = new StringBuilder();

        for (String methodName : stack) {
            sb.append(methodName).append(" -> ");
        }

        sb.delete(sb.length() - 4, sb.length()); // Remove the last " -> "

        return sb.toString();
    }
}

```


LOGO
```
A logo for an application library called TimerNinja. There is a clock look human like (with arms, legs, eyes), his eyes are cover with Ninja cloth
```


Artifact Publishing
https://www.albertgao.xyz/2018/01/18/how-to-publish-artifact-to-maven-central-via-gradle/

