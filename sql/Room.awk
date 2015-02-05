BEGIN { 
		FS=";"; 
        printf("CREATE TABLE `room` (`id` INTEGER PRIMARY KEY AUTOINCREMENT, `number` VARCHAR, `name` VARCHAR, `function` VARCHAR , `floor_id` INTEGER , `coordX` INTEGER , `coordY` INTEGER , `radius` INTEGER, `doorsX` INTEGER , `doorsY` INTEGER , `navigationPointConnection_id` INTEGER , `aliases` VARCHAR , `building_id` INTEGER);\n");
	  }
NR>1 {  
		printf("INSERT INTO \"room\" VALUES(%s,'%s','%s','%s',%s,%s,%s,%s,%s,%s,'%s','%s',%s);\n", $1, $2, $3, $4, $5, $6, $7, $8, $9, $10, $11, $12, $13); 
     }