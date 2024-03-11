package testData;

import jakarta.inject.Inject;

public class ComponentWithMultipleInjectionConstructors implements CustomComponent {
    String value;
    int age;


    @Inject
    public ComponentWithMultipleInjectionConstructors(String value) {
        this.value = value;
    }

    @Inject
    public ComponentWithMultipleInjectionConstructors(int age) {
        this.age = age;
    }
}
