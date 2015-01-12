package sample;
import javafx.util.StringConverter ;

public class NombreConverter extends  StringConverter<Integer> {

	
	@Override
	public Integer fromString(String arg0) {
		int int0 = Integer.parseInt(arg0);
		int0 = (int) (Math.log(int0)/Math.log(2)) ;
		System.out.println("testdsfksjdfkdsjf") ;
		return int0  ;
	}

	@Override
	public String toString(Integer arg0) {
		int arg1 = arg0 ;
		int valeurConverti = 0 ;
		if(arg1>=0)
		{
			valeurConverti = (int) Math.pow(2,arg1)  ;
		}
		System.out.println("valeur:"+valeurConverti);
		return valeurConverti+"" ;
	}

}
