package it.opentusk.pathway;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import java.util.List;

@Path("/api/v1/pathway")
@ApplicationScoped
@Produces(MediaType.APPLICATION_JSON)
public class PathwayResource {

    private static final String CUP_URL = "https://www.sanita.puglia.it/web/asl-bari/cup";

    @Inject
    WaitTimeRepository waitTimeRepository;

    @GET
    public PathwayResponse getPathway() {
        var doctor = new PathwayResponse.DoctorProfile(
                "Medico di famiglia — profilo dimostrativo",
                "MMG",
                "ASL Bari (scenario demo)",
                true,
                "Profilo fittizio, senza recapiti. SPID e i dati del medico reale non sono collegati in questa demo.");

        var steps = List.of(
                new PathwayResponse.PathwayStep(
                        "contact-mmg",
                        "Contatta il tuo medico di famiglia",
                        "Nella vita reale useresti i recapiti che ti ha fornito il tuo MMG. Porta la tessera sanitaria.",
                        "Persona assistita e MMG",
                        List.of("Tessera sanitaria"),
                        "Il profilo mostrato qui è solo dimostrativo e non fornisce un contatto reale."),
                new PathwayResponse.PathwayStep(
                        "medical-assessment",
                        "Il medico valuta la richiesta",
                        "Il medico decide se bastano indicazioni o cure di propria competenza oppure se serve un altro passaggio. Non tutte le richieste richiedono una ricetta specialistica.",
                        "MMG",
                        List.of(),
                        "La decisione appartiene al professionista; MòSalute non valuta sintomi o urgenza."),
                new PathwayResponse.PathwayStep(
                        "prescription-if-indicated",
                        "Ricetta, solo se indicata dal medico",
                        "Se prescrive una prestazione SSN, chiedi i riferimenti della ricetta/promemoria e le eventuali istruzioni direttamente al medico.",
                        "Professionista prescrittore",
                        List.of("Ricetta o promemoria", "Tessera sanitaria", "Codice fiscale"),
                        "La ricetta e le istruzioni non vengono create o modificate dall'app."));

        var outcomes = List.of(
                new PathwayResponse.DemoOutcome(
                        "no-prescription",
                        "Nel caso demo: nessuna ricetta",
                        "Esito simulato. Nella realtà segui le indicazioni ricevute dal tuo medico.",
                        "Il supporto MòSalute termina qui; per altri passi rivolgiti al professionista.",
                        false),
                new PathwayResponse.DemoOutcome(
                        "prescription-indicated",
                        "Nel caso demo: ricetta indicata",
                        "Esito simulato. Se nella realtà hai una prescrizione, prosegui con il canale CUP indicato dal prescrittore.",
                        "Tieni a portata di mano tessera sanitaria, codice fiscale e ricetta/promemoria. Il CUP conferma sede, data, ora, eventuale ticket e istruzioni.",
                        true));

        var cup = new PathwayResponse.CupReference(
                "CUP — ASL Bari",
                CUP_URL,
                "ASL Bari",
                "2026-10-01",
                List.of("Tessera sanitaria", "Codice fiscale", "Ricetta o promemoria con NRE, se presente"),
                "MòSalute rimanda alla pagina ufficiale: non invia richieste, non prenota e non conferma disponibilità.");

        return new PathwayResponse(
                doctor,
                steps,
                outcomes,
                cup,
                waitTimeRepository.loadAslBariContext());
    }
}
