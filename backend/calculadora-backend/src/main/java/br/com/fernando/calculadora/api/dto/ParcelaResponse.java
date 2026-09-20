package br.com.fernando.calculadora.api.dto;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

public record ParcelaResponse(
        LocalDate dataCompetencia,
        BigDecimal valorEmprestimo,
        BigDecimal saldoDevedor,
        String consolidada,
        BigDecimal total,
        BigDecimal amortizacao,
        BigDecimal saldo,
        BigDecimal provisao,
        BigDecimal acumulado,
        BigDecimal pago
) {
    private static BigDecimal exibicao(BigDecimal v) {
        return v == null ? null : v.setScale(4, RoundingMode.HALF_EVEN);
    }

    public ParcelaResponse {
        valorEmprestimo = exibicao(valorEmprestimo);
        saldoDevedor   = exibicao(saldoDevedor);
        total          = exibicao(total);
        amortizacao    = exibicao(amortizacao);
        saldo          = exibicao(saldo);
        provisao       = exibicao(provisao);
        acumulado      = exibicao(acumulado);
        pago           = exibicao(pago);
    }
}
