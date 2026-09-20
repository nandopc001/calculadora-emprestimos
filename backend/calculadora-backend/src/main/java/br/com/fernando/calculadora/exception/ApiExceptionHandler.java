package br.com.fernando.calculadora.exception;

import br.com.fernando.calculadora.domain.RegraNegocioException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.List;
import java.util.Map;

public class ApiExceptionHandler {

    /** Erros de Bean Validation no DTO: 400 com campo + mensagem. */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<List<Map<String, String>>> tratarValidacao(MethodArgumentNotValidException ex) {
        List<Map<String, String>> erros = ex.getBindingResult().getFieldErrors().stream()
                .map(erro -> Map.of("campo", erro.getField(), "mensagem", erro.getDefaultMessage()))
                .toList();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(erros);
    }

    /** Regras de negócio do service: 400 com mensagem amigável. */
    @ExceptionHandler(RegraNegocioException.class)
    public ResponseEntity<Map<String, String>> tratarRegraNegocio(RegraNegocioException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("mensagem", ex.getMessage()));
    }
}
