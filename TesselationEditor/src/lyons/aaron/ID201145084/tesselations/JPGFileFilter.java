package lyons.aaron.ID201145084.tesselations;

import java.io.File;

import javax.swing.filechooser.FileFilter;
//Custom filter for saving files as JPG
public class JPGFileFilter extends FileFilter{

	@Override
	public boolean accept(File f) {
		if(f.isDirectory()){
            return true;
        }
        return f.getName().endsWith(".jpg");
	}

	@Override
	public String getDescription() {		
		return "JPG file (*.jpg)";
	}	

}
