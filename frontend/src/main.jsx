import { useEffect, useState } from 'react'
import { createRoot } from 'react-dom/client'
import './style.css'

const API_BASE_URL = import.meta.env.VITE_API_BASE_URL || 'http://localhost:8080'

function formatMetric(value) {
  return value === null || value === undefined ? '—' : value.toLocaleString('it-IT')
}

function App() {
  const [entered, setEntered] = useState(false)
  const [pathway, setPathway] = useState(null)
  const [selectedOutcome, setSelectedOutcome] = useState(null)
  const [error, setError] = useState('')

  useEffect(() => {
    if (!entered) return undefined
    let active = true
    fetch(`${API_BASE_URL}/api/v1/pathway`)
      .then((response) => {
        if (!response.ok) throw new Error(`Il servizio ha risposto con ${response.status}.`)
        return response.json()
      })
      .then((data) => { if (active) setPathway(data) })
      .catch(() => {
        if (active) setError('Non riesco a caricare il percorso. Verifica che backend e database siano avviati.')
      })
    return () => { active = false }
  }, [entered])

  const logout = () => {
    setSelectedOutcome(null)
    setPathway(null)
    setError('')
    setEntered(false)
  }

  if (!entered) return <LoginPage onEnter={() => setEntered(true)} />

  if (error) {
    return <main className="page-shell"><Header onLogout={logout} /><section className="notice notice-error" role="alert"><h1>Il percorso non è disponibile</h1><p>{error}</p><button className="button button-secondary" onClick={() => window.location.reload()}>Riprova</button></section></main>
  }
  if (!pathway) return <main className="page-shell"><Header onLogout={logout} /><p className="loading" role="status">Caricamento del percorso…</p></main>

  const outcome = pathway.outcomes.find((item) => item.id === selectedOutcome)

  return (
    <main className="page-shell">
      <Header onLogout={logout} />
      <section className="intro" aria-labelledby="page-title">
        <p className="eyebrow">Percorso SSN · ASL Bari</p>
        <h1 id="page-title">Il prossimo passo verso le cure</h1>
        <p className="intro-copy">Parti dal tuo medico di famiglia. Se prescrive una prestazione, ti mostriamo come proseguire attraverso il canale ufficiale.</p>
      </section>
      <section className="notice-banner" aria-label="Informazioni sul profilo"><strong>Profilo mock</strong><span>MòSalute non valuta sintomi o urgenza.</span></section>

      <div className="content-grid">
        <div className="journey-column">
          <section className="doctor-card" aria-labelledby="doctor-heading">
            <div className="doctor-avatar" aria-hidden="true">MMG</div>
            <div><p className="eyebrow">Medico associato</p><h2 id="doctor-heading">{pathway.doctor.displayName}</h2><p className="muted">{pathway.doctor.role} · {pathway.doctor.territory}</p><p className="card-note">{pathway.doctor.note}</p></div>
            <span className="mock-tag">MOCK</span>
          </section>

          <section aria-labelledby="steps-heading">
            <div className="section-heading"><div><p className="eyebrow">Dal primo contatto alla prescrizione</p><h2 id="steps-heading">Come funziona il percorso</h2></div><span className="step-count">{pathway.steps.length} passaggi</span></div>
            <ol className="steps-list">
              {pathway.steps.map((step, index) => (
                <li className="step" key={step.id}>
                  <span className="step-number" aria-hidden="true">{index + 1}</span>
                  <div className="step-content"><h3>{step.title}</h3><p>{step.action}</p>{step.documents.length > 0 && <p className="document-line"><strong>Da tenere a portata di mano:</strong> {step.documents.join(', ')}.</p>}<p className="step-boundary">{step.boundary}</p></div>
                </li>
              ))}
            </ol>
          </section>

          <section className="outcome-panel" aria-labelledby="outcome-heading">
            <p className="eyebrow">Esiti del medico</p><h2 id="outcome-heading">Cosa indica il medico?</h2><p className="muted">Gli esiti sono mock; nella realtà decide il professionista.</p>
            <div className="outcome-buttons">
              {pathway.outcomes.map((option) => <button className={`button ${selectedOutcome === option.id ? 'button-selected' : 'button-secondary'}`} key={option.id} onClick={() => setSelectedOutcome(option.id)} aria-pressed={selectedOutcome === option.id}>{option.label}</button>)}
            </div>
            {outcome && <div className={`outcome-result ${outcome.leadsToCup ? 'outcome-cup' : 'outcome-stop'}`} aria-live="polite"><p className="result-label">Esito selezionato</p><h3>{outcome.label}</h3><p>{outcome.message}</p><p className="result-next"><strong>Prossimo passo:</strong> {outcome.nextStep}</p>{outcome.leadsToCup && <CupCard cup={pathway.cup} />}</div>}
          </section>
        </div>
        <OpenDataPanel context={pathway.openDataContext} />
      </div>

      <footer className="page-footer"><p>Le decisioni cliniche restano al professionista. Per le emergenze, fai riferimento ai servizi ufficiali di emergenza.</p><p>MòSalute orienta al prossimo passaggio: non emette ricette, non accede al CUP e non prenota.</p></footer>
    </main>
  )
}

function LoginPage({ onEnter }) {
  return (
    <main className="login-shell">
      <Header />
      <div className="login-layout">
        <section className="login-intro">
          <p className="eyebrow">Orientamento nel percorso SSN</p>
          <h1>Accedi a MòSalute</h1>
          <p>Un punto di partenza chiaro per capire qual è il prossimo passaggio nel percorso di cura.</p>
        </section>
        <section className="login-card" aria-labelledby="login-heading">
          <div className="spid-mark" aria-hidden="true">SPID</div>
          <p className="eyebrow">Accesso personale</p>
          <h2 id="login-heading">Entra con SPID</h2>
          <p className="login-disclaimer">L’accesso è simulato: non inserire credenziali. Il pulsante apre MòSalute senza collegarsi a SPID.</p>
          <button className="spid-button" type="button" onClick={onEnter}>
            <svg className="spid-button-icon" viewBox="0 0 24 24" aria-hidden="true"><path d="M12 12a4.1 4.1 0 1 0 0-8.2 4.1 4.1 0 0 0 0 8.2Zm0 2c-4.3 0-7.8 2.2-7.8 4.9V21h15.6v-2.1c0-2.7-3.5-4.9-7.8-4.9Z" fill="currentColor"/></svg>
            <span>Entra con SPID</span>
          </button>
          <p className="login-footnote">Nessun dato viene richiesto o inviato.</p>
        </section>
      </div>
      <footer className="login-footer">MòSalute · Orientamento nel percorso SSN</footer>
    </main>
  )
}

function Header({ onLogout }) {
  return <header className="site-header"><a className="brand" href="#top" aria-label="MòSalute, inizio"><span className="brand-mark" aria-hidden="true">M</span><span>MòSalute</span></a><div className="header-actions"><span className="header-context">Orientamento nel percorso SSN</span>{onLogout && <button className="logout-button" type="button" onClick={onLogout}>Esci</button>}</div></header>
}

function CupCard({ cup }) {
  return <div className="cup-card"><div><p className="eyebrow">Canale ufficiale</p><h4>{cup.title}</h4><p className="muted">{cup.limitation}</p><p className="source-date">Fonte verificata il {cup.verifiedAt}</p></div><ul className="cup-documents">{cup.bringWithYou.map((document) => <li key={document}>{document}</li>)}</ul><a className="button button-primary" href={cup.url} target="_blank" rel="noreferrer">Apri la pagina CUP ASL Bari <span aria-hidden="true">↗</span></a></div>
}

function OpenDataPanel({ context }) {
  return (
    <aside className="data-panel" aria-labelledby="data-heading">
      <div className="data-panel-heading"><div><p className="eyebrow">Open data · contesto separato dal percorso</p><h2 id="data-heading">Monitoraggio storico · ASL Bari</h2></div><span className="data-period">{context.period}</span></div>
      <p className="data-summary">{context.available ? `${context.recordCount} righe del monitoraggio per ASL Bari.` : 'Nessun dato importato per ASL Bari.'}</p><p className="data-limitation">{context.limitation}</p>
      {context.available ? <details className="data-details"><summary>Esplora i dati aggregati</summary><div className="table-scroll" tabIndex="0" aria-label="Tabella dati storici, scorribile"><table><thead><tr><th scope="col">Prestazione</th><th scope="col">Codice</th><th scope="col">Prenotazioni</th><th scope="col">Da garantire</th><th scope="col">B · TMAX</th><th scope="col">D · TMAX</th><th scope="col">P · TMAX</th></tr></thead><tbody>{context.records.map((record) => <tr key={`${record.performanceId}-${record.code}`}><th scope="row">{record.description}</th><td>{record.code || '—'}</td><td>{formatMetric(record.reservations)}</td><td>{formatMetric(record.reservationsToGuarantee)}</td><td>{formatMetric(record.guaranteeB)} · {formatMetric(record.guaranteeBTmax)}</td><td>{formatMetric(record.guaranteeD)} · {formatMetric(record.guaranteeDTmax)}</td><td>{formatMetric(record.guaranteeP)} · {formatMetric(record.guaranteePTmax)}</td></tr>)}</tbody></table></div></details> : <p className="empty-data">Il percorso assistenziale resta consultabile; l’area dati si aggiorna quando il CSV viene importato.</p>}
      <div className="source-card"><p><strong>Fonte:</strong> {context.owner} · {context.license}</p><p><strong>Dati aggiornati al:</strong> {context.dataUpdatedAt || 'data non indicata'} · <strong>metadati verificati:</strong> {context.metadataVerifiedAt}</p><p>{context.importedAt ? `Importato il ${new Date(context.importedAt).toLocaleString('it-IT')}.` : 'CSV non ancora importato nel database.'}</p><a href={context.datasetUrl} target="_blank" rel="noreferrer">Scheda del dataset ↗</a><span className="source-separator">·</span><a href={context.distributionUrl} target="_blank" rel="noreferrer">Distribuzione CSV ↗</a></div>
    </aside>
  )
}

createRoot(document.getElementById('root')).render(<App />)
