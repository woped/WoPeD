# WFC-US15: P2T automatisch beim Öffnen eines Petrinetzes starten

## Status
Implementiert

## Anforderung

Wenn ein neues Petrinetz geöffnet wird, soll **Prozess zu Text (P2T)** automatisch ausgeführt werden – mit gespeicherter LLM-Konfiguration (Provider, API-Key, Modell).

*(Hinweis: T2P = Text→Prozess; beim Öffnen einer PNML-Datei ist P2T gemeint.)*

## Ist-Zustand (vor Fix)

- Sidebar und P2T-Panel werden geöffnet (WFC-US13)
- Buttons und Anbieter sind sichtbar
- **Keine** automatische Textgenerierung (`showP2TBar(false)`)

## Lösung

`EditorPanel.openDefaultSidebar()` ruft `showP2TBar(true)` auf → `P2TSideBar.onSideBarShown(..., autoGenerate=true)` startet `getText()` beim ersten Anzeigen.

## Einschränkungen (unverändert)

- Netz muss **sound** sein, sonst Meldung „Das Petrinetz ist nicht sound.“ (z. B. `LoanApplication.pnml`)
- Keine Bogengewichte
- Mindestens 4 Knoten
- Gültiges, unterstütztes LLM-Modell (WFC-US14)

## Testplan

- [ ] `Example-Workflow.pnml` öffnen → P2T startet automatisch, Text erscheint
- [ ] `LoanApplication.pnml` → Sidebar mit Fehler „nicht sound“, kein LLM-Aufruf
- [ ] Reload-Button funktioniert weiterhin manuell

## GitHub Issue

**Titel:** `WFC-US15: P2T automatisch beim Öffnen eines Petrinetzes`

```markdown
## Summary
Auto-start Process-to-Text (LLM) when opening a Petri net, using saved NLP config.

## Change
`openDefaultSidebar()` → `showP2TBar(true)` instead of `false`

## Notes
Unsound nets still show error without LLM call.
```
