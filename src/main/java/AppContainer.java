import jakarta.inject.Inject;
import jakarta.inject.Provider;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

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
                        for (Constructor<?> constructor : constructors) {
                            if (constructor.isAnnotationPresent(Inject.class)) {
                                Class<?>[] parameterTypes = constructor.getParameterTypes();
                                Object[] dependencyArray = new Object[parameterTypes.length];
                                for (int i = 0; i < parameterTypes.length; i++) {
                                    Object injectedInstance = provider.get(parameterTypes[i]).get();
                                    dependencyArray[i] = injectedInstance;
                                }
                                return constructor.newInstance(dependencyArray);
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
