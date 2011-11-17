/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package JavaO;

import javax.swing.filechooser.FileFilter;
import java.io.File;

/**
 *
 * @author nikita
 */
public class TextFileChooserFilter extends javax.swing.filechooser.FileFilter{
    @Override
    public boolean accept(File file) {
        // Allow only directories, or files with ".txt" extension
        return file.isDirectory() || file.getAbsolutePath().endsWith(".txt");
    }
    @Override
    public String getDescription() {
        // This description will be displayed in the dialog,
        // hard-coded = ugly, should be done via I18N
        return "Text documents (*.txt)";
    }

}
