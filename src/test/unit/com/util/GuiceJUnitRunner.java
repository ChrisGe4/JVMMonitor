package com.util;

import com.google.common.base.Throwables;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.util.Modules;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.InitializationError;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

public class GuiceJUnitRunner extends BlockJUnit4ClassRunner {

    private Injector injector;

    public GuiceJUnitRunner(Class<?> clazz) throws InitializationError {
        super(clazz);
        injector = Guice.createInjector(getModules(clazz));
    }

    @Override
    public Object createTest() throws Exception {
        Object obj = super.createTest();
        injector.injectMembers(obj);
        return obj;
    }

    private List<Module> getModules(Class<?> clazz) {

        Modules annotation = clazz.getAnnotation(Modules.class);

        if (annotation == null)
            return ImmutableList.of();

        Class<? extends Module>[] modules = annotation.value();

        if (modules == null || modules.length == 0)
            return ImmutableList.of();

        //Module[] result = new Module[modules.length];
        List<Module> result = Lists.newArrayListWithCapacity(modules.length);

        for (Class<? extends Module> module : modules) {

            checkNotNull(module);
            Constructor constructor = null;

            try {
                constructor = module.getDeclaredConstructor();
            } catch (NoSuchMethodException e) {
                Throwables.propagate(e);
            }

            constructor.setAccessible(true);

            try {
                result.add((Module) constructor.newInstance());
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
                throw new RuntimeException("A module class" + module.getName()
                    + " within the @Modules annotation cannot be instantiated.", e);
            }

        }
        return result;
    }
}
