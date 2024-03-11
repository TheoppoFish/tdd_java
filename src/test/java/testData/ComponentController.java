package testData;

import jakarta.inject.Inject;

public class ComponentController implements CustomComponent{

    private final ComponentService componentService;

    public ComponentService getInjection() {
        return componentService;
    }

    @Inject
    public ComponentController(ComponentService componentService) {
        this.componentService = componentService;
    }


}
