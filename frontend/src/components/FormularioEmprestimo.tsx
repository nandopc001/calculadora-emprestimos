import type { CamposFormulario } from '../hooks/useCalculoEmprestimo'
import './FormularioEmprestimo.scss'

interface Props {
  campos: CamposFormulario
  errosCoerencia: { datas?: string; pagamento?: string }
  podeCalcular: boolean
  carregando: boolean
  alterarCampo: (nome: keyof CamposFormulario, valor: string) => void
  calcular: () => void
}

export function FormularioEmprestimo({
  campos, podeCalcular, carregando, alterarCampo, calcular,
}: Props) {
  const aoSubmeter = (e: React.FormEvent) => {
    e.preventDefault()
    calcular()
  }

  return (
    <form className="formulario" onSubmit={aoSubmeter}>
      <div className="formulario__campo">
        <label htmlFor="dataInicial">Data inicial</label>
        <input id="dataInicial" type="date" required
          value={campos.dataInicial}
          onChange={e => alterarCampo('dataInicial', e.target.value)} />
      </div>

      <div className="formulario__campo">
        <label htmlFor="dataFinal">Data final</label>
        <input id="dataFinal" type="date" required
          value={campos.dataFinal}
          onChange={e => alterarCampo('dataFinal', e.target.value)} />
      </div>

      <div className="formulario__campo">
        <label htmlFor="primeiroPagamento">Primeiro pagamento</label>
        <input id="primeiroPagamento" type="date" required
          value={campos.primeiroPagamento}
          onChange={e => alterarCampo('primeiroPagamento', e.target.value)} />
      </div>

      <div className="formulario__campo">
        <label htmlFor="valorEmprestimo">Valor do empréstimo (R$)</label>
        <input id="valorEmprestimo" type="number" min="0.01" step="0.01" required
          value={campos.valorEmprestimo}
          onChange={e => alterarCampo('valorEmprestimo', e.target.value)} />
      </div>

      <div className="formulario__campo">
        <label htmlFor="taxaJuros">Taxa de juros (% a.a.)</label>
        <input id="taxaJuros" type="number" min="0.01" step="0.01" required
          value={campos.taxaJuros}
          onChange={e => alterarCampo('taxaJuros', e.target.value)} />
      </div>

      <button type="submit" className="formulario__botao"
              disabled={!podeCalcular || carregando}>
        {carregando ? 'Calculando...' : 'Calcular'}
      </button>
    </form>
  )
}