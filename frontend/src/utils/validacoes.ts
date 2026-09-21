export interface ErrosFormulario {
  datas?: string
  pagamento?: string
}

export function validarDatas(
  dataInicial: string,
  dataFinal: string,
  primeiroPagamento: string
): ErrosFormulario {
  const erros: ErrosFormulario = {}

  if (dataInicial && dataFinal && dataFinal <= dataInicial) {
    erros.datas = 'A data final deve ser posterior à data inicial.'
  }
  if (
    dataInicial && dataFinal && primeiroPagamento &&
    (primeiroPagamento < dataInicial || primeiroPagamento > dataFinal)
  ) {
    erros.pagamento = 'O primeiro pagamento deve estar entre a data inicial e a final.'
  }
  return erros
}

/** Botão habilitado só com os 5 campos válidos — regra do PDF. */
export function formularioValido(
  dataInicial: string, dataFinal: string, primeiroPagamento: string,
  valor: number, taxa: number
): boolean {
  if (!dataInicial || !dataFinal || !primeiroPagamento) return false
  if (valor <= 0 || taxa <= 0) return false
  const erros = validarDatas(dataInicial, dataFinal, primeiroPagamento)
  return Object.keys(erros).length === 0
}