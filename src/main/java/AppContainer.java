import jakarta.inject.Inject;
import jakarta.inject.Provider;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

public class AppContainer {

    private final Map<Class<?>, Provider> provider = new HashMap<>();

    public <T> Optional<T> get(Class<T> target) {
        if (!provider.containsKey(target)) {
            return Optional.empty();
        }

        return Optional.of((T) provider.get(target).get());
    }

    public <T> void bind(Class<T> bindType, T instance) {
        provider.put(bindType, () -> instance);
    }

    public <S, T extends S> void bind(Class<S> bindType, Class<T> bindTarget) {
        Constructor<?> injectionConstructor = getInjectedConstructor(bindTarget);

        Object[] dependencies = Arrays
                .stream(injectionConstructor.getParameters())
                .map(constructorParam -> {
                    Class<?> dependencyType = constructorParam.getType();
                    Optional<S> container = (Optional<S>) get(dependencyType);
                    if (container.isEmpty()) {
                        throw new NoSuchElementException(String.format("can not get target dependency %s for %s from container", dependencyType.getCanonicalName(), bindTarget.getName()));
                    }
                    return container.get();
                })
                .toArray(Object[]::new);


        provider.put(bindType, () -> {
                    try {
                        return injectionConstructor.newInstance(dependencies);
                    } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
                        throw new RuntimeException(e);
                    }
                }

        );
    }

    private static <S, T extends S> Constructor<?> getInjectedConstructor(Class<T> targetClass) {
        List<Constructor<?>> injectConstructors = Arrays.stream(targetClass.getConstructors()).filter(it -> it.isAnnotationPresent(Inject.class)).toList();
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
