import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


interface Component {}

class ComponentWithConstructor implements Component{
}

class ComponentWithDependencies implements Component {

    private final ComponentServiceInjection injectedDependency;




    @Inject
    public ComponentWithDependencies(ComponentServiceInjection componentServiceInjection){
        this.injectedDependency = componentServiceInjection;
    }

    public ComponentServiceInjection getInjection() {
        return injectedDependency;
    }
}

class ComponentServiceInjection {
}


public class ContainerTest {
    @Test
    public void should_bind_type_to_class (){
        AppContainer context = new AppContainer();
        Component instance = new Component() {
        };
        context.bind(Component.class , instance);
        assertSame(context.get(Component.class), instance) ;
    }

    @Test
    public void should_bind_no_args_constructor (){
        AppContainer appContainer = new AppContainer();

        appContainer.bind(Component.class, ComponentWithConstructor.class);
        Component result = appContainer.get(Component.class);

        assertNotNull(result);
        assertTrue(result instanceof ComponentWithConstructor);

    }

    @Test
    public void should_bind_component_with_dependencies() {

        AppContainer container = new AppContainer();


        container.bind(ComponentServiceInjection.class, new ComponentServiceInjection() );
        container.bind(Component.class, ComponentWithDependencies.class );
        ComponentWithDependencies result = (ComponentWithDependencies) container.get(Component.class);

        assertNotNull(result);
        assertTrue(result.getInjection() instanceof ComponentServiceInjection);
    }



}
