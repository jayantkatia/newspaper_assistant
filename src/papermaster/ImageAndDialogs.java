package papermaster;
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

public class ImageAndDialogs {
	public String imagePath;
	private final ArrayList < String > extensionFilterList = new ArrayList < String > (Arrays.asList("*.jpg", "*.jpeg", "*.png"));
	public File choosenImage;
	
	public ImageAndDialogs(String imagePath) {
		this.imagePath=imagePath;
	}
	
	public Image getImageAfterChoose() {
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
	
	public Task<Void> savingUpdatingAfterChoosing(String folder,String title) {
		
		
		if(this.choosenImage==null)
			if(!getingImageFileIfNotChoosen(title))   
				return null;
		
		String fileName = "database/"+folder+"/"+title+"-"+choosenImage.getName();
		this.imagePath="file:"+fileName;
		
		
		
		Task < Void > task = new Task < Void > () {
				@Override
				protected Void call() throws Exception {
					System.out.println("Saving in local storage");
					System.out.println(choosenImage);
					FileInputStream f1 = new FileInputStream(choosenImage);
					System.out.println(choosenImage.getName());
					
					FileOutputStream f2 = new FileOutputStream(fileName);
					
					int maxlength = f1.available();
					int i = 0;
					
					System.out.println(maxlength);
					System.out.println(choosenImage.getName());
					
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

//		task.setOnSucceeded(new EventHandler < WorkerStateEvent > () {
//			@Override
//			public void handle(WorkerStateEvent event) {
//				System.out.println("Done");
//			}
//		});
		return task;
	}
	
	
	public boolean getingImageFileIfNotChoosen(String title) {
		
		String[] fileNameArray= this.imagePath.split("/");
		String[] titleFromFileNameArray=fileNameArray[2].split("-");
		System.out.println(titleFromFileNameArray[0]);
		if(titleFromFileNameArray[0].equals(title)) {
			System.out.println("yeh shi hai ?");
			return false;
		}
			System.out.println(titleFromFileNameArray[0]);
			System.out.println(title);
			
		this.choosenImage=new File(System.getProperty("user.dir")+"/database/images/"+fileNameArray[2]);
		System.out.println("herere");
		return true;
	}
	
	
	
	
	
	public Task<Void> deleteImage(String imageToDelete){
		Task<Void> task=new Task<Void>() {
			@Override
			protected Void call() throws Exception {
				System.out.println("Deleting image from local storage");
				
				File f=new File(imageToDelete);
				if(f.delete());
					System.out.println("Deleted");
				return null;
			}
			
		};
//		task.setOnSucceeded(new EventHandler < WorkerStateEvent > () {
//			@Override
//			public void handle(WorkerStateEvent event) {
//				imageDeletedLocally=true;
//				System.out.println("Done");
//			}
//		});
		return task;
	}
	
	
}
