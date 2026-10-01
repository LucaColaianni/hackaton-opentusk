# MòSalute

MòSalute accompagna una persona nel prossimo passaggio del percorso di accesso alle cure. Questa MVP dimostra il flusso **MMG → eventuale prescrizione → rinvio al CUP ASL Bari**. La decisione medica e l’esito selezionabile sono scenari fittizi; SPID non è integrato.

## Avvio locale

Prerequisiti: Docker Compose, Java 25 e Node.js/npm. Avvia i servizi in terminali separati.

1. Avvia PostgreSQL dal repository:

   ```sh
   docker compose up -d postgres
   ```

2. Applica la migrazione versionata e importa (o reimporta) il CSV verificato:

   ```sh
   ./database/migrate.sh
   ./database/import_open_data.sh
   ```

   Lo script importa le sole righe ASL Bari (`160114`) dal file regionale conservato in `database/data/`. Non duplica righe se eseguito di nuovo.

3. Avvia il backend:

   ```sh
   cd backend
   ./mvnw quarkus:dev
   ```

   L’API è disponibile su `http://localhost:8080/api/v1/pathway`; OpenAPI su `/q/openapi`.

4. In un altro terminale avvia il frontend:

   ```sh
   cd frontend
   npm run dev
   ```

   Apri `http://localhost:5173`. Se backend o database non sono disponibili, l’interfaccia mostra lo stato di errore e consente di riprovare.

Il datasource locale usa i valori generici già impostati in `compose.yaml` (`root`/`root`). Si possono sovrascrivere con `DB_USERNAME`, `DB_PASSWORD` e `DB_URL`.

## Caso demo e limiti

- Il profilo del MMG è fittizio e senza recapiti. Non cerca il medico, non accede a SPID e non raccoglie informazioni personali.
- La scelta fra “nessuna ricetta” e “ricetta indicata” simula l’esito del medico; non è una prescrizione né un consiglio clinico. La prima scelta termina con le indicazioni del professionista; la seconda rinvia al [CUP ufficiale ASL Bari](https://www.sanita.puglia.it/web/asl-bari/cup).
- Il CUP conferma struttura, data, ora, ticket e istruzioni. MòSalute non prenota e non conferma disponibilità.
- Il pannello open data mostra il monitoraggio aggregato storico, distinto dal percorso individuale. Non indica slot correnti o attese personali e non consiglia prestazioni o strutture.
- I collegamenti e i recapiti ufficiali vanno ricontrollati prima della demo: PugliaSalute ha annunciato una migrazione del portale il 6 ottobre 2026.

## Open data

Il CSV della Regione Puglia copre la settimana **7–11 ottobre 2024**; la distribuzione risulta modificata il 3 gennaio 2025, mentre i metadati del dataset sono stati aggiornati il 26 settembre 2026. Queste date si riferiscono a elementi diversi. La licenza è CC BY 4.0. Il CSV è codificato Windows-1252; l’import importa 69 righe per ASL Bari, identificata dal codice `160114`.

Il catalogo non descrive tutte le colonne numeriche. La UI mantiene le etichette vicine ai campi originali, lascia vuoti come `—` e non interpreta `TMAX` come previsione o unità di tempo. Fonte, titolare, distribuzione, date, checksum e limiti sono riportati in [`database/DATA_SOURCES.md`](database/DATA_SOURCES.md).

## Struttura

- `backend/`: API REST Quarkus.
- `frontend/`: interfaccia React/Vite.
- `database/migrations/`: migrazioni SQL versionate.
- `database/data/`: copia del CSV ufficiale importato.
- `database/import_open_data.sh`: import ripetibile del solo sottoinsieme ASL Bari.
