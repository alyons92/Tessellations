package lyons.aaron.ID201145084.tesselations;

import java.io.File;

import javax.swing.filechooser.FileFilter;
//Custom filter for saving the model to be edited again later
public class ModelFileFilter extends FileFilter {
	
	@Override
	public boolean accept(File f) {
		if(f.isDirectory()){
            return true;
        }
        return f.getName().endsWith(".txt");
	}

	@Override
	public String getDescription() {		
		return "Tessellation File (*.txt)";
	}
}
