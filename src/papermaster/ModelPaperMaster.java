package papermaster;

public class ModelPaperMaster {
	public final String title;
	public final String imagePath;
	public final Float price;
	ModelPaperMaster(String title,String imagePath,Float price){
		this.price = price;
		this.imagePath=imagePath;
		this.title = title ;
	}
}
