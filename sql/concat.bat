del dump.sql
del Poligdzie.db
type Init.sql >> dump.sql
gawk -f Building.awk < Building.csv >> dump.sql
gawk -f BuildingEntry.awk < BuildingEntry.csv >> dump.sql
gawk -f Floor.awk < Floor.csv >> dump.sql
gawk -f Room.awk < Room.csv >> dump.sql
gawk -f NavigationPoint.awk < NavigationPoint.csv >> dump.sql
gawk -f NavigationConnection.awk < NavigationConnection.csv >> dump.sql
gawk -f SpecialConnection.awk < SpecialConnection.csv >> dump.sql
type Unit.sql >> dump.sql
type End.sql >> dump.sql
sqlite3 Poligdzie.db ".read dump.sql"
copy /Y Poligdzie.db "../Poligdzie/Poligdzie/res/raw"