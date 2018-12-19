package cn.greatoo.easymill.ui.teach;

import cn.greatoo.easymill.external.communication.socket.CNCSocketCommunication;
import cn.greatoo.easymill.external.communication.socket.RobotSocketCommunication;
import cn.greatoo.easymill.external.communication.socket.RobotStatusChangeThread;
import cn.greatoo.easymill.external.communication.socket.TeachSocketThread;
import cn.greatoo.easymill.ui.alarms.AlarmListenThread;
import cn.greatoo.easymill.ui.alarms.AlarmView;
import cn.greatoo.easymill.ui.main.Controller;
import cn.greatoo.easymill.ui.main.MainViewController;
import cn.greatoo.easymill.util.ThreadManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.GridPane;

public class TeachMainContentViewController extends Controller{
	public static TeachMainContentViewController INSTANCE; 
	@FXML
	private GridPane gridPane;
	@FXML
	private Button btnStart;
	@FXML
	private Button btnStartAll;
	@FXML
	private Button reset;
	@FXML
	private Button save;
	@FXML
	private Label messegeText;
	@FXML
	private Button stopBt;
	private ToolBar toolBarMenu;
	public void init(ToolBar toolBarMenu) {	
		this.toolBarMenu = toolBarMenu;
		stopBt.setVisible(false);
		messegeText.setVisible(false);
		gridPane.setVisible(true);
	}
	@FXML
	public void btnStartAction(ActionEvent event) {
		
	}
	
	@FXML
	public void btnStartAllAction(ActionEvent event) {		
		RobotSocketCommunication roboSocketConnection = AlarmListenThread.roboSocketConnection;
		CNCSocketCommunication cncSocketConnection = AlarmListenThread.cncSocketConnection;	
		if(roboSocketConnection != null && cncSocketConnection != null) {
			toolBarMenu.setDisable(true);
			gridPane.setVisible(false);
			stopBt.setVisible(true);
			messegeText.setVisible(true);
			TeachSocketThread teachSocketThread = new TeachSocketThread(roboSocketConnection,cncSocketConnection);
			ThreadManager.submit(teachSocketThread);
		}else {
			showNotificationOverlay(MainViewController.parentStackPane, "示教错误", "请注意，设备连接错误！");
		}
	}
	
	@FXML
	public void resetAction(ActionEvent event) {
		
	}
	
	@FXML
	public void saveAction(ActionEvent event) {
		
	}
	@FXML
	public void stopBtAction(ActionEvent event) {
		toolBarMenu.setDisable(false);
		stopBt.setVisible(false);
		messegeText.setVisible(false);
		gridPane.setVisible(true);
		//ThreadManager.shutDown();
	}
	
	public void setMessege(String messege) {
		if(messegeText != null) {
			messegeText.setText(messege);
		}
	}
	
}
