import org.junit.jupiter.api.Test;
import testData.ComponentController;
import testData.ComponentRepository;
import testData.ComponentService;
import testData.ComponentServiceInjection;
import testData.ComponentWithDefaultConstructor;
import testData.CustomComponent;
import testData.ComponentWithDependency;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class ContainerTest {

    private final AppContainer container = new AppContainer();

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


}
