ALTER TABLE `HomeManagementSystem`.`User` 
ADD COLUMN `is_email_verified` TINYINT(1) NULL DEFAULT 0 AFTER `maker_timestamp`;