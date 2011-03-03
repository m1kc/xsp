/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * OpenDialog.java
 *
 * Created on 20.01.2011, 3:11:46
 */

package ui;

import java.io.File;
import java.util.logging.*;
import javax.swing.*;

/**
 *
 * @author m1kc
 */
public class OpenDialog extends javax.swing.JFrame {

    int mode;
    final int FILE = 1;
    final int DIR = 2;
    JTextField target;
    JLabel sizeTarget;

    /** Creates new form OpenDialog */
    public OpenDialog() {
        initComponents();
        setLocationRelativeTo(null);
        jFileChooser1.setApproveButtonText("Открыть");
    }

    public void openFile(JTextField target, JLabel sizeTarget)
    {
        this.mode = FILE;
        this.target = target;
        this.sizeTarget = sizeTarget;
        jFileChooser1.setFileSelectionMode(JFileChooser.FILES_ONLY);
        this.setTitle("Выбор файла");
        this.setVisible(true);
    }

    public void openDir(JTextField target)
    {
        this.mode = DIR;
        this.target = target;
        jFileChooser1.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        this.setTitle("Выбор каталога");
        this.setVisible(true);
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jFileChooser1 = new javax.swing.JFileChooser();

        jFileChooser1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jFileChooser1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jFileChooser1, javax.swing.GroupLayout.DEFAULT_SIZE, 504, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jFileChooser1, javax.swing.GroupLayout.DEFAULT_SIZE, 362, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    public static String bestSize(long size)
    {
        if (size<1024L*10L) return ""+size+" байт";
        if (size>=1024L*1024L*1024L*10L) return ""+size/(1024L*1024L*1024L)+" Гб";
        if (size>=1024L*1024L*10L) return ""+size/(1024L*1024L)+" Мб";
        return ""+size/1024L+" Кб";
    }

    private void jFileChooser1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jFileChooser1ActionPerformed
        if (evt.getActionCommand().hashCode()=="ApproveSelection".hashCode())
        {
            if (mode==FILE)
            {
                if (target!=null) target.setText(jFileChooser1.getSelectedFile().getAbsolutePath());
                try {
                    if (sizeTarget!=null) sizeTarget.setText("Размер файла: "+bestSize(jFileChooser1.getSelectedFile().length()));
                } catch (Throwable ex) {
                    Logger.getLogger(OpenDialog.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

            if (mode==DIR)
            {
                String s = jFileChooser1.getSelectedFile().getAbsolutePath();
                //if ((s.startsWith("/"))&&(!s.endsWith("/"))) s+="/";
                //if ((!s.startsWith("/"))&&(s.charAt(1)==':')&&(!s.endsWith("\\"))) s+="\\";
                if (!s.endsWith(File.separator)) s+=File.separator;
                if (target!=null) target.setText(s);
            }
        }
        this.dispose();
    }//GEN-LAST:event_jFileChooser1ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JFileChooser jFileChooser1;
    // End of variables declaration//GEN-END:variables

}
