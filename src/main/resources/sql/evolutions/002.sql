 --liquibase formatted sql
 --changeset adam.zimny:2 labels:DEV
 ALTER TABLE account ADD COLUMN password_last_time_changed TIMESTAMP;
 --rollback DROP COLUMN password_last_time_changed;
