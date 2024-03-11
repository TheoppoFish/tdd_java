import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import testData.ComponentController;
import testData.ComponentRepository;
import testData.ComponentService;
import testData.ComponentServiceInjection;
import testData.ComponentWithDefaultConstructor;
import testData.ComponentWithDependency;
import testData.ComponentWithMultipleInjectionConstructors;
import testData.ComponentWithoutConstructor;
import testData.CustomComponent;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class ContainerTest {

    private AppContainer container;

    @BeforeEach
    public void setUp() {
        container = new AppContainer();
    }

    @Test
    public void should_bind_type_to_class() {
        CustomComponent instance = new CustomComponent() {
        };
        container.bind(CustomComponent.class, instance);
        assertSame(container.get(CustomComponent.class), instance);
    }

    @Test
    public void should_bind_no_args_constructor() {

        container.bind(CustomComponent.class, ComponentWithDefaultConstructor.class);
        CustomComponent result = container.get(CustomComponent.class);

        assertNotNull(result);
        assertTrue(result instanceof ComponentWithDefaultConstructor);

    }

    @Test
    public void should_bind_component_with_dependencies() {


        container.bind(ComponentServiceInjection.class, new ComponentServiceInjection());
        container.bind(CustomComponent.class, ComponentWithDependency.class);
        ComponentWithDependency result = (ComponentWithDependency) container.get(CustomComponent.class);

        assertNotNull(result);
        assertTrue(result.getInjection() instanceof ComponentServiceInjection);
    }

    @Test
    public void should_bind_component_with_another_dependency() {

        container.bind(ComponentRepository.class, new ComponentRepository());
        container.bind(ComponentService.class, ComponentService.class);
        container.bind(CustomComponent.class, ComponentController.class);
        ComponentController result = (ComponentController) container.get(CustomComponent.class);

        ComponentService injection = result.getInjection();
        assertNotNull(result);
        assertTrue(injection instanceof ComponentService);
        assertTrue(injection.getInjection() instanceof ComponentRepository);
    }

    @Test
    public void should_throw_exception_given_multiple_inject_constructors() {
        assertThrows(IllegalConstructorException.class, () -> {
            container.bind(CustomComponent.class, ComponentWithMultipleInjectionConstructors.class);
        }, "multiple injection constructors");
    }

    @Test
    public void should_throw_exception_without_any_type_of_constructors() {
        assertThrows(IllegalConstructorException.class, () -> {
            container.bind(CustomComponent.class, ComponentWithoutConstructor.class);
        }, "no constructors exception");

    }


}
