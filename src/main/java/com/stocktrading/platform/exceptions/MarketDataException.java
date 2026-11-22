
package com.stocktrading.platform.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_GATEWAY)
public class MarketDataException extends RuntimeException {

    public MarketDataException(String message) {
        super(message);
    }
}
