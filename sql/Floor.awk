BEGIN { 
		FS=";"; 
        printf("CREATE TABLE `floor` (`id` INTEGER PRIMARY KEY, `name` VARCHAR, `building_id` INTEGER, `number` INTEGER, `width` INTEGER , `height` INTEGER , `drawableId` VARCHAR, `tag` VARCHAR, `pixelsPerMeter` INTEGER);\n");
	  }
NR>1 { 
		printf("INSERT INTO \"floor\" VALUES(%s,'%s',%s,%s,%s,%s,'%s','%s',%s);\n", $1, $2, $3, $4, $5, $6, $7, $8, $9); 
     }