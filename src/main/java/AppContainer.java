import java.awt.*;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

public class AppContainer {

    private final Map<Class<?>, Object> containerMap = new HashMap<>();
    private final Map<Class<?>, Class<?>> containerMap1 = new HashMap<>();

    public <Component> Component get(Class<Component> componentClass) {
        if (containerMap.containsKey(componentClass)) {
            return (Component) containerMap.get(componentClass);
        }
        try {
            return (Component) containerMap1.get(componentClass).getDeclaredConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    public <Component> void bind(Class<Component> componentClass, Component instance) {
        containerMap.put(componentClass, instance);
    }

    public <S,T extends S> void bind(Class<S> componentClass, Class<T> componentWithConstructorClass) {
        containerMap1.put(componentClass, componentWithConstructorClass);
    }
}
