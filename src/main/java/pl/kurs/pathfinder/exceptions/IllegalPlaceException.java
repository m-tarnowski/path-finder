package pl.kurs.pathfinder.exceptions;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class IllegalPlaceException extends RuntimeException {
    private final String message;
}
