import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import testData.ComponentController;
import testData.ComponentRepository;
import testData.ComponentService;
import testData.ComponentServiceInjection;
import testData.ComponentWithDefaultConstructor;
import testData.ComponentWithDependency;
import testData.ComponentWithMultipleInjectionConstructors;
import testData.ComponentWithNoInjectAnnoConstructors;
import testData.CustomComponent;

import java.util.Optional;

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
        assertSame(container.get(CustomComponent.class).get(), instance);
    }

    @Test
    public void should_bind_no_args_constructor() {

        container.bind(CustomComponent.class, ComponentWithDefaultConstructor.class);
        CustomComponent result = container.get(CustomComponent.class).get();

        assertNotNull(result);
        assertTrue(result instanceof ComponentWithDefaultConstructor);

    }

    @Test
    public void should_bind_component_with_dependencies() {


        container.bind(ComponentServiceInjection.class, new ComponentServiceInjection());
        container.bind(CustomComponent.class, ComponentWithDependency.class);
        ComponentWithDependency result = (ComponentWithDependency) container.get(CustomComponent.class).get();

        assertNotNull(result);
        assertTrue(result.getInjection() instanceof ComponentServiceInjection);
    }

    @Test
    public void should_bind_component_with_another_dependency() {

        container.bind(ComponentRepository.class, new ComponentRepository());
        container.bind(ComponentService.class, ComponentService.class);
        container.bind(CustomComponent.class, ComponentController.class);
        ComponentController result = (ComponentController) container.get(CustomComponent.class).get();

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
    public void should_throw_exception_when_constructor_has_no_inject_annotation() {
        assertThrows(IllegalConstructorException.class, () -> {
            container.bind(CustomComponent.class, ComponentWithNoInjectAnnoConstructors.class);
        }, "can not found injected constructor annotation");
    }

    @Test
    void should_throw_exception_when_can_not_get_type_from_container() {
            Optional<CustomComponent> result = container.get(CustomComponent.class);
       assertTrue(result.isEmpty());
    }

}
