package ui;

import exp.Run;
import exp.shiro.Shiro_550;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public class Controller implements Initializable {
    /**
     * Attack
     */

    @FXML
    private TextField TargetURL;

    @FXML
    private ChoiceBox<String> AttackModuleBox;

    @FXML
    private ChoiceBox<String> AttackGadgetBox;

    @FXML
    private ChoiceBox<String> AttackComponentsBox;

    @FXML
    private ChoiceBox<String> AttackServerBox;

    @FXML
    private TextArea AttackTextArea;

    @FXML
    private Label ShiroKeyLabel;

    @FXML
    private TextField ShiroKeyField;

    @FXML
    private Button ShiroKeyButton;

    /**
     * Generate
     */
    @FXML
    private ChoiceBox<String> GenerateServerBox;

    @FXML
    private ChoiceBox<String> GenerateEncodingBox;

    @FXML
    private ChoiceBox<String> GenerateComponentsBox;

    @FXML
    private ChoiceBox<String> GenerateCompileBox;

    @FXML
    private TextArea GenerateTextArea;

    /*
    Attack test
    * */

    @FXML
    private TextField target;
    
    private ComboBox<String> protocol;

    @FXML
    private ComboBox<String> modules;

    @FXML
    private ComboBox<String> exps;

    @FXML
    private GridPane attackGridPane;

    @FXML
    private Pane expPane;

    private TextArea expLog;

    private ComboBox<String> servers;
    private ComboBox<String> components;
    private ComboBox<String> gadgets;
    private Button attack;

    // shiro exp
    private TextField keyWord;
    private TextField securityKey;
    private CheckBox aesGCM;

    final ObservableList<String> FL = FXCollections.observableArrayList("Filter", "Listener");
    final ObservableList<String> CI = FXCollections.observableArrayList("Controller", "Interceptor");

    public static ArrayList<String> LogText = new ArrayList<String>();
    public static ArrayList<String> temp;

    public static String logPath = System.getProperties().getProperty("user.dir").replace("\\", "/");
    public static String fileName = logPath + "/data/log.txt";

    /**
     * Generate
     */

    public void Generate(ActionEvent actionEvent) {
        String Server = GenerateServerBox.getValue().trim();
        String Components = GenerateComponentsBox.getValue().trim();
        String Encoding = GenerateEncodingBox.getValue().trim();
        String Compile = GenerateCompileBox.getValue().trim();
        String memName = "";

        memName = Server + Components + "MemShell";


        String result = Run.generateMemString(memName, Encoding, Compile);
        GenerateTextArea.appendText(result);
        GenerateTextArea.setWrapText(true); //TextArea换行
    }

    public void Clear(ActionEvent actionEvent) {
        GenerateTextArea.clear();
    }


    public GridPane getBaseEXP() throws Exception {
        GridPane root = new GridPane();
        Label serverLable = new Label("Server:");
        Label protocolLable = new Label("Protocol:");
        Label componentsLable = new Label("Component:");
        List<String> expNames = Arrays.asList("550", "CVE_2016_3510", "InjectMemShell", "CVE_2017_12149", "CVE_2017_7504");
        String expName = exps.getSelectionModel().getSelectedItem();
        Label gadgetsLable = null;
        if (expNames.contains(expName)) {
            gadgetsLable = new Label("Gadget:");
        } else {
            gadgetsLable = new Label("Payload:");
        }


        servers = new ComboBox<String>();
        protocol = new ComboBox<String>();
        components = new ComboBox<String>();
        gadgets = new ComboBox<String>();
        attack = new Button("Run");

        root.add(protocolLable, 0, 0);
        root.add(protocol, 1, 0);
        root.add(serverLable, 2, 0);
        root.add(servers, 3, 0);
        root.add(componentsLable, 4, 0);
        root.add(components, 5, 0);
        root.add(gadgetsLable, 6, 0);
        root.add(gadgets, 7, 0);
        root.add(attack, 8, 0);

        // 样式
        serverLable.setPadding(new Insets(0, 0, 0, 10));
        componentsLable.setPadding(new Insets(0, 0, 0, 10));
        protocolLable.setPadding(new Insets(0, 0, 0, 10));
        gadgetsLable.setPadding(new Insets(0, 0, 0, 10));
//        serverLable.setPrefWidth(90);
        servers.setPrefWidth(150);

//        componentsLable.setPrefWidth(140);
        components.setPrefWidth(130);
//        gadgetsLable.setPrefWidth(100);
        gadgets.setPrefWidth(280);

        // envent
        try {
//        servers.setItems(FXCollections.observableList(ui.Config.serverNameList));
            servers.setItems(FXCollections.observableList(Config.moduleServers.get(modules.getSelectionModel().getSelectedItem())));
            servers.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {
                @Override
                public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                    List<String> values = Config.serverComponent.get(newValue.toString());
                    if (values != null && values.size() > 0) {
                        components.setItems(FXCollections.observableList(values));
                    } else {
                        components.setItems(FXCollections.observableList(Arrays.asList("Filter", "Listener")));
                    }
                    components.getSelectionModel().selectFirst();
                }
            });
            servers.getSelectionModel().selectFirst();

            gadgets.setItems(FXCollections.observableList(Config.gadGetMap.get(modules.getSelectionModel().getSelectedItem() + "_" + exps.getSelectionModel().getSelectedItem())));
            gadgets.getSelectionModel().selectFirst();

            protocol.setItems(FXCollections.observableList(Config.protocolMap.get(modules.getSelectionModel().getSelectedItem() + "_" + exps.getSelectionModel().getSelectedItem())));
            protocol.getSelectionModel().selectFirst();


            attack.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent e) {
                    try {
                        String targetUrl = target.getText();
                        String expName = modules.getSelectionModel().getSelectedItem() + "_" + exps.getSelectionModel().getSelectedItem();
                        String protocolStr = protocol.getSelectionModel().getSelectedItem();
                        String serverName = servers.getSelectionModel().getSelectedItem();
                        String componentName = components.getSelectionModel().getSelectedItem();
                        String gadgetName = gadgets.getSelectionModel().getSelectedItem();
                        String memshellName = serverName + componentName + "MemShell";
                        HashMap<String, String> params = new HashMap<String, String>();
                        if (expName.contains("Shiro")) {
                            String key = keyWord.getText().toString().trim();
                            String secret = securityKey.getText().toString().trim();
                            boolean isAESGCM = aesGCM.isSelected();
                            if (secret == null || secret.equalsIgnoreCase("")) {
                                Alert alert = new Alert(Alert.AlertType.ERROR);
                                alert.setTitle("Error Dialog");
                                alert.setHeaderText("请确认正确输入了秘钥！！");
                                alert.showAndWait();
                                return;
                            }
                            params.put("target", targetUrl);
                            params.put("gadgetType", gadgetName);
                            params.put("trojanType", memshellName);
                            params.put("payloadType", "ShiroMemShell");
                            params.put("keyWord", key);
                            params.put("aesGCM", String.valueOf(isAESGCM));
                            params.put("shiroKey", secret);

                        } else {
                            params.put("payloadType", memshellName);
                            params.put("target", targetUrl);
                            params.put("gadgetType", gadgetName);
                        }
                        params.put("protocol", protocolStr);
                        Run.attack(expLog, expName, params);
                    } catch (Exception ex) {
                    }
                }
            });
        } catch (Exception e) {
        }

        return root;
    }


    public void getShiroEXP(Pane expPane) throws Exception {
        TitledPane keyPane = new TitledPane();
        keyPane.setText("SecretKey");

        Label keyLabel = new Label("KeyWord:");
        Label securityLabel = new Label("SecretKey:");
        final Label aesGCMLabel = new Label("AES GCM");

        keyWord = new TextField();
        keyWord.setPrefWidth(150);
        keyWord.appendText("rememberMe");
        if (expLog == null) {
            expLog = new TextArea();
        }
        expLog.setPrefHeight(290);
        securityKey = new TextField();
        securityKey.setPrefWidth(320);
        securityKey.clear();

        aesGCM = new CheckBox();

        Button detectKey = new Button("Detect");

        detectKey.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    String targetUrl = target.getText();
                    String key = keyWord.getText().toString();
                    String secret = securityKey.getText().toString();
                    boolean isAESGCM = aesGCM.isSelected();
                    // 调用shiroEXP
                    Shiro_550 shiro_550 = new Shiro_550(targetUrl, key, secret, isAESGCM);
                    String info = "";
                    // 如果输入了秘钥， 则只对该秘钥进行检测
                    if (secret != null && !secret.equalsIgnoreCase("")) {
                        info = shiro_550.singleKeyTestTask(secret);
                    } else {
                        info = shiro_550.allKeyTestTask();
                    }
                    expLog.appendText(info);
                } catch (Exception e) {
                }
            }
        });

        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.add(keyLabel, 0, 0);
        gridPane.add(keyWord, 1, 0);
        gridPane.add(securityLabel, 2, 0);
        gridPane.add(securityKey, 3, 0);
        gridPane.add(aesGCM, 4, 0);
        gridPane.add(aesGCMLabel, 5, 0);
        gridPane.add(detectKey, 6, 0);
        gridPane.setAlignment(Pos.CENTER);
        keyPane.setContent(gridPane);
        keyPane.setPrefWidth(expPane.getPrefWidth());

        GridPane exp = getBaseEXP();
        exp.setVgap(10);
        exp.setAlignment(Pos.CENTER);
        TitledPane expTPane = new TitledPane();
        expTPane.setPrefWidth(expPane.getPrefWidth());
        expTPane.setText("Payload");
        expTPane.setContent(exp);
        VBox vBox = new VBox();
        keyPane.setCollapsible(false);
        expTPane.setCollapsible(false);
        vBox.getChildren().add(keyPane);
        vBox.getChildren().add(expTPane);
        vBox.getChildren().add(expLog);
        expPane.getChildren().add(vBox);
    }


    public void getBaseEXP1(Pane expPane) throws Exception {
        if (expLog == null) {
            expLog = new TextArea();
        }
        expLog.setPrefHeight(390);
        GridPane exp = getBaseEXP();
        exp.setVgap(10);
        exp.setAlignment(Pos.CENTER);
        TitledPane expTPane = new TitledPane();
        expTPane.setCollapsible(false);
        expTPane.setPrefWidth(expPane.getPrefWidth());
        expTPane.setText("Payload");
        expTPane.setContent(exp);
        VBox vBox = new VBox();
        vBox.getChildren().add(expTPane);
        vBox.getChildren().add(expLog);
        expPane.getChildren().add(vBox);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // System Out Redirection
        OutputStream out = new OutputStream() {
            @Override
            public void write(int b) throws IOException {
                appendText(String.valueOf((char) b));
            }
        };
        System.setOut(new PrintStream(out, true));

        //exps
        exps.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {

                // loadEXP
                expPane.getChildren().clear();
                try {
                    if (newValue != null && newValue.equalsIgnoreCase("550")) {
                        getShiroEXP(expPane);
                    } else {
                        getBaseEXP1(expPane);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        // Modules
        modules.setItems(FXCollections.observableList(Config.supportModules));
        modules.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                List<String> values = Config.moduleExps.get(newValue.toString());
                if (values != null && values.size() > 0) {
                    exps.setItems(FXCollections.observableList(values));
                    exps.getSelectionModel().selectFirst();
                }

            }
        });
        modules.getSelectionModel().selectFirst();

        // Generate
        GenerateServerBox.setItems(FXCollections.observableList(Config.serverNameList));
        GenerateServerBox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                List<String> values = Config.serverComponent.get(newValue.toString());
                if (values != null && values.size() > 0) {
                    GenerateComponentsBox.setItems(FXCollections.observableList(values));
                } else {
                    GenerateComponentsBox.setItems(FXCollections.observableList(Arrays.asList("Filter", "Listener")));
                }
                GenerateComponentsBox.getSelectionModel().selectFirst();
            }
        });
        GenerateServerBox.getSelectionModel().selectFirst();
    }

    /**
     * Resolve System Out Redirection
     */

    public void appendText(final String strResult) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                // 根据头部信息，判断输入到那个textarea
                if (strResult.contains("+")) {
                    expLog.appendText(strResult);
                } else {
                }
            }
        });
    }

    public void appendTextToExpLog() {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
//
            }
        });
    }

    public static class Console extends OutputStream {

        private TextArea output;

        public Console(TextArea ta) {
            this.output = ta;
        }

        @Override
        public void write(int i) throws IOException {
            output.appendText(String.valueOf((char) i));
        }
    }


    /**
     * General Function
     */

    public static String getLocalTime() {
        Date d = new Date();
        DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(d);
    }


}
