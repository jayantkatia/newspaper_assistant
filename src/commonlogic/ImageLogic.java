package commonlogic;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.scene.image.Image;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;

public class ImageLogic {
	
	public String dummyImage;
	public final String folder;
	private final ArrayList < String > extensionFilterList = new ArrayList < String > (Arrays.asList("*.jpg", "*.jpeg", "*.png"));
	
	public String imagePath;
	public File choosenImage;
	 
	
	public ImageLogic(String imagePath,String folder) {
		this.imagePath=imagePath;
		this.dummyImage=imagePath;
		this.folder=folder;
	}
	public void setIntialState() {
		this.imagePath=this.dummyImage;
		this.choosenImage=null;
	}
	
	
	public Image getImageAfterChoose() {
//		Displays browse window and returns an Image
		FileChooser filechooser = new FileChooser();
		filechooser.setTitle("Choose Images(jpg,png,jpeg)");
		filechooser.getExtensionFilters().add(new ExtensionFilter("Images", extensionFilterList));
		this.choosenImage = filechooser.showOpenDialog(null);
		if (choosenImage == null) 
		return null;

		Image pic = null;
		try {
			pic = new Image(new FileInputStream(choosenImage));
		} catch(FileNotFoundException e) {
			e.printStackTrace();
		}
		if (pic != null) {
			return pic;
		}	
		return null;
	}
	
	
	
	public Task<Void> savingUpdatingAfterChoosing(String title) {	
		//title is basically the primary key of the table for uniqueness

		/* used in case user fetches data but not chooses image...then we manually set choosenImage to 
		file corresponding to imagePath fetched*/ 
		if(this.choosenImage==null) 
			if(!getingImageFileIfNotChoosen(title)) {
				System.out.println("Updated with using Previous Saved Image");
				return null;
			}   
		/*executed if primaryKey(title) unchanged implying user is updating without choosing any new file
		in above case we don't need to save the image as name is same and file is same	*/
		
		/*if title is different it implies user fetched>changed primary key
		we need to save the file with updated title for a new record*/
		
		
		
		String format=getFormatName(this.choosenImage.getName());
		String fileName = "database/"+this.folder+"/"+title+format;
		this.imagePath="file:"+fileName;
		
		
		Task < Void > task = new Task < Void > () {
			
				@Override
				protected Void call() throws Exception {
					System.out.println("Saving in local storage");
					System.out.println("Input Location ::"+choosenImage);
					System.out.println("Output Location ::"+fileName);

					FileInputStream f1 = new FileInputStream(choosenImage);
					

					FileOutputStream f2 = new FileOutputStream(fileName);   //fileName must be final/effectively-final to access this
					int maxlength = f1.available();
					int i = 0;

					
					for (int c = f1.read(); c != -1; c = f1.read()) {
						f2.write(c);
						updateProgress(i, maxlength);
						i++;
					}
					f1.close();
					f2.close();
					
				
					
					return null;
			}
		};

		task.setOnSucceeded(new EventHandler < WorkerStateEvent > () {
			@Override
			public void handle(WorkerStateEvent event) {
				System.out.println("Done");
			}
		});
		return task;
	}
	
	
	public boolean getingImageFileIfNotChoosen(String title) {
		//title is basically the primary key of the table for uniqueness
		String[] titleFromFileName= getFileName().split("\\.");
		System.out.println(titleFromFileName[0]);
		if(titleFromFileName[0].equals(title)) {
			return false;
		}

		this.choosenImage=new File(System.getProperty("user.dir")+"/database/"+this.folder+"/"+getFileName());
		return true;
	}
	
	
	
	
	
	public Task<Void> deleteImage(String fileName){
		//filename is of form   img.png
		//returns a task which deletes given fileName.... 
		
		String fileNameWithPath = System.getProperty("user.dir")+"/database/" + this.folder+ "/" + fileName;
		Task<Void> task=new Task<Void>() {
			@Override
			protected Void call() throws Exception {
				System.out.println("Deleting image from local storage");
				
				File f=new File(fileNameWithPath);
				if(f.delete())
					System.out.println("Deleted");
				else
					System.out.println("Deletion Failed");
				return null;
			}
		};
		task.setOnSucceeded(new EventHandler < WorkerStateEvent > () {
			@Override
			public void handle(WorkerStateEvent event) {
				System.out.println("Done");
			}
		});
		
		return task;
	}
	
	
	
	public String getFileName() {
		//  returns img.png    from   file:database/img_folder/img.png
		String[] fileNameArray=this.imagePath.split("/");
		return fileNameArray[2];
	}
	
	
	public String getFormatName(String fileName) {
		//returns  .png (includng .)
		return fileName.substring(fileName.lastIndexOf('.'));
	}
	
}
