package com.diesel.dz.ui.main;

import com.diesel.dz.jfxutils.chart.StableTicksAxis;
import com.fazecast.jSerialComm.SerialPort;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXSlider;
import javafx.application.Platform;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import org.kordamp.ikonli.javafx.FontIcon;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.LinkedHashMap;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.kordamp.ikonli.dashicons.Dashicons.*;
import static org.kordamp.ikonli.material.Material.ERROR;
import static org.kordamp.ikonli.material.Material.STOP;

public class main implements Initializable {

    // SerialPort spConnected;


    @FXML
    private JFXButton arrowButton;

 /*   @FXML
    private ScrollPane scrollPane;*/


    @FXML
    private FontIcon runIcon;

    @FXML
    private AnchorPane centerAnchorPane;


    @FXML
    private ScrollPane scrollPane;


    @FXML
    private JFXButton runButton;


    @FXML
    private JFXSlider vZoomSlider;

    @FXML
    private JFXSlider hZoomSlider;

    @FXML
    private FontIcon arrowIcon;

    @FXML
    private VBox leftVBox;

    @FXML
    private AnchorPane leftAnchorPane;


    @FXML
    private JFXButton channelButton1;

    @FXML
    private JFXButton channelButton2;

    @FXML
    private JFXButton channelButton3;

    @FXML
    private JFXButton channelButton4;

    @FXML
    private JFXButton channelButton5;

    @FXML
    private JFXButton channelButton6;

    @FXML
    private JFXButton channelButton7;

    @FXML
    private JFXButton channelButton8;

    @FXML
    private VBox chartVBox;


    @FXML
    private JFXComboBox<String> portCombo;


    @FXML
    private FontIcon connectIcon;

    @FXML
    private JFXButton connectButton;

    private SerialPort spConnected;
    private boolean run = false;
    Thread th;

    private double xUpperBound = 200;
    private double xLowerBound = 0;
    private double xTickUnit = 5;
    private double xMinUpLowDifference = 2;
    private double xMaxUpLowDifference = 200;
    private double xActUpLowDifference = 200;

    private double yUpperBound = 12;
    private double yLowerBound = -5;
    private double yTickUnit = 5;

    private DoubleProperty minChartWidthProperty;
    private double minChartWidth = 50.;
    private double maxChartWidth = 4000.;
    private double prefChartWidth = 600.;

    private double minChartHeight = 50.;
    private double prefChartHeight = 100.;
    private double maxChartHeight = 550;

    private boolean fourInjectors = true;


    XYChart.Series<Number, Number> emptySeries, crank, cam, injector1, injector2, injector3, injector4, injector5, injector6;
    private double heightPaceInc = 5;
    private double widthPaceInc = 500;


    private LinkedHashMap<Integer, LineChart> chartsList;
    private LinkedHashMap<Integer, StableTicksAxis> xAxisList;
    private LinkedHashMap<Integer, StableTicksAxis> yAxisList;
    private LinkedHashMap<Integer, JFXButton> channelButtonsList;
    private LinkedHashMap<Integer, Boolean> channelButtonsOnList;
    private boolean vSlide;
    private boolean hSlide;
    private int ctr = 0;
    private boolean connect = false;
    private URL url = null;
    private ResourceBundle rb = null;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        if (this.url == null) this.url = url;
        if (this.rb == null) this.rb = rb;
        ConnectToPort();
//        try {
//            while (!connect) {
//                Thread.sleep(1000);
//            }
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//          //  if (!connect) initialize(url, rb);
//        }

        vZoomSlider.setValue(0);
        hZoomSlider.setValue(0);
//        vZoomSlider.setDisable(true);
//        hZoomSlider.setDisable(true);
        initializeCharts();


    }


    @FXML
    void runOnAction(ActionEvent event) {
        run = !run;
        if (run) {
            deleteAllCharts();
            //   hZoomSlider.setValue(hZoomSlider.getValue());
//            if (hZoomSlider.isDisable()) hZoomSlider.setDisable(false);
//            if (vZoomSlider.isDisable()) vZoomSlider.setDisable(false);
            runIcon.setIconColor(Paint.valueOf("#F0151B"));
            runIcon.setIconCode(STOP);
            cam.getData().clear();
            for (int j = 1; j <= 8; j++) {
//                    chartsList.get(j).setPrefWidth(scrollPane.getWidth());
//                    hZoomSlider.setValue(0);
            }

            th = new Thread(new ArduinoReader());
            th.start();
        } else {
/*            if (hZoomSlider.isDisable()) hZoomSlider.setDisable(false);
            if (vZoomSlider.isDisable()) vZoomSlider.setDisable(false);*/
            runIcon.setIconColor(Paint.valueOf("#00FF79"));
            runIcon.setIconCode(CONTROLS_PLAY);


            //  th.interrupt();
        }
    }

    public void arrowOnAction(ActionEvent actionEvent) {
        if (arrowIcon.getIconCode() == ARROW_RIGHT_ALT2) {
            arrowIcon.setIconCode(ARROW_LEFT_ALT2);
            leftAnchorPane.getChildren().add(leftVBox);
            AnchorPane.setBottomAnchor(leftVBox, 0.0);
            AnchorPane.setLeftAnchor(leftVBox, 0.0);
            AnchorPane.setTopAnchor(leftVBox, 0.0);
            AnchorPane.setRightAnchor(leftVBox, 20.0);
        } else {
            arrowIcon.setIconCode(ARROW_RIGHT_ALT2);
            leftAnchorPane.getChildren().remove(leftVBox);


        }


    }

    public void channelOnAction(ActionEvent actionEvent) {
        for (int x : channelButtonsList.keySet()) {
            if (actionEvent.getSource() == channelButtonsList.get(x)) {
                if (!channelButtonsOnList.get(x)) {
                    channelButtonsList.get(x).setOpacity(1);
                    chartVBox.getChildren().remove(x - 1);
                    chartVBox.getChildren().add(x - 1, chartsList.get(x));
                } else {
                    Label line = new Label();
                    line.setMaxHeight(0);
                    line.setMinHeight(0);
                    line.setPrefHeight(0);
                    channelButtonsList.get(x).setOpacity(0.1);
                    chartVBox.getChildren().add(x - 1, line);
                    chartVBox.getChildren().remove(chartsList.get(x));
                }

                channelButtonsOnList.put(x, !channelButtonsOnList.get(x));
                break;
            }
        }


    }

    private void initializeCharts() {
        minChartWidthProperty = new SimpleDoubleProperty();
        minChartWidthProperty.setValue(minChartWidth);
        chartsList = new LinkedHashMap<>();
        xAxisList = new LinkedHashMap<>();
        yAxisList = new LinkedHashMap<>();
        channelButtonsList = new LinkedHashMap<>();
        channelButtonsOnList = new LinkedHashMap<>();

        for (int j = 1; j <= 8; j++) {
            StableTicksAxis xAxis = new StableTicksAxis();
            xAxis.getStyleClass().add("x-axis");
            xAxisList.put(j, xAxis);
            StableTicksAxis yAxis = new StableTicksAxis();
            yAxis.getStyleClass().add("y-axis");
            yAxis.getStyleClass().add("y-axis" + j);
            yAxisList.put(j, yAxis);
            LineChart<Number, Number> lineChart = new LineChart<>(xAxis, yAxis);
            chartsList.put(j, lineChart);
            lineChart.getStyleClass().add("chart" + j);

            lineChart.setMinWidth(minChartWidthProperty.getValue());
            lineChart.setMinHeight(minChartHeight);
            lineChart.setPrefWidth(prefChartWidth);
            lineChart.setPrefHeight(prefChartHeight);

            xAxis.setLowerBound(xLowerBound);
            xAxis.setUpperBound(xUpperBound);
            xAxis.setAutoRanging(false);
            xAxis.setAnimated(false);
//            xAxis.setTickUnit(xTickUnit);

            yAxis.setAutoRanging(false);
            yAxis.setLowerBound(yLowerBound);
            yAxis.setUpperBound(yUpperBound);
//            yAxis.setTickUnit(yTickUnit);
            yAxis.setAnimated(false);

        }

        channelButtonsList.put(1, channelButton1);
        channelButtonsList.put(2, channelButton2);
        channelButtonsList.put(3, channelButton3);
        channelButtonsList.put(4, channelButton4);
        channelButtonsList.put(5, channelButton5);
        channelButtonsList.put(6, channelButton6);
        channelButtonsList.put(7, channelButton7);
        channelButtonsList.put(8, channelButton8);

        for (int j = 1; j <= 8; j++) channelButtonsOnList.put(j, true);

        chartVBox.getChildren().clear();
        if (fourInjectors) {
            chartVBox.getChildren().addAll(chartsList.get(1), chartsList.get(2), chartsList.get(3), chartsList.get(4), chartsList.get(5), chartsList.get(6));
            channelButtonsList.get(7).setDisable(true);
            channelButtonsList.get(8).setDisable(true);

        } else
            chartVBox.getChildren().addAll(chartsList.get(1), chartsList.get(2), chartsList.get(3), chartsList.get(4), chartsList.get(5), chartsList.get(6), chartsList.get(7), chartsList.get(8));
        leftAnchorPane.getChildren().remove(leftVBox);
        scrollPane.setHvalue(0.0);


        scrollPane.heightProperty().addListener((observable, oldValue, newValue) -> {
            double height = (double) newValue;
            if (height > chartVBox.getHeight()) {
                chartVBox.setMinHeight((Double) newValue);
            }
        });

        scrollPane.widthProperty().addListener((observable, oldValue, newValue) -> {
            double width = (double) newValue;
            for (int j = 1; j <= 8; j++) chartsList.get(j).setMinWidth(width - 20);


//            if (trashOn)
//                for (int j = 1; j <= 16; j++) {
//                    chartsList.get(j).setMinWidth(width);
//                    chartsList.get(j).setPrefWidth(width);
//                    chartsList.get(j).setMaxWidth(width);
//                }
        });

     /*   minChartWidthProperty.addListener((observable, oldValue, newValue) -> {
            for (int j = 1; j <= 8; j++) {
                chartsList.get(j).setMinWidth((Double) newValue);
            }


        });

        chartsList.get(1).widthProperty().addListener((observable, oldValue, newValue) -> {
            if(hSlide){hSlide = false; return; }
            if (run)
                if (chartsList.get(1).getWidth() > scrollPane.getWidth()) scrollPane.setHvalue((Double) newValue);
        });
*/


        vZoomSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            vSlide = true;
            double x = (double) newValue;
            for (int y : chartsList.keySet()) {
                chartsList.get(y).setPrefHeight(minChartHeight + heightPaceInc * x);
//                yAxisList.get(y).setTickUnit(yTickUnit*10/x);
            }
        });

        hZoomSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            double x = (double) newValue;
          /*  System.out.println("xxxxxxxxx" + ctr);
            if(!run && ctr!= 0){
                for (int  y : chartsList.keySet()){
                    chartsList.get(y).setPrefWidth(maxChartWidth + widthPaceInc * x);
                    chartsList.get(y).setMaxWidth(maxChartWidth + widthPaceInc * x);
                    chartsList.get(y).setMinWidth(maxChartWidth + widthPaceInc * x);
//                yAxisList.get(y).setTickUnit(yTickUnit*10/x);
                }
                return;
            }*/
            double y = xMaxUpLowDifference - (xMaxUpLowDifference - xMinUpLowDifference) * x / 100.;
            for (StableTicksAxis axis : xAxisList.values()) {
                axis.setUpperBound(axis.getLowerBound() + y);

            }
            xActUpLowDifference = y;

        });


        crank = new XYChart.Series<>();
        cam = new XYChart.Series<>();
        injector1 = new XYChart.Series<>();
        injector2 = new XYChart.Series<>();
        injector3 = new XYChart.Series<>();
        injector4 = new XYChart.Series<>();
        injector5 = new XYChart.Series<>();
        injector6 = new XYChart.Series<>();


        chartsList.get(1).getData().add(crank);
        chartsList.get(2).getData().add(cam);
        chartsList.get(3).getData().add(injector1);
        chartsList.get(4).getData().add(injector2);
        chartsList.get(5).getData().add(injector3);
        chartsList.get(6).getData().add(injector4);
        if (!fourInjectors) {
            chartsList.get(7).getData().add(injector5);
            chartsList.get(8).getData().add(injector6);

        }


    }

    public void deleteAllCharts() {

        if (!crank.getData().isEmpty()) crank.getData().remove(0, crank.getData().size());
        if (!cam.getData().isEmpty()) cam.getData().remove(0, cam.getData().size());
        if (!injector1.getData().isEmpty()) injector1.getData().remove(0, injector1.getData().size());
        if (!injector2.getData().isEmpty()) injector2.getData().remove(0, injector2.getData().size());
        if (!injector3.getData().isEmpty()) injector3.getData().remove(0, injector3.getData().size());
        if (!injector4.getData().isEmpty()) injector4.getData().remove(0, injector4.getData().size());
        if (!injector4.getData().isEmpty()) injector5.getData().remove(0, injector5.getData().size());
        if (!injector6.getData().isEmpty()) injector6.getData().remove(0, injector6.getData().size());

        for (int j = 1; j <= 8; j++) {
            int x1 = (int) xAxisList.get(j).getLowerBound();
            int x2 = (int) xAxisList.get(j).getUpperBound();
            chartsList.get(j).setMinWidth(scrollPane.getWidth());
            chartsList.get(j).setPrefWidth(scrollPane.getWidth());
            chartsList.get(j).setMaxWidth(scrollPane.getWidth());

            xAxisList.get(j).setLowerBound(0);
            xAxisList.get(j).setUpperBound(xActUpLowDifference);
//            xAxisList.get(j).setTickUnit(xTickUnit);
//            yAxisList.get(j).setLowerBound(yLowerBound);
//            yAxisList.get(j).setUpperBound(yUpperBound);
//            yAxisList.get(j).setTickUnit(yTickUnit);
        }
        ctr = 0;
    }


    public void saveOnAction(ActionEvent actionEvent) {

    }

    public void connectOnAction(ActionEvent actionEvent) {
        if (spConnected != null && spConnected.isOpen()) spConnected.closePort();
        spConnected = null;
        ConnectToPort();

    }


    private void ConnectToPort() {
        SerialPort[] cmPorts = SerialPort.getCommPorts();
        System.out.println(cmPorts);
        if (cmPorts.length >= 1) {
            for (SerialPort comPort : cmPorts) {
                SerialPort sp = comPort;
                sp.setBaudRate(115200);
                sp.setComPortTimeouts(SerialPort.TIMEOUT_NONBLOCKING, 100, 100);

                connect = connect(comPort);
                if (connect) {
                    spConnected = sp;
                    spConnected.setBaudRate(115200);
                    spConnected.setComPortTimeouts(SerialPort.TIMEOUT_NONBLOCKING, 0, 0);
//                    new Thread(() -> {
//                        while (run) {
//                            try {
//                                InputStream portInputStream = sp.getInputStream();
//                                if (portInputStream == null) {
//                                    //   isRun = false;
//                                    break;
//                                }
//                                BufferedReader bufferedReader
//                                        = new BufferedReader(new InputStreamReader(
//                                        portInputStream));
//
//                                String data = bufferedReader.readLine();
//                                //debug
//                                System.out.println(".serialEvent() " + data);
//
//                                // we are in a different thread so update the label on ui thread
//                                Platform.runLater(() -> {
//                                });
//
//                                Thread.sleep(500);
//                            } catch (IOException ex) {
//
//                                System.out.println(".serialEvent() time out no data read");
//                            } catch (InterruptedException ex) {
//                                Logger.getLogger(main.class.getName()).log(Level.SEVERE, null, ex);
//                            }
//                        }
//                    }).start();
                    break;
                }
            }
        } else connect = false;

        if (connect) {
            connectButton.setText("Connected");
            connectIcon.setStyle("-fx-icon-color: #00c600;");
            connectIcon.setIconCode(YES_ALT);

        } else {
            spConnected = null;
            connectButton.setText("Not Connected");
            connectIcon.setStyle("-fx-icon-color: #fa3c00;");
            connectIcon.setIconCode(ERROR);
        }


    }

    private boolean connect(SerialPort sp) {
        System.out.println("connect");
        sp.openPort();

        InputStream portInputStream = null;
        BufferedReader bufferedReader = null;
        String data = null;
        for (int j = 0; j < 11; j++) {
            try {
                portInputStream = sp.getInputStream();
                 bufferedReader = new BufferedReader(new InputStreamReader(portInputStream));
                 data = bufferedReader.readLine();
                System.out.println(".serialEvent() " + data);
//                if (data.equals("dkflD8F5S F8S5q4f8f7sqsSF74DFD4S")) return true;
//                else return false;

                System.out.println("portInputStream" + portInputStream);
                Thread.sleep(100);
                break;

            } catch (Exception ex) {
                System.out.println(".serialEvent() time out no data read********");
            }
        }
        if (data == null) return false;
        else return true;






    }


    class ArduinoReader implements Runnable {

        @Override
        public void run() {

            try {

                ctr = 0;
                while (run) {
                    System.out.println("up " + xAxisList.get(1).getUpperBound() + " low " + xAxisList.get(1).getLowerBound());
                    int finalCtr = ctr;
                    Platform.runLater(() -> {
                        System.out.println("qqqqq999 " + finalCtr + "  " + xActUpLowDifference);

                        crank.getData().add(new XYChart.Data<>(finalCtr, Math.random() * 10));
//                        cam.getData().add(new XYChart.Data<>(finalCtr, Math.random() * 10));
//                        injector1.getData().add(new XYChart.Data<>(finalCtr, Math.random() * 10));
//                        injector2.getData().add(new XYChart.Data<>(finalCtr, Math.random() * 10));
//                        injector3.getData().add(new XYChart.Data<>(finalCtr, Math.random() * 10));
//                        injector4.getData().add(new XYChart.Data<>(finalCtr, Math.random() * 10));
//                        injector5.getData().add(new XYChart.Data<>(finalCtr, Math.random() * 10));
//                        injector6.getData().add(new XYChart.Data<>(finalCtr, Math.random() * 10));
                        if (finalCtr > xActUpLowDifference + 5) {
                            for (int j = 1; j <= 1; j++) {
                                xAxisList.get(j).setUpperBound(xAxisList.get(j).getUpperBound() + 1);
                                xAxisList.get(j).setLowerBound(xAxisList.get(j).getLowerBound() + 1);
                            }
                            crank.getData().remove(0);
//                            cam.getData().remove(0);
//                            injector1.getData().remove(0);injector2.getData().remove(0);
//                            injector3.getData().remove(0);injector4.getData().remove(0);
//                            injector5.getData().remove(0);injector6.getData().remove(0);


                        }
                    });


                    ctr++;
                    Thread.sleep(20);
                }
            } catch (Exception ex) {
                Logger.getLogger(main.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }


}
