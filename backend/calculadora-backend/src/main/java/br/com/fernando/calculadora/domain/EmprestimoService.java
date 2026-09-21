package br.com.fernando.calculadora.domain;

import br.com.fernando.calculadora.api.dto.ParcelaResponse;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;

@Service
public class EmprestimoService {

    private final CalculadoraJuros calculadoraJuros;
    private final GeradorDeDatas geradorDeDatas;
    private static final MathContext MC = new MathContext(15, RoundingMode.HALF_EVEN);
    private static final BigDecimal ZERO = BigDecimal.ZERO;

    public EmprestimoService(GeradorDeDatas geradorDeDatas, CalculadoraJuros calculadoraJuros) {
        this.geradorDeDatas = geradorDeDatas;
        this.calculadoraJuros = calculadoraJuros;
    }

    public List<ParcelaResponse> calcularParcelas(LocalDate dataInicial, LocalDate dataFinal,
                                                  LocalDate primeiroPagamento, BigDecimal valorEmprestimo,
                                                  double taxaJuros) {
        if (!dataFinal.isAfter(dataInicial)) {
            throw new RegraNegocioException("A data final deve ser posterior à data inicial.");
        }

        if (primeiroPagamento.isBefore(dataInicial) || primeiroPagamento.isAfter(dataFinal)) {
            throw new RegraNegocioException("O primeiro pagamento deve estar entre a data inicial e a final.");
        }

        int n = geradorDeDatas.mesesEntre(dataInicial, dataFinal);
        BigDecimal amortizacao = valorEmprestimo
                .divide(BigDecimal.valueOf(n), MC);

        List<ParcelaResponse> linhas = new ArrayList<>();

        // Linha inicial saldo inicial, demais colunas zeradas
        linhas.add(new ParcelaResponse(dataInicial, valorEmprestimo,
                valorEmprestimo, null, ZERO, ZERO, valorEmprestimo,
                ZERO, ZERO, ZERO));

        BigDecimal saldo = valorEmprestimo;
        LocalDate ultimoPagamento = dataInicial;
        LocalDate pagamento = primeiroPagamento;
        BigDecimal jurosAcumulados = ZERO;

        for (int k = 1; k <= n; k++) {

            // 1) PROVISÕES — último dia de cada mês entre o último pagamento e a parcela
            YearMonth mes = YearMonth.from(ultimoPagamento);
            while (true) {
                LocalDate fimMes = mes.atEndOfMonth();
                if (!fimMes.isBefore(pagamento)) break;   // chegou no mês da parcela
                BigDecimal juros = calculadoraJuros.calcularJuros(saldo, ultimoPagamento, fimMes, taxaJuros);
                jurosAcumulados = jurosAcumulados.add(juros);
                BigDecimal saldoDevedor = saldo.add(jurosAcumulados);
                linhas.add(new ParcelaResponse(fimMes, valorEmprestimo, saldoDevedor, null, ZERO, ZERO, saldo,
                        juros, jurosAcumulados, ZERO));
                mes = mes.plusMonths(1);
            }

            // 2) PARCELA — juros recalculados do zero desde o último pagamento
            //    (a provisão do mês é apenas informativa; não é somada)
            BigDecimal jurosPago = calculadoraJuros.calcularJuros(saldo, ultimoPagamento, pagamento, taxaJuros);
            BigDecimal provisaoPeriodo = jurosPago.subtract(jurosAcumulados);
            BigDecimal total = amortizacao.add(jurosPago);
            saldo = saldo.subtract(amortizacao);
            linhas.add(new ParcelaResponse(pagamento, valorEmprestimo, saldo, k + "/" + n, total,
                    amortizacao, saldo, provisaoPeriodo, ZERO, jurosPago));
            jurosAcumulados = ZERO;
            ultimoPagamento = pagamento;
            pagamento = geradorDeDatas.ajustarParaDiaUtil(primeiroPagamento.plusMonths(k));
        }
        return linhas;
    }
}
