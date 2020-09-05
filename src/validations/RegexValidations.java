package validations;



public class RegexValidations {
	public static boolean wholeNumberCheck(String input) {
		
		return input.matches("[0-9]+");
	}
}
