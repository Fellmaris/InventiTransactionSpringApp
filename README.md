****Duombazės parsiuntimas ir inicializavimas
1.Parsisiųsti Postgres duombazę iš  https://www.postgresql.org/download/ (aš naudojau Windows x86-64, 16.2 postgres versiją)
2.Tada parsiunčiau pgAdmin grafinį interfeisą iš https://www.pgadmin.org/download/ ir naudojau windowsinę pgAdmin 4 v8.5 versiją
3.Tada pasileidus pgAdmin interfeisa einama į skilį Object->Create->Database ir sukuriam duombazę pavadinimu "Inventi bank"
4.Pakeičiam application.properties faile duombazės username ir password į savo sukurtos duombazės username ir password ir dar pakeiciam datasource.url gala i savo duombazės pavadinima
5.Paleidžiam programa, kad automatiškai su JPA pasikurtu reikiama lentelė duombazėje
6.per pgAdmin į duombazę įkeliam kelis įrašus, prasukę šia komandą:
INSERT INTO transactions (account_number, operation_time, beneficiary, comment, amount, currency) VALUES
('123456789', '2024-04-15 09:00:00', 'John Doe', 'Salary', 2000.00, 'USD'),
('987654321', '2024-04-15 10:30:00', 'Jane Smith', NULL, 1500.00, 'EUR'),
('123456789', '2024-04-15 11:45:00', 'John Doe', 'Bonus', 500.00, 'USD'),
('555515555', '2024-04-15 12:00:00', 'Brad Pit', 'Refund', 300.00, 'GBP'),
('987654321', '2024-04-15 13:15:00', 'Jane Smith', 'Investment', 1000.00, 'EUR'),
('123456789', '2024-04-15 14:30:00', 'John Doe', 'Overtime', 300.00, 'USD'),
('444444444', '2024-04-15 15:45:00', 'Bob Brown', NULL, 700.00, 'CAD'),
('123456788', '2024-04-15 16:00:00', 'Jane Smith', 'Rent', 1000.00, 'USD'),
('999999999', '2024-04-15 17:30:00', 'Alice Cooper', 'Dividends', 400.00, 'EUR'),
('555555555', '2024-04-15 18:45:00', 'Alice Johnson', 'Groceries', 150.00, 'GBP

***Programos paleidimas
1.Atsidarom projekto aplanką kaip naują Maven projektą per IDE
2.Paleidžiam programa is InventiTransactionAppApplication Run'inant tą klasę

***Testavimas su Postman
1.Parsisiunčiam postman su kuriuo testuosime programos veikimą.
2.Cvs generavimas iš duombazėje easančiais duomenimis:
	*Parenkame GET tipo requesta iš dorpdown listo
	*Į šalia esantį lauką ivedame: http://localhost:8080/generate-csv
	*Spaudžame send ir turetų grąžinti visus duombazėje esančius įrašus
		*Jeigu norime gauti įrašus tam tikram periodui reikia Param skiltyje suvesti raktą "startDate" ir kitą raktą "endDate" ir jų Value skiltyje įrašome reikiamus rėžius
3.Cvs importui
	*Pasirinkti POST requesta
	*Į url lauką suvedam http://localhost:8080/import-csv
	*Headers skiltyje pridedam įrašą su raktu "Content-Type" o prie Value parašome "multipart/form-data"
	*Tada Body skiltyje prie Key ivedame "file" ir dešniau pasirenkame "file", o value skiltyje įdedame jūsų norimą failą kurį importuosite
		*Failo eilutes pvz.: "666133456,2024-04-16 18:45:00.0,Jhonny Cash,Groceries,150.00,GBP"
	*Spaudžiame Send ir duomenys turėtų susi'import'uoti į duomenų baze
4.Sąskaitos balanco skaičiavimas
	*Pasirenkame GET requesta
	*Į url lauką suvedam http://localhost:8080/calculateBalance
	*PRIVALOMA į params skiltį prideti įraša su raktu "accountNumber" o prie value įvedame norimos sąskaitos numerį
	*Neprivaloma jeigu norime gauti įrašus tam tikram periodui reikia Param skiltyje suvesti raktą "startDate" ir kitą raktą "endDate" ir jų Value skiltyje įrašome reikiamus rėžius
	*Spaudžiame send ir gausime reikiamos sąskaitos balansą paverstą į EUR, jeigu buvo pasirinktas periodas, tai skaičiuojama tik tam periodui
