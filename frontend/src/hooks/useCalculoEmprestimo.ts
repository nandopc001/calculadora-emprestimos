import { useState } from 'react'
import { calcularEmprestimo } from '../services/emprestimoApi'
import { formularioValido, validarDatas } from '../utils/validacoes'
import type { ParcelaResponse } from '../types/emprestimo'

export interface CamposFormulario {
  dataInicial: string
  dataFinal: string
  primeiroPagamento: string
  valorEmprestimo: string   // string para o input; número derivado
  taxaJuros: string
}

const CAMPOS_INICIAIS: CamposFormulario = {
  dataInicial: '', dataFinal: '', primeiroPagamento: '',
  valorEmprestimo: '', taxaJuros: '',
}

export function useCalculoEmprestimo() {
  const [campos, setCampos] = useState<CamposFormulario>(CAMPOS_INICIAIS)
  const [parcelas, setParcelas] = useState<ParcelaResponse[]>([])
  const [carregando, setCarregando] = useState(false)
  const [erro, setErro] = useState<string | null>(null)

  const errosCoerencia = validarDatas(
    campos.dataInicial, campos.dataFinal, campos.primeiroPagamento
  )

  const valorNumerico = Number(campos.valorEmprestimo) || 0
  const taxaNumerica = Number(campos.taxaJuros) / 100 || 0  // usuário digita 7 => 0.07

  const podeCalcular = formularioValido(
    campos.dataInicial, campos.dataFinal, campos.primeiroPagamento,
    valorNumerico, taxaNumerica
  )

  const alterarCampo = (nome: keyof CamposFormulario, valor: string) => {
    setCampos(anterior => ({ ...anterior, [nome]: valor }))
  }

  const calcular = async () => {
    if (!podeCalcular) return
    setCarregando(true)
    setErro(null)
    try {
      const resultado = await calcularEmprestimo({
        dataInicial: campos.dataInicial,
        dataFinal: campos.dataFinal,
        primeiroPagamento: campos.primeiroPagamento,
        valorEmprestimo: valorNumerico,
        taxaJuros: taxaNumerica,
      })
      setParcelas(resultado)
    } catch (e) {
      setErro(e instanceof Error ? e.message : 'Erro inesperado.')
    } finally {
      setCarregando(false)
    }
  }

  return {
    campos, alterarCampo, errosCoerencia,
    podeCalcular, calcular, carregando, erro,
    parcelas, temResultado: parcelas.length > 0,
  }
}