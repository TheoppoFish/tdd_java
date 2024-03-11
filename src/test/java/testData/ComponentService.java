package testData;

import jakarta.inject.Inject;

public class ComponentService {

    public ComponentRepository getInjection() {
        return componentRepository;
    }

    private final ComponentRepository componentRepository;

    @Inject
    public ComponentService(ComponentRepository componentRepository) {
        this.componentRepository = componentRepository;
    }
}
