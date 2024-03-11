import org.junit.jupiter.api.Test;
import testData.ComponentServiceInjection;
import testData.ComponentWithDefaultConstructor;
import testData.CustomComponent;
import testData.ComponentWithDependency;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class ContainerTest {
    @Test
    public void should_bind_type_to_class (){
        AppContainer context = new AppContainer();
        CustomComponent instance = new CustomComponent() {
        };
        context.bind(CustomComponent.class , instance);
        assertSame(context.get(CustomComponent.class), instance) ;
    }

    @Test
    public void should_bind_no_args_constructor (){
        AppContainer appContainer = new AppContainer();

        appContainer.bind(CustomComponent.class, ComponentWithDefaultConstructor.class);
        CustomComponent result = appContainer.get(CustomComponent.class);

        assertNotNull(result);
        assertTrue(result instanceof ComponentWithDefaultConstructor);

    }

    @Test
    public void should_bind_component_with_dependencies() {

        AppContainer container = new AppContainer();


        container.bind(ComponentServiceInjection.class, new ComponentServiceInjection() );
        container.bind(CustomComponent.class, ComponentWithDependency.class );
        ComponentWithDependency result = (ComponentWithDependency) container.get(CustomComponent.class);

        assertNotNull(result);
        assertTrue(result.getInjection() instanceof ComponentServiceInjection);
    }



}
