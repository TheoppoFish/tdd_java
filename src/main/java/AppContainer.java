import jakarta.inject.Inject;
import jakarta.inject.Provider;

import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Parameter;
import java.util.*;

public class AppContainer {

    private final Map<Class<?>, Provider> provider = new HashMap<>();

    public <T> T get(Class<T> componentClass) {
       return (T) provider.get(componentClass).get();
    }

    public <T> void bind(Class<T> componentClass, T instance) {
        provider.put(componentClass, () -> instance);
    }

    public <S, T extends S> void bind(Class<S> componentClass, Class<T> componentWithConstructorClass) {
        provider.put(componentClass, () -> {
                    try {
                        Constructor<?>[] constructors = componentWithConstructorClass.getConstructors();
                        Map<Constructor<?>, Object> dependencyMap = new HashMap<>();
                        for (Constructor<?> constructor : constructors) {
                            if (constructor.isAnnotationPresent(Inject.class)) {
                                Class<?>[] parameterTypes = constructor.getParameterTypes();
                                for (Class<?> parameterType : parameterTypes) {
                                    Object injectedInstance = provider.get(parameterType).get();
                                    dependencyMap.put(constructor, injectedInstance);
                                }
                                return constructor.newInstance(dependencyMap.get(constructor));
                            }

                        }

                        return componentWithConstructorClass.getDeclaredConstructor().newInstance();
                    } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                             NoSuchMethodException e) {
                        throw new RuntimeException(e);
                    }
                }

        );
    }
}
