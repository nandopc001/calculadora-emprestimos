import { FormularioEmprestimo } from './components/FormularioEmprestimo'
import { GridParcelas } from './components/GridParcelas'
import { useCalculoEmprestimo } from './hooks/useCalculoEmprestimo'
import './styles/main.scss'

export default function App() {
  const {
    campos, alterarCampo, errosCoerencia,
    podeCalcular, calcular, carregando, erro,
    parcelas, temResultado,
  } = useCalculoEmprestimo()

  return (
    <main className="pagina">
      <h1>Calculadora de Empréstimos</h1>

      <FormularioEmprestimo
        campos={campos}
        errosCoerencia={errosCoerencia}
        podeCalcular={podeCalcular}
        carregando={carregando}
        alterarCampo={alterarCampo}
        calcular={calcular}
      />

      {erro && <p className="pagina__erro" role="alert">{erro}</p>}

      {temResultado && <GridParcelas parcelas={parcelas} />}
    </main>
  )
}