package it.unisa.software_architecture_design.drawsnapdrawingtool;

import it.unisa.software_architecture_design.drawsnapdrawingtool.forme.Forma;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
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

    /**
     * Test che verifica che il metodo drawGrid disegni le linee con la spaziatura corretta
     * in base al currentGridSize e allo zoom.
     * @throws Exception in caso di errori durante il caricamento della vista o l'accesso riflessivo
     */
    @Test
    void testDrawGrid_GridSizeAndZoomInfluenceLines() throws Exception {
        CountDownLatch latch = new CountDownLatch(1);
        Platform.runLater(() -> {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("DrawSnapView.fxml"));
                Parent root = loader.load();
                DrawSnapController controller = loader.getController();

                GraphicsContext mockGc = mock(GraphicsContext.class);

                // Inietta il mock di GraphicsContext nel controller
                Field gcField = DrawSnapController.class.getDeclaredField("gc");
                gcField.setAccessible(true);
                gcField.set(controller, mockGc);

                // Imposta un canvas di dimensioni note per calcoli prevedibili
                Field canvasField = DrawSnapController.class.getDeclaredField("canvas");
                canvasField.setAccessible(true);
                Canvas canvas = new Canvas(600, 400); // Dimensioni semplici per calcoli
                canvasField.set(controller, canvas);

                // Mock del modello (non dovrebbe avere forme per questo test)
                Field formeField = DrawSnapController.class.getDeclaredField("forme");
                formeField.setAccessible(true);
                DrawSnapModel mockModel = mock(DrawSnapModel.class);
                when(mockModel.getIteratorForme()).thenReturn(Collections.emptyIterator());
                formeField.set(controller, mockModel);

                // Abilita la griglia
                Field gridVisibleField = DrawSnapController.class.getDeclaredField("gridVisible");
                gridVisibleField.setAccessible(true);
                gridVisibleField.set(controller, true);

                // Imposta un array di zoom per il test
                Field zoomLevelsField = DrawSnapController.class.getDeclaredField("zoomLevels");
                zoomLevelsField.setAccessible(true);
                Double[] testZoomLevels = {1.0, 2.0}; // Esempio: zoom 100% e 200%
                zoomLevelsField.set(controller, testZoomLevels);

                Field currentZoomIndexField = DrawSnapController.class.getDeclaredField("currentZoomIndex");
                currentZoomIndexField.setAccessible(true);

                Field currentGridSizeField = DrawSnapController.class.getDeclaredField("currentGridSize");
                currentGridSizeField.setAccessible(true);

                currentZoomIndexField.set(controller, 0); // Seleziona zoom 1.0
                currentGridSizeField.set(controller, 20.0); // Dimensione griglia 20

                controller.redrawAll();

                verify(mockGc, atLeastOnce()).setStroke(Color.LIGHTGRAY);
                verify(mockGc, atLeastOnce()).setLineWidth(1.0 / testZoomLevels[0]); // 1.0 / 1.0 = 1.0


                verify(mockGc, times(31)).strokeLine(anyDouble(), eq(0.0), anyDouble(), eq(400.0)); // Verticali
                verify(mockGc, times(21)).strokeLine(eq(0.0), anyDouble(), eq(600.0), anyDouble()); // Orizzontali

                // Resetta il mock per non far interferire le chiamate del primo test con il secondo
                reset(mockGc);

                // --- Test 2: Dimensione griglia 50.0 e zoom 2.0 (200%) ---
                currentGridSizeField.set(controller, 50.0); // Griglia più grande
                currentZoomIndexField.set(controller, 1); // Zoom 2.0

                controller.redrawAll();

                verify(mockGc, atLeastOnce()).setStroke(Color.LIGHTGRAY);
                verify(mockGc, atLeastOnce()).setLineWidth(1.0 / testZoomLevels[1]); // 1.0 / 2.0 = 0.5


                verify(mockGc, times(7)).strokeLine(anyDouble(), eq(0.0), anyDouble(), eq(200.0)); // Verticali
                verify(mockGc, times(5)).strokeLine(eq(0.0), anyDouble(), eq(300.0), anyDouble()); // Orizzontali


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
}