USE `paymybuddy_test`;

INSERT INTO `user_account` VALUES (1,'test@gmail.com','$2a$10$84qTuePCx8QRkdlZ0gZdfu8mBpp14lfMG4aG6v7cddsYj6jaymvX2','prénom','nom','2 rue de paris 75201 LABAS',1000.00),
								  (2,'tyler.durden@gmail.com','$2a$10$2pOLPDmISgpEAvePgEpcAO52cJ//koJVTutsYgIHsOPkIbihesq1W','tyler','durden','1rue test 00000 ici',100.00),
								  (3,'jack.sparrow@mail.com','$2a$10$.BrSkL7mWh.IZ.LEAactXOdRaK/.3wTILt3QBOmTBNYKzQ7uWbZ.q','Jack','Sparrow','quelque part en mer',10.00),
								  (4,'robb.fynn@mh.com','$2a$10$yHlevn4KgUtnR17IKkFdaOhzc4w4KkJ5a/HdDtqhUUTtVzJVSDq0a','robb','fynn','Oakland CA',1025.00);

INSERT INTO `bank_account` VALUES (1,'FR76000012345678910111213KK','IBAN 1','bob kelso',1),
								  (2,'FR76000099999999999999999KK','IBAN 2','tyler durden',2),
								  (3,'FR7600001234567891011121399','IBAN 3','jack sparrow',3),
								  (4,'FR76000012345678910111213MH','Iban Robb','Robb Fynn',4);

INSERT INTO `contact` VALUES (1,'contact_test',2,1),
							 (2,'contact_jack',2,3);

INSERT INTO `transaction` VALUES (1,'2021-05-07 08:07:21','transaction 10€ à test','2',0.50,10.00,2,1,NULL),
								 (2,'2021-05-31 15:17:45','test deposit','0',0.50,100.00,2,2,2),
								 (3,'2021-05-31 15:18:58','test withdraw','1',0.50,100.00,2,2,2);