package cn.greatoo.easymill.cnc;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import cn.greatoo.easymill.external.communication.socket.AbstractCommunicationException;
import cn.greatoo.easymill.external.communication.socket.CNCSocketCommunication;
import cn.greatoo.easymill.external.communication.socket.SocketConnection;

public abstract class AbstractCNCMachine  {	
	private SocketConnection socketConnection;
	private int currentStatus;
	private boolean statusChanged;
	private static Object syncObject;
	private boolean stopAction;
	private Map<Integer, Integer> statusMap;
	private boolean timAllowed;
	private CNCMachineAlarm cncMachineTimeout;
	private Set<CNCMachineAlarm> alarms;
	private String name;
	private EWayOfOperating wayOfOperating;
	private MCodeAdapter mCodeAdapter;
	private int id;
	private boolean running;
	private static Logger logger = LogManager.getLogger(AbstractCNCMachine.class.getName());
	
	private static final String EXCEPTION_DISCONNECTED_WHILE_WAITING = "AbstractCNCMachine.disconnectedWhileWaiting";
	private static final String EXCEPTION_WHILE_WAITING = "AbstractCNCMachine.exceptionWhileWaiting";
	
	public AbstractCNCMachine(SocketConnection socketConnection,MCodeAdapter mCodeAdapter, final EWayOfOperating wayOfOperating) {
		this.socketConnection = socketConnection;
		this.statusChanged = false;
		syncObject = new Object();
		this.mCodeAdapter = mCodeAdapter;
		this.currentStatus = 0;
		this.stopAction = false;
		this.alarms = new HashSet<CNCMachineAlarm>();
		//default values
		this.timAllowed = false;
		this.statusMap = new HashMap<Integer, Integer>();
		this.wayOfOperating = wayOfOperating;
		running = true;
	}
	
	public int getId() {
		return id;
	}

	public void setId(final int id) {
		this.id = id;
	}
	
	public EWayOfOperating getWayOfOperating() {
		return wayOfOperating;
	}

	public void setWayOfOperating(final EWayOfOperating wayOfOperating) {
		this.wayOfOperating = wayOfOperating;
	}
	public MCodeAdapter getMCodeAdapter() {
		return mCodeAdapter;
	}
	
	public void setMCodeAdapter(final MCodeAdapter mCodeAdapter) {
		this.mCodeAdapter = mCodeAdapter;
	}
	public CNCMachineAlarm getCncMachineTimeout() {
		return cncMachineTimeout;
	}

	public void setCncMachineTimeout(final CNCMachineAlarm cncMachineTimeout) {
		this.cncMachineTimeout = cncMachineTimeout;
	}

	public void interruptCurrentAction() {
		setCncMachineTimeout(null);
		stopAction = true;
		setRunning(false);
		synchronized (syncObject) {
			syncObject.notifyAll();
		}
	}

	public void checkProcessExecutorStatus() throws InterruptedException {
		if (isRunning()) {
			return;
		} else {
			throw new InterruptedException("Executor stopped running.");
		}
	}
	
	public boolean isRunning() {
		return running;
	}

	public void setRunning(boolean running) {
		this.running = running;
	}
	public int getStatus() {
		return currentStatus;
	}
	
	public void setStatus(final int status) {
		this.currentStatus = status;
	}
	
	public Map<Integer,Integer> getStatusMap() {
		return statusMap;
	}
	
	public int getStatus(final int registerIndex) {
		return statusMap.get(registerIndex);
	}
	
	public void setStatus(final int status, final int registerIndex) {
		statusMap.put(registerIndex, status);
	}
	
	public void setAlarms(final Set<CNCMachineAlarm> alarms) {
		this.alarms = alarms;
	}
	
	public abstract void updateStatusAndAlarms() throws AbstractCommunicationException, InterruptedException;
	public abstract void disconnect();
	
	public Set<CNCMachineAlarm> getAlarms() {
		return alarms;
	}	
	
	/**
	 * This method will be called after processing a STATUS_CHANGED event, so if the waitForStatus method
	 * is waiting, it will be notified
	 */
	public void statusChanged() {
		synchronized (syncObject) {
			statusChanged = true;
			syncObject.notifyAll();
		}
	}
	
	public abstract void reset() throws AbstractCommunicationException, InterruptedException;
	public abstract void nCReset() throws AbstractCommunicationException, InterruptedException, DeviceActionException;
	public abstract void powerOff() throws AbstractCommunicationException, InterruptedException;
	public abstract void indicateAllProcessed() throws AbstractCommunicationException, InterruptedException, DeviceActionException;
	public abstract void indicateOperatorRequested(boolean requested) throws AbstractCommunicationException, InterruptedException;
	public abstract void clearIndications() throws AbstractCommunicationException, InterruptedException;
	public abstract boolean isConnected();
	//public abstract void prepareForProcess() throws AbstractCommunicationException, InterruptedException;
	protected boolean waitForStatus(final int status, final long timeout) throws InterruptedException, DeviceActionException {
		return waitForStatusCondition(new Callable<Boolean>() {
			@Override
			public Boolean call() throws Exception {
				return ((currentStatus & status) == status);
			}
		}, timeout);
	}
	
	protected boolean waitForStatusDevIntv2(final int registerIndex, final int status, final long timeout) throws InterruptedException, DeviceActionException {
		return waitForStatusCondition(new Callable<Boolean>() {
			@Override
			public Boolean call() throws Exception {
				return ((statusMap.get(registerIndex) & status) == status);
			}
		}, timeout);
	}
	
	protected boolean waitForStatusGoneDevIntv2(final int registerIndex, final int status, final long timeout) throws InterruptedException, DeviceActionException {
		return waitForStatusCondition(new Callable<Boolean>() {
			@Override
			public Boolean call() throws Exception {
				return ((statusMap.get(registerIndex) | status) == (statusMap.get(registerIndex) ^ status));
			}
		}, timeout);
	}
	protected boolean waitForMCodes(final int processId, final int... indexList) throws InterruptedException, DeviceActionException {
		String loggerString = "PRC[" + processId + "] is waiting for M CODE: " + indexList[0];
		for (int i = 1; i < indexList.length; i++) {
			loggerString += " OR " + indexList[i];
		}
		logger.info(loggerString);
		return waitForStatusCondition(new Callable<Boolean>() {
			@Override
			public Boolean call() throws Exception {
				for (int index: indexList) {
					if (mCodeAdapter.isMCodeActive(index))
						return true;
				}
				return false;
			}
		}, 0);
	}
	
	protected boolean waitForNoMCode(final int processId, final int... indexList) throws InterruptedException, DeviceActionException {
		String loggerString = "PRC[" + processId + "] is waiting for M CODE gone: " + indexList[0];
		for (int i = 1; i < indexList.length; i++) {
			loggerString += " OR " + indexList[i];
		}
		logger.info(loggerString);
		return waitForStatusCondition(new Callable<Boolean>() {
			@Override
			public Boolean call() throws Exception {
				for (int index: indexList) {
					if (mCodeAdapter.isMCodeActive(index)) {
						return false;
					}
				}
				return true;
			}
		}, 0);
	}
	protected boolean waitForStatusCondition(final Callable<Boolean> condition, final long timeout) throws InterruptedException, DeviceActionException {
		long waitedTime = 0;
		stopAction = false;
		// check status before we start
		try {
			boolean call = condition.call();
			if (call) {
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new DeviceActionException(EXCEPTION_WHILE_WAITING);
		}
		// also check connection status
		if (!isConnected()) {
			throw new DeviceActionException(EXCEPTION_DISCONNECTED_WHILE_WAITING);
		}
		while ((timeout == 0) || ((timeout > 0) && (waitedTime < timeout))) {
			// start waiting
			statusChanged = false;
			if ((timeout == 0) || ((timeout > 0) && (timeout > waitedTime))) {
				long timeBeforeWait = System.currentTimeMillis();
				synchronized (syncObject) {
					try {
						if (condition.call()) {
							return true;
						}
					} catch (Exception e) {
						e.printStackTrace();
						throw new DeviceActionException(EXCEPTION_WHILE_WAITING);
					}
					// at this point the wait is finished, either by a notify (status changed, or request to stop), or by a timeout
					if (stopAction) {
						//stopAction = false;
						throw new InterruptedException("Waiting for status got interrupted");
					}
					if (timeout > 0) {
						syncObject.wait(timeout - waitedTime);
					} else {
						syncObject.wait();
					}
				}
				// at this point the wait is finished, either by a notify (status changed, or request to stop), or by a timeout
				if (stopAction) {
					//stopAction = false;
					throw new InterruptedException("Waiting for status got interrupted");
				}
				// just to be sure, check connection
				if (!isConnected()) {
					throw new DeviceActionException(EXCEPTION_DISCONNECTED_WHILE_WAITING);
				}
				// check if status has changed
				try {
					if ((statusChanged) && (condition.call())) {
						statusChanged = false;
						return true;
					}
				} catch (Exception e) {
					e.printStackTrace();
					throw new DeviceActionException(EXCEPTION_WHILE_WAITING);
				}
				// update waited time
				waitedTime += System.currentTimeMillis() - timeBeforeWait;
			} else {
				return false;
			}
		} 
		return false;
	}
	
	protected void waitForStatus(final int status) throws DeviceActionException, InterruptedException {
		waitForStatus(status, 0);
	}
	
	protected void waitForStatusDevIntv2(final int registerIndex, final int status) throws DeviceActionException, InterruptedException {
		waitForStatusDevIntv2(registerIndex, status, 0);
	}
	
	protected void waitForStatusGoneDevIntv2(final int registerIndex, final int status) throws DeviceActionException, InterruptedException {
		waitForStatusGoneDevIntv2(registerIndex, status, 0);
	}
	
	public EDeviceGroup getType() {
		return EDeviceGroup.CNC_MACHINE;
	}

	public String toString() {
		return "AbstractCNCMachine: " + getName();
	}
	
	public String getName() {
		return name;
	}

	public void setName(final String name) {
		this.name = name;
	}
	
	public int getMaxNbOfProcesses() {
		return wayOfOperating.getNbOfSides() + 1;
	}
	
	public abstract boolean isUsingNewDevInt();
	
	public abstract CNCSocketCommunication getCNCSocketCommunication();
	
	public boolean getTIMAllowed() {
		return this.timAllowed;
	}
	
	public void setTIMAllowed(boolean timAllowed) {
		this.timAllowed = timAllowed;
	}
	
	public static int getNxtMCode(final int mCode, final int nbCNCSteps) {
		//start from 0
		int maxMCode = nbCNCSteps * 2;
		return (mCode+1)%maxMCode;
	}
	
	public static int getPrvMCode(final int mCode, final int nbCNCSteps) {
		//start from 0
		int maxMCode = nbCNCSteps * 2;
		return (Math.abs(mCode-1))%maxMCode;
	}
	
	 public abstract void stopMonitoringMotionEnablingThreads();

	public SocketConnection getSocketConnection() {
		return socketConnection;
	}

	public void setSocketConnection(SocketConnection socketConnection) {
		this.socketConnection = socketConnection;
	}

}