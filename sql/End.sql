
DELETE FROM sqlite_sequence;
INSERT INTO "sqlite_sequence" VALUES('building', (SELECT COUNT(*) FROM "building"));
INSERT INTO "sqlite_sequence" VALUES('floor', (SELECT COUNT(*) FROM "floor"));
INSERT INTO "sqlite_sequence" VALUES('navigationPoint', (SELECT COUNT(*) FROM "navigationPoint"));
INSERT INTO "sqlite_sequence" VALUES('buildingEntry', (SELECT COUNT(*) FROM "buildingEntry"));
INSERT INTO "sqlite_sequence" VALUES('navigationConnection', (SELECT COUNT(*) FROM "navigationConnection"));
INSERT INTO "sqlite_sequence" VALUES('specialConnection', (SELECT COUNT(*) FROM "specialConnection"));
INSERT INTO "sqlite_sequence" VALUES('room', (SELECT COUNT(*) FROM "room"));
INSERT INTO "sqlite_sequence" VALUES('unit', (SELECT COUNT(*) FROM "unit"));
CREATE INDEX `room_aliases_idx` ON `room` ( `aliases` );
CREATE INDEX `room_name_idx` ON `room` ( `name` );
CREATE INDEX `room_number_idx` ON `room` ( `number` );
CREATE INDEX `unit_aliases_idx` ON `unit` ( `aliases` );
CREATE INDEX `unit_name_idx` ON `unit` ( `name` );
CREATE INDEX `building_name_idx` ON `building` ( `name` );
CREATE INDEX `building_aliases_idx` ON `building` ( `aliases` );
COMMIT;