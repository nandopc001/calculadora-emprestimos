package br.com.fernando.calculadora.domain;

import org.springframework.stereotype.Component;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Set;

@Component
public class GeradorDeDatas {

    private static final Set<LocalDate> FERIADOS = Set.of(
            LocalDate.of(2024, 11, 15)   // Proclamação da República
    );

    public LocalDate ajustarParaDiaUtil(LocalDate d) {
        while (d.getDayOfWeek() == DayOfWeek.SATURDAY
                || d.getDayOfWeek() == DayOfWeek.SUNDAY
                || FERIADOS.contains(d)) {
            d = d.plusDays(1);
        }
        return d;
    }

    public int mesesEntre(LocalDate ini, LocalDate fim) {
        return (fim.getYear() - ini.getYear()) * 12
                + fim.getMonthValue() - ini.getMonthValue();
    }
}
