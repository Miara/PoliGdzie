BEGIN { 
		FS=";"; 
        printf("CREATE TABLE `buildingEntry` (`id` INTEGER PRIMARY KEY, `coordX` INTEGER, `coordY` INTEGER , `building_id` INTEGER , `point_id` INTEGER);\n");
	  }
NR>1 { 
		printf("INSERT INTO \"buildingEntry\" VALUES(%s,%s,%s,%s,%s);\n", $1, $2, $3, $4, $5); 
     }


