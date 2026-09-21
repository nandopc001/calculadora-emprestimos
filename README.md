# Calculadora de Empréstimos

## Arquitetura

```
calculadora-emprestimos/
calculadora-emprestimos/
├── backend/          # API REST — Java 17 + Spring Boot
│   └── calculadora-backend/
└── frontend/         # SPA — React 18 + TypeScript + Vite + Sass
```

## Backend

A API sobe em `http://localhost:8080`.

### Endpoint

`POST /api/emprestimos/calcular`

**Request:**

```json
{
  "dataInicial": "2024-01-01",
  "dataFinal": "2034-01-01",
  "primeiroPagamento": "2024-02-15",
  "valorEmprestimo": 140000,
  "taxaJuros": 0.07
}
```

**Response:** array de linhas do cronograma (1 linha inicial + provisões
mensais + parcelas). Cada linha contém: `dataCompetencia`, `valorEmprestimo`,
`saldoDevedor`, `consolidada` (ex.: `"1/120"` apenas nas parcelas), `total`,
`amortizacao`, `saldo`, `provisao`, `acumulado`, `pago`.

**Erros de validação** (400): data final ≤ data inicial; primeiro pagamento
fora do intervalo `[dataInicial, dataFinal]`.

### Executando

```bash
cd backend/calculadora-backend
mvn spring-boot:run
```

## Frontend

### Stack

- React 18 + TypeScript + Vite
- Sass (arquitetura: `_variables.scss` + SCSS por componente)
- Estado central em hook customizado (`useCalculoEmprestimo`)

### Executando

```bash
cd frontend
npm install
npm run dev
```

Abre em `http://localhost:5173`, com proxy de `/api` para `localhost:8080`.
