package br.com.indra.jp_capacitacao_2026.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class EntidadeEmUsoException extends RuntimeException {
    public EntidadeEmUsoException(String message) {
        super(message);
    }
}
