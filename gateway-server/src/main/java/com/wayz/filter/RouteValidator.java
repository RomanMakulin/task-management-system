package com.wayz.filter;

@Component
public class RouteValidator {

    public static final List<String> openApiEndpoints = List.of(
            "/auth/registration",
            "/auth/token",
            "/eureka"
    );

    public Predicate<ServerHttpRequest> isSecured =
            request -> openApiEndpoints
                    .stream()
                    .noneMatch(uri -> request.getURI().getPath().contains(uri));

}
