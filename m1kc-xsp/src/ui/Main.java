/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ui;

/**
 *
 * @author m1kc
 */
public class Main {

    public static ConnectFrame cf;
    public static SessionFrame sf;
    public static OpenDialog od;

    public static boolean developer = false;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        for (int i=0; i<args.length; i++)
        {
            if (args[i].hashCode()=="--developer".hashCode()) developer = true;
            if (args[i].hashCode()=="-d".hashCode()) developer = true;
        }
        
        setLookAndFeel(new com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel());
        
        cf = new ConnectFrame();
        if (!developer) cf.jButton3.setVisible(false);
        cf.setVisible(true);
        od = new OpenDialog();
    }

    public static void setLookAndFeel(javax.swing.LookAndFeel f)
    {
        try
        {
            if (f==null) javax.swing.UIManager.setLookAndFeel(javax.swing.UIManager.getSystemLookAndFeelClassName());
            else javax.swing.UIManager.setLookAndFeel(f);
        }
        catch (Throwable ex)
        {
        }
    }

}
