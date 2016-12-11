import java.util.ArrayList;
import java.util.Scanner;

public class Parser
{
	int index = 0;
	int tableIndex = 0;
	int comp_1,comp_2;
	int if_enter = 0;
	String str = "";
	String str_aux = "";
	Scanner scan = new Scanner(System.in);
	//Token auxTok = new Token();
	ArrayList<Token> tokenList = new ArrayList<Token>();
	ArrayList<SymbolTable> symTable = new ArrayList<SymbolTable>();
	Metodos met = new Metodos();
	
	public void parse(ArrayList<Token> tokens)
	{
		tokenList = tokens;
		//System.out.println(tokenList);
		if (Programa())
		{
			System.out.println("\n\nSyntax is correct");		
		}
		else
		{
			System.out.println("\n\nSyntax is incorrect error in token: " + index);
		}
	}
	
	//Funcion para asignar variables a la tabla de simbolos
	public void doAssign(String name,String value)
	{
		SymbolTable sym = new SymbolTable();
		sym.setValue(value);
		sym.setName(name);
		for(int i=0;i<tableIndex;i++)
		{
			if(symTable.get(i).getName().equals(sym.name))
			{
				//si existe actualiza valor de variable
				symTable.get(i).setValue(sym.value);
				//System.out.println(symTable.get(i).getName() + " " + symTable.get(i).getValue());
				return;
			}
		}	
		//guardar en la tabla de simbolos
		symTable.add(sym);
		//System.out.println(symTable.get(tableIndex).getName() + " " + symTable.get(tableIndex).getValue());
		tableIndex++;
	}
	
	public int findInTable(Token tokenList)
	{
		int comp_1 = 0;
		for(int i=0;i<tableIndex;i++)
		{
			if(symTable.get(i).getName().equals(tokenList.getData()))
			{
				//si existe saca valor de tabla de simbolos
				comp_1 = Integer.parseInt(symTable.get(i).getValue());
				return comp_1;
			}
		}	
		return comp_1;
	}
	
	public boolean Programa()
	{
		//Encontrar inicio de programa
		if(tokenList.get(index).getData().contains("START") || if_enter == 1)
		{
			if(if_enter == 0)
			{
				index++;
			}
			//System.out.println(tokenList.size());
			//Verificar Programa
			while(index<tokenList.size())
			{
				//Expresion para imprimir
				//System.out.println(index+ "again");//1 4again 5,7,9,11,14,16
				if(tokenList.get(index).getType().getName().contains("PRINT"))
				{
					index++;
					//System.out.println(index);//6,8,10
					if(tokenList.get(index).getType().getName().contains("STRING"))
					{
						str = tokenList.get(index).getData();
						System.out.println(str.substring(1,str.length()-1));
					}
					else if(tokenList.get(index).getType().getName().contains("NUMERO"))
					{
						System.out.println(tokenList.get(index).getData());
					}
				}
				else if(tokenList.get(index).getData().equals("ENDIF")&& if_enter == 1)
				{
					if_enter = 0;
					index++;
					return true;
				}
				//Verificar variable y asignaciones validas
				else if(tokenList.get(index).getType().getName().contains("VARIABLE"))
				{
					str = tokenList.get(index).getData();
					index++;
					//System.out.println(index);
					if(tokenList.get(index).getType().getName().contains("ASSIGN"))
					{
						index++;
						//System.out.println(index);
						if(tokenList.get(index).getType().getName().contains("NUMERO"))
						{
							doAssign(str,tokenList.get(index).getData());
						}
						else if(tokenList.get(index).getType().getName().contains("VARIABLE"))
						{
							doAssign(str,tokenList.get(index).getData());
						}
						else if(tokenList.get(index).getType().getName().contains("STRING"))
						{
							doAssign(str,tokenList.get(index).getData());
						}
						//Recibir entrada por usuario
						else if(tokenList.get(index).getType().getName().contains("INPUT"))
						{
							//data asigned by user
							System.out.println("\nInsert data for "+ str +": ");
							str_aux = scan.next();
							doAssign(str,str_aux);
						}
						else
						{
							System.out.println("Error: No se esta asignando un valor a la variable");
							return false;
						}
					}
					else{return false;}
					
				}
				//Validar enunciados if
				else if(tokenList.get(index).getType().getName().contains("IF"))
				{
					index++;
					//System.out.println(index);//2
					//System.out.println("YES");
					if(Valor(tokenList.get(index).getType().getName()))
					{
						//System.out.println("YES");
						//si es variable
						if(tokenList.get(index).getType().getName().contains("VARIABLE"))
						{
							comp_1 = findInTable(tokenList.get(index));
							if(comp_1==0)
							{
								System.out.println("Error variable not excist!!");
								return false;
							}
						}
						else
						{
							comp_1 = Integer.parseInt(tokenList.get(index).getData()); //guardar dato a comparar
						}
						index++;
						//System.out.println(index);//3
						if(tokenList.get(index).getType().getName().contains("COMPARADOR"))
						{
							//System.out.println("YES");
							str = tokenList.get(index).getData();//guardar comparador
							index++;
							//System.out.println(index);//4
							if(Valor(tokenList.get(index).getType().getName()))
							{
								if(tokenList.get(index).getType().getName().contains("VARIABLE"))
								{
									comp_2 = findInTable(tokenList.get(index));
									if(comp_2 == 0)
									{
										System.out.println("Error variable not excist!!");
										return false;
									}
								}
								else
								{
									comp_2 = Integer.parseInt(tokenList.get(index).getData()); //guardar dato a comparar
								}
								//index++;
								if(Validacion(comp_1,str,comp_2))
								{
									if_enter = 1;
									if(Programa())
									{
										if_enter=0;
										index--;//parche
										//System.out.println("End if");
									}
									//index++; //return
									//System.out.println(index);//13
								}
								//else{return false;}
							}
							else{return false;}
						}
						else{return false;}
					}
					else{return false;}
				}
				//Encontrar final de programa
				else if(tokenList.get(index).getData().contains("END"))
				{
					return true;
				}
				index++;
			}//end while
		}//end if start
		return false;
	}//end programa
	
	//verificar si es un valor valido para una comparacion
	public boolean Valor(String value)
	{
		if(value.contains("NUMERO") || value.contains("VARIABLE"))
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	
	//verificar si la condicion se cumple
	public boolean Validacion(int comp_1, String comparador, int comp_2)
	{
		//System.out.println("Validando");
		if(comparador.equals("<"))
		{
			if(comp_1 < comp_2)
			{
				return true;
			}
			else
			{
				while(index<tokenList.size())
				{
					index++;
					if(tokenList.get(index).getData().contains("ENDIF"))
					{
						index++;
						return false;
					}
				}
			}
		}
		if(comparador.equals("#"))
		{
			if(comp_1 == comp_2)
			{
				return true;
			}
			else
			{
				while(index<tokenList.size())
				{
					index++;
					if(tokenList.get(index).getData().contains("ENDIF"))
					{
						index++;
						return false;
					}
				}
			}
		}
		if(comparador.equals(">"))
		{
			if(comp_1 > comp_2)
			{
				return true;
			}
			else
			{
				while(index<tokenList.size())
				{
					index++;
					//System.out.println(index);
					if(tokenList.get(index).getData().contains("ENDIF"))
					{
						//index++;
						return false;
					}
				}
			}
		}
		return false;
	}
}