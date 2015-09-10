ALTER TABLE `colfusion`.`colfusion_users` 
ADD COLUMN `dataverse_token` VARCHAR(255) NULL DEFAULT NULL AFTER `status_excludes`;