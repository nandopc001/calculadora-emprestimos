package br.com.fernando.calculadora.domain;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CalculadoraJurosTest {
    private final CalculadoraJuros calculadora = new CalculadoraJuros();
    private static final double DELTA = 0.05; // tolerância de centavos vs. planilha

    @Test
    void taxaMensalEfetivaDeveReproduzirProvisaoDeJaneiroDaPlanilha() {
        // Planilha: 140.000 x taxaMensalEfetiva(7%) = 791,5804 (linha 31/01/2024)
        BigDecimal juros = calculadora.calcularJuros(
                new BigDecimal("140000"),
                LocalDate.of(2024, 1, 1),
                LocalDate.of(2024, 1, 31),
                0.07);
        assertEquals(791.5804, juros.doubleValue(), DELTA);
    }

    @Test
    void provisaoDeFimDeFevereiroDeveReproduzirProRataDaPlanilha() {
        // Planilha 29/02/2024: 138.833,33 x F(14 dias: 15/02 -> 29/02) = 365,7751
        BigDecimal juros = calculadora.calcularJuros(
                new BigDecimal("138833.333333333"),
                LocalDate.of(2024, 2, 15),
                LocalDate.of(2024, 2, 29),
                0.07);
        assertEquals(365.7751, juros.doubleValue(), DELTA);
    }

    @Test
    void jurosAcumuladosAtePrimeiraParcelaDevemReproduzirPagoDaPlanilha() {
        // Planilha 15/02/2024: 140.000 x F(45 dias: 01/01 -> 15/02) = 1.189,0473
        BigDecimal juros = calculadora.calcularJuros(
                new BigDecimal("140000"),
                LocalDate.of(2024, 1, 1),
                LocalDate.of(2024, 2, 15),
                0.07);
        assertEquals(1189.0473, juros.doubleValue(), DELTA);
    }

    @Test
    void parcela2DeveFecharComPagoDaPlanilha() {
        // Planilha 15/03/2024: 138.833,33 x F(29 dias: 15/02 -> 15/03) = 758,7464
        BigDecimal juros = calculadora.calcularJuros(
                new BigDecimal("138833.333333333"),
                LocalDate.of(2024, 2, 15),
                LocalDate.of(2024, 3, 15),
                0.07);
        assertEquals(758.7464, juros.doubleValue(), DELTA);
    }

    @Test
    void jurosDePeriodoZeroDeveSerZero() {
        BigDecimal juros = calculadora.calcularJuros(
                new BigDecimal("1000"),
                LocalDate.of(2024, 1, 1),
                LocalDate.of(2024, 1, 1),
                0.07);
        assertEquals(BigDecimal.ZERO.setScale(0), juros.setScale(0, RoundingMode.HALF_EVEN));
    }
}