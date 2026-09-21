import type { EmprestimoRequest, ParcelaResponse } from '../types/emprestimo'

export async function calcularEmprestimo(
  request: EmprestimoRequest
): Promise<ParcelaResponse[]> {
  const resposta = await fetch('/api/emprestimos/calcular', {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(request),
  })

  if (!resposta.ok) {
    const erro = await resposta.json().catch(() => null)
    const mensagem = Array.isArray(erro)
      ? erro.map(e => e.mensagem).join(' ')
      : erro?.mensagem ?? 'Erro ao calcular o empréstimo.'
    throw new Error(mensagem)
  }
  return resposta.json()
}