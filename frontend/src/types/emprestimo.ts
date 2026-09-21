export interface EmprestimoRequest {
  dataInicial: string
  dataFinal: string
  primeiroPagamento: string
  valorEmprestimo: number
  taxaJuros: number
}

export interface ParcelaResponse {
  dataCompetencia: string
  valorEmprestimo: number
  saldoDevedor: number
  consolidada: string | null
  total: number
  amortizacao: number
  saldo: number
  provisao: number
  acumulado: number
  pago: number
}

export type ErroApi = { mensagem?: string } | Array<{ campo: string; mensagem: string }>