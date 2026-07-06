# WFC-US47: P2T-Textbereich scrollbar machen

## Status
Implementiert (Branch: `feature/wfc-us10-http500-fix`)

## Problem

Bei langen P2T-Beschreibungen (z. B. `LoanApplication.pnml`) wird der generierte Text im Panel **„Prozess zu Text“** unten abgeschnitten. Der Nutzer kann den Rest nicht lesen, weil kein Scrollbalken vorhanden ist.

## Erwartetes Verhalten

- Lange P2T-Ausgaben sind im Sidebar-Panel **vertikal scrollbar**
- Buttons (Neu laden, Export) und Anbieter-Zeile bleiben oben sichtbar
- Hyperlinks zur Diagramm-Hervorhebung funktionieren weiterhin

## Lösung

- `P2TSideBar`: `JEditorPane` in `JScrollPane` mit `VERTICAL_SCROLLBAR_AS_NEEDED` einbetten

## Betroffene Dateien

- `WoPeD-QualAnalysis/.../P2TSideBar.java`

## Testplan

- [ ] `LoanApplication.pnml` → P2T generieren → gesamter Text per Scrollbalken erreichbar
- [ ] Klick auf Textpassage → Element im Netz wird hervorgehoben
- [ ] Kurzer Text → kein unnötiger Scrollbalken
- [ ] Fehlermeldung (nicht sound) → lesbar und scrollbar falls lang

## GitHub Issue

**Titel:** `WFC-US47: P2T-Textbereich scrollbar machen`

**Labels:** `enhancement`, `ui`, `p2t`

**Body (Copy & Paste für GitHub):**

```markdown
## Summary
Long Process-to-Text output in the sidebar was clipped without a scrollbar. Wrap the P2T editor in a JScrollPane so users can read the full description.

## Change
`P2TSideBar` — JEditorPane inside JScrollPane (vertical scroll as needed)

## Files
- `P2TSideBar.java`
```
