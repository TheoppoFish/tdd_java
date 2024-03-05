import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


interface Component {}

class ComponentWithConstructor implements Component{
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


}
