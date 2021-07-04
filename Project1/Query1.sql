CREATE TABLE Bank(BankName TEXT NOT NULL, City TEXT NOT NULL, NoAccounts INTEGER, Security TEXT CONSTRAINT SecurityLevel CHECK(Security IN('excellent','very good','good','weak')), CONSTRAINT BANK_PK PRIMARY KEY(BankName,City));

CREATE TABLE Robberies(BankName TEXT NOT NULL, City TEXT NOT NULL, Date DATE NOT NULL, Amount DECIMAL(12,2), CONSTRAINT Robberies_PK PRIMARY KEY(BankName, City, Date), CONSTRAINT Robberies_FK FOREIGN KEY(BankName, City) references Bank(BankName, City) ON DELETE RESTRICT ON UPDATE CASCADE);

CREATE TABLE Plans(BankName TEXT NOT NULL, City TEXT NOT NULL, NoRobbers INTEGER CONSTRAINT NoRobber CHECK(NoRobbers > 0), PlannedDate DATE NOT NULL, CONSTRAINT Plans_PK PRIMARY KEY(BankName, City,PlannedDate), CONSTRAINT Plans_FK FOREIGN KEY(BankName, City) references Bank(BankName, City) ON DELETE RESTRICT ON UPDATE CASCADE);

CREATE TABLE Robbers(RobberId SERIAL, NickName TEXT, Age INTEGER CONSTRAINT AgeCheck CHECK(Age >= 0), NoYears INTEGER CONSTRAINT PrisonYears CHECK(NoYears < Age AND NoYears >= 0), PRIMARY KEY(RobberId));

CREATE TABLE Skills(SkillId SERIAL, Description TEXT, PRIMARY KEY(SkillId));

CREATE TABLE HasSkills(RobberID INTEGER NOT NULL, SkillId INTEGER NOT NULL, Preference INTEGER CONSTRAINT PreferenceRank CHECK(Preference > 0 AND Preference <= 3), Grade CHAR(2), CONSTRAINT GradeId CHECK(Grade IN('A+','A','A-','B+','B','B-','C+','C')), CONSTRAINT HasSkills_PK PRIMARY KEY(RobberId, SkillId), CONSTRAINT HasSkill_FKrobbers FOREIGN KEY(RobberId) references Robbers(RobberId) ON DELETE RESTRICT ON UPDATE RESTRICT, CONSTRAINT HasSkills_FKskills FOREIGN KEY(SkillId) references Skills(SkillId) ON DELETE RESTRICT ON UPDATE RESTRICT);

CREATE TABLE HasAccounts(RobberId INTEGER NOT NULL, BankName TEXT NOT NULL, City TEXT NOT NULL, CONSTRAINT HasAccounts_PK PRIMARY KEY(RobberId, BankName, City), CONSTRAINT HasAccounts_FKbank FOREIGN KEY(BankName, City) references Bank(BankName, City) ON DELETE RESTRICT ON UPDATE RESTRICT,CONSTRAINT HasAccount_FKrobbers FOREIGN KEY(RobberId) references Robbers(RobberId) ON DELETE RESTRICT ON UPDATE CASCADE);

CREATE TABLE Accomplices(RobberId INTEGER NOT NULL, BankName TEXT NOT NULL, City TEXT NOT NULL, Date DATE NOT NULL, Share INTEGER CONSTRAINT Shares CHECK(Share > 0), CONSTRAINT Accomplices_PK PRIMARY KEY(RobberId, BankName, City, Date), CONSTRAINT Accomplices_FKrobbers FOREIGN KEY(RobberId) references Robbers(RobberId) ON DELETE RESTRICT ON UPDATE CASCADE, CONSTRAINT Accomplices_FKbank FOREIGN KEY(BankName, City) references Bank(BankName, City) ON DELETE RESTRICT ON UPDATE CASCADE);
