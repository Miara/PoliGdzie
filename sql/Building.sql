CREATE TABLE `building` (`id` INTEGER PRIMARY KEY AUTOINCREMENT, `name` VARCHAR, `address` VARCHAR , `coordX` DOUBLE PRECISION , `coordY` DOUBLE PRECISION , `width` INTEGER , `height` INTEGER , `aliases` VARCHAR , `markerImageResource` VARCHAR , `imageResource` VARCHAR);
INSERT INTO "building" VALUES(1,'Centrum Wyk³adowe','ul. Piotrowo 2, 60-965, Poznañ ',52.404091,16.949686,150,2,'CW','cw_ic','cw_marker');
INSERT INTO "building" VALUES(2,'Elektryk','ul. Piotrowo 3a, 60-965, Poznañ ',52.401972,16.951360,70,10,'el,elektryk','we_ic','el_marker');
INSERT INTO "building" VALUES(3,'Budowa maszyn','ul. Piotrowo 3, 60-965, Poznañ ',52.402357,16.950573,70,10,'bm','bm_ic','bm_marker');
INSERT INTO "building" VALUES(4,'Biblioteka Techniczna','ul. Piotrowo 2, 60-965, Poznañ ',52.403447,16.949182,90,100,'BT','bt_ic','bt_marker');
INSERT INTO "building" VALUES(5,'Wydzia³ Technologii Chemicznej','ul. Bedrychowo 4, 60-965, Poznañ ',52.404995,16.950661,100,120,'TCh','tch_ic','tch_marker');
