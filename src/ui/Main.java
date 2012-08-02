/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ui;

import javax.swing.UIManager;

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
    public static void main(String[] args)
    {
        for (int i=0; i<args.length; i++)
        {
            if (args[i].hashCode()=="--developer".hashCode()) developer = true;
            if (args[i].hashCode()=="-d".hashCode()) developer = true;
        }
        
        setLookAndFeel();
        
        cf = new ConnectFrame();
        if (!developer) cf.jButton3.setVisible(false);
        cf.setVisible(true);
        od = new OpenDialog();
    }
    
    private static void setLookAndFeel()
    {
        // Well, let's try to set Nimbus explicitly first.
        try
        {
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
            System.out.println("LaF: com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
            return; // success
        }
        catch(Throwable ex)
        {
            // do nothing
        }
        
        // No? Let's try to find proper class name from the installed LookAndFeel's.
        for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels())
        {
            if ("Nimbus".equals(info.getName()))
            {
                try
                {
                    UIManager.setLookAndFeel(info.getClassName());
                    System.out.println("LaF: "+info.getClassName()+" (found)");
                    return; // success
                }
                catch (Throwable ex)
                {
                    // do nothing
                }
            }
        }
        
        // Still no? Let's just use the system one.
        try
        {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            System.out.println("LaF: "+UIManager.getSystemLookAndFeelClassName()+" (system)");
            return; // success
        }
        catch (Throwable ex2)
        {
            System.out.println("LaF: everything failed, leaving default one");
            // Sadly.
        }
    }
}
