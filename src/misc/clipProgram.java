package misc;
import javax.sound.midi.SysexMessage;
import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.util.Scanner;
import java.util.ArrayList;

public class clipProgram {
    private static String readClipboard(){
        try {
            return (String) Toolkit.getDefaultToolkit().getSystemClipboard().getData(DataFlavor.stringFlavor);
        } catch (UnsupportedFlavorException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "NULL";
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
    public static void main(String args) {
        boolean run = true;
        int i = 0;
        ArrayList<String> holdTEXT = new ArrayList<String>();
        while (run) {
            try{
                Scanner reader = new Scanner(System.in);
                System.out.println("your current id is: " + i);
                System.out.println("menu: "); int hold = reader.nextInt();
                switch (hold){
                    case 1:
                        System.out.println("Current id clipboard: " +readClipboard());
                        try {
                            holdTEXT.set(i, readClipboard());
                        }
                        catch (Exception e){
                            holdTEXT.add(readClipboard());
                        }
                        break;
                    case 2:
                        writeClipboard(holdTEXT.get(i));
                        System.out.println("your computer clipboard has been set to:\n" + holdTEXT.get(i));
                        break;
                    case 3:
                        System.out.println("currentINDEX selected is:\n" + holdTEXT.get(i));
                        break;
                    case 5:
                        if (i != 1000){ i = i + 1;}
                        else{System.out.println("\nindex must be great than -1\n");}
                        break;
                    case 4:
                        if (i != 0){ i = i - 1;}
                        else{System.out.println("\nindex must be great than -1\n");}
                        break;
                    default:
                        System.out.println("invalid 1,2,3");
                        break;
                }}
            catch(Exception e){
                System.out.println("\nelement error\n");
            }

        }

    }
}
