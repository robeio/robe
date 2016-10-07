package io.robe.admin.util.junit;

import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;

import java.util.Collections;
import java.util.List;

/**
 * Created by adem on 07/10/2016.
 */
public class Roadrunner extends BlockJUnit4ClassRunner {

    public Roadrunner(Class<?> clazz) throws InitializationError {
        super(clazz);
    }

    @Override
    protected List<FrameworkMethod> computeTestMethods() {
        List<FrameworkMethod> methods = super.computeTestMethods();
        Collections.sort(methods, (o1, o2) -> {
            if (o1.getMethod().isAnnotationPresent(Order.class) && o2.getMethod().isAnnotationPresent(Order.class)) {
                Order order1 = o1.getAnnotation(Order.class);
                Order order2 = o2.getAnnotation(Order.class);
                return Integer.compare(order1.order(), order2.order());
            }
            return -1;
        });
        return methods;
    }

}
