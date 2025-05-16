package it.unisa.software_architecture_design.drawsnapdrawingtool;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.canvas.Canvas;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;
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
}