import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class FXMain extends Application {

    private static final int TILE_SIZE = 60;
    private static final int BOARD_SIZE = 8;
    
    // --- KOYU RENK PALETİ ---
    private static final Color APP_BG = Color.web("#121212"); // Ana Arka Plan (Neredeyse Siyah)
    private static final Color PANEL_BG = Color.web("#1e1e1e"); // Panel Arka Planı (Koyu Gri)
    private static final Color TEXT_COLOR = Color.web("#e0e0e0");
    
    // Board Renkleri (Matte Style)
    private static final Color BOARD_LIGHT = Color.web("#95a5a6"); // Mat Gümüş
    private static final Color BOARD_DARK = Color.web("#2c3e50");  // Mat Antrasit
    
    // Obje Renkleri
    private static final Color PATH_COLOR = Color.web("#f1c40f");
    private static final Color START_COLOR = Color.web("#3498db");
    private static final Color TARGET_COLOR = Color.web("#e74c3c");
    private static final Color BOMB_COLOR = Color.web("#2d3436");

    // UI Bileşenleri
    private Tile[][] gridUI = new Tile[BOARD_SIZE][BOARD_SIZE];
    private TableView<AlgoResult> resultTable;
    private ToggleGroup modeGroup;
    private Button runBtn;

    // Oyun Mantığı
    private Board backendBoard;
    private Node startNode = null;
    private Node targetNode = null;
    private Timeline currentAnimation;

    @Override
    public void start(Stage primaryStage) {
        backendBoard = new Board(BOARD_SIZE);

        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: " + toHex(APP_BG) + ";");
        
        // --- SOL PANEL ---
        VBox controls = new VBox(20);
        controls.setPadding(new Insets(20));
        controls.setPrefWidth(280);
        controls.setStyle("-fx-background-color: " + toHex(PANEL_BG) + "; -fx-border-color: #333; -fx-border-width: 0 1 0 0;");

        Label title = new Label("Algoritma Yarışı");
        title.setFont(Font.font("Segoe UI", FontWeight.BOLD, 22));
        title.setTextFill(TEXT_COLOR);

        VBox modeBox = new VBox(12);
        Label modeLabel = new Label("Yerleştirme Modu");
        modeLabel.setTextFill(Color.web("#7f8c8d"));
        
        RadioButton rbStart = createStyledRadioButton("At (Başlangıç)", "START");
        RadioButton rbTarget = createStyledRadioButton("Hedef (Coin)", "TARGET");
        RadioButton rbBomb = createStyledRadioButton("Bomba (Engel)", "BOMB");
        rbStart.setSelected(true);

        modeGroup = new ToggleGroup();
        rbStart.setToggleGroup(modeGroup);
        rbTarget.setToggleGroup(modeGroup);
        rbBomb.setToggleGroup(modeGroup);
        
        modeBox.getChildren().addAll(modeLabel, rbStart, rbTarget, rbBomb);

        runBtn = new Button("HEPSİNİ KARŞILAŞTIR");
        runBtn.setMaxWidth(Double.MAX_VALUE);
        runBtn.setPrefHeight(50);
        runBtn.setStyle("-fx-background-color: #27ae60; -fx-text-fill: white; -fx-font-weight: bold; -fx-cursor: hand; -fx-background-radius: 4;");
        
        Button resetBtn = new Button("TAHTAYI TEMİZLE");
        resetBtn.setMaxWidth(Double.MAX_VALUE);
        resetBtn.setPrefHeight(40);
        resetBtn.setStyle("-fx-background-color: #c0392b; -fx-text-fill: white; -fx-font-weight: bold; -fx-cursor: hand; -fx-background-radius: 4;");

        Label tipLabel = new Label("İpucu: Bombalara tekrar basarak kaldırabilirsiniz.");
        tipLabel.setWrapText(true);
        tipLabel.setTextFill(Color.GRAY);
        tipLabel.setFont(Font.font("Arial", 11));

        controls.getChildren().addAll(title, new Separator(), modeBox, new Region(), runBtn, resetBtn, new Separator(), tipLabel);

        // --- ORTA PANEL ---
        StackPane boardContainer = new StackPane();
        boardContainer.setStyle("-fx-background-color: " + toHex(APP_BG) + ";"); 
        
        GridPane boardPane = new GridPane();
        boardPane.setAlignment(Pos.CENTER);
        boardPane.setEffect(new DropShadow(20, Color.BLACK)); 
        boardPane.setStyle("-fx-border-color: #333; -fx-border-width: 8; -fx-border-radius: 2;");

        for (int row = 0; row < BOARD_SIZE; row++) {
            for (int col = 0; col < BOARD_SIZE; col++) {
                Tile tile = new Tile(row, col);
                gridUI[row][col] = tile;
                boardPane.add(tile, col, row);
            }
        }
        boardContainer.getChildren().add(boardPane);

        // --- ALT PANEL ---
        VBox bottomPanel = new VBox(10);
        bottomPanel.setPadding(new Insets(10));
        bottomPanel.setPrefHeight(280);
        bottomPanel.setStyle("-fx-background-color: " + toHex(PANEL_BG) + "; -fx-border-color: #333; -fx-border-width: 1 0 0 0;");

        Label tableTitle = new Label("Performans Sonuçları (En Hızlıdan Yavaşa)");
        tableTitle.setTextFill(TEXT_COLOR);
        tableTitle.setFont(Font.font("Segoe UI", FontWeight.SEMI_BOLD, 14));

        createResultTable(); 

        bottomPanel.getChildren().addAll(tableTitle, resultTable);

        root.setLeft(controls);
        root.setCenter(boardContainer);
        root.setBottom(bottomPanel);

        // Eventler
        runBtn.setOnAction(e -> runAllAlgorithms());
        resetBtn.setOnAction(e -> resetBoard());
        
        resultTable.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null && newVal.path != null) {
                animatePath(newVal.path);
            }
        });

        // Hover Efektleri
        runBtn.setOnMouseEntered(e -> runBtn.setStyle("-fx-background-color: #2ecc71; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 4;"));
        runBtn.setOnMouseExited(e -> runBtn.setStyle("-fx-background-color: #27ae60; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 4;"));

        Scene scene = new Scene(root, 1100, 900);
        
        // --- CSS ILE TAM KOYU MOD TABLO ---
        scene.getStylesheets().add("data:text/css," + getDarkTableCSS());
        
        primaryStage.setTitle("Algoritma Karşılaştırıcı Pro (Dark Mode)");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    // --- DEEP DARK TABLO CSS ---
    private String getDarkTableCSS() {
        return 
            ".table-view {" +
            "   -fx-base: #1e1e1e;" +
            "   -fx-control-inner-background: #1e1e1e;" +
            "   -fx-background-color: #1e1e1e;" +
            "   -fx-table-cell-border-color: transparent;" +
            "   -fx-table-header-border-color: transparent;" +
            "   -fx-padding: 5;" +
            "}" +
            // Header (Başlıklar)
            ".table-view .column-header-background {" +
            "   -fx-background-color: #2c2c2c;" +
            "   -fx-background-radius: 5 5 0 0;" +
            "}" +
            ".table-view .column-header {" +
            "   -fx-background-color: transparent;" +
            "   -fx-size: 40px;" +
            "}" +
            ".table-view .column-header .label {" +
            "   -fx-text-fill: #bdc3c7;" +
            "   -fx-font-weight: bold;" +
            "}" +
            // Satırlar (Rows)
            ".table-row-cell {" +
            "   -fx-background-color: #1e1e1e;" +
            "   -fx-border-width: 0 0 1 0;" +
            "   -fx-border-color: #2a2a2a;" +
            "}" +
            ".table-row-cell:odd {" +
            "   -fx-background-color: #252525;" + // Zebra efekti
            "}" +
            ".table-row-cell:hover {" +
            "   -fx-background-color: #333333;" +
            "}" +
            ".table-row-cell:selected {" +
            "   -fx-background-color: #2980b9;" + // Seçili Parlak Mavi
            "   -fx-background-insets: 0;" +
            "   -fx-background-radius: 0;" +
            "}" +
            // Hücre İçi Yazı
            ".table-cell {" +
            "   -fx-text-fill: #ecf0f1;" +
            "   -fx-alignment: center;" +
            "   -fx-font-size: 14px;" +
            "}" +
            // Scrollbar (Karanlık Mod İçin Özelleştirilmiş)
            ".scroll-bar:horizontal, .scroll-bar:vertical {" +
            "   -fx-background-color: transparent;" +
            "}" +
            ".scroll-bar:vertical .track, .scroll-bar:horizontal .track {" +
            "   -fx-background-color: transparent;" +
            "   -fx-border-color: transparent;" +
            "}" +
            ".scroll-bar:vertical .thumb, .scroll-bar:horizontal .thumb {" +
            "   -fx-background-color: #555;" +
            "   -fx-background-radius: 5em;" +
            "}" +
            // Boş Tablo Yazısı
            ".table-view .placeholder .label {" +
            "   -fx-text-fill: #7f8c8d;" +
            "}";
    }

    @SuppressWarnings("unchecked")
    private void createResultTable() {
        resultTable = new TableView<>();
        resultTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        // 1. İsim
        TableColumn<AlgoResult, String> nameCol = new TableColumn<>("Algoritma");
        nameCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().name));
        nameCol.setCellFactory(column -> new StyledTableCell<>(Pos.CENTER_LEFT));

        // 2. Süre
        TableColumn<AlgoResult, Long> timeCol = new TableColumn<>("Süre (µs)");
        timeCol.setCellValueFactory(data -> new SimpleLongProperty(data.getValue().time).asObject());
        timeCol.setCellFactory(column -> new StyledTableCell<>(Pos.CENTER)); 

        // 3. Adım
        TableColumn<AlgoResult, Integer> stepsCol = new TableColumn<>("Adım");
        stepsCol.setCellValueFactory(data -> new SimpleIntegerProperty(data.getValue().stepCount).asObject());
        stepsCol.setCellFactory(column -> new StyledTableCell<>(Pos.CENTER)); 
        
        // 4. Durum
        TableColumn<AlgoResult, String> statusCol = new TableColumn<>("Durum");
        statusCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().found ? "✔ Buldu" : "✘ Başarısız"));
        statusCol.setCellFactory(column -> new StatusTableCell()); 

        resultTable.getColumns().addAll(nameCol, timeCol, stepsCol, statusCol);
    }
    
    // Hücre Stili (CSS ile override edilecek ama formatlama için gerekli)
    private class StyledTableCell<T> extends TableCell<AlgoResult, T> {
        private Pos alignment;
        
        public StyledTableCell(Pos alignment) {
            this.alignment = alignment;
        }

        @Override
        protected void updateItem(T item, boolean empty) {
            super.updateItem(item, empty);
            if (empty || item == null) {
                setText(null);
            } else {
                if (item instanceof Number) setText(String.format("%,d", ((Number) item).longValue()));
                else setText(item.toString());
                setAlignment(alignment);
            }
        }
    }

    private class StatusTableCell extends TableCell<AlgoResult, String> {
        @Override
        protected void updateItem(String item, boolean empty) {
            super.updateItem(item, empty);
            if (empty || item == null) {
                setText(null);
            } else {
                setText(item);
                setAlignment(Pos.CENTER);
                String color = item.contains("✔") ? "#2ecc71" : "#e74c3c"; // Yeşil / Kırmızı (Neon)
                setStyle("-fx-text-fill: " + color + "; -fx-font-weight: bold;");
            }
        }
    }

    // --- Tile (Kare) ---
    private class Tile extends StackPane {
        int row, col;
        Rectangle bg;
        Text icon;
        Text stepNumber;

        public Tile(int row, int col) {
            this.row = row;
            this.col = col;
            bg = new Rectangle(TILE_SIZE, TILE_SIZE);
            bg.setStroke(null); 
            resetColor();

            icon = new Text();
            icon.setFont(Font.font("Segoe UI Symbol", FontWeight.BOLD, 28));
            
            stepNumber = new Text();
            stepNumber.setFont(Font.font("Arial", FontWeight.BOLD, 12));
            stepNumber.setTranslateX(TILE_SIZE/2.0 - 12);
            stepNumber.setTranslateY(-(TILE_SIZE/2.0) + 12);

            getChildren().addAll(bg, icon, stepNumber);
            setOnMouseClicked(e -> handleMouseClick());
        }

        private void handleMouseClick() {
            if (currentAnimation != null) currentAnimation.stop();
            clearPathVisuals();
            
            String mode = (String) modeGroup.getSelectedToggle().getUserData();
            
            if (startNode != null && startNode.row == row && startNode.col == col) startNode = null;
            if (targetNode != null && targetNode.row == row && targetNode.col == col) targetNode = null;

            if (mode.equals("START")) {
                if (!backendBoard.isSafe(row, col)) backendBoard.setEmpty(row, col);
                if (startNode != null) gridUI[startNode.row][startNode.col].clearTile();
                startNode = new Node(row, col, null);
                setVisual("♞", START_COLOR);
                
            } else if (mode.equals("TARGET")) {
                if (!backendBoard.isSafe(row, col)) backendBoard.setEmpty(row, col);
                if (targetNode != null) gridUI[targetNode.row][targetNode.col].clearTile();
                targetNode = new Node(row, col, null);
                backendBoard.setCoin(row, col); 
                setVisual("★", TARGET_COLOR);
                
            } else if (mode.equals("BOMB")) {
                if ((startNode != null && startNode.row == row && startNode.col == col) || 
                    (targetNode != null && targetNode.row == row && targetNode.col == col)) {
                    return;
                }
                if (!backendBoard.isSafe(row, col)) {
                    backendBoard.setEmpty(row, col);
                    clearTile();
                } else {
                    backendBoard.setBomb(row, col);
                    setVisual("💣", BOMB_COLOR);
                }
            }
        }

        public void setVisual(String s, Color c) { icon.setText(s); icon.setFill(c); }
        public void clearTile() { icon.setText(""); stepNumber.setText(""); resetColor(); }
        public void resetColor() {
            if ((row + col) % 2 == 0) bg.setFill(BOARD_LIGHT); else bg.setFill(BOARD_DARK);
            if (!backendBoard.isSafe(row, col)) { icon.setText("💣"); icon.setFill(BOMB_COLOR); }
        }
    }

    private void runAllAlgorithms() {
        if (startNode == null || targetNode == null) {
            showAlert("Eksik Bilgi", "Lütfen At ve Hedef yerleştirin!");
            return;
        }

        boolean startSafe = backendBoard.isSafe(startNode.row, startNode.col);
        boolean targetSafe = backendBoard.isSafe(targetNode.row, targetNode.col);

        if (!startSafe || !targetSafe) {
            showAlert("Kritik Hata", "Başlangıç veya Hedef bombanın üzerinde!");
            return;
        }

        resultTable.getItems().clear();
        List<AlgoResult> results = new ArrayList<>();

        results.add(runSingleAlgo("BFS", () -> reconstructPath(BFSSolver.solve(backendBoard, startNode.row, startNode.col, targetNode.row, targetNode.col))));
        results.add(runSingleAlgo("DFS", () -> reconstructPath(DFSSolver.solve(backendBoard, startNode.row, startNode.col, targetNode.row, targetNode.col))));
        results.add(runSingleAlgo("A*", () -> {
            AStarSolver solver = new AStarSolver();
            return solver.findPath(backendBoard, new Node(startNode.row, startNode.col, null), new Node(targetNode.row, targetNode.col, null));
        }));
        results.add(runSingleAlgo("Greedy BFS", () -> {
            GreedyBestFirstSearch solver = new GreedyBestFirstSearch();
            return solver.greedyBestFirstSearch(backendBoard, new Node(startNode.row, startNode.col, null), new Node(targetNode.row, targetNode.col, null));
        }));
        results.add(runSingleAlgo("IDDFS", () -> {
            IDDFS solver = new IDDFS(backendBoard, targetNode.row, targetNode.col);
            solver.search(startNode.row, startNode.col);
            return solver.isSolutionFound() ? solver.getPath() : null;
        }));

        results.sort(Comparator.comparingLong(r -> r.time));

        resultTable.getItems().addAll(results);
        if (!results.isEmpty()) resultTable.getSelectionModel().select(0);
    }
    
    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private AlgoResult runSingleAlgo(String name, AlgorithmRunner runner) {
        long startTime = System.nanoTime();
        List<Node> path = null;
        try { path = runner.run(); } catch (Exception e) { System.out.println(name + " hatası: " + e.getMessage()); }
        long duration = (System.nanoTime() - startTime) / 1000;
        boolean found = (path != null && !path.isEmpty());
        int steps = found ? path.size() - 1 : 0;
        return new AlgoResult(name, duration, steps, found, path);
    }

    interface AlgorithmRunner { List<Node> run(); }

    public static class AlgoResult {
        String name; long time; int stepCount; boolean found; List<Node> path;
        public AlgoResult(String name, long time, int stepCount, boolean found, List<Node> path) {
            this.name = name; this.time = time; this.stepCount = stepCount; this.found = found; this.path = path;
        }
    }

    private RadioButton createStyledRadioButton(String text, String userData) {
        RadioButton rb = new RadioButton(text);
        rb.setUserData(userData);
        rb.setTextFill(TEXT_COLOR);
        return rb;
    }

    private void animatePath(List<Node> path) {
        if (currentAnimation != null) currentAnimation.stop();
        clearPathVisuals();
        if (path == null || path.isEmpty()) return;

        currentAnimation = new Timeline();
        for (int i = 0; i < path.size(); i++) {
            Node n = path.get(i);
            int stepIndex = i;
            KeyFrame frame = new KeyFrame(Duration.millis(i * 50), e -> {
                boolean isStart = (n.row == startNode.row && n.col == startNode.col);
                boolean isTarget = (n.row == targetNode.row && n.col == targetNode.col);
                Tile tile = gridUI[n.row][n.col];
                if (!isStart && !isTarget) {
                    tile.bg.setFill(PATH_COLOR);
                    tile.stepNumber.setText(String.valueOf(stepIndex));
                    tile.stepNumber.setFill(Color.web("#2c3e50")); 
                }
            });
            currentAnimation.getKeyFrames().add(frame);
        }
        currentAnimation.play();
    }

    private List<Node> reconstructPath(Node target) {
        if (target == null) return null;
        List<Node> path = new ArrayList<>();
        Node curr = target;
        while (curr != null) { path.add(curr); curr = curr.parent; }
        Collections.reverse(path);
        return path;
    }

    private void clearPathVisuals() {
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                Tile t = gridUI[i][j];
                if (t.bg.getFill().equals(PATH_COLOR)) { t.resetColor(); t.stepNumber.setText(""); }
            }
        }
    }

    private void resetBoard() {
        if (currentAnimation != null) currentAnimation.stop();
        backendBoard = new Board(BOARD_SIZE);
        startNode = null; targetNode = null;
        resultTable.getItems().clear();
        for (int i = 0; i < BOARD_SIZE; i++) for (int j = 0; j < BOARD_SIZE; j++) gridUI[i][j].clearTile();
    }

    private String toHex(Color c) {
        return String.format("#%02X%02X%02X", (int)(c.getRed()*255), (int)(c.getGreen()*255), (int)(c.getBlue()*255));
    }

    public static void main(String[] args) {
        launch(args);
    }
}