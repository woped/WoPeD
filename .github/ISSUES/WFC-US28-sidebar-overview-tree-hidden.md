# WFC-US28: Übersicht und Baumansicht standardmäßig ausblenden

## Status
Implementiert (Branch: `feature/wfc-us10-http500-fix`)

## Problem

Beim Öffnen eines Petrinetzes waren **Übersicht** und **Baumansicht** bisher sichtbar und nahmen Platz weg. Gespeicherte PNML-Layouts konnten die Panels zusätzlich wieder einblenden.

Nach Umstellung auf „standardmäßig ausgeblendet“ blieb beim erneuten Aktivieren über **Ansicht → Seitenleiste** die rechte Spalte eingeklappt: Die Checkbox war gesetzt, aber die Seitenleiste hatte keine sinnvolle Breite.

## Erwartetes Verhalten

- Beim Öffnen eines Netzes sind **Übersicht** und **Baumansicht** ausgeblendet (Checkboxen deaktiviert).
- Gespeicherte PNML-Sichtbarkeit für diese Panels wird ignoriert.
- Aktiviert der Nutzer **Übersicht** oder **Baumansicht** im Ribbon, klappt die Seitenleiste automatisch auf die **Standardbreite** (~320 px, `EditorSize.SIDEBAR_WIDTH`) auf — analog zum P2T-Panel.

## Lösung

- `openDefaultSidebar()` / `setSavedLayoutInfo()` — Panels beim Öffnen erzwingen auf `false`.
- `expandMainSplitPaneForSidebar()` — setzt beim Einblenden per Ribbon die Teilerposition über `resolveSidebarDividerLocation()` statt `getLastDividerLocation()` (letzter Wert war nach dem Ausblenden „zugeklappt“).
- Aufruf in `setOverviewPanelVisible(true)` und `setTreeviewPanelVisible(true)`, wenn keine andere Sidebar (Analyse, Metriken, P2T) aktiv ist.

## Betroffene Dateien

- `WoPeD-Editor/.../EditorPanel.java`
- `WoPeD-FileInterface/.../PNMLImport.java` (gespeicherte Panel-Sichtbarkeit)

## Testplan

- [ ] Netz öffnen → Übersicht und Baumansicht ausgeblendet
- [ ] **Ansicht → Seitenleiste → Übersicht** aktivieren → Seitenleiste rechts mit Standardbreite sichtbar
- [ ] Ausblenden und **Baumansicht** aktivieren → gleiches Verhalten
- [ ] Beide Panels nacheinander aktivieren → Seitenleiste bleibt auf Standardbreite
- [ ] P2T-Panel parallel geöffnet → Übersicht/Baumansicht erscheinen im oberen Bereich der rechten Spalte

## GitHub Issue

**Titel:** `WFC-US28: Übersicht und Baumansicht standardmäßig ausblenden`

**Labels:** `enhancement`, `ui`

**Body (Copy & Paste für GitHub):**

```markdown
## Summary
Übersicht und Baumansicht sind beim Netz-Öffnen standardmäßig ausgeblendet. Beim erneuten Einblenden über das Ribbon klappt die Seitenleiste automatisch auf die Standardbreite (~320 px) auf.

## Solution
- Panels beim Öffnen/PNML-Import auf ausgeblendet setzen
- `expandMainSplitPaneForSidebar()` nutzt `resolveSidebarDividerLocation()` beim Ribbon-Toggle

## Files
- `EditorPanel.java`, `PNMLImport.java`
```
