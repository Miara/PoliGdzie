BEGIN { 
		FS=";"; 
        printf("CREATE TABLE `building` (`id` INTEGER PRIMARY KEY AUTOINCREMENT, `name` VARCHAR, `address` VARCHAR , `coordX` DOUBLE PRECISION , `coordY` DOUBLE PRECISION , `width` INTEGER , `height` INTEGER , `aliases` VARCHAR , `imageResource` VARCHAR , `markerImageResource` VARCHAR);\n");
	  }
NR>1 { 
		printf("INSERT INTO \"building\" VALUES(%s,'%s','%s',%s,%s,%s,%s,'%s','%s','%s');\n", $1, $2, $3, $4, $5, $6, $7, $8, $9, $10); 
     }


