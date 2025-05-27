package it.unisa.software_architecture_design.drawsnapdrawingtool.interactionstate;

import it.unisa.software_architecture_design.drawsnapdrawingtool.enumeration.Forme;
import it.unisa.software_architecture_design.drawsnapdrawingtool.forme.AttributiForma;
import javafx.application.Platform;

import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.ApplicationTest;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;


/**
 * Classe di test automatica per la classe {@link DrawState}, focalizzata
 * sul metodo {@code helpUIHandleMousePressed(Forme)} che mostra una finestra
 * di dialogo JavaFX per la selezione dei colori.
 * <p>
 * Utilizza TestFX per simulare l'interazione dell'utente.
 */
@ExtendWith(ApplicationExtension.class)
public class DrawStateTestUI extends ApplicationTest {

    private DrawState drawState;

    /**
     * Inizializza lo stato con una forma di tipo RETTANGOLO
     * prima di ogni test.
     */
    @BeforeEach
    void setUp() {
        drawState = new DrawState(Forme.RETTANGOLO) {
            @Override
            public AttributiForma helpUIHandleMousePressed(Forme tipoForma) {
                return super.helpUIHandleMousePressed(tipoForma);
            }
        };
    }

    /**
     * Lancia una finestra invisibile per avviare il toolkit JavaFX.
     *
     * @param stage stage principale richiesto da TestFX (non usato nel test)
     */
    @Override
    public void start(Stage stage) {
        stage.show();
    }

    /**
     * Testa il metodo {@code helpUIHandleMousePressed} della classe {@link DrawState}
     * verificando che, in seguito alla conferma della finestra di dialogo per la selezione
     * degli attributi di un rettangolo, vengano restituiti correttamente gli attributi scelti.
     * @throws Exception se si verifica un'interruzione durante l'attesa del completamento
     *                   del thread JavaFX o altre eccezioni durante il test.
     */
    @Test
    void testHelpUIHandleMousePressed_Rettangolo() throws Exception {
        CountDownLatch latch = new CountDownLatch(1);
        final AttributiForma[] result = new AttributiForma[1];

        Platform.runLater(() -> {
            try {
                DrawState state = new DrawState(Forme.RETTANGOLO);
                result[0] = state.helpUIHandleMousePressed(Forme.RETTANGOLO);
            } finally {
                latch.countDown();
            }
        });

        if (!latch.await(10, TimeUnit.SECONDS)) {
            fail("Timeout scaduto per l'interazione con JavaFX");
        }

        AttributiForma attributi = result[0];

        assertNotNull(attributi, "La finestra di dialogo non ha restituito attributi");
        assertNotNull(attributi.getColore(), "Il colore del bordo non deve essere nullo");
        assertNotNull(attributi.getColoreInterno(), "Il colore interno non deve essere nullo");

        assertEquals(100.0, attributi.getAltezza(), 0.01, "Valore di altezza errato");
        assertEquals(100.0, attributi.getLarghezza(), 0.01, "Valore di larghezza errato");
        assertEquals(0.0, attributi.getAngoloInclinazione(), 0.01, "Valore di angolo errato");
    }

    /**
     * Testa il metodo {@code helpUIHandleMousePressed} della classe {@link DrawState}
     * verificando che, in seguito alla conferma della finestra di dialogo per la selezione
     * degli attributi di una linea, vengano restituiti correttamente gli attributi scelti.
     * @throws Exception se si verifica un'interruzione durante l'attesa del completamento
     *                   del thread JavaFX o altre eccezioni durante il test.
     */
    @Test
    void testHelpUIHandleMousePressed_Linea() throws Exception {
        CountDownLatch latch = new CountDownLatch(1);
        final AttributiForma[] result = new AttributiForma[1];

        Platform.runLater(() -> {
            try {
                DrawState state = new DrawState(Forme.LINEA);
                result[0] = state.helpUIHandleMousePressed(Forme.LINEA);
            } finally {
                latch.countDown();
            }
        });

        if (!latch.await(10, TimeUnit.SECONDS)) {
            fail("Timeout scaduto per l'interazione con JavaFX");
        }

        AttributiForma attributi = result[0];

        assertNotNull(attributi, "La finestra di dialogo non ha restituito attributi");
        assertNotNull(attributi.getColore(), "Il colore del bordo non deve essere nullo");
        assertEquals(Color.TRANSPARENT, attributi.getColoreInterno(), "Il colore interno di una linea deve essere trasparente");
        assertEquals(0.0, attributi.getAltezza(), 0.01, "L'altezza di una linea deve essere 0");

        assertEquals(100.0, attributi.getLarghezza(), 0.01, "Larghezza non corretta");
        assertEquals(0.0, attributi.getAngoloInclinazione(), 0.01, "Angolo di inclinazione non corretto");
    }


    /**
     * Testa il metodo {@code helpUIHandleMousePressed} della classe {@link DrawState}
     * quando l'utente preme il pulsante "Annulla" nella finestra di dialogo.
     * @throws Exception se si verifica un'interruzione durante l'attesa del completamento
     * del thread JavaFX o altre eccezioni durante il test.
     */
    @Test
    void testHelpUIHandleMousePressed_Annulla_Dialog() throws Exception {
        CountDownLatch latch = new CountDownLatch(1);
        final AttributiForma[] result = new AttributiForma[1];

        Platform.runLater(() -> {
            try {
                DrawState state = new DrawState(Forme.RETTANGOLO);
                result[0] = state.helpUIHandleMousePressed(Forme.RETTANGOLO);
            } finally {
                latch.countDown();
            }
        });

        if (!latch.await(10, TimeUnit.SECONDS)) {
            fail("Timeout scaduto per l'interazione con JavaFX");
        }

        AttributiForma attributi = result[0];
        assertNull(attributi, "Se si preme Annulla, il metodo deve restituire null");
    }

}
