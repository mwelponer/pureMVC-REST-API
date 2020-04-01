package eu.it.welponer.testPuremvc;

import eu.it.welponer.testPuremvc.controller.StartupCommand;
import eu.it.welponer.testPuremvc.controller.messages.ClearMessagesCommand;
import eu.it.welponer.testPuremvc.controller.messages.ReceiveMessageCommand;
import eu.it.welponer.testPuremvc.controller.messages.SendMessageCommand;
import eu.it.welponer.testPuremvc.controller.server.ChangeServerPortCommand;
import eu.it.welponer.testPuremvc.controller.server.StartServerCommand;
import org.puremvc.java.multicore.interfaces.IFacade;
import org.puremvc.java.multicore.patterns.facade.Facade;


public class ApplicationFacade extends Facade implements IFacade {

	private static ApplicationFacade instance = new ApplicationFacade();
	public static final String NAME = "ApplicationFacade";
	public static final String STARTUP = "startup";
	
	//public static final String ADD_ITEM = "addItem";
	
//	public static final String ADD_ITEM = "AddItem";
//	public static final String ITEM_ADDED = "ItemAdded";
//	public static final String LOAD_ITEMS = "LoadItems";

	public static final String START_SERVER = "StartServer";
	public static final String SERVER_STARTED = "ServerStarted";
	public static final String SEND_MESSAGE = "SendMessage";
	public static final String MESSAGE_SENT = "MessageSent";
	public static final String RECEIVE_MESSAGE = "ReceiveMessage";
	public static final String UPDATE_CONSOLE = "UpdateConsole";
	public static final String UPDATE_STATUSBAR = "UpdateStatusBar";
	public static final String LOAD_MESSAGES = "LoadMessages";
	public static final String CLEAR_MESSAGES = "ClearMessages";
	public static final String CHANGE_SERVER_PORT = "ChangeServerPort";

	public static final String SHOW_MAIN_WINDOW = "show_main_window";
	public static final String SHUTDOWN = "shutdown";


	////////////////////////////////////
	// THIRD
	////////////////////////////////////
	protected ApplicationFacade() {
		super(NAME);
		System.out.println("ApplicationFacade()");
	}

	////////////////////////////////////
	// SECOND
	////////////////////////////////////
	public static ApplicationFacade getInstance() { return instance;}

	////////////////////////////////////
	// FIRST
	////////////////////////////////////
	@Override
	protected void initializeController() {
		// TODO Auto-generated method stub
		super.initializeController();
		System.out.println("  ApplicationFacade: initializeController()");
		registerCommand(STARTUP, new StartupCommand());
		registerCommand(START_SERVER, new StartServerCommand());
		registerCommand(SEND_MESSAGE, new SendMessageCommand());
		registerCommand(RECEIVE_MESSAGE, new ReceiveMessageCommand());
		registerCommand(CLEAR_MESSAGES, new ClearMessagesCommand());
		registerCommand(CHANGE_SERVER_PORT, new ChangeServerPortCommand());
	}
	
	public void startup() {
		// TODO Auto-generated method stub
		System.out.println("  ApplicationFacade: startup()");
		sendNotification(STARTUP);
	}
}