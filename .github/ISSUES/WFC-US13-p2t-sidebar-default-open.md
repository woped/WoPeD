# WFC-US13: Sidebar, P2T-Panel und Editor-Fenstergröße beim Öffnen optimieren

## Status
Implementiert (Branch: `feature/wfc-us10-http500-fix`)

## Problem

Beim Öffnen eines Petrinetzes in WoPeD:

1. Die rechte **Sidebar** (Übersicht, Baumansicht) ist **standardmäßig eingeklappt**.
2. Der Bereich **„Prozess zu Text“** erscheint erst nach Klick auf **„In Text umwandeln“** im Ribbon.
3. **Buttons** (Neu laden, Export) und **Anbieter-Zeile** (`LLM | Anbieter: OpenAI`) fehlen initial, obwohl sie Teil des P2T-Panels sind.
4. Das **Editor-Fenster** ist oft **zu klein** (z. B. `Example-Workflow.pnml` speichert `654×267` in den PNML-Bounds) und nutzt nur einen kleinen Teil des verfügbaren Desktop-Bereichs.

Gespeicherte PNML-Dateien mit `treeWidth=1` (z. B. `Example-Workflow.pnml`) verstärken das Verhalten, weil die Sidebar beim Import auf „zu“ gesetzt wird.

## Erwartetes Verhalten

- Beim Öffnen eines Petrinetzes ist die Sidebar **sichtbar** (Übersicht + Baumansicht).
- Wenn **Process2Text** in den NLP-Tools aktiviert ist, ist der Bereich **„Prozess zu Text“** direkt sichtbar mit:
  - Reload-Button
  - Export-Button
  - Anbieter-Label (`LLM | Anbieter: …`)
- Automatische LLM-Generierung nur bei **explizitem** Aufruf über Ribbon/Dialog („In Text umwandeln“), nicht beim bloßen Öffnen des Netzes.
- Das Editor-Fenster nutzt beim Öffnen ca. **85 %** der verfügbaren Desktop-Fläche (mindestens 900×600).

## Lösung

- `EditorPanel.openDefaultSidebar()` – öffnet Sidebar und P2T-Panel nach Editor-Erstellung und PNML-Import.
- `EditorPanel.applyDefaultFrameSize()` – setzt die Internal-Frame-Größe auf 85 % des `JDesktopPane` (Fallback: Bildschirmgröße).
- `setSavedLayoutInfo()` – behandelt `treeWidth <= 1` als eingeklappt und nutzt Standard-Position.
- `P2TSideBar.onSideBarShown(visible, autoGenerate)` – Buttons/Anbieter sofort; LLM nur bei `autoGenerate=true`.

## Betroffene Dateien

- `WoPeD-Editor/.../EditorPanel.java`
- `WoPeD-QualAnalysis/.../P2TSideBar.java`
- `WoPeD-FileInterface/.../PNMLImport.java`

## Testplan

- [ ] `Example-Workflow.pnml` öffnen → Sidebar sofort sichtbar
- [ ] P2T-Bereich mit Buttons und Anbieter-Zeile sichtbar (ohne vorherigen Ribbon-Klick)
- [ ] Kein automatischer LLM-Aufruf beim reinen Öffnen
- [ ] „In Text umwandeln“ → Dialog → Generierung startet wie bisher
- [ ] Reload-Button in P2T-Panel funktioniert
- [ ] Subprozess-Editor: Sidebar wird nicht automatisch geöffnet
- [ ] `Example-Workflow.pnml` öffnen → Fenster nutzt ~85 % des Desktop-Bereichs (nicht mehr 654×267)

## GitHub Issue

**Titel:** `WFC-US13: Sidebar, P2T-Panel und Editor-Fenstergröße beim Öffnen optimieren`

**Labels:** `enhancement`, `ui`, `p2t`

**Body (Copy & Paste für GitHub):**

```markdown
## Summary
Beim Öffnen eines Petrinetzes soll das Editor-Fenster den verfügbaren Platz besser nutzen und die Sidebar inkl. P2T-Bereich sofort sichtbar sein.

## Problems
- Sidebar standardmäßig eingeklappt
- P2T-Buttons und Anbieter erst nach Ribbon-Klick sichtbar
- Gespeicherte PNML-Bounds (z. B. 654×267) erzeugen sehr kleine Fenster

## Solution
- `openDefaultSidebar()` + `applyDefaultFrameSize()` (85 % Desktop, min. 900×600)
- P2T-Panel initial mit Buttons/Anbieter, LLM nur bei explizitem Aufruf

## Files
- `EditorPanel.java`, `P2TSideBar.java`, `PNMLImport.java`
```
