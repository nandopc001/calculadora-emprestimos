package br.com.fernando.calculadora.api;

import br.com.fernando.calculadora.api.dto.EmprestimoRequest;
import br.com.fernando.calculadora.api.dto.ParcelaResponse;
import br.com.fernando.calculadora.domain.EmprestimoService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/emprestimos")
public class EmprestimoController {

    private final EmprestimoService emprestimoService;

    public EmprestimoController(EmprestimoService emprestimoService) {
        this.emprestimoService = emprestimoService;
    }

    @PostMapping("/calcular")
    public List<ParcelaResponse> calcular(@Valid @RequestBody EmprestimoRequest request) {
        return emprestimoService.calcularParcelas(request.dataInicial(), request.dataFinal(),
                request.primeiroPagamento(), request.valorEmprestimo(), request.taxaJuros());
    }
}
