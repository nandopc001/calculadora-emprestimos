package br.com.fernando.calculadora.domain;

import br.com.fernando.calculadora.api.dto.ParcelaResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class EmprestimoServiceTest {

    private EmprestimoService service;

    @BeforeEach
    void setUp() {
        service = new EmprestimoService(
                new GeradorDeDatas(),
                new CalculadoraJuros());
    }

    /** Dados do exemplo da planilha Calculadora_Emprestimos.xlsx. */
    private List<ParcelaResponse> calcularPlanilha() {
        return service.calcularParcelas(
                LocalDate.of(2024, 1, 1),    // dataInicial
                LocalDate.of(2034, 1, 1),    // dataFinal
                LocalDate.of(2024, 2, 15),   // primeiroPagamento
                new BigDecimal("140000"),    // valorEmprestimo
                0.07);                       // taxaJuros
    }

    @Test
    void deveGerarQuantidadeCorretaDeLinhas() {
        // 1 abertura + fins de mês + 120 parcelas
        List<ParcelaResponse> linhas = calcularPlanilha();
        assertEquals(120, linhas.stream().filter(l -> l.consolidada() != null).count());
        assertTrue(linhas.size() > 121);
        assertEquals(LocalDate.of(2034, 1, 16), linhas.get(linhas.size() - 1).dataCompetencia());
    }

    @Test
    void parcelaDe15DeFevereiroDeveReproduzirPlanilha() {
        List<ParcelaResponse> linhas = calcularPlanilha();

        ParcelaResponse primeira = linhas.stream()
                .filter(l -> "1/120".equals(l.consolidada()))
                .findFirst().orElseThrow();

        assertEquals(LocalDate.of(2024, 2, 15), primeira.dataCompetencia());
        assertEquals(0, primeira.total().compareTo(new BigDecimal("2355.7140")));
        assertEquals(0, primeira.amortizacao().compareTo(new BigDecimal("1166.6667")));
        assertEquals(0, primeira.pago().compareTo(new BigDecimal("1189.0473")));
        assertEquals(0, primeira.provisao().compareTo(new BigDecimal("397.4670")));
    }

    @Test
    void saldoDevedorEmFimDeMesNaoDeveIncluirJurosProvisionados() {
        // Planilha: 31/01/2024 mostra saldo 140.000,0000 (juros são informativos)
        List<ParcelaResponse> linhas = calcularPlanilha();

        ParcelaResponse fimJaneiro = linhas.stream()
                .filter(l -> l.dataCompetencia().equals(LocalDate.of(2024, 1, 31)))
                .findFirst().orElseThrow();

        assertEquals(0, fimJaneiro.saldo().compareTo(new BigDecimal("140000")));
        assertEquals(0, fimJaneiro.acumulado().compareTo(new BigDecimal("791.5804")));
        assertEquals(BigDecimal.ZERO.compareTo(BigDecimal.ZERO), BigDecimal.ZERO.compareTo(fimJaneiro.pago()));
    }

    @Test
    void quintaParcelaDeveSerAjustadaPara17DeJunho() {
        // Planilha: 15/06/2024 é sábado -> 17/06/2024
        List<ParcelaResponse> linhas = calcularPlanilha();
        assertTrue(linhas.stream().anyMatch(l ->
                l.consolidada() != null && l.consolidada().startsWith("5/120")
                        && l.dataCompetencia().equals(LocalDate.of(2024, 6, 17))));
    }

    @Test
    void decimaParcelaDeveSerAjustadaPara18DeNovembroPorFeriado() {
        // Planilha: 15/11/2024 é sexta + feriado -> 18/11/2024
        List<ParcelaResponse> linhas = calcularPlanilha();
        assertTrue(linhas.stream().anyMatch(l ->
                l.consolidada() != null && l.consolidada().startsWith("10/120")
                        && l.dataCompetencia().equals(LocalDate.of(2024, 11, 18))));
    }

    @Test
    void ultimaParcelaDeveEncerrarComSaldoZero() {
        List<ParcelaResponse> linhas = calcularPlanilha();
        ParcelaResponse ultima = linhas.get(linhas.size() - 1);

        assertEquals("120/120", ultima.consolidada());
        assertEquals(0, ultima.saldo().compareTo(BigDecimal.ZERO));
    }

    @Test
    void deveRejeitarDataFinalIgualOuAnteriorAInicial() {
        assertThrows(RegraNegocioException.class, () ->
                service.calcularParcelas(
                        LocalDate.of(2024, 1, 1),
                        LocalDate.of(2024, 1, 1),
                        LocalDate.of(2024, 2, 15),
                        new BigDecimal("10000"), 0.07));
    }

    @Test
    void deveRejeitarPrimeiroPagamentoForaDoIntervalo() {
        assertThrows(RegraNegocioException.class, () ->
                service.calcularParcelas(
                        LocalDate.of(2024, 1, 1),
                        LocalDate.of(2034, 1, 1),
                        LocalDate.of(2023, 12, 1),
                        new BigDecimal("10000"), 0.07));
    }
}