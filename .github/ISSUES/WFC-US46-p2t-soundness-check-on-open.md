# WFC-US46: Auto-Refresh beim Laden eines neuen Modells (nur Soundness-Check)

## Status
Implementiert (Branch: `feature/wfc-us10-http500-fix`)

## Problem

Beim Laden eines neuen Petrinetzes kann im P2T-Panel noch eine **veraltete Fehlermeldung** (z. B. „Das Petrinetz ist nicht sound.“) vom vorherigen Modell stehen, obwohl das neue Modell sound ist.

## Erwartetes Verhalten (PO)

Beim **initialen Aufruf** (Netz öffnen / neues Modell laden):

- **Nur Soundness-Check** ausführen
- Bei nicht-sound Netz: Fehlermeldung anzeigen
- Bei sound Netz: Panel leer lassen, **kein** automatischer LLM-Aufruf / keine Textübersetzung

Textgenerierung erfolgt weiterhin **manuell** über:

- Ribbon **„In Text umwandeln“**
- Reload-Button im P2T-Panel

## Lösung

- `EditorPanel.openDefaultSidebar()` → `showP2TBar(false)` statt `showP2TBar(true)`
- `P2TSideBar.onSideBarShown(visible, autoGenerate)`:
  - Immer Soundness-Check bei sichtbarem Panel
  - `autoGenerate=true` nur bei explizitem Nutzeraufruf (Ribbon/Dialog)
  - Bei sound + ohne Auto-Generate: Textbereich leeren (veraltete Meldungen entfernen)

## Betroffene Dateien

- `WoPeD-Editor/.../EditorPanel.java`
- `WoPeD-QualAnalysis/.../P2TSideBar.java`

## Testplan

- [ ] `LoanApplication.pnml` öffnen → P2T-Panel zeigt „nicht sound“, **kein** LLM-Aufruf
- [ ] Anschließend sound Netz öffnen → **keine** alte Fehlermeldung, leeres Panel, **kein** LLM-Aufruf
- [ ] Reload-Button → Textgenerierung startet manuell
- [ ] Ribbon „In Text umwandeln“ → Panel öffnet mit Auto-Generierung wie bisher

## GitHub Issue

**Titel:** `WFC-US46: Auto-Refresh beim Laden eines neuen Modells`

**Labels:** `enhancement`, `p2t`, `ui`

**Body (Copy & Paste für GitHub):**

```markdown
## Summary
Beim Laden eines neuen Petrinetzes wird im P2T-Panel nur der Soundness-Check ausgeführt. Veraltete Fehlermeldungen werden entfernt; LLM-Textgenerierung nur noch manuell.

## Change
- `openDefaultSidebar()` → `showP2TBar(false)`
- Soundness-Check in `onSideBarShown`; Text leeren bei sound Netz ohne Auto-Generate

## Files
- `EditorPanel.java`, `P2TSideBar.java`
```
