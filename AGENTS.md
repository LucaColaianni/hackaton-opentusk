# MòSalute — istruzioni per gli agenti

## Contesto e obiettivo

Questo repository contiene MòSalute, progetto per l'hackathon OpenTusk. L'app accompagna una persona lungo il percorso di accesso alle cure nel Servizio sanitario nazionale (SSN), dall'assenza di una prescrizione fino all'avvio di una cura indicata da professionisti sanitari.

L'obiettivo dell'hackathon è un MVP funzionante e dimostrabile. Proteggere il tempo della demo, scegliere un percorso piccolo e coerente, e non promettere funzioni che fonti e integrazioni non supportano.

## Percorso assistenziale di riferimento

Il flusso parte da una persona che non ha ancora una prescrizione. Il percorso ordinario da rappresentare è:

1. **Primo contatto con il medico di famiglia.** La persona contatta il proprio medico di medicina generale (MMG) — o il pediatra di libera scelta per i minori — attraverso i recapiti e le modalità ufficiali del proprio medico. Porta la tessera sanitaria e descrive al medico il motivo della richiesta. L'app può aiutare a trovare i riferimenti ufficiali, ma non deve valutare sintomi né stabilire l'urgenza.
2. **Valutazione del medico.** Il medico decide, nell'ambito delle proprie competenze, se bastano indicazioni o cure di propria competenza, se servono accertamenti, una visita specialistica o un altro percorso. Non tutte le richieste generano una ricetta specialistica.
3. **Prescrizione, se indicata.** Il medico abilitato rilascia la ricetta SSN con le informazioni necessarie, comprese priorità e quesito diagnostico quando previste. Il paziente riceve i riferimenti della ricetta elettronica o il promemoria e verifica con il medico eventuali istruzioni preparatorie.
4. **Prenotazione della prestazione.** Il paziente usa i canali ufficiali del CUP della propria Regione/ASL o quelli indicati dal prescrittore. Tiene a disposizione tessera sanitaria, ricetta/promemoria e codice fiscale. Il CUP conferma struttura, data, ora, eventuale ticket e istruzioni. L'app può indirizzare a canali verificati; non effettua né conferma la prenotazione.
5. **Esecuzione e referto.** Il paziente si presenta con i documenti e la preparazione indicata dalla struttura. Ritira o consulta il referto attraverso i canali ufficiali. La prestazione può essere una visita, un esame o un trattamento ambulatoriale.
6. **Valutazione successiva e decisione clinica.** Il paziente segue le indicazioni del professionista che ha eseguito la prestazione e, quando richiesto, torna dal MMG o dallo specialista con il referto. Il professionista decide i passi successivi: nessun altro intervento, controllo, ulteriori accertamenti, invio ad altro servizio o proposta di cura.
7. **Avvio della cura indicata.** Se viene proposta una cura, il professionista chiarisce modalità e passaggi. Possono servire una prescrizione o un piano terapeutico, una nuova prenotazione tramite il canale competente, il ritiro di un farmaco, una procedura o un percorso riabilitativo. Il paziente segue le istruzioni e i canali ufficiali fino all'avvio della cura. Il percorso termina quando l'avvio è stato concordato o effettuato secondo le indicazioni del professionista; non implica guarigione o completamento di un ciclo terapeutico.

Questo è un riferimento, non una sequenza obbligatoria uguale per tutti. Accesso diretto, prestazioni successive, esenzioni, ticket, priorità e modalità di prenotazione dipendono dalla prestazione, dalla prescrizione e dalle regole regionali. Le cure urgenti e le emergenze seguono canali distinti: l'app non fa triage; per emergenze si deve fare riferimento ai numeri e ai servizi ufficiali di emergenza.

Fonti nazionali di riferimento (ricontrollare prima di descrivere regole operative):

- [Ministero della Salute — Assistenza specialistica ambulatoriale](https://www.salute.gov.it/new/it/tema/livelli-essenziali-di-assistenza/assistenza-specialistica-ambulatoriale/)
- [Ministero della Salute — Chi può rilasciare la ricetta medica](https://www.salute.gov.it/new/it/tema/ricetta-elettronica-e-cup/chi-puo-rilasciare-la-ricetta-medica/)
- [Ministero della Salute — Ricetta elettronica e CUP](https://www.salute.gov.it/new/it/tema/ricetta-elettronica-e-cup/)
- [Ministero della Salute — FAQ liste di attesa](https://www.salute.gov.it/new/it/faq/faq-liste-di-attesa/)
- [Ministero della Salute — Ticket ed esenzioni](https://www.salute.gov.it/new/it/tema/esenzioni-dal-ticket/ticket-ed-esenzioni/)

## Ambito dell'MVP

Il percorso completo sopra descritto è l'orizzonte del prodotto; per l'hackathon va dimostrata una sola verticale circoscritta. Prima di implementare, scegliere e documentare in README una combinazione sostenibile di area, prestazione/percorso e canale ufficiale, basandosi sulle fonti effettivamente disponibili. Preferire Puglia/Bari solo se le fonti lo consentono.

La demo deve rendere chiari:

- cosa fare per il prossimo passo e chi contattare;
- quali dati o documenti tenere a portata di mano;
- quale parte del percorso è coperta e quale va proseguita presso MMG, CUP o struttura;
- fonte, ente titolare, data di aggiornamento disponibile e limiti di ogni informazione mostrata.

Non presentare una struttura come erogatrice di una prestazione senza una fonte che colleghi esplicitamente struttura e prestazione. In assenza di tale prova, descriverla come riferimento territoriale da verificare. Non inventare disponibilità, tempi di attesa, prescrizioni, indicazioni preparatorie o esiti.

## Fuori scope e confini clinici

- Diagnosi, triage, valutazione di sintomi o urgenza, scelta di esami, strutture o terapie e consigli clinici personalizzati.
- Emissione o modifica di ricette, gestione di appuntamenti, pagamenti, accesso ai sistemi CUP o ai sistemi sanitari senza integrazione ufficiale autorizzata.
- Raccolta di credenziali, dati personali o sanitari non necessari; account, profili, fascicolo sanitario e persistenza di dati sanitari.
- Chat AI e copertura nazionale completa.
- Directory separate di farmacie, medici e ospedali che non sostengono la verticale scelta.

Non chiamare “prenotazione completata” un rinvio al CUP. Non chiamare “cura avviata” una semplice informazione o un link. Se il percorso dipende da una decisione clinica, esplicitarlo e rimandare al professionista.

## Struttura del monorepo

```text
hackaton-opentusk/
├── AGENTS.md
├── backend/     # API REST e import/normalizzazione dati — Quarkus
├── frontend/    # interfaccia del percorso — React
└── database/    # schema/migrazioni, seed e provenienza dataset — PostgreSQL
```

Rispettare queste directory. Preferire pochi moduli e dipendenze; aggiungere tecnologie o servizi solo se indispensabili alla demo. Configurazioni e segreti vanno in variabili d'ambiente; non committare credenziali o dati personali.

## Dati e fonti

Il portale [dati.gov.it](https://www.dati.gov.it/) è un punto di scoperta; le distribuzioni effettive possono risiedere sui siti degli enti titolari. I seguenti dataset sono candidati individuati per la copertura pugliese. Prima di ogni utilizzo, ricontrollare scheda, distribuzione e contenuto effettivamente scaricabile: un aggiornamento del catalogo non implica che siano stati aggiornati i dati sottostanti.

| Dataset | Ente e copertura | Formato, licenza e aggiornamento dichiarato | Uso possibile e limiti |
|---|---|---|---|
| [Anagrafe strutture sanitarie](https://dati.puglia.it/v2/dataset/anagrafe-strutture-sanitarie) | Regione Puglia; strutture regionali | CSV, XML, XSD e ODS; IODL 2.0; aggiornamento dichiarato annuale. La scheda riporta dati con copertura temporale 1/11/2015–31/07/2016 e ultima modifica dei dati 27/10/2015. | Nomi, tipologie e indirizzi possono servire da riferimento territoriale storico. Non usare per affermare che una sede sia ancora attiva o eroghi una specifica prestazione senza riscontro ufficiale aggiornato. La risorsa CSV è consultabile tramite catalogo/API CKAN. |
| [Monitoraggio tempi di attesa](https://dati.puglia.it/v2/dataset/monitoraggio-tempi-di-attesa) | Regione Puglia; estrazioni aggregate dai sistemi CUP aziendali | CSV, XML, XSD e ODS; CC BY 4.0; frequenza dichiarata trimestrale. La scheda indica copertura delle risorse dal 13/07/2020 all'11/10/2024, anche se il catalogo può mostrare aggiornamenti dei metadati successivi. | Statistiche storiche aggregate per ASL, periodo, prestazione e priorità. Non rappresenta agende, disponibilità correnti o il tempo che un singolo paziente otterrà prenotando oggi. Non usarlo per consigliare una sede sulla base di uno slot attuale. |
| [Monitoraggio ex post delle attese](https://dati.puglia.it/ckan/dataset/monitoraggio-tempi-di-attesa-ex-post-riferiti-alle-erogazioni-di-prestazioni-ambulatoriali) | Regione Puglia; prestazioni ambulatoriali erogate | CSV; il catalogo riporta aggiornamento 25/12/2024. Verificare licenza, periodo e data della singola risorsa prima dell'uso. | Analisi storica aggregata dei tempi effettivi. Non è una previsione né una disponibilità di prenotazione attuale. |
| [Elenco ASL e strutture di ricovero](https://www.salute.gov.it/new/it/banche-dati/elenco-aziende-sanitarie-locali-e-strutture-di-ricovero/) | Ministero della Salute, fonte NSIS; copertura nazionale | Elenchi in XLSX e ODS; la pagina riporta anni di riferimento 2010–2026 per le ASL e strutture di ricovero attive al 01/03/2023. La licenza e la data della distribuzione vanno verificate sulla pagina prima dell'import. | Riferimento per denominazioni, codici e indirizzi di ASL e ospedali. La lista ospedaliera non conferma l'offerta di una prestazione specialistica ambulatoriale presso una sede. |

La Regione Puglia pubblica anche il dataset [Continuità Assistenziale](https://dati.puglia.it/ckan/dataset/continuit-assistenziale-innovapuglia), in CSV/XML con licenza IODL 2.0, ma la scheda indica copertura temporale 01/08/2016–31/07/2017 e modifica dei dati del 28/07/2016. Non usarlo come elenco corrente di sedi o recapiti.

Il catalogo regionale delle prestazioni specialistiche reperito in PDF è un documento di riferimento, non un dataset aperto strutturato; la versione individuata è del 2022. Non usarlo da solo per collegare una prestazione a una struttura o dichiarare l'offerta attuale.

Per trovare il proprio MMG, ottenere contatti CUP aggiornati e seguire istruzioni operative, preferire le pagine ufficiali aggiornate di Regione, ASL o struttura. In questa ricognizione non è stato identificato un open dataset pugliese aggiornato sufficiente a fornire l'elenco corrente dei MMG con i relativi contatti o gli slot CUP. Registrare tale assenza come limite; non sostituirla con recapiti inferiti o con dati storici.

Prima di importare qualsiasi dataset, registrare in `database/DATA_SOURCES.md`: URL della scheda e della distribuzione usata, ente titolare, licenza, copertura geografica e temporale, campi utili, data di aggiornamento dei dati (distinta da quella dei metadati), disponibilità del download, data di verifica/import e limiti noti. Conservare per ogni record importato identificativo di origine, URL della fonte, ente, data di aggiornamento del dataset e data di import. Usare migrazioni versionate. Dati fittizi, se indispensabili alla demo, devono essere separati e marcati chiaramente come tali.

Non presumere che i dataset contengano prenotazioni, disponibilità o informazioni correnti su CUP, MMG, farmacie e strutture. Non presentare statistiche storiche sui tempi di attesa come previsioni o slot disponibili. Non dedurre che una struttura eroghi una prestazione specialistica dalla sola presenza nell'anagrafe: serve una fonte aggiornata che associ esplicitamente prestazione e struttura.

## Contratto fra frontend, backend e dati

- **Database:** conservare provenienza e date dei dati importati; usare uno schema minimo adatto alla verticale scelta.
- **Backend:** esporre solo gli endpoint necessari a restituire il prossimo passo del percorso e i riferimenti verificati. Normalizzare i dati all'import; non esporre a React formati CSV/XML degli enti. Restituire provenienza e limiti insieme a ogni risultato pertinente.
- **Frontend:** un flusso mobile e accessibile con stato iniziale, caricamento, nessun risultato, errore e risultato. Usare linguaggio semplice in italiano; indicare sempre prossimo passo, confini del servizio e fonte ufficiale. Raccogliere solo informazioni indispensabili e non richiedere dettagli clinici liberi.
- **Coordinamento:** concordare il contratto JSON minimo prima di implementare le parti in parallelo. Se un design Figma esiste, usarlo come riferimento UI e segnalare subito discrepanze con dati e percorso realmente supportati.

## Modo di lavorare

1. Leggere repository e istruzioni locali prima di cambiare file; ricostruire il percorso verticale più breve dal primo contatto con il MMG a un prossimo passo concreto verso la cura.
2. Verificare fonti e dataset reali prima di decidere la copertura; registrare le fonti in `database/DATA_SOURCES.md` prima dell'import.
3. Concordare il caso dimostrativo e il contratto JSON minimo, poi implementare nell'ordine: import riproducibile, endpoint, schermata del percorso, rifinitura della demo.
4. Mantenere le modifiche piccole e focalizzate. Evitare infrastruttura, astrazioni e dipendenze che non riducono un rischio concreto della demo.
5. Prima della consegna, documentare in README avvio riproducibile, copertura, fonti, limiti e un esempio che arrivi al canale ufficiale. Quando il caso senza risultato fa parte della verticale implementata, mostrarlo senza inventare alternative.

## Definizione di completato

Una persona può capire come iniziare contattando il proprio MMG, quali passaggi dipendono dal medico, come proseguire attraverso un canale ufficiale verificato e dove termina il supporto dell'app. La demo documenta una copertura concreta, si avvia con istruzioni riproducibili e non simula prescrizioni, disponibilità o prenotazioni. Il traguardo del prodotto è l'avvio della cura indicata da un professionista, senza affermare che MòSalute scelga o fornisca la cura.
