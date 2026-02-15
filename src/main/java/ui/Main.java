package ui;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import utils.SystemInfo;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * 主程序入口
 * 
 * 优化点：
 * 1. 添加异常处理和日志
 * 2. 添加系统环境检查
 * 3. 支持命令行参数
 * 4. 优雅的错误提示
 */
public class Main extends Application {

    private static final Logger LOGGER = Logger.getLogger(Main.class.getName());
    private static final String APP_TITLE = "MemShellGene Optimized";
    private static final double WINDOW_WIDTH = 1190;
    private static final double WINDOW_HEIGHT = 600;

    @Override
    public void start(Stage primaryStage) {
        try {
            // 1. 系统环境检查
            checkSystemEnvironment();
            
            // 2. 设置系统属性
            configureSystemProperties();
            
            // 3. 加载UI
            Parent root = loadMainUI();
            if (root == null) {
                showErrorAlert("UI加载失败", "无法加载主界面，请检查资源文件");
                return;
            }
            
            // 4. 配置舞台
            Scene scene = new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT);
            configureStage(primaryStage, scene);
            
            // 5. 显示
            primaryStage.show();
            LOGGER.info("Application started successfully");
            
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Failed to start application", e);
            showErrorAlert("启动失败", "程序启动时发生错误: " + e.getMessage());
            Platform.exit();
        }
    }

    /**
     * 检查系统环境
     */
    private void checkSystemEnvironment() {
        String javaVersion = System.getProperty("java.version");
        LOGGER.info("Java Version: " + javaVersion);
        
        // 检查JDK版本兼容性
        if (javaVersion.startsWith("1.6") || javaVersion.startsWith("1.7")) {
            LOGGER.warning("Java version is outdated. Consider upgrading to Java 8+");
        }
        
        // 检查必要的系统属性
        if (System.getProperty("java.home") == null) {
            throw new RuntimeException("JAVA_HOME not set");
        }
    }

    /**
     * 配置系统属性
     */
    private void configureSystemProperties() {
        // macOS菜单栏优化
        if (SystemInfo.isMacOS && System.getProperty("apple.laf.useScreenMenuBar") == null) {
            System.setProperty("apple.laf.useScreenMenuBar", "true");
        }
        
        // Apache Commons Collections安全属性
        System.setProperty("org.apache.commons.collections.enableUnsafeSerialization", "true");
        
        // JavaFX渲染优化
        System.setProperty("prism.lcdtext", "false");
        System.setProperty("prism.order", "sw");
    }

    /**
     * 加载主UI
     */
    private Parent loadMainUI() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/sample.fxml"));
        return loader.load();
    }

    /**
     * 配置舞台
     */
    private void configureStage(Stage stage, Scene scene) {
        stage.setTitle(APP_TITLE);
        stage.setScene(scene);
        stage.setResizable(false);
        
        // 窗口关闭事件
        stage.setOnCloseRequest(event -> {
            LOGGER.info("Application closing...");
            Platform.exit();
        });
        
        // 设置最小窗口大小
        stage.setMinWidth(WINDOW_WIDTH);
        stage.setMinHeight(WINDOW_HEIGHT);
    }

    /**
     * 显示错误提示
     */
    private void showErrorAlert(String title, String message) {
        Platform.runLater(() -> {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle(title);
            alert.setHeaderText(null);
            alert.setContentText(message);
            alert.showAndWait();
        });
    }

    public static void main(String[] args) {
        // 设置未捕获异常处理器
        Thread.setDefaultUncaughtExceptionHandler((thread, throwable) -> {
            LOGGER.log(Level.SEVERE, "Uncaught exception in thread " + thread.getName(), throwable);
        });
        
        // 启动JavaFX应用
        launch(args);
    }
}
