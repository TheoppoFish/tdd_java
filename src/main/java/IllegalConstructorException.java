public class IllegalConstructorException extends RuntimeException {

    private final String message;

    public IllegalConstructorException(String message) {
        this.message = message;
    }

    @Override
    public String getMessage(){
        return this.message;
    }
}
