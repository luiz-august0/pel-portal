# Fluxo de Pré-Cadastro — Programa de Ensino de Línguas

## 1. Domínios

### Usuário
**Atributos obrigatórios e opcionais:**
- **CPF**: identificador único do usuário (obrigatório, não pode se repetir)
- **Nome**: obrigatório
- **Data de nascimento**: obrigatório
- **Email**: pode repetir entre usuários diferentes
- **Celular**: opcional
- **Possui necessidades especiais**: valores possíveis → "Sim" ou "Não"
- **Origem de conhecimento do programa**: valores possíveis → "Facebook", "Instagram", "Google", "WhatsApp", "Email", "Outro" (se "Outro", campo livre obrigatório)
- **Senha**: obrigatória

---

## 2. Estados do Usuário

- **Usuário menor de idade**
    - Fica **bloqueado** até que um responsável se cadastre e reconheça o vínculo de dependência.
    - Acesso restrito até a liberação pelo responsável.

- **Usuário maior de idade**
    - Fluxo normal de cadastro, sem necessidade de responsável.

---

## 3. Comportamentos

### 3.1 Cadastro de Usuário Menor de Idade
1. Usuário menor preenche formulário de pré-cadastro.
2. Ao confirmar, o sistema **gera um link** para envio ao responsável.
3. Esse link permite que o responsável realize o acesso/cadastro.

---

### 3.2 Acesso do Responsável via Link
**Condições:**
- **Responsável já cadastrado**
    - Faz login no sistema usando suas credenciais.
    - Sistema realiza **vínculo de dependência** entre o usuário menor e o responsável.
    - Responsável deve **reconhecer o dependente** para liberar funcionalidades ao menor.

- **Responsável não cadastrado**
    - Efetua cadastro completo, obedecendo as obrigatoriedades do domínio **Usuário** com exceção do campo "Origem de conhecimento do programa" (essa informação é herdada do cadastro do menor).
    - Após cadastro, vínculo é criado e responsável deve reconhecer o dependente.

---

## 4. Regras e Restrições

- **CPF** deve ser único no sistema.
- **Data de nascimento** define se o usuário é menor ou maior de idade.
- Usuário menor de idade **não tem acesso total** até a aprovação do responsável.
- Origem de conhecimento do programa:
    - Obrigatório para usuários comuns.
    - Para responsáveis cadastrados via convite do menor, o campo é **herdado** do menor.
- Caso o responsável recuse o vínculo, o usuário menor permanece bloqueado.

---

## 5. Fluxo Resumido (Pré-Cadastro)

1. **Usuário → Formulário inicial**
    - Preenche dados pessoais, senha e origem de conhecimento do programa.
2. **Validação de idade**
    - Se maior de idade → segue para finalização do cadastro.
    - Se menor de idade → sistema envia link ao responsável.
3. **Responsável acessa link**
    - Já cadastrado → login + vínculo + reconhecimento.
    - Não cadastrado → cadastro + vínculo + reconhecimento.
4. **Reconhecimento do dependente**
    - Se confirmado → menor tem acesso liberado.
    - Se negado ou pendente → menor permanece bloqueado.

---
