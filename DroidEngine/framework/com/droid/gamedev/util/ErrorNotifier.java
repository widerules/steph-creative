package com.droid.gamedev.util;

import javax.swing.JOptionPane;

import com.droid.gamedev.GameLoader;

/**
 * Handle game error notifications
 * 
 * @author Steph
 *
 */
public class ErrorNotifier {
	
	private ErrorNotifier(){	
	}
	
	
	
	/**
	 * This method is called when an error is thrown by the game.
	 * @param error
	 */
	public static void notifyError(String title, Throwable error){
		if(GameLoader.getPlatform()==GameLoader.WINDOW){
			ErrorNotifier.notifyErrorWin(title, error);
		}
	}
	
	private static void notifyErrorWin(String title, Throwable error){
		error.printStackTrace();
		String message = StringUtil.formatLines(error.getMessage(), 30);
		ErrorNotifier.showErrorDialog(title, message);
	}
	
	private static void showErrorDialog(String title, String message) {

		//TODO add option quit or continue game
		
		JOptionPane.showMessageDialog(null, "ERROR: " + message, title,
				JOptionPane.ERROR_MESSAGE);
	}

}
