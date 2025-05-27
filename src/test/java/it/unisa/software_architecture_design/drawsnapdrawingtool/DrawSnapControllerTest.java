package it.unisa.software_architecture_design.drawsnapdrawingtool;

import it.unisa.software_architecture_design.drawsnapdrawingtool.forme.Forma;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DrawSnapControllerTest {

    /**
     * Parametro per l'inizializzazione del tool kit di javafx
     */
    private static boolean javaFxToolKitInitialized = false;

    /**
     * Prima di fare i test vi è bisogno di controllare che il tool kit di java fx sia caricato
     * Se non presente, non sarà possibile inizializzare il contesto grafico e interagire con le sue API
     * @throws IllegalStateException se il toolkit di JavaFX non si inizializza entro il tempo massimo specificato
     * @throws InterruptedException se l'attesa per l'inizializzazione fatta da {@code countDownLatch.await()}
     * viene interrotta
     */
    @BeforeAll
    public static void initJavaFX() throws Exception{
        if(!javaFxToolKitInitialized) {
            CountDownLatch countDownLatch = new CountDownLatch(1);
            Platform.startup(() -> {
                javaFxToolKitInitialized = true;
                countDownLatch.countDown();
            });
            if((!countDownLatch.await(5, TimeUnit.SECONDS))){
                throw new IllegalStateException("JavaFxToolKit not initialized");
            }
        }
    }

    /**
     * Test per verificare che il canvas e il GraphicContext siano stati inizializzati correttamente
     * Il CountDownLatch viene utilizzato per sincronizzare il metodo di inizializzazione con il test
     * Dobbiamo aspettare che javafx finisca l'inizializzazione prima di procedere con il test
     * @throws InterruptedException se l'attesa per l'inizializzazione fatta da {@code countDownLatch.await()}
     * viene interrotta
     */
    @Test
    void testCanvasInit() throws Exception{
        final boolean[] result = new boolean[1];
        CountDownLatch countDownLatch = new CountDownLatch(1);

        Platform.runLater(() -> {
            try{
                FXMLLoader loader = new FXMLLoader(getClass().getResource("DrawSnapView.fxml"));
                Parent root = loader.load();
                DrawSnapController controller = loader.getController();
                Field canvasField = DrawSnapController.class.getDeclaredField("canvas");
                canvasField.setAccessible(true);
                Canvas canvas = (Canvas) canvasField.get(controller);

                result[0] = (canvas != null && canvas.getGraphicsContext2D() != null);
            }catch(Exception e){
                e.printStackTrace();
                result[0] = false;
            }finally {
                countDownLatch.countDown();
            }
        });
        if((!countDownLatch.await(2, TimeUnit.SECONDS))){
            throw new IllegalStateException("JavaFX Toolkit non inizializzato entro 2 secondi");
        }

        assertTrue(result[0], "Errore, Canvas o GraphicContext non inizializzati correttamente");
    }

    /**
     * Test che verifica che quando viene invocato {@code redrawAll()},
     * tutte le forme presenti nella lista privata {@code forme} vengano ridisegnate
     * correttamente sul canvas.
     * @throws Exception in caso di errori
     */
    @Test
    void testRedrawAll_ShouldClearCanvas_DrawForme() throws Exception {
        CountDownLatch latch = new CountDownLatch(1);
        Platform.runLater(() -> {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("DrawSnapView.fxml"));
                Parent root = loader.load();
                DrawSnapController controller = loader.getController();

                GraphicsContext mockGc = mock(GraphicsContext.class);

                Field gcField = DrawSnapController.class.getDeclaredField("gc");
                gcField.setAccessible(true);
                gcField.set(controller, mockGc);

                Field canvasField = DrawSnapController.class.getDeclaredField("canvas");
                canvasField.setAccessible(true);
                Canvas canvas = new Canvas(500, 500);
                canvasField.set(controller, canvas);

                Field formeField = DrawSnapController.class.getDeclaredField("forme");
                formeField.setAccessible(true);
                DrawSnapModel mockModel = mock(DrawSnapModel.class);
                Forma f = mock(Forma.class);
                when(mockModel.getIteratorForme()).thenReturn(List.of(f).iterator());
                formeField.set(controller, mockModel);


                controller.redrawAll();

                // Verifica che venga pulito il canvas
                verify(mockGc).clearRect(0, 0, canvas.getWidth(), canvas.getHeight());

                verify(f).disegna(mockGc);

            } catch (Exception e) {
                fail("Eccezione durante il test: " + e.getMessage());
            } finally {
                latch.countDown();
            }
        });

        if (!latch.await(5, TimeUnit.SECONDS)) {
            throw new IllegalStateException("Test non completato entro il timeout");
        }
    }





    /**
     * Questo test verifica che il controller, quando chiama {@code redrawAll()} se il parametro
     * {@code gridVisible} è messo a {@code true} allora viene disegnata più di una volta
     * una linea per il disegno della griglia.
     * @throws Exception in caso di errori
     */
    @Test
    void testDrawGrid_WhenGridVisible() throws Exception {
        CountDownLatch latch = new CountDownLatch(1);
        Platform.runLater(() -> {
           try{
               FXMLLoader loader = new FXMLLoader(getClass().getResource("DrawSnapView.fxml"));
               Parent root = loader.load();
               DrawSnapController controller = loader.getController();

               GraphicsContext mockGc = mock(GraphicsContext.class);

               Field gcField = DrawSnapController.class.getDeclaredField("gc");
               gcField.setAccessible(true);
               gcField.set(controller, mockGc);

               Field canvasField = DrawSnapController.class.getDeclaredField("canvas");
               canvasField.setAccessible(true);
               Canvas canvas = new Canvas(500, 500);
               canvasField.set(controller, canvas);

               Field formeField = DrawSnapController.class.getDeclaredField("forme");
               formeField.setAccessible(true);
               DrawSnapModel mockModel = mock(DrawSnapModel.class);
               when(mockModel.getIteratorForme()).thenReturn(Collections.emptyIterator());
               formeField.set(controller, mockModel);

               Field gridVisibleField = DrawSnapController.class.getDeclaredField("gridVisible");
               gridVisibleField.setAccessible(true);
               gridVisibleField.set(controller, true);

               controller.redrawAll();

               verify(mockGc, atLeast(1)).strokeLine(anyDouble(), anyDouble(), anyDouble(), anyDouble());
           }catch(Exception e){
               fail("Eccezione durante il test: " + e.getMessage());
           }finally {
               latch.countDown();
           }
        });

        if (!latch.await(5, TimeUnit.SECONDS)) {
            throw new IllegalStateException("Test non completato entro il timeout");
        }
    }

    /**
     * Questo test verifica che il controller, quando chiama {@code redrawAll()} se il parametro
     * {@code gridVisible} è messo a {@code false} allora non viene disegnata nemmeno una volta
     * una linea per il disegno della griglia.
     * @throws Exception in caso di errori durante il caricamento della vista o l'accesso riflessivo
     */
    @Test
    void testDrawGrid_WhenGridNotVisible() throws Exception {
        CountDownLatch latch = new CountDownLatch(1);
        Platform.runLater(() -> {
            try{
                FXMLLoader loader = new FXMLLoader(getClass().getResource("DrawSnapView.fxml"));
                Parent root = loader.load();
                DrawSnapController controller = loader.getController();

                GraphicsContext mockGc = mock(GraphicsContext.class);

                Field gcField = DrawSnapController.class.getDeclaredField("gc");
                gcField.setAccessible(true);
                gcField.set(controller, mockGc);

                Field canvasField = DrawSnapController.class.getDeclaredField("canvas");
                canvasField.setAccessible(true);
                Canvas canvas = new Canvas(500, 500);
                canvasField.set(controller, canvas);

                Field formeField = DrawSnapController.class.getDeclaredField("forme");
                formeField.setAccessible(true);
                DrawSnapModel mockModel = mock(DrawSnapModel.class);
                when(mockModel.getIteratorForme()).thenReturn(Collections.emptyIterator());
                formeField.set(controller, mockModel);

                Field gridVisibleField = DrawSnapController.class.getDeclaredField("gridVisible");
                gridVisibleField.setAccessible(true);
                gridVisibleField.set(controller, false);

                controller.redrawAll();

                verify(mockGc, never()).strokeLine(anyDouble(), anyDouble(), anyDouble(), anyDouble());
            }catch(Exception e){
                fail("Eccezione durante il test: " + e.getMessage());
            }finally {
                latch.countDown();
            }
        });

        if (!latch.await(5, TimeUnit.SECONDS)) {
            throw new IllegalStateException("Test non completato entro il timeout");
        }
    }
}