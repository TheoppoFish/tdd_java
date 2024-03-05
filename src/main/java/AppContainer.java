import jakarta.inject.Provider;

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
                        return componentWithConstructorClass.getDeclaredConstructor().newInstance();
                    } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                             NoSuchMethodException e) {
                        throw new RuntimeException(e);
                    }
                }
        );
    }
}
