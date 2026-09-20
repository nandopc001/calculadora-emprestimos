package br.com.fernando.calculadora.domain;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Component
public class CalculadoraJuros {
    private static final int BASE_DIAS = 360;
    private static final MathContext MC = new MathContext(15, RoundingMode.HALF_EVEN);

    public BigDecimal calcularJuros(BigDecimal saldoBase, LocalDate de, LocalDate ate, double taxaJuros) {
        long dias = ChronoUnit.DAYS.between(de, ate);
        double fator = Math.pow(1 + taxaJuros, (double) dias / BASE_DIAS) - 1;
        return saldoBase.multiply(BigDecimal.valueOf(fator), MC);
    }
}

