package application.view;

import java.awt.Dimension;
import java.util.Date;
import java.util.List;

import application.model.messages.MessageProxy;
import application.model.messages.MessageVO;
import org.puremvc.java.multicore.interfaces.IMediator;
import org.puremvc.java.multicore.interfaces.INotification;
import org.puremvc.java.multicore.patterns.mediator.Mediator;

import application.ApplicationFacade;

import javax.swing.*;


public class MainWindowMediator extends Mediator implements IMediator {

	private static final String NAME = "MainWindowMediator";
	private static MainWindow mainWindow = null;
	private MessageProxy messageProxy = null;
	
	public MainWindowMediator() {
		super(NAME, null);
		
		System.out.println("MainWindowMediator()");
		mainWindow = new MainWindow(this);
		setViewComponent(mainWindow);
	}

	/**
	 * Called by the View when the Mediator is registered
	 */
	@Override
	public void onRegister() {
		// TODO Auto-generated method stub
		System.out.println("  MainWindowMediator: onRegister()");
		messageProxy = (MessageProxy) getFacade().retrieveProxy(MessageProxy.NAME);
		super.onRegister();
	}

	/**
	 * List the INotification names this Mediator is interested in being notified of.
	 */
	@Override
	public String[] listNotificationInterests() {
		System.out.println("  MainWindowMediator: listNotificationInterests()");
		// TODO Auto-generated method stub
		return new String[] {
			ApplicationFacade.SHOW_MAIN_WINDOW,
			ApplicationFacade.SEND_MESSAGE,
            ApplicationFacade.MESSAGE_SENT,
			ApplicationFacade.UPDATE_CONSOLE,
			ApplicationFacade.LOAD_MESSAGES,
			ApplicationFacade.CHANGE_SERVER_PORT,
            ApplicationFacade.SHUTDOWN
		};
		
		//return super.listNotificationInterests();
	}
	
	@Override
	public void handleNotification(INotification notification) {
		System.out.println("  MainWindowMediator: handleNotification(): " +
				notification.getName().toUpperCase());

		// TODO Auto-generated method stub
		switch(notification.getName()) {
        	case ApplicationFacade.SHOW_MAIN_WINDOW:
				mainWindow.setPreferredSize(new Dimension(650, 500));
				mainWindow.pack();
				mainWindow.setVisible(true);
        		break;

			case ApplicationFacade.SERVER_STARTED:
				break;

			case ApplicationFacade.MESSAGE_SENT:
			case ApplicationFacade.UPDATE_CONSOLE:
				mainWindow.clearOutputConsole();
				mainWindow.writeToOutputConsole((String)notification.getBody());
				break;

			case ApplicationFacade.LOAD_MESSAGES:
				mainWindow.clearOutputConsole();
				List messages = messageProxy.messages();

				// use only last 100 messages
				List<MessageVO> tail = messages.subList(Math.max(messages.size() - 100, 0), messages.size());
				for(MessageVO messageVO : tail){
					Date resultdate = new Date(messageVO.getTimestamp());
					//mainWindow.writeToOutputConsole(resultdate.toString());
					String coords = resultdate.toString() + " - received new coordinates: (" +
							messageVO.getJsonObject().getFloat("coordX") + ", " +
							messageVO.getJsonObject().getFloat("coordY") + ")";
					mainWindow.writeToOutputConsole(coords);
				}

				break;

			case ApplicationFacade.SHUTDOWN:
				mainWindow.dispose();
				System.exit(0);
				break;
            
        	default:
        		break;
		}
            
    	//super.handleNotification(notification);
    }

    public void changeServerPort() {
		System.out.println("  MainWindowMediator: changeServerPort()");

		String m = JOptionPane.showInputDialog(mainWindow, "Enter the Server port",
				"Server Configuration", JOptionPane.OK_CANCEL_OPTION);

		int port = -1;
		if (m == null) { // cancel is pressed
			//System.out.println("cancel button");
			return;
		} else if (m.isEmpty()) { // empty
			//mainWindow.getContentPane().setVisible(false);
			//mainWindow.dispose();
			changeServerPort();
		} else if (!m.matches("(0|[1-9]\\d*)")) { // not integer
			JOptionPane.showMessageDialog(null,
					"Enter a valid integer value.", "Alert", JOptionPane.WARNING_MESSAGE);

			changeServerPort();
		} else {
			port = Integer.parseInt(m);

			if (port > 49151 || port < 1024) { // port not in range 1024-49151
				JOptionPane.showMessageDialog(null,
						"Enter a valid user port (1024-49151).", "Alert", JOptionPane.WARNING_MESSAGE);
				changeServerPort();
			}

			sendNotification(ApplicationFacade.CHANGE_SERVER_PORT, port);
		}
	}
}