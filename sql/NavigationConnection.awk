BEGIN { 
		FS=";"; 
        printf("CREATE TABLE `navigationConnection` (`id` INTEGER PRIMARY KEY, `navigationPointFirst_id` INTEGER , `navigationPointLast_id` INTEGER, `length` INTEGER);\n");
	  }
NR>1 { 
		printf("INSERT INTO \"navigationConnection\" VALUES(%s,%s,%s,%s);\n", $1, $2, $3, $4); 
     }

