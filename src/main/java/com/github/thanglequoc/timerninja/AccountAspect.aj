import com.github.thanglequoc.timerninja.Account;

public aspect AccountAspect {
    final int MIN_BALANCE = 10;

    pointcut callWithDraw(int amount, Account acc) :
     call(boolean Account.withdraw(int)) && args(amount) && target(acc);

    before(int amount, Account acc) : callWithDraw(amount, acc) {
        System.out.println("Before is called");
    }

    boolean around(int amount, Account acc) :
      callWithDraw(amount, acc) {
        System.out.println("around is called");
        if (acc.balance < amount) {
            return false;
        }
        return proceed(amount, acc);
    }

    after(int amount, Account balance) : callWithDraw(amount, balance) {
        System.out.println("after is called");
    }
}