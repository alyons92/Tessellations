/**
 * 
 */
package lyons.aaron.ID201145084.tesselations;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * @author Aaron Lyons
 *
 */
public class TessellationsEditor extends JFrame implements ActionListener {
	private DrawingView drawingView;
	private TessellationModel tesModel;
	private ToolbarController toolButtonsPanel;
    private DrawingCanvas drawingCanvas;
    private OptionsController optionsPanel; 
    private ShapeMenu shapeMenu; 
    private ColorPickerMenu colorMenu;
    private File savedFile;
	
	public TessellationsEditor() {
		super("TessellationsEditor");		
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setBackground(Color.decode("#282828"));
		
		setVisible(true);
		buildGUI();
		pack();	
		setExtendedState(JFrame.MAXIMIZED_BOTH); 		
	}
	
	protected void buildGUI() {
		//MVC
		tesModel = new TessellationModel();
        drawingView = new DrawingView(tesModel);
        toolButtonsPanel = new ToolbarController(tesModel);
        drawingCanvas = new DrawingCanvas(tesModel, drawingView);
        optionsPanel = new OptionsController(tesModel, drawingView);
        shapeMenu = new ShapeMenu(tesModel, toolButtonsPanel);
        colorMenu = new ColorPickerMenu(tesModel, optionsPanel);        
         
        //Colours
        String lightGrey = "#4d4b4b";        
        String darkGrey = "#232323";
        String mediumGrey = "#404040";
         
        //Panel Areas
        JPanel mainFull = new JPanel();
        JPanel rightPanel = new JPanel();        
        JPanel workArea = new JPanel();        
        JPanel drawingArea = new JPanel();
        JPanel drawingWindow = new JPanel();
;
        drawingView.setBorder(BorderFactory.createLineBorder(Color.black));
        drawingCanvas.setBorder(BorderFactory.createLineBorder(Color.black));
         
        //Menu bar
        JMenuBar menuBar = new JMenuBar();   
        menuBar.setBackground(Color.decode(darkGrey));
        setJMenuBar(menuBar);
        JMenu fileM = new JMenu("File");
        fileM.setBackground(Color.decode(darkGrey));
        
        JMenuItem newDoc = new JMenuItem("New");
        JMenuItem saveDoc = new JMenuItem("Save");
        JMenuItem saveAsDoc = new JMenuItem("Save As...");
        JMenuItem openDoc = new JMenuItem("Open");
        newDoc.setBackground(Color.decode(mediumGrey));
        newDoc.setForeground(Color.decode("#ffffff"));
        saveDoc.setBackground(Color.decode(mediumGrey));
        saveDoc.setForeground(Color.decode("#ffffff"));
        saveAsDoc.setBackground(Color.decode(mediumGrey));
        saveAsDoc.setForeground(Color.decode("#ffffff"));
        openDoc.setBackground(Color.decode(mediumGrey));
        openDoc.setForeground(Color.decode("#ffffff"));
        fileM.setFont(new Font("Trebuchet MS", Font.BOLD, 16));
        newDoc.setFont(new Font("Trebuchet MS", Font.BOLD, 16));
        saveDoc.setFont(new Font("Trebuchet MS", Font.BOLD, 16));
        saveAsDoc.setFont(new Font("Trebuchet MS", Font.BOLD, 16));
        openDoc.setFont(new Font("Trebuchet MS", Font.BOLD, 16));
        menuBar.add(fileM);
        fileM.add(newDoc);
        fileM.add(saveDoc);
        fileM.add(saveAsDoc);
        fileM.add(openDoc);
        
        newDoc.addActionListener(this);
        saveDoc.addActionListener(this);
        saveAsDoc.addActionListener(this);
        openDoc.addActionListener(this);

        menuBar.add(fileM);
        menuBar.setBorder(BorderFactory.createMatteBorder(0,0,3,0,Color.decode(mediumGrey)));
         
        add(mainFull, BorderLayout.CENTER);
        mainFull.setLayout(new BorderLayout());
        workArea.setLayout(new BorderLayout());     
        rightPanel.setLayout(new BorderLayout());
        drawingWindow.setLayout(new FlowLayout());
        drawingArea.setLayout(new OverlayLayout(drawingArea));
        drawingArea.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
     
        workArea.setBorder(BorderFactory.createMatteBorder(3,0,0,0,Color.decode(mediumGrey)));    
         
        mainFull.add(optionsPanel, BorderLayout.NORTH);
        mainFull.add(workArea, BorderLayout.CENTER);
        workArea.add(toolButtonsPanel, BorderLayout.WEST);   
        rightPanel.add(shapeMenu, BorderLayout.EAST);
        rightPanel.add(colorMenu, BorderLayout.CENTER);
        workArea.add(drawingWindow, BorderLayout.CENTER);
       
        drawingWindow.add(drawingArea);
        workArea.add(rightPanel, BorderLayout.EAST);
         
         
        mainFull.setBackground(Color.decode(darkGrey)); 
        colorMenu.setBackground(Color.decode(darkGrey)); 
        colorMenu.setForeground(Color.decode(mediumGrey)); 
      
        
        workArea.setBackground(Color.decode(darkGrey));
        mainFull.setBackground(Color.decode(darkGrey));
        rightPanel.setBackground(Color.decode(darkGrey));
        drawingWindow.setBackground(Color.decode(darkGrey));
        menuBar.setBackground(Color.decode(lightGrey));
        menuBar.setForeground(Color.decode("#ffffff"));
        fileM.setForeground(Color.decode("#ffffff"));
        
        drawingArea.add(drawingCanvas,BorderLayout.CENTER);
        drawingArea.add(drawingView, BorderLayout.CENTER);         
         
        workArea.setSize(new Dimension(800, 800));
	}
	
	public void actionPerformed(ActionEvent e) {
		String cmd = e.getActionCommand();

		if (cmd.equals("New")) {
		    createFile();
		}
		else if (cmd.equals("Open")) {
		    openFile();
		}
		else if (cmd.equals("Save As...")) {
		    saveFile();
		
	    }else if (cmd.equals("Save")) {
	    	if(savedFile != null) {
	    		FileOutputStream f = null;
    			ObjectOutputStream o = null;
    			try {
    				f = new FileOutputStream(savedFile);
    				o = new ObjectOutputStream(f);
    				
    				o.writeObject(tesModel);
    				o.close();
    				f.close();    				
    			}catch (FileNotFoundException e2) {
    				System.out.println("File not found");
    			} catch (IOException e2) {
    				System.out.println("Error initializing stream");
    				e2.printStackTrace();
    			}
	    	}else {
	    		saveFile();
	    	}
		}
	}
	
    public void saveFile() {
    	JFileChooser chooser = new JFileChooser();
    	chooser.addChoosableFileFilter(new PNGFileFilter());
    	chooser.addChoosableFileFilter(new JPGFileFilter());
    	chooser.addChoosableFileFilter(new ModelFileFilter());
    	int result = chooser.showSaveDialog(this);
    	if(result == JFileChooser.APPROVE_OPTION) {
    		File file = chooser.getSelectedFile();
    		String path = file.getPath();
    		//load file
    		String ext = "";
    		String extType = chooser.getFileFilter().getDescription();
    		if(extType == "PNG file (*.png)") {    			
    			ext = "png";    	
    			String newPath = path + "." + ext;
    			file = new File(newPath);
    		}else if(extType == "JPG file (*.jpg)") {
    			ext = "jpg";
    			String newPath = path + "." + ext;
    			file = new File(newPath);
    		}else if(extType == "Tessellation File (*.txt)") {
    			ext = "txt";
    			String newPath = path + "." + ext;
    			file = new File(newPath);
    			FileOutputStream f = null;
    			ObjectOutputStream o = null;
    			try {
    				f = new FileOutputStream(file);
    				o = new ObjectOutputStream(f);
    				
    				o.writeObject(tesModel);
    				o.close();
    				f.close();
    				savedFile = file;
    			}catch (FileNotFoundException e) {
    				System.out.println("File not found");
    			} catch (IOException e) {
    				System.out.println("Error initializing stream");
    				e.printStackTrace();
    			}
    		}    		
    		try {
    		    // retrieve image
    		    BufferedImage bi = drawingView.getBImage();    		    
    		    ImageIO.write(bi, ext, file);
    		} catch (IOException e) {
    		    System.out.println("Could not create bufferedImage");
    		    e.printStackTrace();
    		}
    	}else {
    		//cancel dialog
    	}
    }
 
    public void openFile() {
    	JFileChooser chooser = new JFileChooser();
    	chooser.addChoosableFileFilter(new ModelFileFilter());
    	int result = chooser.showOpenDialog(this);
    	if(result == JFileChooser.APPROVE_OPTION) {
    		File file = chooser.getSelectedFile();
    		savedFile = file;  
    		//load file
    		try {
    			FileInputStream fi = new FileInputStream(file);
    			ObjectInputStream oi = new ObjectInputStream(fi);
    			tesModel = (TessellationModel) oi.readObject();
    			System.out.println(tesModel.getShapes().get(0).getEdges().size());
    			drawingCanvas.setTesModel(tesModel);
    			toolButtonsPanel.setTesModel(tesModel);
    			optionsPanel.setTesModel(tesModel);
    			shapeMenu.setTesModel(tesModel);
    			colorMenu.setTesModel(tesModel);
    			drawingView.setTesModel(tesModel);
    			drawingView.repaint();
    			  	   	        
    			oi.close();
    			fi.close();
    		} catch (FileNotFoundException e) {
    			System.out.println("File not found");
    		} catch (IOException e) {
    			System.out.println("Error initializing stream");
    		} catch (ClassNotFoundException e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		}    		
    	}else {
    		//cancel dialog
    	}
    }
 
    public void createFile() {
        savedFile = null;
        tesModel.clearAll();
        drawingView.repaint();
    }

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		new TessellationsEditor();
	}

}
