# WFC-US34: P2T Standard-Prompt optimieren

## Status
Implementiert (Branch: `feature/wfc-us10-http500-fix`)

## Problem

Der bisherige Default-Prompt bezog sich auf BPMN, nannte keine WoPeD-Operatoren (XOR/AND) und konnte durch Beispiel-Aktivitäten (`register`, `clone`, …) falsche Beschreibungen erzeugen (z. B. bei `Example-Workflow.pnml`).

## Lösung

Neuer Default in `DefaultStaticConfiguration.DEFAULT_P2T_PROMPT`:
- Nur das gelieferte PNML beschreiben
- Exakte Transition-Labels und Operator-Semantik
- Schleifen chronologisch im Pfad
- Keine erfundenen Business-Regeln oder Schlusskommentare
- Phrase-IDs für WoPeD-Highlighting

## Betroffene Dateien

- `WoPeD-Core/.../DefaultStaticConfiguration.java`
- `WoPeD-Editor/.../ConfNLPToolsPanel.java`
- `WoPeD-GUI/.../Messages.properties`, `Messages_de.properties`

## Testplan

- [ ] `LoanApplication.pnml` — vollständige Beschreibung inkl. Schleife
- [ ] `Example-Workflow.pnml` — xor/and-Operatoren, kein Kreditantrag
- [ ] `LoanApplicationResources.pnml` — inkl. Rollen
- [ ] „Standard wiederherstellen“ in NLP-Tools setzt neuen Prompt
