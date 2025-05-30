package it.unisa.software_architecture_design.drawsnapdrawingtool.forme;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;

public class Poligono extends Forma {
    /*
     * Attributi
     */
    private List<Double> puntiX; // Punti intrinseci, relativi al centro del poligono (0,0)
    private List<Double> puntiY; // Punti intrinseci, relativi al centro del poligono (0,0)
    private transient Color coloreInterno;
    // Dimensioni intrinseche (non ruotate) del poligono.
    private double intrinsicLarghezza;
    private double intrinsicAltezza;
    private double intrinsicCenterX; // Centro X della bounding box intrinseca, relativo all'origine interna (0,0)
    private double intrinsicCenterY; // Centro Y della bounding box intrinseca, relativo all'origine interna (0,0)

    /**
     * Costruttore, Getter e Setter
     *
     * @param initialRefX La coordinata X del punto di riferimento iniziale (es. primo click del mouse).
     * Questa sarà la coordinata X globale del poligono.
     * @param initialRefY La coordinata Y del punto di riferimento iniziale (es. primo click del mouse).
     * Questa sarà la coordinata Y globale del poligono.
     * @param larghezza   (Non usato direttamente per il calcolo della larghezza intrinseca, ma mantenuto per compatibilità con Forma)
     * @param altezza     (Non usato direttamente per il calcolo dell'altezza intrinseca, ma mantenuto per compatibilità con Forma)
     * @param angoloInclinazione L'angolo di inclinazione iniziale del poligono.
     * @param colore      Il colore del bordo del poligono.
     * @param rawPuntiX   La lista delle coordinate X grezze dei vertici del poligono (come disegnate/cliccate sul canvas).
     * @param rawPuntiY   La lista delle coordinate Y grezze dei vertici del poligono (come disegnate/cliccate sul canvas).
     * @param coloreInterno Il colore di riempimento del poligono.
     */
    public Poligono(double initialRefX, double initialRefY, double larghezza, double altezza, double angoloInclinazione, Color colore, List<Double> rawPuntiX, List<Double> rawPuntiY, Color coloreInterno) {
        // 1. CHIAMA super() COME PRIMA ISTRUZIONE.
        // Imposta la posizione globale del poligono direttamente al punto di riferimento iniziale.
        super(initialRefX, initialRefY, 0, angoloInclinazione, colore);

        this.puntiX = new ArrayList<>();
        this.puntiY = new ArrayList<>();
        this.coloreInterno = coloreInterno;

        // 2. Trasla i punti grezzi (rawPuntiX, rawPuntiY) in modo che siano relativi al punto di riferimento iniziale.
        // Questi diventano i punti "intrinseci" del poligono.
        // Quando il poligono verrà disegnato, gc.translate(super.getCoordinataX(), super.getCoordinataY())
        // sposterà l'origine del contesto grafico a (initialRefX, initialRefY),
        // e disegnare questi punti relativi farà sì che il poligono appaia esattamente dove i punti sono stati cliccati.
        for (int i = 0; i < rawPuntiX.size(); i++) {
            this.puntiX.add(rawPuntiX.get(i) - initialRefX);
            this.puntiY.add(rawPuntiY.get(i) - initialRefY);
        }

        // 3. Calcola le dimensioni intrinseche (non ruotate) basate su questi punti centrati.
        // NOTA: calcolaBoundingBox() NON deve più modificare super.coordinataX/Y
        calcolaBoundingBox();
    }

    // Getter per le dimensioni intrinseche
    @Override
    public double getLarghezza() {
        return intrinsicLarghezza;
    }

    public double getAltezza() {
        return intrinsicAltezza;
    }

    @Override
    public void setCoordinataX(double coordinataX) {
        super.setCoordinataX(coordinataX);
    }

    @Override
    public void setCoordinataY(double coordinataY) {
        super.setCoordinataY(coordinataY);
    }


    public Color getColoreInterno() {
        return coloreInterno;
    }

    public void setColoreInterno(Color coloreInterno) {
        this.coloreInterno = coloreInterno;
    }

    // Aggiungi questi getter:
    public double getIntrinsicCenterX() {
        return intrinsicCenterX;
    }

    public double getIntrinsicCenterY() {
        return intrinsicCenterY;
    }


    @Override
    public void disegna(GraphicsContext gc) {
        gc.save();

        // Trasla il contesto grafico al centro del poligono (che è il suo punto di riferimento iniziale)
        gc.translate(super.getCoordinataX(), super.getCoordinataY());

        // Applica la rotazione al contesto grafico
        gc.rotate(super.getAngoloInclinazione());

        // I punti sono già relativi all'origine (0,0) a causa della traslazione iniziale nel costruttore.
        double[] xArray = puntiX.stream().mapToDouble(Double::doubleValue).toArray();
        double[] yArray = puntiY.stream().mapToDouble(Double::doubleValue).toArray();

        // Imposta il colore del contorno e dell'interno
        gc.setFill(getColoreInterno());
        gc.setStroke(getColore());
        gc.setLineWidth(2);

        // fill disegna l'area interna del poligono
        gc.fillPolygon(
                xArray,
                yArray,
                puntiX.size() // Numero di punti
        );

        // stroke disegna il contorno
        gc.strokePolygon(
                xArray,
                yArray,
                puntiX.size() // Numero di punti
        );

        gc.restore();
    }

    // Trasforma il punto di input nel sistema di coordinate del poligono non ruotato
    // prima di eseguire il test di appartenenza.
    @Override
    public boolean contiene(double x, double y) {

        //Trasla il punto (x,y) in modo che il centro del poligono sia l'origine
        double translatedX = x - super.getCoordinataX();
        double translatedY = y - super.getCoordinataY();

        //Applica la rotazione inversa al punto
        double angleRad = Math.toRadians(-super.getAngoloInclinazione()); // Angolo inverso
        double rotatedX = translatedX * Math.cos(angleRad) - translatedY * Math.sin(angleRad);
        double rotatedY = translatedX * Math.sin(angleRad) + translatedY * Math.cos(angleRad);

        // Ora il punto (rotatedX, rotatedY) è nel sistema di coordinate del poligono non ruotato.
        int n = puntiX.size();
        boolean dentro = false;

        for (int i = 0, j = n - 1; i < n; j = i++) {
            double xi = puntiX.get(i), yi = puntiY.get(i);
            double xj = puntiX.get(j), yj = puntiY.get(j);
            boolean interseca = ((yi > rotatedY) != (yj > rotatedY)) &&
                    (rotatedX < (xj - xi) * (rotatedY - yi) / (yj - yi + 1e-10) + xi);
            if (interseca) dentro = !dentro;
        }
        return dentro;
    }

    @Override
    public void specchia() {
        // Specchia i punti lungo l'asse Y (negando le coordinate X)
        List<Double> nuoviPuntiX = new ArrayList<>();
        for (double px : puntiX) {
            nuoviPuntiX.add(-px); // Negare la coordinata X
        }
        this.puntiX = nuoviPuntiX;

        // Le coordinate Y rimangono invariate per lo specchiamento lungo l'asse Y

        // Dopo aver modificato i punti, ricalcola la bounding box.
        // Questo aggiorna le dimensioni intrinseche del poligono se sono cambiate.
        calcolaBoundingBox();

        // NUOVA MODIFICA: Specchia anche l'angolo di inclinazione
        // Questo è cruciale per far sì che lo specchiamento visivo sia coerente
        // anche quando la figura è ruotata.
        super.setAngoloInclinazione(-super.getAngoloInclinazione());
    }


    /**
     * Calcola la bounding box del poligono (min/max X/Y) e il suo centro
     * basandosi sui punti intrinseci (non ruotati) del poligono.
     * Aggiorna le proprietà `intrinsicLarghezza` e `intrinsicAltezza`.
     */
    private void calcolaBoundingBox() {
        if (puntiX == null || puntiX.isEmpty()) {
            this.intrinsicLarghezza = 0;
            this.intrinsicAltezza = 0;
            this.intrinsicCenterX = 0; // Inizializza anche il centro
            this.intrinsicCenterY = 0; // Inizializza anche il centro
            super.setLarghezza(0);
            return;
        }

        double minX = puntiX.get(0);
        double maxX = puntiX.get(0);
        double minY = puntiY.get(0);
        double maxY = puntiY.get(0);

        for (int i = 1; i < puntiX.size(); i++) {
            minX = Math.min(minX, puntiX.get(i));
            maxX = Math.max(maxX, puntiX.get(i));
            minY = Math.min(minY, puntiY.get(i));
            maxY = Math.max(maxY, puntiY.get(i));
        }

        this.intrinsicLarghezza = maxX - minX;
        this.intrinsicAltezza = maxY - minY;

        // Calcola e salva il centro della bounding box intrinseca
        this.intrinsicCenterX = (minX + maxX) / 2.0;
        this.intrinsicCenterY = (minY + maxY) / 2.0;

        super.setLarghezza(this.intrinsicLarghezza);
    }

    @Override
    public Poligono clone() {
        Poligono cloned = (Poligono) super.clone();

        // Copia profonda delle liste di punti (sono i punti intrinseci)
        cloned.puntiX = new ArrayList<>(this.puntiX);
        cloned.puntiY = new ArrayList<>(this.puntiY);

        cloned.coloreInterno = this.coloreInterno;
        cloned.intrinsicLarghezza = this.intrinsicLarghezza;
        cloned.intrinsicAltezza = this.intrinsicAltezza;

        return cloned;
    }

    // Scala i punti intrinseci del poligono rispetto al suo centro (0,0).
    public void scala(double scalaX, double scalaY) {
        if (puntiX.isEmpty()) return;

        List<Double> nuoviPuntiX = new ArrayList<>();
        List<Double> nuoviPuntiY = new ArrayList<>();

        for (int i = 0; i < puntiX.size(); i++) {
            // I punti sono già relativi al centro (0,0)
            double oldX = puntiX.get(i);
            double oldY = puntiY.get(i);

            // Scala il punto
            nuoviPuntiX.add(oldX * scalaX);
            nuoviPuntiY.add(oldY * scalaY);
        }
        this.puntiX = nuoviPuntiX;
        this.puntiY = nuoviPuntiY;
        calcolaBoundingBox(); // Ricalcola le dimensioni intrinseche dopo la scala
    }

    // Aggiorna solo l'angolo nella superclasse.
    // La rotazione effettiva è gestita dal metodo disegna() tramite il contesto grafico.
    @Override
    public void setAngoloInclinazione(double nuovoAngolo) {
        super.setAngoloInclinazione(nuovoAngolo);
    }

    // Il metodo trasla() è già corretto, aggiorna solo la posizione globale.
    public void trasla(double deltaX, double deltaY) {
        super.setCoordinataX(super.getCoordinataX() + deltaX);
        super.setCoordinataY(super.getCoordinataY() + deltaY);
    }
}
