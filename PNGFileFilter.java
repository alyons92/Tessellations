package lyons.aaron.ID201145084.tesselations;

import java.io.File;

import javax.swing.filechooser.FileFilter;

public class PNGFileFilter extends FileFilter {

	@Override
	public boolean accept(File f) {
		if(f.isDirectory()){
            return true;
        }
        return f.getName().endsWith(".png");
	}

	@Override
	public String getDescription() {		
		return "PNG file (*.png)";
	}

}
