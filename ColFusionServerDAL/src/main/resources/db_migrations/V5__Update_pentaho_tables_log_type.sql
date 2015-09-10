ALTER TABLE `colfusion`.`colfusion_pentaho_log_transformaion` 
CHANGE COLUMN `LOG_FIELD` `LOG_FIELD` LONGTEXT NULL DEFAULT NULL ;

ALTER TABLE `colfusion`.`colfusion_pentaho_log_step` 
CHANGE COLUMN `LOG_FIELD` `LOG_FIELD` LONGTEXT NULL DEFAULT NULL ;