package com.learning;

import javafx.geometry.Pos;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.application.Application;


public class GUI{

	// JAVAFX COMPONENTS
	private Button startServer;
	private Button stopServer;
	private static TextArea chatLogs;
	private ScrollPane chatLogsLayout;
	private GridPane layout;
	private Scene scene;
	private Stage stage;
	// ACTUAL SERVER
	Server server;
	// SERVER THREAD
	Thread serverThread;


	private void layoutComponents(){
		this.layout.add(this.startServer, 0, 0);
		this.layout.add(this.stopServer, 1, 0);
		this.layout.add(this.chatLogsLayout, 0, 1, 2, 10);
	}


	private void switchButtonsDisability(){
		if(this.startServer.isDisabled()){
			this.stopServer.setDisable(true);
			this.startServer.setDisable(false);
		}
		else{
			this.stopServer.setDisable(false);
			this.startServer.setDisable(true);
		}
	}

	private void createObjects(){
		this.startServer = new Button("Start server");
		this.stopServer = new Button("Stop server");
		chatLogs = new TextArea("");
		this.chatLogsLayout = new ScrollPane();
		this.layout = new GridPane();
		this.scene = new Scene(this.layout, 600, 500);
		this.stage = new Stage();
	}


	private void setObjectProperties(){
		this.stopServer.setDisable(true);
		chatLogs.prefWidthProperty().bind(layout.widthProperty());
		chatLogs.prefHeight(400);
		this.chatLogsLayout.prefWidthProperty().bind(layout.widthProperty());
		this.chatLogsLayout.prefHeight(800);
		this.chatLogsLayout.setContent(chatLogs);
		//this.layout.setAlignment(Pos.CENTER);
		this.stage.setScene(this.scene);
		this.stage.setResizable(false);
		this.stage.setTitle("Chat server");
	}

	private void stopButtonAction(){
		this.server.stopServer();
		try {
			serverThread.join();
		}
		catch(Exception e) {}
		this.switchButtonsDisability();
	}

	private void startButtonAction(){
			server = new Server(49191);
			serverThread = new Thread(server);
			serverThread.start();
			this.switchButtonsDisability();
	}

	private void setActions(){
		this.startServer.setOnAction(event -> {this.startButtonAction();});
		this.stopServer.setOnAction(event -> {this.stopButtonAction();});
	}



	public GUI(){
		// create objects
		this.createObjects();
		// set object properties
		this.setObjectProperties();
		// set actions
		this.setActions();
		// layout components
		this.layoutComponents();
	}

	public void show(){
		this.stage.showAndWait();
	}


	public static void appendChatText(String text){
		GUI.chatLogs.appendText(text);
	}

}
