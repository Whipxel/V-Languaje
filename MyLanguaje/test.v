START
	heroe = 9
	itera = 0
	
	
	IF 9 # heroe
		
			PRINT "Inserta numero: "
			erick = INPUT
			
			prom = erick
			
			PRINT "Inserta numero: "
			erick = INPUT
			prom= prom+ erick
			
			PRINT "Inserta numero: "
			erick = INPUT
			prom= prom+ erick
			
			
			prom = prom/3
			
			PRINT "Tu promedio es de: "
			PRINT prom
			PRINT "Revisando..."
			
		IF prom < 59.9
			PRINT "Que paso!?"
			PRINT "Reprobado"
		ENDIF
		-
		
		IF prom > 59.9
		PRINT "PASASTE!!"
		ENDIF
		-
		
	ENDIF
	-
	
END