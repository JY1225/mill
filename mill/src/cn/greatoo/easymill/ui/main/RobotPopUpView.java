package cn.greatoo.easymill.ui.main;

import cn.greatoo.easymill.external.communication.socket.AbstractCommunicationException;
import cn.greatoo.easymill.external.communication.socket.SocketDisconnectedException;
import cn.greatoo.easymill.external.communication.socket.SocketResponseTimedOutException;
import cn.greatoo.easymill.external.communication.socket.SocketWrongResponseException;
import cn.greatoo.easymill.robot.FanucRobot;
import cn.greatoo.easymill.ui.alarms.AlarmView;
import cn.greatoo.easymill.util.UIConstants;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class RobotPopUpView extends StackPane{
	public static RobotPopUpView INSTANCE = null;
	private VBox vBoxMenuItems1;
	private VBox vBoxMenuItems2;
	
	private Button btnReset;
	
	private Button btnRestart;
	private Button btnToHome;
	private Button btnToChange;
	private Button btnOpenA;
	private Button btnCloseA;
	private Button btnOpenB;
	private Button btnCloseB;
	private Button btnToCustomPos;
	
	private Button btn5;
	private Button btn10;
	private Button btn25;
	private Button btn50;
	private Button btn75;
	private Button btn100;
	
	private static final int BUTTON_HEIGHT = UIConstants.BUTTON_HEIGHT + 5;
	private static final int WIDTH = BUTTON_HEIGHT * 4;

	
	private static final String CSS_CLASS_POPUP_BUTTON = "pop-up-btn";
	private static final String CSS_CLASS_POPUP_BUTTON_BOTTOM = "pop-up-btn-bottom";

	private int speed = 10;
	public RobotPopUpView() {
		this.setVisible(false);
		build();
		this.setTranslateX(60);
		this.setTranslateY(43);
	}
	protected void build() {
		HBox hbox = new HBox();
		
		vBoxMenuItems1 = new VBox();
		vBoxMenuItems2 = new VBox();
		
		hbox.setSpacing(-1);
		hbox.getChildren().addAll(vBoxMenuItems1, vBoxMenuItems2);
		
		this.getChildren().add(hbox);
		
		btnReset = new Button();
		btnReset.setGraphic(new Text("重置"));
		btnReset.setPrefSize(WIDTH, BUTTON_HEIGHT);
		btnReset.getStyleClass().add(CSS_CLASS_POPUP_BUTTON);
		btnReset.getStyleClass().add("pop-up-first");
		btnReset.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(final ActionEvent arg0) {
				try {
					if(FanucRobot.getInstance(null,0,null) != null && FanucRobot.getInstance(null,0,null).isConnected()) {
						FanucRobot.getInstance(null,0,null).reset();
					}
				} catch (AbstractCommunicationException | InterruptedException e) {
					
				}
			}
		});
		
		btnRestart = new Button();
		btnRestart.setGraphic(new Text("重启"));
		btnRestart.setPrefSize(WIDTH, BUTTON_HEIGHT);
		btnRestart.getStyleClass().add(CSS_CLASS_POPUP_BUTTON);
		btnRestart.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(final ActionEvent arg0) {
				try {
					if(FanucRobot.getInstance(null,0,null) != null && FanucRobot.getInstance(null,0,null).isConnected()) {
						FanucRobot.getInstance(null,0,null).restartProgram();
					}
				} catch (AbstractCommunicationException | InterruptedException e) {
					
				}
			}
		});
		
		btnToHome = new Button();
		btnToHome.setGraphic(new Text("回原点"));
		btnToHome.setPrefSize(WIDTH, BUTTON_HEIGHT);
		btnToHome.getStyleClass().add(CSS_CLASS_POPUP_BUTTON);
		btnToHome.getStyleClass().add("pop-up-first");
		btnToHome.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(final ActionEvent arg0) {
				try {
					if(FanucRobot.getInstance(null,0,null) != null && FanucRobot.getInstance(null,0,null).isConnected()) {
						FanucRobot.getInstance(null,0,null).moveToHome(speed);
					}
				} catch (SocketDisconnectedException | SocketResponseTimedOutException | SocketWrongResponseException
						| InterruptedException e) {
					
				}
			}
		});
		
		btnToChange = new Button();
		btnToChange.setGraphic(new Text("to change"));
		btnToChange.setPrefSize(WIDTH, BUTTON_HEIGHT);
		btnToChange.getStyleClass().add(CSS_CLASS_POPUP_BUTTON);
		btnToChange.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(final ActionEvent arg0) {
				try {
					if(FanucRobot.getInstance(null,0,null) != null && FanucRobot.getInstance(null,0,null).isConnected()) {
						FanucRobot.getInstance(null,0,null).moveToChangePoint();
					}
				} catch (AbstractCommunicationException | InterruptedException e) {
					
				}
			}
		});
		
		btn5 = new Button();
		btn5.setGraphic(new Text("5%"));
		btn5.setPrefSize(WIDTH, BUTTON_HEIGHT);
		btn5.getStyleClass().add(CSS_CLASS_POPUP_BUTTON);
		btn5.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(final ActionEvent arg0) {
				setSpeedClicked(5);
			}
		});
		
		btn10 = new Button();
		btn10.setGraphic(new Text("10%"));
		btn10.setPrefSize(WIDTH, BUTTON_HEIGHT);
		btn10.getStyleClass().add(CSS_CLASS_POPUP_BUTTON);
		btn10.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(final ActionEvent arg0) {
				setSpeedClicked(10);
			}
		});
		
		btn25 = new Button();
		btn25.setGraphic(new Text("25%"));
		btn25.setPrefSize(WIDTH, BUTTON_HEIGHT);
		btn25.getStyleClass().add(CSS_CLASS_POPUP_BUTTON);
		btn25.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(final ActionEvent arg0) {
				setSpeedClicked(25);
			}
		});
		
		btn50 = new Button();
		btn50.setGraphic(new Text("50%"));
		btn50.setPrefSize(WIDTH, BUTTON_HEIGHT);
		btn50.getStyleClass().add(CSS_CLASS_POPUP_BUTTON);
		btn50.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(final ActionEvent arg0) {
				setSpeedClicked(50);
			}
		});
		
		btn75 = new Button();
		btn75.setGraphic(new Text("75%"));
		btn75.setPrefSize(WIDTH, BUTTON_HEIGHT);
		btn75.getStyleClass().add(CSS_CLASS_POPUP_BUTTON);
		btn75.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(final ActionEvent arg0) {
				setSpeedClicked(75);
			}
		});
		
		btn100 = new Button();
		btn100.setGraphic(new Text("100%"));
		btn100.setPrefSize(WIDTH, BUTTON_HEIGHT);
		btn100.getStyleClass().add(CSS_CLASS_POPUP_BUTTON);
		btn100.getStyleClass().add(CSS_CLASS_POPUP_BUTTON_BOTTOM);
		btn100.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(final ActionEvent arg0) {
				setSpeedClicked(100);
			}
		});

		btnOpenA = new Button();
		btnOpenA.setGraphic(new Text("OPEN_A"));
		btnOpenA.setPrefSize(WIDTH, BUTTON_HEIGHT);
		btnOpenA.getStyleClass().add(CSS_CLASS_POPUP_BUTTON);
		btnOpenA.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(final ActionEvent arg0) {
				try {
					if(FanucRobot.getInstance(null,0,null) != null && FanucRobot.getInstance(null,0,null).isConnected()) {
						FanucRobot.getInstance(null,0,null).openGripperA();
					}
				} catch (SocketDisconnectedException | SocketResponseTimedOutException | SocketWrongResponseException
						| InterruptedException e) {
					
				}
			}
		});
		
		btnCloseA = new Button();
		btnCloseA.setGraphic(new Text("CLOSE_A"));
		btnCloseA.setPrefSize(WIDTH, BUTTON_HEIGHT);
		btnCloseA.getStyleClass().add(CSS_CLASS_POPUP_BUTTON);
		btnCloseA.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(final ActionEvent arg0) {
				try {
					if(FanucRobot.getInstance(null,0,null) != null && FanucRobot.getInstance(null,0,null).isConnected()) {
						FanucRobot.getInstance(null,0,null).closeGripperA();
					}
				} catch (SocketDisconnectedException | SocketResponseTimedOutException | SocketWrongResponseException
						| InterruptedException e) {
					
				}
			}
		});
		
		btnOpenB = new Button();
		btnOpenB.setGraphic(new Text("OPEN_B"));
		btnOpenB.setPrefSize(WIDTH, BUTTON_HEIGHT);
		btnOpenB.getStyleClass().add(CSS_CLASS_POPUP_BUTTON);
		btnOpenB.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(final ActionEvent arg0) {
				try {
					if(FanucRobot.getInstance(null,0,null) != null && FanucRobot.getInstance(null,0,null).isConnected()) {
						FanucRobot.getInstance(null,0,null).openGripperB();
					}
				} catch (SocketDisconnectedException | SocketResponseTimedOutException | SocketWrongResponseException
						| InterruptedException e) {
					
				}
			}
		});
		
		btnCloseB = new Button();
		btnCloseB.setGraphic(new Text("CLOSE_B"));
		btnCloseB.setPrefSize(WIDTH, BUTTON_HEIGHT);
		btnCloseB.getStyleClass().add(CSS_CLASS_POPUP_BUTTON);
		btnCloseB.getStyleClass().add(CSS_CLASS_POPUP_BUTTON_BOTTOM);
		btnCloseB.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(final ActionEvent arg0) {
				try {
					if(FanucRobot.getInstance(null,0,null) != null && FanucRobot.getInstance(null,0,null).isConnected()) {
						FanucRobot.getInstance(null,0,null).closeGripperB();
					}
				} catch (SocketDisconnectedException | SocketResponseTimedOutException | SocketWrongResponseException
						| InterruptedException e) {
					
				}
			}
		});
		
		btnToCustomPos = new Button();
		btnToCustomPos.setGraphic(new Text("TO_CUSTOM_POS"));
		btnToCustomPos.setPrefSize(WIDTH, BUTTON_HEIGHT);
		btnToCustomPos.getStyleClass().add(CSS_CLASS_POPUP_BUTTON);
		btnToCustomPos.getStyleClass().add(CSS_CLASS_POPUP_BUTTON_BOTTOM);
		btnToCustomPos.setTranslateY(-1);
		btnToCustomPos.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(final ActionEvent arg0) {
				try {
					if(FanucRobot.getInstance(null,0,null) != null && FanucRobot.getInstance(null,0,null).isConnected()) {
						FanucRobot.getInstance(null,0,null).moveToCustomPosition();
					}
				} catch (AbstractCommunicationException | InterruptedException e) {
					
				}
			}
		});
		
		vBoxMenuItems1.getChildren().add(btnReset);
		vBoxMenuItems1.getChildren().add(btn5);
		vBoxMenuItems1.getChildren().add(btn10);
		vBoxMenuItems1.getChildren().add(btn25);
		vBoxMenuItems1.getChildren().add(btn50);
		vBoxMenuItems1.getChildren().add(btn75);
		vBoxMenuItems1.getChildren().add(btn100);
				
		vBoxMenuItems2.getChildren().add(btnToHome);
		vBoxMenuItems2.getChildren().add(btnRestart);
		vBoxMenuItems2.getChildren().add(btnToChange);
		vBoxMenuItems2.getChildren().add(btnOpenA);
		vBoxMenuItems2.getChildren().add(btnCloseA);
		vBoxMenuItems2.getChildren().add(btnOpenB);
		vBoxMenuItems2.getChildren().add(btnCloseB);
		
	}
	private void setSpeedClicked(int speed){
		try {
			if(FanucRobot.getInstance(null,0,null) != null && FanucRobot.getInstance(null,0,null).isConnected()) {
				this.speed = speed;
				FanucRobot.getInstance(null,0,null).setSpeed(speed);
			}
		} catch (InterruptedException | AbstractCommunicationException e) {
			
		}
	}
	/**
     * 获取实例
     * @return RobotPopUpView
     */
    public static RobotPopUpView getInstance()
    {
        if (INSTANCE == null)
        {
            INSTANCE = new RobotPopUpView();
        }
 
        return INSTANCE;
    }
}
