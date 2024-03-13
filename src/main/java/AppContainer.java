import jakarta.inject.Inject;
import jakarta.inject.Provider;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.stream.Collectors;

public class AppContainer {

    private final Map<Class<?>, Provider> provider = new HashMap<>();

    public <T> T get(Class<T> target) throws NoSuchElementException {
        if (!provider.containsKey(target)) {
            throw new NoSuchElementException("can not get target from container");
        }

        return (T) provider.get(target).get();
    }

    public <T> void bind(Class<T> bindType, T instance) {
        provider.put(bindType, () -> instance);
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
                throw new IllegalConstructorException("can not found injected constructor annotation");
            }
        });
    }
}
