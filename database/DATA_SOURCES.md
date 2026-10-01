# Provenienza dei dati

## Monitoraggio tempi di attesa ex ante

- **Scheda:** https://dati.puglia.it/v2/dataset/monitoraggio-tempi-di-attesa
- **Distribuzione importata:** https://dati.puglia.it/ckan/dataset/8d6b91a6-9575-4dba-b4f0-f8771ce08825/resource/26096f59-111e-41ec-a726-281d1dd2dbdf/download/monitoraggio-tempi-di-attesa-07_11-ottobre-2024.csv
- **Ente titolare:** Regione Puglia — Ufficio Sistemi Informativi e Flussi Informativi.
- **Licenza:** Creative Commons Attribuzione 4.0 Internazionale (CC BY 4.0).
- **Copertura:** Regione Puglia. Il dataset dichiara copertura 13/07/2020–11/10/2024; la distribuzione importata riguarda la settimana 07–11/10/2024.
- **Ultima modifica dei dati/distribuzione:** 03/01/2025. **Ultimo aggiornamento dei metadati del dataset:** 26/09/2026. Le date descrivono oggetti diversi.
- **Campi utilizzati:** ASL, anno, settimana, identificativo/descrizione/codice prestazione, prenotazioni e colonne `PRENOTAZIONI_DAGARANTIRE_*` con relativi `TMAX`. Il dizionario CKAN non descrive il significato o l'unità di tutte le colonne numeriche: nell'interfaccia sono esposti con etichette vicine ai nomi originali e non vengono interpretati come previsione individuale.
- **Verifica download e contenuto:** 01/10/2026. CSV Windows-1252, 414 righe, una settimana di riferimento; SHA-256 `03115c0c5396f62d728334337e10c986151109c921cda69132ced45f60807f2e`.
- **Filtro demo:** codice ASL `160114` (ASL BA, Bari), corrispondente all'anagrafe ASL regionale. Importate 69 righe disponibili per questo codice. I campi numerici vuoti diventano `NULL`; un identificativo prestazione vuoto resta stringa vuota perché fa parte della chiave di deduplicazione.
- **Limiti:** estrazione aggregata storica dai sistemi CUP aziendali; non contiene agende correnti, disponibilità, appuntamenti né attese individuali. Non usare per scegliere una sede, suggerire prestazioni o stimare una prenotazione.
- **Importazione:** `database/import_open_data.sh`; il processo è idempotente sulla chiave fonte/ASL/anno/periodo/prestazione.

## Crosswalk del codice ASL Bari

- **Scheda:** https://dati.puglia.it/ckan/dataset/anagrafe-asl
- **Ente titolare/licenza:** Regione Puglia; Italian Open Data License 2.0.
- **Uso limitato:** associare il codice `160114` a `ASL BA`/Bari. La distribuzione anagrafica è storica (copertura dichiarata 2015–2017) e non viene importata né usata come directory di servizi o strutture correnti.
- **Verifica:** 01/10/2026.

## Riferimento operativo CUP

- **Pagina ufficiale:** https://www.sanita.puglia.it/web/asl-bari/cup
- **Ente titolare:** ASL Bari.
- **Verifica del collegamento:** 01/10/2026. La pagina e i recapiti possono cambiare; ricontrollarli prima della demo. MòSalute rimanda al canale ufficiale e non prenota.
