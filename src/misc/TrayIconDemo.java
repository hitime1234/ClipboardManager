
package misc;
/*
 * TrayIconDemo.java
 */
import misc.clipProgram;
import java.awt.*;
import java.awt.event.*;
import java.net.URL;
import javax.swing.*;
import javax.sound.midi.SysexMessage;
import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.util.Scanner;
import java.util.ArrayList;


public class TrayIconDemo {
    public static int i = 1;
    public static ArrayList<String> holdTEXT = new ArrayList<String>();
    private static String readClipboard(){
        try {
            return (String) Toolkit.getDefaultToolkit().getSystemClipboard().getData(DataFlavor.stringFlavor);
        } catch (Exception e){
            return "Images aren't supported";
        }
    }
    private static boolean writeClipboard(String theString){
        try {
            StringSelection selection = new StringSelection(theString);
            Toolkit.getDefaultToolkit().getSystemClipboard().setContents(selection, selection);
            return true;
        }
        catch (Exception e){
            return false;
        }
    }
    public static void main(String[] args) {
        /* Use an appropriate Look and Feel */
        try {
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
            //UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
        } catch (UnsupportedLookAndFeelException ex) {
            ex.printStackTrace();
        } catch (IllegalAccessException ex) {
            ex.printStackTrace();
        } catch (InstantiationException ex) {
            ex.printStackTrace();
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        }
        /* Turn off metal's use of bold fonts */
        UIManager.put("swing.boldMetal", Boolean.FALSE);
        //Schedule a job for the event-dispatching thread:
        //adding TrayIcon.
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                holdTEXT.add("");holdTEXT.add("");holdTEXT.add("");holdTEXT.add("");
                createAndShowGUI();
            }
        });
    }

    private static void createAndShowGUI() {
        //Check the SystemTray support
        if (!SystemTray.isSupported()) {
            System.out.println("SystemTray is not supported");
            return;
        }

        final PopupMenu popup = new PopupMenu();
        final TrayIcon trayIcon =
                new TrayIcon(createImage("images/bulb.gif", "tray icon"));
        final SystemTray tray = SystemTray.getSystemTray();

        // Create a popup menu components
        MenuItem saveCurrentClipboard = new MenuItem("save current clipboard");
        CheckboxMenuItem cb1 = new CheckboxMenuItem("Set auto size");
        MenuItem setSavedClipboard = new MenuItem("set saved clipboard");
        Menu displayMenu = new Menu("Index manager");
        MenuItem index1 = new MenuItem("1");
        MenuItem index2 = new MenuItem("2");
        MenuItem index3 = new MenuItem("3");
        MenuItem index4 = new MenuItem("4");
        MenuItem exit = new MenuItem("Exit");

        //Add components to popup menu
        popup.add(saveCurrentClipboard);
        popup.addSeparator();
        popup.add(setSavedClipboard);
        popup.add(cb1);
        popup.addSeparator();
        popup.add(displayMenu);
        displayMenu.add(index1);
        displayMenu.add(index2);
        displayMenu.add(index3);
        displayMenu.add(index4);
        popup.add(exit);

        trayIcon.setPopupMenu(popup);
        try {
            tray.add(trayIcon);
        } catch (AWTException e) {
            System.out.println("TrayIcon could not be added.");
            return;
        }
        trayIcon.setImageAutoSize(true);



        saveCurrentClipboard.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                trayIcon.displayMessage("ClipManager",
                        "Current saved clipboard:\n" +readClipboard(), TrayIcon.MessageType.INFO);
                try {
                    holdTEXT.set(i, readClipboard());

                }
                catch (Exception x){
                    try {
                        holdTEXT.add(readClipboard());
                    }
                    catch (Exception z){
                        trayIcon.displayMessage("ClipManager",
                                "there was a problem", TrayIcon.MessageType.ERROR);
                    }
                }
            }
        });

        cb1.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                int cb1Id = e.getStateChange();
                if (cb1Id == ItemEvent.SELECTED){
                    trayIcon.setImageAutoSize(true);
                } else {
                    trayIcon.setImageAutoSize(false);
                }
            }
        });

        setSavedClipboard.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                writeClipboard(holdTEXT.get(i));
                trayIcon.displayMessage("ClipManager",
                        "your computer clipboard has been set to:\n" + holdTEXT.get(i), TrayIcon.MessageType.INFO);
            }
        });

        ActionListener listener = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                MenuItem item = (MenuItem)e.getSource();
                System.out.println(item.getLabel());
                if ("1".equals(item.getLabel())) {
                    i = 1;
                    trayIcon.displayMessage("ClipManager",
                            "index 1 selected", TrayIcon.MessageType.INFO);

                } else if ("2".equals(item.getLabel())) {
                    i = 2;
                    trayIcon.displayMessage("ClipManager",
                            "index 2 selected", TrayIcon.MessageType.INFO);

                } else if ("3".equals(item.getLabel())) {
                    i = 3;
                    trayIcon.displayMessage("ClipManager",
                            "index 3 selected", TrayIcon.MessageType.INFO);

                } else if ("4".equals(item.getLabel())) {
                    i= 4;
                    trayIcon.displayMessage("ClipManager",
                            "index 4 selected", TrayIcon.MessageType.INFO);
                }
            }
        };

        index1.addActionListener(listener);
        index2.addActionListener(listener);
        index3.addActionListener(listener);
        index4.addActionListener(listener);

        exit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                tray.remove(trayIcon);
                System.exit(0);
            }
        });
    }

    //Obtain the image URL
    protected static Image createImage(String path, String description) {
        URL imageURL = TrayIconDemo.class.getResource(path);

        if (imageURL == null) {
            System.err.println("Resource not found: " + path);
            return null;
        } else {
            return (new ImageIcon(imageURL, description)).getImage();
        }
    }
}