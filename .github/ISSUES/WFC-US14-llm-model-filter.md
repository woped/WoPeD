# WFC-US14: LLM-Modellfilter (OpenAI/Gemini) für P2T/T2P

## Status
Implementiert

## Problem

Beim Abruf über **„GPT-Modelle abrufen“** listet WoPeD **alle** Modelle des Providers (`/v1/models` bzw. Gemini `v1beta/models`). Der P2T-/T2P-Server nutzt aber nur **Chat-/Text-Generierung** (`chat/completions` / `generateContent`).

Folge: Nutzer wählen z. B. `gpt-4o-mini-transcribe-2025-03-20` → HTTP 500:

> *This is not a chat model and thus not supported in the v1/chat/completions endpoint.*

## Lösung (Hybrid: Whitelist + Blacklist)

Neue Klasse `LlmModelFilter` in **WoPeD-Core**:

1. **Whitelist-Präfixe** (Modell muss passen)
   - **OpenAI:** `gpt-3.5-turbo`, `gpt-4*`, `gpt-4o*`, `gpt-4.1*`, `gpt-5*`, `o1*`, `o3*`, `o4*`, `chatgpt-*`
   - **Gemini:** `gemini-*`, `gemini`

2. **Blacklist-Fragmente** (Ausreißer ausschließen)
   - **OpenAI:** `transcribe`, `whisper`, `tts`, `embedding`, `dall-e`, `moderation`, `realtime`, `audio`, `gpt-image`, Legacy-Completion, `ft:`, …
   - **Gemini:** `embedding`, `imagen`, `veo`, `aqa`, `tuner`, …

3. **LM Studio:** unverändert (lokale Modelle)

## Integration

| Datei | Änderung |
|-------|----------|
| `WoPeD-Core/.../LlmModelFilter.java` | Filterlogik, Defaults, `resolveSelection()` |
| `WoPeD-Editor/.../ApiHelper.java` | Filter nach API-Abruf |
| `WoPeD-FileInterface/.../P2TUI.java` | Dropdown + Validierung vor Generierung |
| `WoPeD-Editor/.../ConfNLPToolsPanel.java` | Gefilterte Liste in NLP-Einstellungen |
| `WoPeD-QualAnalysis/.../WebServiceThreadLLM.java` | Client-seitige Absicherung vor Request |
| `Messages.properties` / `Messages_de.properties` | Fehlermeldung bei ungültigem Modell |
| `WoPeD-UnitTests/.../LlmModelFilterTest.java` | Unit-Tests |

## Defaults

- OpenAI: `gpt-4o-mini`
- Gemini: `gemini-2.0-flash`

Gespeichertes, nicht mehr unterstütztes Modell → Fallback auf Default bzw. erstes gefiltertes Modell.

## Testplan

- [ ] OpenAI: Modelle abrufen → keine `*-transcribe*`, `embedding`, `whisper`, `dall-e` in Liste
- [ ] Gemini: Modelle abrufen → keine `embedding`, `imagen` in Liste
- [ ] P2T mit `gpt-4o-mini` → erfolgreich
- [ ] Manuell ungültiges Modell in Config → Fehlermeldung statt HTTP 500
- [ ] Unit-Tests `LlmModelFilterTest` grün

## GitHub Issue (manuell anlegen)

**Titel:** `WFC-US14: LLM-Modellfilter (OpenAI/Gemini) für P2T/T2P`

**Labels:** `enhancement`, `p2t`, `llm`

**Body:**

```markdown
## Summary
Filter OpenAI/Gemini model dropdowns to chat/text-capable models only (hybrid whitelist + blacklist).

## Problem
All provider models were listed; non-chat models (transcribe, embedding, imagen, …) caused P2T HTTP 500.

## Solution
- `LlmModelFilter` in WoPeD-Core (whitelist prefixes + blacklist fragments)
- Applied in `ApiHelper.fetchModels()`, P2T dialog, NLP settings, `WebServiceThreadLLM`

## Defaults
- OpenAI: gpt-4o-mini
- Gemini: gemini-2.0-flash
```
