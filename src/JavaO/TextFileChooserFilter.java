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
        return "Text documents (*.txt)";
    }

}
