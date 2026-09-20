package br.com.fernando.calculadora.domain;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

class GeradorDeDatasTest {
    private final GeradorDeDatas gerador = new GeradorDeDatas();

    @Test
    void deveAjustarSabadoParaSegundaSeguinte() {
        // Planilha: 15/06/2024 é sábado -> 17/06
        assertEquals(LocalDate.of(2024, 6, 17),
                gerador.ajustarParaDiaUtil(LocalDate.of(2024, 6, 15)));
    }

    @Test
    void deveAjustarDomingoParaSegundaSeguinte() {
        // Planilha: 15/09/2024 é domingo -> 16/09
        assertEquals(LocalDate.of(2024, 9, 16),
                gerador.ajustarParaDiaUtil(LocalDate.of(2024, 9, 15)));
    }

    @Test
    void deveAjustarFeriadoParaSegundaSeguinte() {
        // Planilha: 15/11/2024 (sexta, feriado) -> 18/11
        assertEquals(LocalDate.of(2024, 11, 18),
                gerador.ajustarParaDiaUtil(LocalDate.of(2024, 11, 15)));
    }

    @Test
    void naoDeveAlterarDataJaUtil() {
        assertEquals(LocalDate.of(2024, 3, 15),
                gerador.ajustarParaDiaUtil(LocalDate.of(2024, 3, 15)));
    }

    @Test
    void ultimaParcelaDeveCairEm16DeJaneiroDe2034() {
        // Planilha: 15/01/2034 é domingo -> 16/01
        assertEquals(LocalDate.of(2034, 1, 16),
                gerador.ajustarParaDiaUtil(LocalDate.of(2034, 1, 15)));
    }

    @Test
    void mesesEntreDeveReproduzirGCelulaDaPlanilha() {
        // G2 da planilha: 01/01/2024 a 01/01/2034 = 120 meses
        assertEquals(120,
                gerador.mesesEntre(LocalDate.of(2024, 1, 1), LocalDate.of(2034, 1, 1)));
    }
}