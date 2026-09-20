package br.com.fernando.calculadora.api.dto;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.time.LocalDate;

public record EmprestimoRequest(
        @NotNull LocalDate dataInicial,
        @NotNull LocalDate dataFinal,
        @NotNull LocalDate primeiroPagamento,
        @NotNull @Positive BigDecimal valorEmprestimo,
        @NotNull @Positive @DecimalMax("1.0") double taxaJuros
) {}
