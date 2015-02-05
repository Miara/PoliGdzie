BEGIN { 
		FS=";"; 
        printf("CREATE TABLE `specialConnection` (`id` INTEGER PRIMARY KEY, `specialPointLower_id` INTEGER, `specialPointUpper_id` INTEGER);\n");
	  }
NR>1 { 
		printf("INSERT INTO \"specialConnection\" VALUES(%s,%s,%s);\n", $1, $2, $3); 
     }