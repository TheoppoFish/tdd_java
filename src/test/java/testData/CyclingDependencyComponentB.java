package testData;

import jakarta.inject.Inject;

public class CyclingDependencyComponentB implements CustomComponent{
    private final CyclingDependencyComponentA injectedDependency;

    @Inject
    public CyclingDependencyComponentB(CyclingDependencyComponentA cyclingDependencyComponentA) {
        this.injectedDependency = cyclingDependencyComponentA;
    }

    public CyclingDependencyComponentA getInjection() {
        return injectedDependency;
    }
}
