package testData;

import jakarta.inject.Inject;

public class CyclingDependencyComponentA implements  CustomComponent{
    private final CyclingDependencyComponentB injectedDependency;

    @Inject
    public CyclingDependencyComponentA(CyclingDependencyComponentB cyclingDependencyComponentB) {
        this.injectedDependency = cyclingDependencyComponentB;
    }

    public CyclingDependencyComponentB getInjection() {
        return injectedDependency;
    }
}
