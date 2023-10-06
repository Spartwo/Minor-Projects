/* 
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Other/SQLTemplate.sql to edit this template
SQL form is held for reference to required statements
 */
CREATE TABLE Items (
    ItemID Int(3) NOT NULL,
    ItemName varchar(255),
    ItemCost Int(6),
    ItemWeight Int(5),
    ItemStatName varchar(255),
    ItemStatValue Int(3),
    PRIMARY KEY (ItemID)
);

INSERT INTO Items (ItemID, ItemName, ItemCost, ItemWeight, ItemStatName, ItemStatValue)
VALUES 	
('001','A','1500','5','Kinetic','40'),
('002','B','1400','5','Kinetic','40'),
('003','C','1300','15','Kinetic','40'),
('004','D','1200','5','Kinetic','40'),
('005','E','1100','21','Kinetic','40'),
('006','F','1000','11','Kinetic','40'),
('007','G','900','8','Kinetic','40'),
('008','H','800','2','Kinetic','40'),
('009','I','700','7','Kinetic','40'),
('010','J','600','15','Kinetic','40')

CREATE TABLE Players (
    PlayerID Int(3) NOT NULL,
    PlayerName varchar(255),
    PlayerHealth varchar(4),
    PlayerLevel TinyInt(2),
    ManagerID Int(3),
    PRIMARY KEY (PlayerID)
);

INSERT INTO Players (PlayerID, PlayerName, PlayerHealth, PlayerLevel, ManagerID)
VALUES 	
('001','Adam','100','21','00'),
('002','Brian','100','12','00'),
('003','Craig','100','15','00'),
('004','David','100','5','00'),
('005','Evan','100','21','02'),
('006','Fredward','10','11','00'),
('007','Greg','100','8','00'),
('008','Harry','100','2','00'),
('009','Ian','100','7','01'),
('010','James','100','15','00')

CREATE TABLE Skills (
    SkillID Int(3) NOT NULL,
    SkillName varchar(255),
    SkillPointValue TinyInt(2),
    PRIMARY KEY (SkillID)
);

INSERT INTO Skills (SkillID, SkillName, SkillPointValue)
VALUES 	
('001','A','1500','50'),
('002','B','1400','50'),
('003','C','1300','10'),
('004','D','1200','50'),
('005','E','1100','20'),
('006','F','1000','10'),
('007','G','900','80'),
('008','G','800','20'),
('009','I','700','70'),
('010','J','600','10')


CREATE TABLE Ownership (
    OwnershipID TinyInt(2) NOT NULL,
    ItemID Int(3),
    PlayerID Int(3),
    PRIMARY KEY (OwnershipID),
    FOREIGN KEY (ItemID) REFERENCES Items(ItemID),
    FOREIGN KEY (PlayerID) REFERENCES Players(PlayerID)
);

CREATE TABLE PlayerSkills (
    SkillOwnID TinyInt(2) NOT NULL,
    SkillID Int(3),
    PlayerID varchar(255),
    PRIMARY KEY (SkillOwnID),
    FOREIGN KEY (SkillID) REFERENCES Skills(SkillID),
    FOREIGN KEY (PlayerID) REFERENCES Players(PlayerID)
);


