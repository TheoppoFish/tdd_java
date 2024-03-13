package testData;


import jakarta.inject.Inject;

public class ComponentWithNoInjectAnnoConstructors implements CustomComponent {

    private final ComponentServiceInjection injectedDependency;


    public ComponentWithNoInjectAnnoConstructors(ComponentServiceInjection componentServiceInjection){
        this.injectedDependency = componentServiceInjection;
    }

    public ComponentServiceInjection getInjection() {
        return injectedDependency;
    }
}
