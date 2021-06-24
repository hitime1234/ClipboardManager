
package misc;
/*
 * TrayIconDemo.java
 */
import java.awt.*;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.*;
import java.io.IOException;
import java.net.URL;
import javax.swing.*;
import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.text.NumberFormat;
import java.util.ArrayList;


public class TrayIconDemo {
    public static String stringHold = " ";
    public static int i = 1;
    public static ArrayList<Transferable> holdTEXT = new ArrayList<Transferable>();
    private static Transferable readClipboard(){
        try {
            Transferable HOLD =Toolkit.getDefaultToolkit().getSystemClipboard().getContents(null);
            stringHold = "ClipManager successfully saved clipboard to Board " + i;
            return HOLD;
        } catch (Exception e){
            stringHold = "ERROR OCCURRED WHILE READING CLIPBOARD.";
            return null;
        }
    }

    private static boolean writeClipboard(Transferable TheEPIC){
        try {
            Toolkit.getDefaultToolkit().getSystemClipboard().setContents(TheEPIC, null);
            stringHold = "ClipManager Successfully set computer clipboard to Board " + i;
            return true;
        }
        catch (Exception e){
            stringHold = "ERROR OCCURRED WHILE WRITING CLIPBOARD.";
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
                Transferable alt = new Transferable() {
                    @Override
                    public DataFlavor[] getTransferDataFlavors() {
                        return new DataFlavor[0];
                    }

                    @Override
                    public boolean isDataFlavorSupported(DataFlavor flavor) {
                        return false;
                    }

                    @Override
                    public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException, IOException {
                        return null;
                    }
                };
                holdTEXT.add(alt);holdTEXT.add(alt);holdTEXT.add(alt);holdTEXT.add(alt);
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
        MenuItem saveCurrentClipboard = new MenuItem("Save current clipboard");
        CheckboxMenuItem cb1 = new CheckboxMenuItem("Set auto size");
        MenuItem setSavedClipboard = new MenuItem("Set saved clipboard");
        Menu displayMenu = new Menu("Board manager");
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
                try {
                    holdTEXT.set(i, readClipboard());
                    trayIcon.displayMessage("ClipManager",
                            stringHold, TrayIcon.MessageType.INFO);

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
                try {
                    Runtime runtime = Runtime.getRuntime();
                    NumberFormat format = NumberFormat.getInstance();
                    StringBuilder sb = new StringBuilder();
                    long maxMemory = runtime.maxMemory();
                    long allocatedMemory = runtime.totalMemory();
                    long freeMemory = runtime.freeMemory();
                    sb.append("free memory: " + format.format(freeMemory / 1024) + "\n");
                    sb.append("allocated memory: " + format.format(allocatedMemory / 1024) + "\n");
                    sb.append("max memory: " + format.format(maxMemory / 1024) + "\n");
                    sb.append("total free memory: " + format.format((freeMemory + (maxMemory - allocatedMemory)) / 1024) + "\n");
                    System.out.println(sb);
                } catch (Exception MEM){
                    System.out.println("\nproblem while ATTEMPTing display memory usage console\n");
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
                        stringHold, TrayIcon.MessageType.INFO);
            }
        });

        ActionListener listener = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                MenuItem item = (MenuItem)e.getSource();
                System.out.println(item.getLabel());
                if ("1".equals(item.getLabel())) {
                    i = 1;
                    trayIcon.displayMessage("ClipManager",
                            "Board 1 selected", TrayIcon.MessageType.INFO);

                } else if ("2".equals(item.getLabel())) {
                    i = 2;
                    trayIcon.displayMessage("ClipManager",
                            "Board 2 selected", TrayIcon.MessageType.INFO);

                } else if ("3".equals(item.getLabel())) {
                    i = 3;
                    trayIcon.displayMessage("ClipManager",
                            "Board 3 selected", TrayIcon.MessageType.INFO);

                } else if ("4".equals(item.getLabel())) {
                    i= 4;
                    trayIcon.displayMessage("ClipManager",
                            "Board 4 selected", TrayIcon.MessageType.INFO);
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