import type { ParcelaResponse } from '../types/emprestimo'
import { formatarData, formatarMoeda } from '../utils/formatadores'
import './GridParcelas.scss'

interface Props {
  parcelas: ParcelaResponse[]
}

export function GridParcelas({ parcelas }: Props) {
  return (
    <section className="grid-container">
      <table className="grid">
        <colgroup>
          <col className="grid__col--data" />
          <col className="grid__col--valor" />
          <col className="grid__col--saldo" />
          <col className="grid__col--consolidada" />
          <col className="grid__col--total" />
          <col className="grid__col--amortizacao" />
          <col className="grid__col--saldo-principal" />
          <col className="grid__col--provisao" />
          <col className="grid__col--acumulado" />
          <col className="grid__col--pago" />
        </colgroup>
        <thead>
          {/* Linha 1: grupos */}
          <tr>
            <th colSpan={3} className="grid__grupo">Empréstimo</th>
            <th colSpan={2} className="grid__grupo">Parcela</th>
            <th colSpan={2} className="grid__grupo">Principal</th>
            <th colSpan={3} className="grid__grupo">Juros</th>
          </tr>
          {/* Linha 2: colunas */}
          <tr>
            <th scope="col">Data Competência</th>
            <th scope="col">Valor de Empréstimo</th>
            <th scope="col">Saldo Devedor</th>
            <th scope="col">Consolidada</th>
            <th scope="col">Total</th>
            <th scope="col">Amortização</th>
            <th scope="col">Saldo</th>
            <th scope="col">Provisão</th>
            <th scope="col">Acumulado</th>
            <th scope="col">Pago</th>
          </tr>
        </thead>
        <tbody>
          {parcelas.map((p, indice) => (
            <tr key={`${p.dataCompetencia}-${indice}`}
                className={`grid__linha ${p.consolidada ? 'grid__linha--parcela' : 'grid__linha--fim-mes'}`}>
              <td>{formatarData(p.dataCompetencia)}</td>
              <td>{formatarMoeda(p.valorEmprestimo)}</td>
              <td>{formatarMoeda(p.saldoDevedor)}</td>
              <td>{p.consolidada ?? ''}</td>
              <td>{formatarMoeda(p.total)}</td>
              <td>{formatarMoeda(p.amortizacao)}</td>
              <td>{formatarMoeda(p.saldo)}</td>
              <td>{formatarMoeda(p.provisao)}</td>
              <td>{formatarMoeda(p.acumulado)}</td>
              <td>{formatarMoeda(p.pago)}</td>
            </tr>
          ))}
        </tbody>
      </table>
    </section>
  )
}