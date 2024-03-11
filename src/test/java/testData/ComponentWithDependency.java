package testData;

import jakarta.inject.Inject;

public class ComponentWithDependency implements CustomComponent {

    private final ComponentServiceInjection injectedDependency;




    @Inject
    public ComponentWithDependency(ComponentServiceInjection componentServiceInjection){
        this.injectedDependency = componentServiceInjection;
    }

    public ComponentServiceInjection getInjection() {
        return injectedDependency;
    }
}
