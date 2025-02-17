package h11;

public record StudentMethodCall(
    Object invoked,
    Invocation call,
    Throwable exception
) {}
