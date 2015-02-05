BEGIN { 
		FS=";"; 
        printf("CREATE TABLE `navigationPoint` (`id` INTEGER PRIMARY KEY, `coordX` INTEGER, `coordY` INTEGER, `floor_id` INTEGER, `type` VARCHAR);\n");
	  }
NR>1 { 
		printf("INSERT INTO \"navigationPoint\" VALUES(%s,%s,%s,%s,'%s');\n", $1, $2, $3, $4, $5); 
     }