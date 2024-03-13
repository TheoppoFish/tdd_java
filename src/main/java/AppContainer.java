import jakarta.inject.Inject;
import jakarta.inject.Provider;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.stream.Collectors;

public class AppContainer {

    private final Map<Class<?>, Provider> provider = new HashMap<>();

    public <T> T get(Class<T> componentClass) {
        return (T) provider.get(componentClass).get();
    }

    public <T> void bind(Class<T> componentClass, T instance) {
        provider.put(componentClass, () -> instance);
    }

    public <S, T extends S> void bind(Class<S> bindType, Class<T> bindTarget) {
        Constructor<?> injectionConstructor = getInjectedConstructor(bindTarget);
        provider.put(bindType, () -> {
                    try {
                        Object[] dependencies = Arrays.stream(injectionConstructor.getParameters()).map(parameter -> get(parameter.getType())).toArray(Object[]::new);
                        return injectionConstructor.newInstance(dependencies);
                    } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
                        throw new RuntimeException(e);
                    }
                }

        );
    }

    private static <S, T extends S> Constructor<?> getInjectedConstructor(Class<T> targetClass) {
        List<Constructor<?>> injectConstructors = Arrays.stream(targetClass.getConstructors()).filter(it -> it.isAnnotationPresent(Inject.class)).collect(Collectors.toList());
        if (injectConstructors.size() > 1) throw new IllegalConstructorException("multiple injection constructors");
        return injectConstructors.stream().findFirst().orElseGet(() -> {
            try {
                return targetClass.getDeclaredConstructor();
            } catch (NoSuchMethodException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
