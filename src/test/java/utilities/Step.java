package utilities;

import io.qameta.allure.Allure;
import io.qameta.allure.model.Status;
import org.testng.Reporter;

public class Step {

    public static void step(String name) {
        Reporter.log(name, true);
        Allure.step(name);
    }

    public static void step(String name, Status status) {
        Reporter.log(name, true);
        Allure.step(name, status);
    }

    public static void step(String name, Allure.ThrowableRunnableVoid runnable) {
        Reporter.log(name, true);
        Allure.step(name, runnable);
    }

    public static <T> T step(String name, Allure.ThrowableRunnable<T> runnable) {
        Reporter.log(name, true);
        return Allure.step(name, runnable);
    }

    public static void step(Allure.ThrowableContextRunnableVoid<Allure.StepContext> runnable) {
        Allure.step(runnable);
    }

    public static void step(String name, Allure.ThrowableContextRunnableVoid<Allure.StepContext> runnable) {
        Reporter.log(name, true);
        Allure.step(name, runnable);
    }

    public static Object step(String name, Allure.ThrowableContextRunnable<Object, Allure.StepContext> runnable) {
        Reporter.log(name, true);
        return Allure.step(name, runnable);
    }

    public static <T> T step(Allure.ThrowableContextRunnable<T, Allure.StepContext> runnable) {
        return Allure.step(runnable);
    }

}
