import javax.script.ScriptEngineManager;
import javax.script.ScriptEngine;
import javax.script.ScriptException;

public class Metodos{

	//Funcion para imprimir
	public void doPrint(String str)
	{
		String strAux = new String();
		int res = 0;
		strAux="";
		
		for(int i=0;i<str.length();i++)
		{
			strAux+=str.charAt(i);
			System.out.println(strAux);
			if(str.charAt(i) == '\"')
			{
				strAux=strAux.substring(1,strAux.length());
				System.out.println(strAux);
				return;
			}
		}
		//String result = evaluating(strAux);
		//System.out.println(result);
	}

	//Se evalua toda una expresion matematica
	//Se utiliza el manejador de scripts para simular javascript y asi evaluar las operaciones matematicasmas facilmente
	public String evaluating(String expr)
	{
		String res = "";
		ScriptEngineManager manager = new ScriptEngineManager();
		ScriptEngine engine = manager.getEngineByName("js");        
		try
		{
			Object result = engine.eval(expr);
			res = result.toString();
			//System.out.println(result);	
		}
		catch(ScriptException e)
		{
			System.err.println("Error evaluationg the script: ");
		}
		return res;
	}


}