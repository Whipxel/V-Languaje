import java.util.ArrayList;
import java.util.Scanner;

public class Parser
{
	int index = 0;
	int tableIndex = 0;
	String comp_1,comp_2;
	int if_enter = 0;
	int while_enter = 0;//while index
	/*String while_comp_1 = "";
	String while_comp_2 = "";*/
	String while_comparador = "";
	String str = "";
	String str_aux = "";
	Scanner scan = new Scanner(System.in);
	ArrayList<Token> tokenList = new ArrayList<Token>();
	ArrayList<SymbolTable> symTable = new ArrayList<SymbolTable>();
	ArrayList<Token> whileTable = new ArrayList<Token>();
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

	//retorna cadena de caracteres almacenada en variable existente en tabla de simbolos
	public String stringInTable(Token tokenList)
	{
		String comp_1 = "n/a";
		for(int i=0;i<tableIndex;i++)
		{
			if(symTable.get(i).getName().equals(tokenList.getData()))
			{
				//si existe saca valor de tabla de simbolos
				comp_1 = symTable.get(i).getValue();
				return comp_1;
			}
		}	
		return comp_1;
	}
	
	public boolean Programa()
	{
		//Encontrar inicio de programa
		if(tokenList.get(index).getData().contains("START") || if_enter > 0 || while_enter > 0)
		{
			if(if_enter == 0 && while_enter == 0)
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
					else if(tokenList.get(index).getType().getName().contains("VARIABLE"))
					{
						str = stringInTable(tokenList.get(index));
						if(str.contains("n/a"))
						{
							System.out.println("Error variable not excist: " + tokenList.get(index).getData());
							return false;
						}
						else
						{
							System.out.println(str);
						}
					}
				}
				else if(tokenList.get(index).getData().equals("ENDIF")&& if_enter > 0)
				{
					//if_enter = 0;
					index++;
					//System.out.println(tokenList.get(index).getData());
					//System.out.println(index + "end if");
					return true;
				}
				else if(tokenList.get(index).getData().equals("ENDWHILE")&& while_enter > 0)
				{
					
					//comp_1 = stringInTable(while_comp_1);
					//comp_2 = stringInTable(while_comp_2);
					if(Validacion(comp_1,while_comparador,comp_2))
					{
						return true;
					}
					else
					{
						return false;
					}
				}
				//Verificar variable y asignaciones validas
				else if(tokenList.get(index).getType().getName().contains("VARIABLE"))
				{
					str = tokenList.get(index).getData();//guardar nombre de variable
					//System.out.println(str);
					index++;
					//System.out.println(index);
					if(tokenList.get(index).getType().getName().contains("ASSIGN"))
					{
						index++;
						//System.out.println(index);
						//System.out.println(tokenList.get(index).getData());
						//Es valor seguido de operacion??
						if(Valor(tokenList.get(index).getType().getName()) && tokenList.get(index+1).getType().getName().contains("OPERADOR") && Valor(tokenList.get(index+2).getType().getName()))
						{
							//System.out.println("YES");
							//si es variable
							String auxString = "";//para concatenar la primera operacion
							//auxString = auxString.concat(tokenList.get(index).getData();
							for(int i=0; i<2;i++)
							{
								if(tokenList.get(index).getType().getName().contains("VARIABLE"))
								{
									//System.out.println("YES");
									comp_1 = stringInTable(tokenList.get(index));//buscar que exista variable
									//System.out.println(comp_1);
									//System.out.println(auxString);
									if(comp_1.contains("n/a"))//si no hay nada error variable no declarada antes
									{
										System.out.println("Error variable not excist!!");
										return false;
									}
									//System.out.println(tokenList.get(index).getData() + "here ");
									auxString = auxString.concat(comp_1);
								}
								else
								{
									auxString = auxString.concat(tokenList.get(index).getData());
									//System.out.println(auxString);
								}
								if(i==0)
								{
									auxString = auxString.concat(tokenList.get(index+1).getData());
									index+=2;	
								}
							}
							//index++;
							//System.out.println(auxString);
							str_aux = Operacion(auxString);
							//System.out.println(str_aux);
							if(str_aux.contains("false"))
							{
								System.out.println("Error en operacion linea: " + index);
								return false;
							}
							doAssign(str,str_aux);
							//System.out.println(tokenList.get(index).getData());
						}
						//Es variable??
						else if(Valor(tokenList.get(index).getType().getName()))
						{
							//System.out.println("YES");
							//si es variable
							if(tokenList.get(index).getType().getName().contains("VARIABLE"))
							{
								//System.out.println("YES");
								comp_1 = stringInTable(tokenList.get(index));//buscar que exista variable
								//System.out.println(comp_1);
								if(comp_1.contains("n/a"))//si no hay nada error variable no declarada antes
								{
									System.out.println("Error variable not excist!!");
									return false;
								}
								//System.out.println(tokenList.get(index).getData() + "here ");
								doAssign(str,comp_1);
							}
							else
							{
								//System.out.println(tokenList.get(index).getData());
								doAssign(str,tokenList.get(index).getData()); //guardar dato
							}
						}
						//guardar un string
						else if(tokenList.get(index).getType().getName().contains("STRING"))
						{
							doAssign(str,tokenList.get(index).getData());
						}
						//Recibir entrada por usuario
						else if(tokenList.get(index).getType().getName().contains("INPUT"))
						{
							//data asigned by user
							//System.out.println("\nInsert data for "+ str +": ");
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
							comp_1 = stringInTable(tokenList.get(index));
							if(comp_1.contains("n/a"))
							{
								System.out.println("Error variable not excist!!");
								return false;
							}
						}
						else
						{
							comp_1 = tokenList.get(index).getData(); //guardar dato a comparar
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
									comp_2 = stringInTable(tokenList.get(index));
									if(comp_2.contains("n/a"))
									{
										System.out.println("Error variable not excist!!");
										return false;
									}
								}
								else
								{
									comp_2 = tokenList.get(index).getData(); //guardar dato a comparar
								}
								index++;
								if(Validacion(comp_1,str,comp_2))
								{
									if_enter++;
									if(Programa())
									{
										//if_enter=0;
										if_enter--;
										//index--;//parche
										//System.out.println(tokenList.get(index).getData());
									}
									//index++; //return
									//System.out.println(index+ "nop");//13
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
				else if(tokenList.get(index).getData().contains("WHILE"))
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
							comp_1 = stringInTable(tokenList.get(index));
							//while_comp_1 = comp_1;
							if(comp_1.contains("n/a"))
							{
								System.out.println("Error variable not excist!!");
								return false;
							}
						}
						else
						{
							comp_1 = tokenList.get(index).getData(); //guardar dato a comparar
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
									comp_2 = stringInTable(tokenList.get(index));
									//while_comp_2 = tokenList.get(index).getData();
									if(comp_2.contains("n/a"))
									{
										System.out.println("Error variable not excist!!");
										return false;
									}
								}
								else
								{
								
									comp_2 = tokenList.get(index).getData(); //guardar dato a comparar
								
								}
								index++;
								//verificar que condicion se cumpla
								if(Validacion(comp_1,str,comp_2))
								{
									while_comparador = str;
									while_enter++;
									if(Programa())
									{
										//if_enter=0;
										while_enter--;
										//index--;//parche
										//System.out.println("End if");
									}
									//index++; //return
									//System.out.println(index+ "nop");//13
								}
								//else{return false;}
							}
							else{return false;}
						}
						else{return false;}
					}
					else{return false;}
				}
				else{return false;}
				index++;//siguiente token
			}//end while
		}//end if start
		return false;
	}//end programa
	
	//Funcion para realizar operaciones aritmeticas y guardarlas en variable asignada
	public String Operacion(String oper)
	{
		int copyIndex = index;
		String auxStr = oper;//en esta variable se guardara el valor de la operacion total
		//System.out.println(oper);
		//System.out.println(tokenList.get(index).getData());
		if(tokenList.get(copyIndex+1).getType().getName().contains("OPERADOR"))
		{
			//nothing here...
			//System.out.println("operador");
		}
		else
		{
			//System.out.println(str);
			auxStr = met.evaluating(oper);
			return auxStr;
		}
		//System.out.println(tokenList.get(index).getType().getName());
		while(copyIndex!=0)
		{
			copyIndex++;
			if(tokenList.get(copyIndex).getType().getName().contains("OPERADOR"))
			{
				auxStr = auxStr.concat(tokenList.get(copyIndex).getData());
				copyIndex++;
				if(Valor(tokenList.get(copyIndex).getType().getName()))
				{
					if(tokenList.get(copyIndex).getType().getName().contains("VARIABLE"))
					{
						comp_1 = stringInTable(tokenList.get(copyIndex));
						auxStr = auxStr.concat(comp_1);
						//System.out.println(auxStr);
						
					}
					else
					{
						auxStr = auxStr.concat(tokenList.get(copyIndex).getData());
						//System.out.println(auxStr);
					}
				}
				else
				{
					return "false";
				}
			}
			else
			{
				auxStr = met.evaluating(auxStr);
				//System.out.println(auxStr);
				index = copyIndex-1;//parche#2
				//doAssign(str,auxStr);
				return auxStr;
			}	
		}
		return "";
	}
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
	public boolean Validacion(String comp_1, String comparador, String comp_2)
	{
		//System.out.println("Validando");
		float num_1,num_2;
		if(comp_1.indexOf('.')>=0)
		{
			float x = Float.parseFloat(comp_1);
			num_1 = x;
		}
		else
		{
			int x = Integer.parseInt(comp_1);
			num_1 = x;
		}
		if(comp_2.indexOf('.')>=0)
		{
			float y = Float.parseFloat(comp_1);
			num_2 = y;
		}
		else
		{
			int y = Integer.parseInt(comp_1);
			num_2 = y;
		}
		if(comparador.equals("<"))
		{
			if(num_1 < num_2)
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
			if(num_1 == num_2)
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
			if(num_1 > num_2)
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