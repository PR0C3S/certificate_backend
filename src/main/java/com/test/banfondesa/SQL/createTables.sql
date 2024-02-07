

CREATE TABLE `client` (
  `birthday` date NOT NULL,
  `id` int NOT NULL AUTO_INCREMENT,
  `dni` varchar(255) NOT NULL,
  `email` varchar(255) NOT NULL,
  `full_name` varchar(255) NOT NULL,
  `gender` varchar(255) NOT NULL,
  `location` varchar(255) NOT NULL,
  `phone` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_ffgfxk34snifdqqbwtoq6pj37` (`dni`),
  UNIQUE KEY `UK_bfgjs3fem0hmjhvih80158x29` (`email`),
  UNIQUE KEY `UK_qe9dtj732yl9u3oqhhsee4lps` (`phone`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


CREATE TABLE `certificate` (
  `account_number` int NOT NULL,
  `amount` float NOT NULL,
  `cancell_interest` float NOT NULL,
  `client_id` int NOT NULL,
  `duration` int NOT NULL,
  `earn_interest` float NOT NULL,
  `finish_date` date NOT NULL,
  `id` int NOT NULL AUTO_INCREMENT,
  `is_able_to_cancell_before` bit(1) NOT NULL,
  `is_able_to_pay_before` bit(1) NOT NULL,
  `is_penalty_for_cancell_before` bit(1) NOT NULL,
  `start_date` date NOT NULL,
  `currency` varchar(255) NOT NULL,
  `status` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKp9mhsc0x1oktdqsbf9ojl9prq` (`client_id`),
  CONSTRAINT `FKp9mhsc0x1oktdqsbf9ojl9prq` FOREIGN KEY (`client_id`) REFERENCES `client` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `certificate_transactions` (
  `certificate_id` int NOT NULL,
  `transactions_id` int NOT NULL,
  UNIQUE KEY `UK_mmu5ti3ymdhwoow1fp1rgd2x4` (`transactions_id`),
  KEY `FKsuvc3c6kqb1iu24yfymuyqn1d` (`certificate_id`),
  CONSTRAINT `FKnx9r2v4goklwq4ro4ph7dsck0` FOREIGN KEY (`transactions_id`) REFERENCES `transaction` (`id`),
  CONSTRAINT `FKsuvc3c6kqb1iu24yfymuyqn1d` FOREIGN KEY (`certificate_id`) REFERENCES `certificate` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `transaction` (
  `amount` float NOT NULL,
  `date` date NOT NULL,
  `id` int NOT NULL AUTO_INCREMENT,
  `message` varchar(255) NOT NULL,
  `type` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

INSERT INTO certificado_db.client
(birthday,
id,
dni,
email,
full_name,
gender,
location,
phone)
VALUES
('1988-06-25', 1, '123-4567890-1', 'john@example.com', 'John Doe', 'Male', 'New York', '123-456-7890'),
('1990-09-15', 2, '987-6543210-2', 'jane@example.com', 'Jane Smith', 'Female', 'Los Angeles', '987-654-3210'),
('1975-04-20', 3, '567-8901230-3', 'mike@example.com', 'Mike Johnson', 'Male', 'Chicago', '567-890-1230');

INSERT INTO certificado_db.certificate
(account_number,
amount,
cancell_interest,
client_id,
duration,
earn_interest,
finish_date,
id,
is_able_to_cancell_before,
is_able_to_pay_before,
is_penalty_for_cancell_before,
start_date,
currency,
status)
VALUES
(1001, 5000.00, 0.25, 1, 12, 0.15, '2024-12-31', 1, 1, 1, 0, '2024-01-01', 'USD', 'active'),
(1002, 7500.00, 0.25, 2, 24, 0.15, '2025-06-30', 2, 1, 1, 1, '2024-07-01', 'DO', 'active'),
(1003, 10000.00, 0.25, 3, 36, 0.15, '2026-03-31', 3, 0, 1, 1, '2024-04-01', 'DO', 'active');

INSERT INTO certificado_db.transaction
(date, type, message, amount) VALUES
(5000.00,'2024-12-31',1, 'Creacion del certificado', "Creacion"),
(5000.00,'2025-06-30',2, 'Creacion del certificado', "Creacion"),
(5000.00,'2026-03-31',3, 'Creacion del certificado', "Creacion");


INSERT INTO certificado_db.certificate_transactions
(certificate_id, transactions_id)
VALUES
(1,1),(2, 2),(3,3);








